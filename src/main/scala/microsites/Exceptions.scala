/*
 * Copyright 2016-2022 47 Degrees Open Source <https://www.47deg.com>
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

package microsites

object Exceptions {

  sealed abstract class MicrositesException(val message: String, val maybeCause: Option[Throwable])
      extends RuntimeException(message)
      with Product
      with Serializable {

    maybeCause foreach initCause

    override def toString: String = message
  }

  case class IOException(msg: String, cause: Option[Throwable] = None)
      extends MicrositesException(msg, cause)

  case class GitHub4sException(msg: String, cause: Option[Throwable] = None)
      extends MicrositesException(msg, cause)

  final case class UnexpectedException(msg: String, cause: Option[Throwable] = None)
      extends MicrositesException(msg, cause)
}
