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

import sbt._

trait MicrositeKeys {

  sealed abstract class GitHostingService(val name: String) extends Product with Serializable
  final case object GitHub                                  extends GitHostingService("GitHub")
  final case object GitLab                                  extends GitHostingService("GitLab")
  final case object Bitbucket                               extends GitHostingService("Bitbucket")
  final case class Other(value: String)                     extends GitHostingService(value)

  object GitHostingService {
    implicit def string2GitHostingService(name: String) = {
      List(GitHub, GitLab, Bitbucket)
        .find(_.name.toLowerCase == name.toLowerCase)
        .getOrElse(Other(name))
    }
  }

  val makeMicrosite = taskKey[Unit]("Main Task to build a Microsite")
  val publishMicrosite =
    taskKey[Unit]("Publish the microsite (using the pushSite task) after build it")
  val microsite                 = taskKey[Seq[File]]("Create microsite files")
  val micrositeConfig           = taskKey[Unit]("Copy microsite config to the site folder")
  val micrositeName             = settingKey[String]("Microsite name")
  val micrositeDescription      = settingKey[String]("Microsite description")
  val micrositeAuthor           = settingKey[String]("Microsite author")
  val micrositeHomepage         = settingKey[String]("Microsite homepage")
  val micrositeTwitter          = settingKey[String]("Microsite twitter")
  val micrositeBaseUrl          = settingKey[String]("Microsite site base url")
  val micrositeDocumentationUrl = settingKey[String]("Microsite site documentation url")
  val micrositeHighlightTheme   = settingKey[String]("Microsite Highlight Theme")
  val micrositeConfigYaml       = settingKey[ConfigYml]("Microsite _config.yml file configuration.")
  val micrositeImgDirectory     = settingKey[File](
    "Optional. Microsite images directory. By default, it'll be the resourcesDirectory + '/microsite/img'")
  val micrositeCssDirectory     = settingKey[File](
    "Optional. Microsite CSS directory. By default, it'll be the resourcesDirectory + '/microsite/css'")
  val micrositeJsDirectory      = settingKey[File](
    "Optional. Microsite Javascript directory. By default, it'll be the resourcesDirectory + '/microsite/js'")
  val micrositeCDNDirectives    = settingKey[CdnDirectives](
    "Optional. Microsite CDN directives lists (for css and js imports). By default, both lists are empty.")
  val micrositeExternalLayoutsDirectory = settingKey[File](
    "Optional. Microsite external layouts directory. By default, it'll be the resourcesDirectory + '/microsite/layout'")
  val micrositeExternalIncludesDirectory = settingKey[File](
    "Optional. Microsite external includes (partial layouts) directory. By default, it'll be the resourcesDirectory + '/microsite/includes'")
  val micrositeDataDirectory    = settingKey[File](
    "Optional. Microsite Data directory, useful to define the microsite data files " +
      "(https://jekyllrb.com/docs/datafiles/). By default, it'll be the resourcesDirectory + '/microsite/data'")
  val micrositeExtraMdFiles     = settingKey[Map[File, ExtraMdFileConfig]](
    "Optional. This key is useful when you want to include automatically markdown documents as a part of your microsite, and these files are located in different places from the tutSourceDirectory. The map key is related with the source file, the map value corresponds with the target relative file path and the document meta-information configuration. By default, the map is empty.")
  val micrositePalette          = settingKey[Map[String, String]]("Microsite palette")
  val micrositeFaviconFilename  = settingKey[Option[String]](
    "Optional. Filename for the PNG/ICO file to be used as favicon for the generated site, located in '/microsite/img'. By default it won't be provided.")
  val micrositeGithubOwner      = settingKey[String]("Microsite Github owner")
  val micrositeGithubRepo       = settingKey[String]("Microsite Github repo")
  val micrositeGitHostingService =
    settingKey[GitHostingService]("Service used for git hosting. By default, it'll be GitHub.")
  val micrositeGitHostingUrl    = settingKey[String](
    "In the case where your project isn't hosted on Github, use this setting to point users to git host (e.g. 'https://internal.gitlab.com/<user>/<project>').")
}
object MicrositeKeys extends MicrositeKeys
