package microsites.ioops

import java.io.File

import cats.syntax.either._
import microsites.Exceptions._
import microsites.ioops.syntax._

import scala.annotation.tailrec

class FileReader {

  def exists(path: String): Boolean =
    Either
      .catchNonFatal(IOUtils.file(path).exists()) getOrElse false

  def getFileContent(filePath: String): String =
    IOUtils.readLines(IOUtils.file(filePath)).mkString("\n")

  def getFileBytes(file: File): Array[Byte] =
    IOUtils.readBytes(file)

  private[this] val defaultValidDirs: (File) => Boolean = (f: File) => {
    !Set("target", "bin", "output").contains(f.getName) && !f.getName.startsWith(".")
  }

  def fetchFilesRecursivelyFromPath(
      sourcePath: String,
      isFileSupported: (File) => Boolean = _ => true,
      isDirSupported: (File) => Boolean = defaultValidDirs
  ): IOResult[List[File]] =
    fetchFilesRecursively(List(sourcePath.toFile))

  def fetchFilesRecursively(
      in: List[File],
      isFileSupported: (File) => Boolean = _ => true,
      isDirSupported: (File) => Boolean = defaultValidDirs
  ): IOResult[List[File]] =
    Either
      .catchNonFatal {
        @tailrec
        def findAllFiles(
            in: List[File],
            isFileSupported: (File) => Boolean,
            isDirSupported: (File) => Boolean,
            processedFiles: List[File] = Nil,
            processedDirs: List[String] = Nil
        ): List[File] = {

          val allFiles: List[File] = processedFiles ++ in.filter(f =>
            f.exists && f.isFile && isFileSupported(f)
          )

          in.filter { f =>
            f.isDirectory &&
            isDirSupported(f) &&
            !processedDirs.contains(f.getCanonicalPath)
          } match {
            case Nil => allFiles
            case list =>
              val subFiles = list.flatMap(_.listFiles().toList)
              findAllFiles(
                subFiles,
                isFileSupported,
                isDirSupported,
                allFiles,
                processedDirs ++ list.map(_.getCanonicalPath)
              )
          }
        }

        findAllFiles(in, isFileSupported, isDirSupported)
      }
      .leftMap(e => IOException(s"Error fetching files recursively", Some(e)))

  def fetchDirsRecursively(
      in: List[File],
      isDirSupported: (File) => Boolean = defaultValidDirs
  ): List[File] = {
        @tailrec
        def findAllDirs(
            in: List[File],
            isDirSupported: (File) => Boolean,
            processedDirs: List[File] = Nil
        ): List[File] = {

          in.filter { f =>
            f.isDirectory &&
            isDirSupported(f) &&
            !processedDirs.map(_.getCanonicalPath).contains(f.getCanonicalPath)
          } match {
            case Nil => processedDirs
            case list =>
              val subDirs = list.flatMap(_.listFiles().toList)
              findAllDirs(subDirs, isDirSupported, processedDirs ++ list)
          }
        }

        findAllDirs(in, isDirSupported)
      }
}

object FileReader extends FileReader
