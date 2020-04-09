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

package microsites.util

import net.jcazevedo.moultingyaml._

object YamlFormats extends DefaultYamlProtocol {

  implicit object AnyYamlFormat extends YamlFormat[Any] {
    def write(x: Any): YamlValue = x match {
      case n: Int            => YamlNumber(n)
      case n: Long           => YamlNumber(n)
      case n: Double         => YamlNumber(n)
      case s: String         => YamlString(s)
      case b: Boolean        => YamlBoolean(b)
      case x: Seq[_]         => seqFormat[Any].write(x)
      case m: Map[String, _] => mapFormat[String, Any].write(m)
      case t =>
        serializationError("Serialization Error - Non expected type " + t.getClass.getName)
    }

    def read(value: YamlValue): Any = value match {
      case YamlNumber(n)  => n.intValue()
      case YamlString(s)  => s
      case YamlBoolean(b) => b
      case _: YamlArray   => listFormat[Any].read(value)
      case _: YamlObject  => mapFormat[String, Any].read(value)
      case x =>
        deserializationError("Deserialization Error - it failed the deserialization of " + x)
    }
  }
}
