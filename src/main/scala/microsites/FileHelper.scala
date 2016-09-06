package microsites

import java.io.{File, FileOutputStream}
import java.net.URL
import java.nio.file.Files.copy
import java.nio.file.Path
import java.nio.file.Paths.get
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.util.zip.ZipInputStream

trait FileHelper {

  implicit class FileNameImplicit(filename: String) {
    def toPath = get(filename)

    def toFile = new File(filename.fixPath)

    def fixPath = filename.replaceAll("/", File.separator)
  }

  def getPathWithSlash(f: File): String =
    f.getAbsolutePath + (if (f.getAbsolutePath.endsWith(File.separator)) "" else File.separator)

  def copyFilesRecursively(sourcePath: String, targetDirPath: String): Unit = {

    val source = sourcePath.toFile
    fetchFilesRecursively(source) foreach { f =>
      val filePath = f.getAbsolutePath.replaceAll(sourcePath, "")
      copyFile(f, s"$targetDirPath$filePath".toFile)
    }
  }

  def copyPluginResources(jarUrl: URL, output: String, filter: String = "styles") = {
    output.fixPath.toFile.mkdir()

    val zipIn = new ZipInputStream(jarUrl.openStream())

    val buffer = new Array[Byte](1024)
    Stream
      .continually(zipIn.getNextEntry)
      .takeWhile(_ != null)
      .foreach { entry =>

        val newFile = new File(output + File.separator + entry.getName)

        (entry.isDirectory,
          !newFile.exists() &&
            newFile.getAbsolutePath.startsWith(s"$output$filter")) match {
          case (true, true) => newFile.mkdir()
          case (true, _) =>
          case (false, true) =>
            createFileIfNotExists(newFile)

            val fos = new FileOutputStream(newFile)

            Stream.continually(zipIn.read(buffer))
              .takeWhile(_ != -1)
              .foreach { count =>
                fos.write(buffer, 0, count)
              }

            fos.close()
          case _ =>
        }
      }
  }

  def fetchFilesRecursively(directory: File): List[File] = {

    val listFiles = Option(directory.listFiles)

    listFiles match {
      case Some(firstLevelFiles) =>
        val onlyFirstLevelFiles = firstLevelFiles filter (_.isFile)
        val firstLevelDirectories = firstLevelFiles filter (_.isDirectory)

        (onlyFirstLevelFiles ++ firstLevelDirectories.flatMap(d => fetchFilesRecursively(d))).toList
      case None =>
        Nil
    }
  }

  def copyFile(sourceFile: File, targetFile: File): Path = {

    createFileIfNotExists(targetFile)

    copy(sourceFile.toPath, targetFile.toPath, REPLACE_EXISTING)
  }

  def createFilePathIfNotExists(f: String): File =
    createFileIfNotExists(f.toFile)

  def createFileIfNotExists(f: File): File = {

    if (!f.exists()) {
      if (!f.getParentFile.exists())
        f.getParentFile.mkdirs()
      f.createNewFile()
    }
    f
  }
}

object FileHelper extends FileHelper