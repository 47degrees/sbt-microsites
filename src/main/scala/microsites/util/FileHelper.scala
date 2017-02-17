/*
 * Copyright 2016 47 Degrees, LLC. <http://www.47deg.com>
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

package microsites.util

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
    f.getAbsolutePath + (if (f.getAbsolutePath.endsWith(File.separator)) ""
                         else File.separator)

  def copyFilesRecursively(sourcePath: String, targetDirPath: String): Unit = {

    val source = sourcePath.toFile

    if (source.isFile) copyFile(source, s"$targetDirPath".toFile)
    else
      fetchFilesRecursively(source) foreach { f =>
        val filePath = f.getAbsolutePath.replaceAll(sourcePath, "")
        copyFile(f, s"$targetDirPath$filePath".toFile)
      }
  }

  def copyPluginResources(jarUrl: URL, output: String, filter: String = "styles") = {
    output.fixPath.toFile.mkdir()

    val zipIn = new ZipInputStream(jarUrl.openStream())

    val buffer = new Array[Byte](1024)
    Stream.continually(zipIn.getNextEntry).takeWhile(_ != null).foreach { entry =>
      val newFile = new File(output + File.separator + entry.getName)

      (entry.isDirectory,
       !newFile.exists() &&
         newFile.getAbsolutePath.startsWith(s"$output$filter")) match {
        case (true, true) => newFile.mkdir()
        case (true, _)    =>
        case (false, true) =>
          createFileIfNotExists(newFile)

          val fos = new FileOutputStream(newFile)

          Stream.continually(zipIn.read(buffer)).takeWhile(_ != -1).foreach { count =>
            fos.write(buffer, 0, count)
          }

          fos.close()
        case _ =>
      }
    }
  }

  def fetchFilesRecursively(directory: File,
                            includeFilesExtension: List[String] = Nil): List[File] = {

    val listFiles = Option(directory.listFiles)

    listFiles match {
      case Some(firstLevelFiles) =>
        val filteredFilesByExtension = includeFilesExtension match {
          case Nil => firstLevelFiles.toList
          case _ =>
            includeFilesExtension flatMap { extension =>
              firstLevelFiles filter (_.getName.endsWith(extension))
            }
        }

        val onlyFirstLevelFiles   = filteredFilesByExtension filter (_.isFile)
        val firstLevelDirectories = filteredFilesByExtension filter (_.isDirectory)

        onlyFirstLevelFiles ++ firstLevelDirectories.flatMap(d => fetchFilesRecursively(d))
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
