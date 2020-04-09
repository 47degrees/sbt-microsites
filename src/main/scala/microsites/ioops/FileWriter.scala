package microsites.ioops

import java.io.{File, FileOutputStream}
import java.net.URL
import java.nio.file.Files.{copy => fcopy}
import java.nio.file.Path
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.util.zip.ZipInputStream

import cats.instances.either._
import cats.instances.list._
import cats.syntax.either._
import cats.syntax.traverse._
import microsites.Exceptions._
import syntax._

class FileWriter {

  def writeContentToFile(content: String, output: String): IOResult[Unit] = {

    def writeFile: Either[Throwable, Unit] =
      Either.catchNonFatal(IOUtils.write(IOUtils.file(output), content))

    (for {
      result <- createFile(output)
      _      <- writeFile if result
    } yield ()).leftMap(e => IOException(s"Error writing to file $output", Some(e)))

  }

  def createFile(file: File): IOResult[Boolean] = {

    def parentDirExists(f: File): Boolean = f.getParentFile.exists() || f.getParentFile.mkdirs()

    Either
      .catchNonFatal(file.exists() || (parentDirExists(file) && file.createNewFile()))
      .leftMap(e => IOException(s"Error creating file", Some(e)))
  }

  def createFile(output: String): IOResult[Boolean] = createFile(output.toFile)

  def createDir(output: File): IOResult[Boolean] =
    Either
      .catchNonFatal(output.exists() || output.mkdir())
      .leftMap(e => IOException(s"Error creating directory $output", Some(e)))

  def createDir(output: String): IOResult[Boolean] =
    createDir(output.fixPath.ensureFinalSlash.toFile)

  def copy(input: String, output: String): IOResult[Path] = {

    def safeCopy: Either[Throwable, Path] =
      Either
        .catchNonFatal(fcopy(input.toPath, output.toPath, REPLACE_EXISTING))

    (for {
      result <- createFile(output)
      path   <- safeCopy if result
    } yield path)
      .leftMap(e => IOException(s"Error copying file from $input to $output", Some(e)))
  }

  def copyFilesRecursively(sourcePath: String, outputPath: String): IOResult[List[Path]] = {

    def copySingleFile(f: File): IOResult[Path] = {
      val filePath = f.getAbsolutePath.replaceAllLiterally(sourcePath, "")
      copy(f.getAbsolutePath, s"${outputPath.ensureFinalSlash}$filePath")
    }

    def copyMultipleFiles(files: List[File]): IOResult[List[Path]] =
      files.traverse(copySingleFile)

    for {
      files <- FileReader.fetchFilesRecursivelyFromPath(sourcePath)
      paths <- copyMultipleFiles(files)
    } yield paths
  }

  def copyJARResourcesTo(
      jarUrl: URL,
      output: String,
      filter: String = ""
  ): Either[IOException, Unit] =
    Either
      .catchNonFatal {
        output.fixPath.toFile.mkdir()

        val zipIn = new ZipInputStream(jarUrl.openStream())

        val buffer = new Array[Byte](1024)
        Stream.continually(zipIn.getNextEntry).takeWhile(_ != null).foreach { entry =>
          val newFile = IOUtils.file(output + File.separator + entry.getName)

          (
            entry.isDirectory,
            !newFile.exists() &&
              newFile.getAbsolutePath.startsWith(s"$output$filter")
          ) match {
            case (true, true) => createDir(newFile)
            case (true, _)    =>
            case (false, true) =>
              createFile(newFile)

              val fos = new FileOutputStream(newFile)

              Stream.continually(zipIn.read(buffer)).takeWhile(_ != -1).foreach { count =>
                fos.write(buffer, 0, count)
              }

              fos.close()
            case _ =>
          }
        }
      }
      .leftMap(e =>
        IOException(s"Error copying resources from $jarUrl to directory $output", Some(e))
      )
}

object FileWriter extends FileWriter
