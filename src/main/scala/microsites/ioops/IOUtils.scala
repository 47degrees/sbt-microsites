/*
 * Copyright 2016-2020 47 Degrees Open Source <http://47deg.com>
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

import java.io._
import scala.io.Source
import java.net.URL
import java.nio.charset.Charset

object IOUtils {

  def file(path: String): File = new File(path)

  def readBytes(file: File): Array[Byte] = {
    val is: InputStream    = new FileInputStream(file)
    val array: Array[Byte] = Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
    is.close()
    array
  }

  def write(file: File, content: String, charset: Charset = Charset.forName("UTF-8")): Unit = {
    val writer = new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(file, false), charset)
    )
    writer.write(content)
    writer.close()
  }

  def relativize(base: File, file: File): Option[String] = {

    def ensureEndingSlash: Option[String] = {
      val path = base.getAbsolutePath
      path.lastOption.map {
        case c if c == File.separatorChar => path
        case _                            => path + File.separatorChar
      }
    }

    val baseFileString = if (base.isDirectory) ensureEndingSlash else None
    val pathString     = file.getAbsolutePath
    baseFileString flatMap {
      case baseString if pathString.startsWith(baseString) =>
        Some(pathString.substring(baseString.length))
      case _ => None
    }
  }

}
