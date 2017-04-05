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

package kazari.codemirror

import org.denigma.codemirror.Position

import scala.scalajs.js.annotation.ScalaJSDefined

object PositionBuilder {
  def apply(_ch: Int, _line: Int): Position = {
    scalajs.js.Dynamic
      .literal(
        ch = _ch,
        line = _line
      )
      .asInstanceOf[Position]
  }
}

@ScalaJSDefined
trait CodeMirrorCharCords extends scalajs.js.Object {
  val left: Double
  val right: Double
  val top: Double
  val bottom: Double
}
