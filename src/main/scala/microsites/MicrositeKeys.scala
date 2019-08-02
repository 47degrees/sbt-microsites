/*
 * Copyright 2016-2019 47 Degrees, LLC. <http://www.47deg.com>
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

import com.typesafe.sbt.sbtghpages.GhpagesPlugin.autoImport.{
  ghpagesBranch,
  ghpagesNoJekyll,
  ghpagesPushSite
}
import com.typesafe.sbt.site.SitePlugin.autoImport.{makeSite, siteDirectory}
import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._
import microsites.util.MicrositeHelper
import sbt.Keys._
import sbt._
import sbt.complete.DefaultParsers.OptNotSpace
import sbtorgpolicies.github.GitHubOps
import tut.TutPlugin.autoImport._
import mdoc.MdocPlugin.autoImport._

trait MicrositeKeys {

  sealed abstract class GitHostingService(val name: String) extends Product with Serializable
  final case object GitHub                                  extends GitHostingService("GitHub")
  final case object GitLab                                  extends GitHostingService("GitLab")
  final case object Bitbucket                               extends GitHostingService("Bitbucket")
  final case class Other(value: String)                     extends GitHostingService(value)

  sealed abstract class PushWith(val name: String) extends Product with Serializable
  final case object GHPagesPlugin                  extends PushWith("ghPagesPlugin")
  final case object GitHub4s                       extends PushWith("github4s")

  sealed abstract class CompilingDocsTool extends Product with Serializable
  final case object WithTut               extends CompilingDocsTool
  final case object WithMdoc              extends CompilingDocsTool

  object GitHostingService {
    implicit def string2GitHostingService(name: String): GitHostingService = {
      List(GitHub, GitLab, Bitbucket)
        .find(_.name.toLowerCase == name.toLowerCase)
        .getOrElse(Other(name))
    }
  }

  val makeMicrosite: TaskKey[Unit] = taskKey[Unit]("Main Task to build a Microsite")
  val makeTut: TaskKey[Unit]       = taskKey[Unit]("Sequential tasks to compile tut and move the result")
  val makeMdoc: TaskKey[Unit] =
    taskKey[Unit]("Sequential tasks to compile mdoc and move the result")
  val publishMicrosite: TaskKey[Unit] =
    taskKey[Unit]("Task helper that wraps the `publishMicrositeCommand`.")
  val microsite: TaskKey[Seq[File]] = taskKey[Seq[File]]("Create microsite files")
  val micrositeConfig: TaskKey[Unit] =
    taskKey[Unit]("Copy microsite config to the site folder")
  val micrositeMakeExtraMdFiles: TaskKey[File] =
    taskKey[File]("Create microsite extra md files")
  val micrositeTutExtraMdFiles: TaskKey[Seq[File]] =
    taskKey[Seq[File]]("Run tut for extra microsite md files")
  val micrositeMdocExtraMdFiles: TaskKey[Seq[File]] =
    taskKey[Seq[File]]("Run mdoc for extra microsite md files")
  val micrositeName: SettingKey[String]        = settingKey[String]("Microsite name")
  val micrositeDescription: SettingKey[String] = settingKey[String]("Microsite description")
  val micrositeAuthor: SettingKey[String]      = settingKey[String]("Microsite author")
  val micrositeHomepage: SettingKey[String]    = settingKey[String]("Microsite homepage")
  val micrositeOrganizationHomepage: SettingKey[String] =
    settingKey[String]("Microsite organisation homepage")
  val micrositeTwitter: SettingKey[String]        = settingKey[String]("Microsite twitter")
  val micrositeTwitterCreator: SettingKey[String] = settingKey[String]("Microsite twitter")
  val micrositeShareOnSocial: SettingKey[Boolean] = settingKey[Boolean](
    "Optional. Includes links to share on social media in the layout. Enabled by default."
  )
  val micrositeUrl: SettingKey[String]     = settingKey[String]("Microsite site absolute url prefix")
  val micrositeBaseUrl: SettingKey[String] = settingKey[String]("Microsite site base url")
  val micrositeDocumentationUrl: SettingKey[String] =
    settingKey[String]("Microsite site documentation url")
  val micrositeDocumentationLabelDescription: SettingKey[String] =
    settingKey[String]("Microsite site documentation Label Description")
  val micrositeHighlightTheme: SettingKey[String] = settingKey[String]("Microsite Highlight Theme")
  val micrositeHighlightLanguages: SettingKey[Seq[String]] =
    settingKey[Seq[String]]("Microsite Highlight Languages")
  val micrositeConfigYaml: SettingKey[ConfigYml] =
    settingKey[ConfigYml]("Microsite _config.yml file configuration.")
  val micrositeImgDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite images directory. By default, it'll be the resourcesDirectory + '/microsite/img'")
  val micrositeCssDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite CSS directory. By default, it'll be the resourcesDirectory + '/microsite/css'")
  val micrositeJsDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite Javascript directory. By default, it'll be the resourcesDirectory + '/microsite/js'")
  val micrositeCDNDirectives: SettingKey[CdnDirectives] = settingKey[CdnDirectives](
    "Optional. Microsite CDN directives lists (for css and js imports). By default, both lists are empty.")
  val micrositeExternalLayoutsDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite external layouts directory. By default, it'll be the resourcesDirectory + '/microsite/layout'")
  val micrositeExternalIncludesDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite external includes (partial layouts) directory. By default, it'll be the resourcesDirectory + '/microsite/includes'")
  val micrositeDataDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite Data directory, useful to define the microsite data files " +
      "(https://jekyllrb.com/docs/datafiles/). By default, it'll be the resourcesDirectory + '/microsite/data'")
  val micrositeStaticDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite static files directory. By default, it'll be the resourcesDirectory + '/microsite/static'")
  val micrositeExtraMdFiles: SettingKey[Map[File, ExtraMdFileConfig]] =
    settingKey[Map[File, ExtraMdFileConfig]](
      "Optional. This key is useful when you want to include automatically markdown documents as a part of your microsite, and these files are located in different places from the tutSourceDirectory. The map key is related with the source file, the map value corresponds with the target relative file path and the document meta-information configuration. By default, the map is empty.")
  val micrositeExtraMdFilesOutput: SettingKey[File] = settingKey[File](
    "Optional. Microsite output location for extra-md files. Default is resourceManaged + '/jekyll/_extra_md'")
  val micrositePluginsDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite Plugins directory. By default, it'll be the resourcesDirectory + '/microsite/plugins'")
  val micrositePalette: SettingKey[Map[String, String]] =
    settingKey[Map[String, String]]("Microsite palette")
  val micrositeFavicons: SettingKey[Seq[MicrositeFavicon]] = settingKey[Seq[MicrositeFavicon]](
    "Optional. List of filenames and sizes for the PNG/ICO files to be used as favicon for the generated site, located in '/microsite/img'. The sizes should be described with a string (i.e.: \"16x16\"). By default, favicons with different sizes will be generated from the navbar_brand2x.jpg file.")
  val micrositeGithubOwner: SettingKey[String] = settingKey[String]("Microsite Github owner")
  val micrositeGithubRepo: SettingKey[String]  = settingKey[String]("Microsite Github repo")
  val micrositeGithubToken: SettingKey[Option[String]] =
    settingKey[Option[String]]("Microsite Github token for pushing the microsite")
  val micrositeGithubLinks: SettingKey[Boolean] = settingKey[Boolean](
    "Optional. Includes Github links (forks, stars) in the layout. Enabled by default.")
  val micrositeGitHostingService: SettingKey[GitHostingService] =
    settingKey[GitHostingService]("Service used for git hosting. By default, it'll be GitHub.")
  val micrositeGitHostingUrl: SettingKey[String] = settingKey[String](
    "In the case where your project isn't hosted on Github, use this setting to point users to git host (e.g. 'https://internal.gitlab.com/<user>/<project>').")
  val micrositePushSiteWith: SettingKey[PushWith] =
    settingKey[PushWith](
      "Determines what will be chosen for pushing the site. The options are sbt-ghpages plugin and github4s library.")

  val micrositeAnalyticsToken: SettingKey[String] =
    settingKey[String](
      "Optional. Add your property id of Google Analytics to add a Google Analytics tracker")
  val micrositeGitterChannel: SettingKey[Boolean] = settingKey[Boolean](
    "Optional. Includes Gitter sidecar Chat functionality. Enabled by default."
  )
  val micrositeGitterChannelUrl: SettingKey[String] = settingKey[String](
    "Optional. Add custom Gitter sidecar Chat URL. By default is owner/repository."
  )

  val micrositeFooterText: SettingKey[Option[String]] = settingKey[Option[String]](
    "Optional. Customize the second line in the footer."
  )

  val publishMicrositeCommandKey: String = "publishMicrositeCommand"

  val micrositeEditButton: SettingKey[Option[MicrositeEditButton]] =
    settingKey[Option[MicrositeEditButton]](
      "Optional. Add a button in DocsLayout pages that links to the file in the repository."
    )

  val micrositeCompilingDocsTool =
    settingKey[CompilingDocsTool]("Choose between compiling code snippets with tut or mdoc")
}

object MicrositeKeys extends MicrositeKeys

trait MicrositeAutoImportSettings extends MicrositeKeys {

  lazy val micrositeHelper: Def.Initialize[MicrositeHelper] = Def.setting {
    val baseUrl =
      if (!micrositeBaseUrl.value.isEmpty && !micrositeBaseUrl.value.startsWith("/"))
        s"/${micrositeBaseUrl.value}"
      else micrositeBaseUrl.value

    val defaultYamlCustomVariables = Map(
      "name"        -> micrositeName.value,
      "description" -> micrositeDescription.value,
      "version"     -> version.value,
      "org"         -> organizationName.value,
      "baseurl"     -> baseUrl,
      "docs"        -> true,
      "markdown"    -> "kramdown",
      "highlighter" -> "rouge",
      "collections" -> Map("tut" -> Map("output" -> true))
    )

    val userCustomVariables = micrositeConfigYaml.value
    val configWithAllCustomVariables = userCustomVariables.copy(
      yamlCustomProperties = defaultYamlCustomVariables ++ userCustomVariables.yamlCustomProperties)

    new MicrositeHelper(
      MicrositeSettings(
        identity = MicrositeIdentitySettings(
          name = micrositeName.value,
          description = micrositeDescription.value,
          author = micrositeAuthor.value,
          homepage = micrositeHomepage.value,
          organizationHomepage = micrositeOrganizationHomepage.value,
          twitter = micrositeTwitter.value,
          twitterCreator = micrositeTwitterCreator.value,
          analytics = micrositeAnalyticsToken.value
        ),
        visualSettings = MicrositeVisualSettings(
          highlightTheme = micrositeHighlightTheme.value,
          highlightLanguages = micrositeHighlightLanguages.value,
          palette = micrositePalette.value,
          favicons = micrositeFavicons.value,
          shareOnSocial = micrositeShareOnSocial.value
        ),
        templateTexts = MicrositeTemplateTexts(
          micrositeFooterText.value
        ),
        configYaml = configWithAllCustomVariables,
        fileLocations = MicrositeFileLocations(
          micrositeImgDirectory = micrositeImgDirectory.value,
          micrositeCssDirectory = micrositeCssDirectory.value,
          micrositeJsDirectory = micrositeJsDirectory.value,
          micrositeCDNDirectives = micrositeCDNDirectives.value,
          micrositeExternalLayoutsDirectory = micrositeExternalLayoutsDirectory.value,
          micrositeExternalIncludesDirectory = micrositeExternalIncludesDirectory.value,
          micrositeDataDirectory = micrositeDataDirectory.value,
          micrositeStaticDirectory = micrositeStaticDirectory.value,
          micrositeExtraMdFiles = micrositeExtraMdFiles.value,
          micrositeExtraMdFilesOutput = micrositeExtraMdFilesOutput.value,
          micrositePluginsDirectory = micrositePluginsDirectory.value
        ),
        urlSettings = MicrositeUrlSettings(
          micrositeUrl = micrositeUrl.value,
          micrositeBaseUrl = micrositeBaseUrl.value,
          micrositeDocumentationUrl = micrositeDocumentationUrl.value,
          micrositeDocumentationLabelDescription = micrositeDocumentationLabelDescription.value
        ),
        gitSettings = MicrositeGitSettings(
          githubOwner = micrositeGitHostingService.value match {
            case GitHub => micrositeGithubOwner.value
            case _      => ""
          },
          githubRepo = micrositeGitHostingService.value match {
            case GitHub => micrositeGithubRepo.value
            case _      => ""
          },
          githubLinks = micrositeGithubLinks.value,
          gitHostingService = micrositeGitHostingService.value.name,
          gitHostingUrl = micrositeGitHostingUrl.value,
          gitSidecarChat = micrositeGitterChannel.value,
          gitSidecarChatUrl = micrositeGitterChannelUrl.value
        ),
        editButtonSettings = MicrositeEditButtonSettings(
          micrositeEditButton.value
        )
      ))
  }

  lazy val micrositeTasksSettings = Seq(
    microsite := micrositeHelper.value.createResources(
      resourceManagedDir = (resourceManaged in Compile).value),
    micrositeConfig := micrositeHelper.value
      .copyConfigurationFile((sourceDirectory in Jekyll).value, siteDirectory.value),
    micrositeMakeExtraMdFiles := micrositeHelper.value.buildAdditionalMd(),
    micrositeTutExtraMdFiles := {
      val r: ScalaRun = (runner in Tut).value
      val in          = micrositeMakeExtraMdFiles.value
      val out         = tutTargetDirectory.value
      val cp          = (fullClasspath in Tut).value
      val opts        = (scalacOptions in Tut).value
      val pOpts       = tutPluginJars.value.map(f => "–Xplugin:" + f.getAbsolutePath)
      val re          = tutNameFilter.value.pattern.toString
      _root_.tut.TutPlugin.tutOne(streams.value, r, in, out, cp, opts, pOpts, re).map(_._1)
    },
    micrositeMdocExtraMdFiles := {



      tutTargetDirectory.value
    },
    makeTut := {
      Def.sequential(microsite, tut, micrositeTutExtraMdFiles, makeSite, micrositeConfig)
    }.value,
    makeMdoc := {
      Def.sequential(
        microsite,
        mdoc.toTask(""),
        micrositeMdocExtraMdFiles,
        makeSite,
        micrositeConfig)
    }.value,
    makeMicrosite := Def.taskDyn {
      micrositeCompilingDocsTool.value match {
        case WithTut  => Def.task(makeTut.value)
        case WithMdoc => Def.task(makeMdoc.value)
      }
    }.value,
    publishMicrosite := Def.taskDyn {
      val siteDir: File                 = (target in makeSite).value
      val noJekyll: Boolean             = ghpagesNoJekyll.value
      val branch: String                = ghpagesBranch.value
      val pushSiteWith: PushWith        = micrositePushSiteWith.value
      val gitHosting: GitHostingService = micrositeGitHostingService.value
      val githubOwner: String           = micrositeGithubOwner.value
      val githubRepo: String            = micrositeGithubRepo.value
      val githubToken: Option[String]   = micrositeGithubToken.value

      val cleanAndMakeMicroSite: Unit = Def.sequential(clean, makeMicrosite).value

      lazy val log: Logger = streams.value.log

      (pushSiteWith.name, gitHosting.name, cleanAndMakeMicroSite) match {
        case (GHPagesPlugin.name, _, _) =>
          Def.task(ghpagesPushSite.value)
        case (GitHub4s.name, GitHub.name, _) if githubToken.nonEmpty =>
          val commitMessage = sys.env.getOrElse("SBT_GHPAGES_COMMIT_MESSAGE", "updated site")

          log.info(s"""Committing files from ${siteDir.getAbsolutePath} into branch '$branch'
                 | * repo: $githubOwner/$githubRepo
                 | * commitMessage: $commitMessage""".stripMargin)

          val ghOps: GitHubOps = new GitHubOps(githubOwner, githubRepo, githubToken)

          if (noJekyll) IO.touch(siteDir / ".nojekyll")

          ghOps.commitDir(branch, commitMessage, siteDir) match {
            case Right(_) => log.info("Success committing files")
            case Left(e) =>
              log.error(s"Error committing files")
              e.printStackTrace()
          }

          Def.task(cleanAndMakeMicroSite)
        case (GitHub4s.name, GitHub.name, _) =>
          log.error(
            s"You must provide a GitHub token through the `micrositeGithubToken` setting for pushing with github4s")
          Def.task(cleanAndMakeMicroSite)
        case (GitHub4s.name, hosting, _) =>
          log.warn(s"github4s doens't have support for $hosting")
          Def.task(cleanAndMakeMicroSite)
        case _ =>
          log.error(
            s"""Unexpected match case (pushSiteWith, gitHosting) = ("${pushSiteWith.name}", "${gitHosting.name}")""")
          Def.task(cleanAndMakeMicroSite)
      }
    }.value
  )

  val publishMicrositeCommand: Command = Command(publishMicrositeCommandKey)(_ => OptNotSpace) {
    (st, _) =>
      val extracted = Project.extract(st)

      extracted.runTask(publishMicrosite, st)._1
  }
}
