/*
 * Copyright 2016-2021 47 Degrees Open Source <https://www.47deg.com>
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

trait BuildHelper {

  def buildWithoutSuffix(scalaVersion: String): String =
    scalaVersion match {
      case scalaV if scalaV.startsWith("2.12") => "2.12"
      case scalaV if scalaV.startsWith("2.11") => "2.11"
      case scalaV                              => scalaV
    }

}

object BuildHelper extends BuildHelper
