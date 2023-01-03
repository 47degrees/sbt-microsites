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

import sbt._

case class MicrositeIdentitySettings(
    name: String,
    description: String,
    author: String,
    homepage: String,
    organizationHomepage: String,
    twitter: String,
    twitterCreator: String,
    analytics: String
)

case class MicrositeFileLocations(
    micrositeImgDirectory: File,
    micrositeCssDirectory: File,
    micrositeSassDirectory: File,
    micrositeJsDirectory: File,
    micrositeCDNDirectives: CdnDirectives,
    micrositeExternalLayoutsDirectory: File,
    micrositeExternalIncludesDirectory: File,
    micrositeDataDirectory: File,
    micrositeStaticDirectory: File,
    micrositeExtraMdFiles: Map[File, ExtraMdFileConfig],
    micrositeExtraMdFilesOutput: File,
    micrositePluginsDirectory: File
)

case class MicrositeGitSettings(
    githubOwner: String,
    githubRepo: String,
    githubLinks: Boolean,
    gitHostingService: GitHostingService,
    gitHostingUrl: String,
    gitSidecarChat: Boolean,
    gitSidecarChatUrl: String
)

case class MicrositeUrlSettings(
    micrositeUrl: String,
    micrositeBaseUrl: String,
    micrositeDocumentationUrl: String,
    micrositeDocumentationLabelDescription: String,
    micrositeHomeButtonTarget: String
)

case class MicrositeFavicon(filename: String, sizeDescription: String)

case class MicrositeVisualSettings(
    highlightTheme: String,
    highlightLanguages: Seq[String],
    palette: Map[String, String],
    favicons: Seq[MicrositeFavicon],
    shareOnSocial: Boolean,
    theme: String
)

case class MicrositeTemplateTexts(footer: Option[String])

case class MicrositeEditButton(text: String, basePath: String)

case class MicrositeEditButtonSettings(button: Option[MicrositeEditButton])

case class MicrositeMultiversionSettings(versionList: Seq[String])

final case class MicrositeSearchSettings(searchEnabled: Boolean)

case class MicrositeSettings(
    identity: MicrositeIdentitySettings,
    visualSettings: MicrositeVisualSettings,
    templateTexts: MicrositeTemplateTexts,
    configYaml: ConfigYml,
    fileLocations: MicrositeFileLocations,
    urlSettings: MicrositeUrlSettings,
    gitSettings: MicrositeGitSettings,
    editButtonSettings: MicrositeEditButtonSettings,
    multiversionSettings: MicrositeMultiversionSettings,
    searchSettings: MicrositeSearchSettings
) {

  def gitSiteUrl: String = {
    (gitSettings.gitHostingService, gitSettings.gitHostingUrl) match {
      case (GitHub, "") =>
        s"https://github.com/${gitSettings.githubOwner}/${gitSettings.githubRepo}"
      case _ => gitSettings.gitHostingUrl
    }
  }

  def gitHostingIconClass: String = {
    gitSettings.gitHostingService match {
      case GitHub    => "fa-github"
      case GitLab    => "fa-gitlab"
      case Bitbucket => "fa-bitbucket"
      case _         => "fa-git"
    }
  }
}

case class ExtraMdFileConfig(
    fileName: String,
    layout: String,
    metaProperties: Map[String, String] = Map.empty
)

case class CdnDirectives(jsList: List[String] = Nil, cssList: List[String] = Nil)

case class ConfigYml(
    yamlCustomProperties: Map[String, Any] = Map.empty,
    yamlPath: Option[File] = None,
    yamlInline: String = ""
)
