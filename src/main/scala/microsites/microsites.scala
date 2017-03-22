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

package microsites

import net.jcazevedo.moultingyaml._
import sbt._

import scala.language.{postfixOps, reflectiveCalls}

case class KazariDependency(
    groupId: String,
    artifactId: String,
    scalaVersion: String,
    version: String)

case class KazariSettings(
    micrositeKazariEvaluatorUrl: String,
    micrositeKazariEvaluatorToken: String,
    micrositeKazariGithubToken: String,
    micrositeKazariCodeMirrorTheme: String,
    micrositeKazariDependencies: Seq[KazariDependency],
    micrositeKazariResolvers: Seq[String])

case class MicrositeIdentitySettings(
    name: String,
    description: String,
    author: String,
    homepage: String,
    twitter: String)

case class MicrositeFileLocations(
    micrositeImgDirectory: File,
    micrositeCssDirectory: File,
    micrositeJsDirectory: File,
    micrositeCDNDirectives: CdnDirectives,
    micrositeExternalLayoutsDirectory: File,
    micrositeExternalIncludesDirectory: File,
    micrositeDataDirectory: File,
    micrositeExtraMdFiles: Map[File, ExtraMdFileConfig])

case class MicrositeGitSettings(
    githubOwner: String,
    githubRepo: String,
    gitHostingService: MicrositeKeys.GitHostingService,
    gitHostingUrl: String)

case class MicrositeUrlSettings(micrositeBaseUrl: String, micrositeDocumentationUrl: String)

case class MicrositeFavicon(filename: String, sizeDescription: String)

case class MicrositeVisualSettings(
    highlightTheme: String,
    palette: Map[String, String],
    favicons: Seq[MicrositeFavicon])

case class MicrositeSettings(
    identity: MicrositeIdentitySettings,
    visualSettings: MicrositeVisualSettings,
    configYaml: ConfigYml,
    fileLocations: MicrositeFileLocations,
    urlSettings: MicrositeUrlSettings,
    gitSettings: MicrositeGitSettings,
    micrositeKazariSettings: KazariSettings) {

  def gitSiteUrl: String = {
    gitSettings.gitHostingService match {
      case MicrositeKeys.GitHub =>
        s"https://github.com/${gitSettings.githubOwner}/${gitSettings.githubRepo}"
      case _ => gitSettings.gitHostingUrl
    }
  }

  def gitHostingIconClass: String = {
    gitSettings.gitHostingService match {
      case MicrositeKeys.GitHub    => "fa-github"
      case MicrositeKeys.GitLab    => "fa-gitlab"
      case MicrositeKeys.Bitbucket => "fa-bitbucket"
      case _                       => "fa-git"
    }
  }
}

case class ExtraMdFileConfig(
    fileName: String,
    layout: String,
    metaProperties: Map[String, String] = Map.empty)

case class CdnDirectives(jsList: List[String] = Nil, cssList: List[String] = Nil)

case class ConfigYml(
    yamlCustomProperties: Map[String, Any] = Map.empty,
    yamlPath: Option[File] = None,
    yamlInline: String = ""
)

object ConfigYamlProtocol extends DefaultYamlProtocol {

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
