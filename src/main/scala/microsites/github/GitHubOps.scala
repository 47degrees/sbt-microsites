/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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
import cats.effect.{Ref as _, *}
import cats.effect.kernel.Temporal
import cats.implicits.*
import com.github.marklister.base64.Base64.*
import github4s.*
import github4s.domain.*
import microsites.Exceptions.*
import microsites.github.Config.*
import microsites.ioops.{FileReader, IOUtils as FIO}
import org.http4s.client.*

import scala.concurrent.duration.*
import scala.language.higherKinds

class GitHubOps[F[_]: Async: Temporal](
    client: Client[F],
    owner: String,
    repo: String,
    accessToken: Option[String],
    fileReader: FileReader = FileReader
)(implicit val config: GithubConfig) {

  private val gh: Github[F]                = Github[F](client, accessToken)
  private val headers: Map[String, String] = Map("user-agent" -> "sbt-microsites")

  def commitDir(branch: String, message: String, dir: File): F[Ref] =
    Sync[F]
      .delay(fileReader.fetchDirsRecursively(List(dir)))
      .flatMap {
        case Nil =>
          IOException(s"Nothing to commit in dir ${dir.getAbsolutePath}").raiseError[F, Ref]
        case h :: t =>
          commitDirAux(branch, message, dir, NonEmptyList(h, t))
      }
      .handleErrorWith { e =>
        IOException(s"Error fetching files recursively", Some(e)).raiseError[F, Ref]
      }

  private def commitDirAux(
      branch: String,
      message: String,
      baseDir: File,
      dirList: NonEmptyList[File],
      blobConfig: BlobConfig = defaultBlobConfig
  ): F[Ref] = {

    def updateCommitDirH(dir: File, sha: Option[String]): F[RefCommit] =
      updateCommitDir(branch, message, baseDir, dir, blobConfig, sha)

    val processAllFiles: F[RefCommit] =
      dirList.reduceLeftM(dirHead => updateCommitDirH(dirHead, None)) { (commit, dir) =>
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
  ): F[RefCommit] = {

    def fetchBaseTreeSha: F[Option[RefCommit]] =
      commitSha
        .map(sha => run(gh.gitData.getCommit(owner, repo, sha, headers)).map(Option.apply))
        .getOrElse(Sync[F].pure(none[RefCommit]))

    def getAllFiles: List[File] = Option(dirToCommit.listFiles()).toList.flatten.filter(_.isFile)

    def createTreeDataList(files: List[File]): F[List[TreeData]] = {

      def readFileAsGithub4sResponse(file: File): F[Array[Byte]] =
        fileReader
          .getFileBytes(file)
          .handleError(e =>
            throw IOException(s"Error loading ${file.getAbsolutePath} content", Some(e))
          )

      def path(file: File): F[String] =
        Sync[F].delay(
          FIO
            .relativize(baseDir, file)
            .getOrElse(throw GitHub4sException(s"Can't determine path for ${file.getAbsolutePath}"))
        )

      def createTreeDataSha(
          filePath: String,
          array: Array[Byte]
      ): F[TreeDataSha] =
        for {
          gh <- ghWithRateLimit
          res <- run(gh.gitData.createBlob(owner, repo, array.toBase64, Some("base64"), headers))
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
        if (
          blobConfig.acceptedExtensions.exists(s => file.getName.toLowerCase.endsWith(s)) &&
          array.length < blobConfig.maximumSize
        )
          OptionT(createTreeDataBlob(filePath, array).map(Option(_)))
        else
          OptionT(createTreeDataSha(filePath, array).map(Option(_)))

      def processFile(file: File): F[TreeData] =
        (for {
          filePath <- OptionT(path(file).map(Option(_)))
          array    <- OptionT(readFileAsGithub4sResponse(file).map(Option(_)))
          treeData <- createTreeData(file, filePath, array)
        } yield treeData).value
          .map(_.getOrElse(throw UnexpectedException("Unable to get the TreeData")))

      files.traverse(processFile)
    }

    def createTree(
        baseTreeSha: Option[String],
        treeData: List[TreeData]
    ): F[TreeResult] =
      for {
        gh  <- ghWithRateLimit
        res <- run(gh.gitData.createTree(owner, repo, baseTreeSha, treeData, headers))
      } yield res

    def createCommit(
        treeSha: String,
        parentCommit: String
    ): F[RefCommit] =
      for {
        gh <- ghWithRateLimit
        res <- run(
          gh.gitData
            .createCommit(owner, repo, message, treeSha, List(parentCommit), author = None, headers)
        )
      } yield res

    def parentCommitSha: F[String] =
      commitSha match {
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

  private def fetchHeadCommit(branch: String): F[Ref] = {
    def findReference(refs: NonEmptyList[Ref]): Ref =
      refs.find(_.ref == s"refs/heads/$branch") match {
        case Some(ref) => ref
        case None =>
          val e = UnexpectedException(s"Branch $branch not found")
          throw GitHub4sException(s"GitHub returned an error: ${e.getMessage}", Some(e))
      }

    run(
      gh.gitData.getReference(owner, repo, s"heads/$branch", pagination = None, headers = headers)
    ).map(findReference)
  }

  private def updateHead(branch: String, commitSha: String): F[Ref] =
    run(
      gh.gitData.updateReference(owner, repo, s"heads/$branch", commitSha, force = false, headers)
    )

  private def run[A](f: F[GHResponse[A]]): F[A] =
    f.map(_.result match {
      case Right(r) => r
      case Left(e)  => throw GitHub4sException("Error making request to GitHub", Some(e))
    })

  // Due to GitHub abuse rate limits, we should wait 1 sec between each request
  // https://developer.github.com/guides/best-practices-for-integrators/#dealing-with-abuse-rate-limits
  private def ghWithRateLimit: F[Github[F]] = Temporal[F].sleep(1.second).as(gh)
}
