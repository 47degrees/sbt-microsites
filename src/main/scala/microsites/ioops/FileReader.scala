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

package microsites.ioops

import java.io.File

import cats.effect.Sync
import cats.syntax.either._
import microsites.Exceptions._
import microsites.ioops.syntax._

import scala.annotation.tailrec

class FileReader {

  private[this] val defaultValidDirs: (File) => Boolean = (f: File) =>
    !Set("target", "bin", "output").contains(f.getName) && !f.getName.startsWith(".")

  def getFileBytes[F[_]: Sync](file: File): F[Array[Byte]] =
    Sync[F].delay(IOUtils.readBytes(file))

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

          val allFiles: List[File] =
            processedFiles ++ in.filter(f => f.exists && f.isFile && isFileSupported(f))

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
