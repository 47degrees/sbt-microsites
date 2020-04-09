/*
 * Copyright 2017-2020 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package microsites.github

import java.io.File

import cats.data.{NonEmptyList, OptionT}
import cats.effect.{ConcurrentEffect, IO, Sync, Timer}
import cats.implicits._
import com.github.marklister.base64.Base64._
import github4s.Github
import github4s.GithubResponses._
import github4s.domain._
import microsites.Exceptions._
import microsites.github.Config._
import microsites.ioops.{FileReader, IOUtils => FIO}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class GitHubOpsIO(
    owner: String,
    repo: String,
    accessToken: Option[String],
    fileReader: FileReader = FileReader
)(implicit ec: ExecutionContext, ce: ConcurrentEffect[IO], t: Timer[IO])
    extends GitHubOps[IO](owner, repo, accessToken, fileReader)

class GitHubOps[F[_]: ConcurrentEffect: Timer](
    owner: String,
    repo: String,
    accessToken: Option[String],
    fileReader: FileReader = FileReader
)(implicit ec: ExecutionContext) {

  val gh: Github[F]                = Github[F](accessToken)
  val headers: Map[String, String] = Map("user-agent" -> "sbt-microsites")

  def commitFiles(
      baseDir: File,
      branch: String,
      message: String,
      files: List[File]
  ) = {

    def relativePath(file: File): String =
      FIO.relativize(baseDir, file).getOrElse(file.getName)

    def readFileContents: F[List[(String, String)]] =
      files.traverse { file =>
        for {
          path         <- Sync[F].delay(file.getAbsolutePath)
          content      <- Sync[F].delay(fileReader.getFileContent(path))
          relativePath <- Sync[F].delay(relativePath(file))
        } yield (content, relativePath)
      }

    readFileContents
      .flatMap { filesAndContents =>
        commitFilesAndContents(branch, message, filesAndContents)
      }
  }

  def commitFilesAndContents(
      branch: String,
      message: String,
      filesAndContents: List[(String, String)]
  ): F[Option[Ref]] = {

    def fetchBaseTreeSha(commitSha: String): F[RefCommit] =
      run(gh.gitData.getCommit(owner, repo, commitSha))

    def fetchFilesContents(
        commitSha: String
    ): F[List[(String, Option[String])]] = {
      def fetchFileContents(
          path: String,
          commitSha: String
      ): F[(String, Option[String])] =
        run(
          gh.repos
            .getContents(owner = owner, repo = repo, path = path, ref = Some(commitSha))
        ).map(_.map(content => path -> content.content).head)

      filesAndContents.map(_._1).traverse(fetchFileContents(_, commitSha))
    }

    def filterNonChangedFiles(remote: List[(String, Option[String])]): List[(String, String)] = {
      val remoteMap = remote.collect {
        case (path, Some(c)) => path -> c
      }.toMap
      filesAndContents.filterNot {
        case (path, content) =>
          remoteMap.get(path).exists { remoteContent =>
            remoteContent.trim.replaceAll("\n", "") == content.getBytes.toBase64.trim
          }
      }
    }

    def createTree(
        baseTreeSha: String,
        filteredFilesContent: List[(String, String)]
    ): F[TreeResult] = {
      val treeData: List[TreeDataBlob] = filteredFilesContent.map {
        case (path, content) => TreeDataBlob(path, blobMode, blobType, content)
      }

      run(gh.gitData.createTree(owner, repo, Some(baseTreeSha), treeData))
    }

    def createCommit(
        treeSha: String,
        baseCommitSha: String
    ): F[RefCommit] =
      run(gh.gitData.createCommit(owner, repo, message, treeSha, List(baseCommitSha), None))

    def commitFilesIfChanged(
        baseTreeSha: String,
        parentCommitSha: String,
        filteredFilesContent: List[(String, String)]
    ): F[Option[Ref]] =
      filteredFilesContent match {
        case Nil =>
          Sync[F].pure(none[Ref])
        case list =>
          for {
            ghResultTree   <- createTree(baseTreeSha, list)
            ghResultCommit <- createCommit(ghResultTree.sha, parentCommitSha)
            ghResultUpdate <- updateHead(branch, ghResultCommit.sha)
          } yield Option(ghResultUpdate)
      }

    for {
      gHResultParentCommit <- fetchHeadCommit(branch)
      parentCommitSha = gHResultParentCommit.`object`.sha
      gHResultBaseTree <- fetchBaseTreeSha(parentCommitSha)
      baseTreeSha = gHResultBaseTree.tree.sha
      ghResultFilesContent <- fetchFilesContents(parentCommitSha)
      ghResultUpdate <- commitFilesIfChanged(
        baseTreeSha,
        parentCommitSha,
        filterNonChangedFiles(ghResultFilesContent)
      )
    } yield ghResultUpdate
  }

  def commitDir(branch: String, message: String, dir: File): F[Ref] =
    Sync[F]
      .delay(fileReader.fetchDirsRecursively(List(dir)))
      .handleError(e => throw IOException(s"Error fetching files recursively", Some(e)))
      .flatMap {
        case Nil => throw IOException(s"Nothing to commit in dir ${dir.getAbsolutePath}")
        case h :: t =>
          commitDirAux(branch, message, dir, NonEmptyList(h, t))
      }

  def commitDirAux(
      branch: String,
      message: String,
      baseDir: File,
      dirList: NonEmptyList[File],
      blobConfig: BlobConfig = defaultBlobConfig
  ): F[Ref] = {

    def updateCommitDirH(dir: File, sha: Option[String]): F[RefCommit] =
      updateCommitDir(branch, message, baseDir, dir, blobConfig, sha)

    val processAllFiles: F[RefCommit] =
      dirList.reduceLeftM { dirHead =>
        updateCommitDirH(dirHead, None)
      } { (commit, dir) =>
        updateCommitDirH(dir, Some(commit.sha))
      }

    for {
      lastCommit <- processAllFiles
      headRef    <- updateHead(branch, lastCommit.sha)
    } yield headRef
  }

  private[this] def updateCommitDir(
      branch: String,
      message: String,
      baseDir: File,
      dirToCommit: File,
      blobConfig: BlobConfig,
      commitSha: Option[String]
  ) = {

    def fetchBaseTreeSha: F[Option[RefCommit]] =
      commitSha
        .map { sha =>
          run(gh.gitData.getCommit(owner, repo, sha)).map(Option.apply)
        }
        .getOrElse(Sync[F].pure(none[RefCommit]))

    def getAllFiles: List[File] = Option(dirToCommit.listFiles()).toList.flatten.filter(_.isFile)

    def createTreeDataList(files: List[File]): F[List[TreeData]] = {

      def readFileAsGithub4sResponse(file: File): F[Array[Byte]] =
        Sync[F]
          .delay(fileReader.getFileBytes(file))
          .handleError(e =>
            throw IOException(s"Error loading ${file.getAbsolutePath} content", Some(e)))

      def path(file: File): F[String] =
        Sync[F].delay(FIO
          .relativize(baseDir, file)
          .getOrElse(throw GitHub4sException(s"Can't determine path for ${file.getAbsolutePath}")))

      def createTreeDataSha(
          filePath: String,
          array: Array[Byte]
      ): F[TreeDataSha] =
        for {
          gh <- ghWithRateLimit
          res <- run(gh.gitData.createBlob(owner, repo, array.toBase64, Some("base64")))
            .map(refInfo => TreeDataSha(filePath, blobMode, blobType, refInfo.sha))
        } yield res

      def createTreeDataBlob(
          filePath: String,
          array: Array[Byte]
      ): F[TreeDataBlob] =
        Sync[F].pure(TreeDataBlob(filePath, blobMode, blobType, new String(array)))

      def createTreeData(
          file: File,
          filePath: String,
          array: Array[Byte]
      ): OptionT[F, TreeData] =
        if (blobConfig.acceptedExtensions.exists(s => file.getName.toLowerCase.endsWith(s)) &&
          array.length < blobConfig.maximumSize) {
          OptionT(createTreeDataBlob(filePath, array).map(Option(_)))
        } else {
          OptionT(createTreeDataSha(filePath, array).map(Option(_)))
        }

      def processFile(file: File): F[TreeData] =
        (for {
          filePath <- OptionT(path(file).map(Option(_)))
          array    <- OptionT(readFileAsGithub4sResponse(file).map(Option(_)))
          treeData <- createTreeData(file, filePath, array)
        } yield treeData).value.map(_.getOrElse(throw UnexpectedException("Unable to get the TreeData")))

      files.traverse(processFile)
    }

    def createTree(
        baseTreeSha: Option[String],
        treeData: List[TreeData]
    ): F[TreeResult] =
      for {
        gh  <- ghWithRateLimit
        res <- run(gh.gitData.createTree(owner, repo, baseTreeSha, treeData))
      } yield res

    def createCommit(
        treeSha: String,
        parentCommit: String
    ): F[RefCommit] =
      for {
        gh <- ghWithRateLimit
        res <- run(
          gh.gitData.createCommit(owner, repo, message, treeSha, List(parentCommit), author = None)
        )
      } yield res

    def parentCommitSha: F[String] = commitSha match {
      case Some(sha) => Sync[F].pure(sha)
      case None      => fetchHeadCommit(branch).map(_.`object`.sha)
    }

    for {
      parentCommit <- parentCommitSha
      baseTree     <- fetchBaseTreeSha
      treeDataList <- createTreeDataList(getAllFiles)
      treeResult   <- createTree(baseTree.map(_.tree.sha), treeDataList)
      refCommit    <- createCommit(treeResult.sha, parentCommit)
    } yield refCommit
  }

  def fetchReference(ref: String): F[NonEmptyList[Ref]] =
    run(gh.gitData.getReference(owner, repo, ref))

  def fetchHeadCommit(branch: String): F[Ref] = {
    def findReference(refs: NonEmptyList[Ref]): Ref =
      refs.find(_.ref == s"refs/heads/$branch") match {
        case Some(ref) => ref
        case None =>
          val e = UnexpectedException(s"Branch $branch not found")
          throw GitHub4sException(s"GitHub returned an error: ${e.getMessage}", Some(e))
      }

    run(gh.gitData.getReference(owner, repo, s"heads/$branch")).map(findReference)
  }

  def updateHead(branch: String, commitSha: String): F[Ref] =
    run(gh.gitData.updateReference(owner, repo, s"heads/$branch", commitSha, force = false))

  def run[A](f: F[GHResponse[A]]): F[A] =
    f.map(_.result match {
      case Right(r) => r
      case Left(e)  => throw GitHub4sException("Error making request to GitHub", Some(e))
    })

  // Due to GitHub abuse rate limits, we should wait 1 sec between each request
  // https://developer.github.com/guides/best-practices-for-integrators/#dealing-with-abuse-rate-limits
  def ghWithRateLimit: F[Github[F]] = Timer[F].sleep(1.second).as(gh)
}
