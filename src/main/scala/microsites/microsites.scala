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

import net.jcazevedo.moultingyaml._
import sbt._

import scala.language.postfixOps

package object microsites {

  case class MicrositeSettings(name: String,
                               description: String,
                               author: String,
                               homepage: String,
                               twitter: String,
                               highlightTheme: String,
                               micrositeConfigYaml: ConfigYaml,
                               micrositeYamlCustom: String,
                               micrositeImgDirectory: File,
                               micrositeCssDirectory: File,
                               micrositeJsDirectory: File,
                               micrositeExternalLayoutsDirectory: File,
                               micrositeExternalIncludesDirectory: File,
                               micrositeDataDirectory: File,
                               micrositeExtraMdFiles: Map[File, ExtraMdFileConfig],
                               micrositeBaseUrl: String,
                               micrositeDocumentationUrl: String,
                               palette: Map[String, String],
                               githubOwner: String,
                               githubRepo: String)

  case class ExtraMdFileConfig(fileName: String,
                               layout: String,
                               metaProperties: Map[String, String] = Map.empty)

  case class DefaultItem(scope: Map[String, String], values: Map[String, String])

  case class CollectionItem(output: Boolean, values: Map[String, String])

  case class ConfigYaml(name: String,
                        description: String,
                        version: String,
                        org: String,
                        baseurl: String,
                        docs: Boolean = true,
                        markdown: String = "kramdown",
                        highlighter: String = "rouge",
                        defaults: List[DefaultItem] = List.empty,
                        collections: Map[String, CollectionItem] = Map.empty)

  object ConfigYamlProtocol extends DefaultYamlProtocol {

    implicit def defaultItemFormat: YamlFormat[DefaultItem] = yamlFormat2(DefaultItem)

    implicit class RichYamlValue(value: YamlValue) {
      def toStr: String = value.convertTo[String]
    }

    implicit object CollectionItemFormat extends YamlFormat[CollectionItem] {
      def write(c: CollectionItem): YamlValue = {
        val outputYamlValue: (YamlString, YamlBoolean) = YamlString("output") -> YamlBoolean(
            c.output)
        val dynamicValues: Map[YamlValue, YamlValue] = c.values.map {
          case (k, v) => k.toYaml -> v.toYaml
        }
        YamlObject(dynamicValues + outputYamlValue)
      }

      def read(value: YamlValue): CollectionItem = {
        val valueAsObject                        = value.asYamlObject
        val fieldsMap: Map[YamlValue, YamlValue] = valueAsObject.fields

        val outputValue: Boolean = valueAsObject.getFields(YamlString("output")) match {
          case Seq(YamlBoolean(outputBool)) => outputBool
          case _                            => deserializationError("output boolean value expected")
        }

        val dynamicValues: Map[String, String] =
          fieldsMap.keys.filterNot(_.toStr == "output") map { key =>
            (key.toStr, fieldsMap.get(key).fold("")(_.toStr))
          } toMap

        CollectionItem(outputValue, dynamicValues)
      }
    }

    implicit def configYamlFormat: YamlFormat[ConfigYaml] = yamlFormat10(ConfigYaml)
  }

}
