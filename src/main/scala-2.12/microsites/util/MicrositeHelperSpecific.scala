/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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

import java.io.File

import sbt.io._
import sbt.io.syntax._

trait MicrositeHelperSpecific {
  /*
   * This method has been extracted from the sbt-native-packager plugin:
   *
   * https://github.com/sbt/sbt-native-packager/blob/b5e2bb9027d08c00420476e6be0d876cf350963a/src/main/scala/com/typesafe/sbt/packager/MappingsHelper.scala#L21
   *
   */
  def directory(sourceDirPath: String): Seq[(File, String)] = {
    val sourceDir = file(sourceDirPath)
    Option(sourceDir.getParentFile)
      .map(parent => sourceDir.allPaths pair Path.relativeTo(parent))
      .getOrElse(sourceDir.allPaths pair Path.basic)
  }
}
