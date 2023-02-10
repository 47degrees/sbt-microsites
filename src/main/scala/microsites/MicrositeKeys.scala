/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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

import java.nio.file.*
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.github.sbt.sbtghpages.GhpagesPlugin.autoImport.*
import com.typesafe.sbt.site.SitePlugin.autoImport.makeSite
import github4s.GithubConfig
import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import mdoc.MdocPlugin.autoImport.*
import microsites.github.GitHubOps
import microsites.ioops.FileWriter.*
import microsites.ioops.*
import microsites.ioops.syntax.*
import microsites.util.MicrositeHelper
import org.http4s.blaze.client.BlazeClientBuilder
import sbt.Keys.*
import sbt.*
import sbt.complete.DefaultParsers.OptNotSpace
import sbt.io.IO as FIO

import scala.language.implicitConversions
import scala.sys.process.*

trait MicrositeKeys {

  sealed abstract class GitHostingService(val name: String) extends Product with Serializable
  final case object GitHub                                  extends GitHostingService("GitHub")
  final case object GitLab                                  extends GitHostingService("GitLab")
  final case object Bitbucket                               extends GitHostingService("Bitbucket")
  final case class Other(value: String)                     extends GitHostingService(value)

  sealed abstract class PushWith(val name: String) extends Product with Serializable
  final case object GHPagesPlugin                  extends PushWith("ghPagesPlugin")
  final case object GitHub4s                       extends PushWith("github4s")

  object GitHostingService {
    implicit def string2GitHostingService(name: String): GitHostingService = {
      List(GitHub, GitLab, Bitbucket)
        .find(_.name.toLowerCase == name.toLowerCase)
        .getOrElse(Other(name))
    }
  }

  case class Version(name: String, own: Boolean)
  object Version {
    implicit val encoder: Encoder[Version] = deriveEncoder[Version]
    implicit val decoder: Decoder[Version] = deriveDecoder[Version]
  }

  val makeMicrosite: TaskKey[Unit] = taskKey[Unit]("Main task to build a microsite")
  val makeMdoc: TaskKey[Unit] =
    taskKey[Unit]("Sequential tasks to compile mdoc and move the result")
  val createMicrositeVersions: TaskKey[Unit] =
    taskKey[Unit](
      "Task to create the different microsites going through the list specified in the settings"
    )
  val moveMicrositeVersions: TaskKey[Unit] =
    taskKey[Unit](
      "Task to move the different microsites to their final publishing directory destination"
    )
  val makeVersionsJson: TaskKey[Unit] =
    taskKey[Unit](
      "Task that will create the expected formattted JSON with the versions specified in the settings"
    )
  val makeVersionedMicrosite: TaskKey[Unit] =
    taskKey[Unit]("Task similar to makeMicrosite, adding a version selector")
  val makeMultiversionMicrosite: TaskKey[Unit] = taskKey[Unit](
    "Main task to build a microsite, including version selector, and microsite different versions"
  )
  val pushMicrosite: TaskKey[Unit] =
    taskKey[Unit]("Task to just push files up.")
  val publishMicrosite: TaskKey[Unit] =
    taskKey[Unit]("Task helper that wraps the `publishMicrositeCommand`.")
  val publishMultiversionMicrosite: TaskKey[Unit] =
    taskKey[Unit]("Task helper that wraps the `publishMultiversionMicrositeCommand`.")
  val microsite: TaskKey[Seq[File]] = taskKey[Seq[File]]("Create microsite files")
  val micrositeMakeExtraMdFiles: TaskKey[File] =
    taskKey[File]("Create microsite extra md files")
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
  val micrositeUrl: SettingKey[String] = settingKey[String]("Microsite site absolute url prefix")
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
    "Optional. Microsite images directory. By default, it'll be the resourcesDirectory + '/microsite/img'"
  )
  val micrositeCssDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite CSS directory. By default, it'll be the resourcesDirectory + '/microsite/css'"
  )
  val micrositeSassDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite SASS directory. By default, it'll be the resourcesDirectory + '/microsite/sass'"
  )
  val micrositeJsDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite Javascript directory. By default, it'll be the resourcesDirectory + '/microsite/js'"
  )
  val micrositeCDNDirectives: SettingKey[CdnDirectives] = settingKey[CdnDirectives](
    "Optional. Microsite CDN directives lists (for css and js imports). By default, both lists are empty."
  )
  val micrositeExternalLayoutsDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite external layouts directory. By default, it'll be the resourcesDirectory + '/microsite/layout'"
  )
  val micrositeExternalIncludesDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite external includes (partial layouts) directory. By default, it'll be the resourcesDirectory + '/microsite/includes'"
  )
  val micrositeDataDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite Data directory, useful to define the microsite data files " +
      "(https://jekyllrb.com/docs/datafiles/). By default, it'll be the resourcesDirectory + '/microsite/data'"
  )
  val micrositeStaticDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite static files directory. By default, it'll be the resourcesDirectory + '/microsite/static'"
  )
  val micrositeExtraMdFiles: SettingKey[Map[File, ExtraMdFileConfig]] =
    settingKey[Map[File, ExtraMdFileConfig]](
      "Optional. This key is useful when you want to include automatically markdown documents as a part of your microsite, and these files are located in different places. The map key is related with the source file, the map value corresponds with the target relative file path and the document meta-information configuration. By default, the map is empty."
    )
  val micrositeExtraMdFilesOutput: SettingKey[File] = settingKey[File](
    "Optional. Microsite output location for extra-md files. Default is resourceManaged + '/jekyll/extra_md'"
  )
  val micrositePluginsDirectory: SettingKey[File] = settingKey[File](
    "Optional. Microsite Plugins directory. By default, it'll be the resourcesDirectory + '/microsite/plugins'"
  )
  val micrositePalette: SettingKey[Map[String, String]] =
    settingKey[Map[String, String]]("Microsite palette")
  val micrositeFavicons: SettingKey[Seq[MicrositeFavicon]] = settingKey[Seq[MicrositeFavicon]](
    "Optional. List of filenames and sizes for the PNG/ICO files to be used as favicon for the generated site, located in '/microsite/img'. The sizes should be described with a string (i.e.: \"16x16\"). By default, favicons with different sizes will be generated from the navbar_brand2x.jpg file."
  )
  val micrositeGithubOwner: SettingKey[String] = settingKey[String](
    "Microsite Github owner, defaults to the information found in the 'origin' Git remote"
  )
  val micrositeGithubRepo: SettingKey[String] = settingKey[String](
    "Microsite Github repo, defaults to the information found in the 'origin' Git remote"
  )
  val micrositeGithubToken: SettingKey[Option[String]] =
    settingKey[Option[String]]("Microsite Github token for pushing the microsite")
  val micrositeGithubLinks: SettingKey[Boolean] = settingKey[Boolean](
    "Optional. Includes Github links (forks, stars) in the layout. Enabled by default."
  )
  val micrositeGitHostingService: SettingKey[GitHostingService] =
    settingKey[GitHostingService]("Service used for git hosting. By default, it'll be GitHub.")
  val micrositeGitHostingUrl: SettingKey[String] = settingKey[String](
    "In the case where your project isn't hosted on Github, use this setting to point users to git host (e.g. 'https://internal.gitlab.com/<user>/<project>')."
  )
  val micrositePushSiteWith: SettingKey[PushWith] =
    settingKey[PushWith](
      "Determines what will be chosen for pushing the site. The options are sbt-ghpages plugin and github4s library."
    )

  val micrositeAnalyticsToken: SettingKey[String] =
    settingKey[String](
      "Optional. Add your property id of Google Analytics to add a Google Analytics tracker"
    )
  val micrositeGitterChannel: SettingKey[Boolean] = settingKey[Boolean](
    "Optional. Includes Gitter sidecar Chat functionality. Enabled by default."
  )
  val micrositeGitterChannelUrl: SettingKey[String] = settingKey[String](
    "Optional. Add custom Gitter sidecar Chat URL. By default is owner/repository."
  )

  val micrositeFooterText: SettingKey[Option[String]] = settingKey[Option[String]](
    "Optional. Customize the second line in the footer."
  )

  val pushMicrositeCommandKey: String                = "pushMicrositeCommand"
  val publishMicrositeCommandKey: String             = "publishMicrositeCommand"
  val publishMultiversionMicrositeCommandKey: String = "publishMultiversionMicrositeCommand"

  val micrositeEditButton: SettingKey[Option[MicrositeEditButton]] =
    settingKey[Option[MicrositeEditButton]](
      "Optional. Add a button in DocsLayout pages that links to the file in the repository."
    )

  val micrositeTheme: SettingKey[String] = settingKey[String](
    "Optional. 'light' by default. Set it to 'pattern' to generate the pattern theme design."
  )

  val micrositeVersionList: SettingKey[Seq[String]] =
    settingKey[Seq[String]]("Optional. Microsite available versions")

  val micrositeSearchEnabled: SettingKey[Boolean] = settingKey[Boolean](
    "Adds a search bar and search features to your website using Lunr.js. Default is 'true'"
  )

  val micrositeHomeButtonTarget: SettingKey[String] = settingKey[String](
    "Determines where the large, home-page call-to-action button links to. Default is 'repo' which links to the project open source repository. Can also be set to `docs` to link to your main documentation page."
  )
}

object MicrositeKeys extends MicrositeKeys

trait MicrositeAutoImportSettings extends MicrositeKeys {

  lazy val fr = new FileReader

  private def createVersionsJson(targetDir: String, content: List[Version]): File = {
    val jekyllDir  = "jekyll"
    val targetPath = s"$targetDir$jekyllDir/_data/versions.json"
    createFile(targetPath)
    writeContentToFile(content.asJson.toString, targetPath)
    targetPath.toFile
  }

  private def generateVersionList(
      versionStringList: List[String],
      ownVersion: String
  ): List[Version] =
    versionStringList
      .map(version => Version(version, own = ownVersion == version))

  private def pluginProjects(pluginName: String): Option[Array[String]] = {
    val sbtPluginsOutput = "sbt --error plugins".lineStream
    val pluginLine =
      sbtPluginsOutput.find(_.trim.startsWith(s"$pluginName: enabled in "))

    pluginLine.map(_.trim.stripPrefix(s"$pluginName: enabled in ").split(", "))
  }

  // Generate a microsite externally through sbt and sbt-microsites tasks
  private def createMicrositeVersion(
      sourceDir: String,
      targetDir: String,
      baseUrl: String,
      version: String
  ): Unit = {
    val newBaseUrl =
      if (version.nonEmpty) s"$baseUrl/$version" else baseUrl
    val pluginName            = "microsites.MicrositesPlugin"
    val sbtMicrositesProjects = pluginProjects(pluginName)
    sbtMicrositesProjects match {
      case Some(projects) =>
        List(
          "sbt",
          s"""clean; project ${projects(0)}; set micrositeBaseUrl := "$newBaseUrl"; makeMicrosite"""
        ).!
        Files.move(
          Paths.get(sourceDir),
          Paths.get(s"$targetDir/$version"),
          StandardCopyOption.REPLACE_EXISTING
        )
        ()
      case None => System.err.println(s"$pluginName not found in version $version")
    }
  }

  lazy val micrositeHelper: Def.Initialize[MicrositeHelper] = Def.setting {
    val baseUrl =
      if (micrositeBaseUrl.value.nonEmpty && !micrositeBaseUrl.value.startsWith("/"))
        s"/${micrositeBaseUrl.value}"
      else micrositeBaseUrl.value

    val baseCssList = List(
      s"css/${micrositeTheme.value}-style.scss"
    )

    val customCssList =
      fr.fetchFilesRecursively(List(micrositeCssDirectory.value), validFile("css")) match {
        case Right(cssList) => cssList.map(css => s"css/${css.getName}")
        case _              => Nil
      }

    val customScssList =
      fr.fetchFilesRecursively(List(micrositeCssDirectory.value), validFile("scss")) match {
        case Right(scssList) => scssList.map(scss => s"css/${scss.getName}")
        case _               => Nil
      }

    val fullCssList = baseCssList ++ customCssList ++ customScssList

    val defaultYamlCustomVariables = Map(
      "name"           -> micrositeName.value,
      "description"    -> micrositeDescription.value,
      "version"        -> version.value,
      "org"            -> organizationName.value,
      "baseurl"        -> baseUrl,
      "docs"           -> true,
      "markdown"       -> "kramdown",
      "highlighter"    -> "rouge",
      "exclude"        -> List("css"),
      "include"        -> fullCssList,
      "search_enabled" -> micrositeSearchEnabled.value,
      "sass" -> Map(
        "load_paths" -> List("_sass", "_sass_custom"),
        "style"      -> "compressed",
        "sourcemap"  -> "never"
      ),
      "collections" -> Map("mdoc" -> Map("output" -> true))
    )

    val userCustomVariables = micrositeConfigYaml.value
    val configWithAllCustomVariables = userCustomVariables.copy(
      yamlCustomProperties = defaultYamlCustomVariables ++ userCustomVariables.yamlCustomProperties
    )

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
          shareOnSocial = micrositeShareOnSocial.value,
          theme = micrositeTheme.value
        ),
        templateTexts = MicrositeTemplateTexts(
          micrositeFooterText.value
        ),
        configYaml = configWithAllCustomVariables,
        fileLocations = MicrositeFileLocations(
          micrositeImgDirectory = micrositeImgDirectory.value,
          micrositeCssDirectory = micrositeCssDirectory.value,
          micrositeSassDirectory = micrositeSassDirectory.value,
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
          micrositeDocumentationLabelDescription = micrositeDocumentationLabelDescription.value,
          micrositeHomeButtonTarget = micrositeHomeButtonTarget.value
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
        ),
        multiversionSettings = MicrositeMultiversionSettings(
          micrositeVersionList.value
        ),
        searchSettings = MicrositeSearchSettings(
          micrositeSearchEnabled.value
        )
      )
    )
  }

  lazy val micrositeTasksSettings: Seq[Def.Setting[?]] = Seq(
    microsite := micrositeHelper.value
      .createResources(resourceManagedDir = (Compile / resourceManaged).value),
    micrositeMakeExtraMdFiles := micrositeHelper.value.buildAdditionalMd(),
    makeMdoc                  := Def.sequential(mdoc.toTask(""), micrositeMakeExtraMdFiles).value,
    makeMicrosite             := Def.sequential(microsite, makeMdoc, makeSite).value,
    makeVersionsJson := {
      "which git".! match {
        case 0 => ()
        case n => sys.error("Could not run git, error: " + n)
      }

      val sourceDir         = (Compile / resourceManaged).value
      val targetDir: String = sourceDir.getAbsolutePath.ensureFinalSlash
      val currentBranchTag  = "git name-rev --name-only HEAD".!!.trim

      val versionList = generateVersionList(
        currentBranchTag :: micrositeVersionList.value.toList,
        currentBranchTag
      )

      createVersionsJson(targetDir, versionList)
    },
    createMicrositeVersions := {
      "which git".! match {
        case 0 => ()
        case n => sys.error("Could not run git, error: " + n)
      }

      val publishingDir    = (makeSite / target).value
      val genDocsDir       = ".sbt-versioned-docs"
      val currentBranchTag = "git name-rev --name-only HEAD".!!.trim

      scala.reflect.io.Directory(new File(genDocsDir)).deleteRecursively()
      createDir(genDocsDir)

      micrositeVersionList.value.foreach { tag =>
        s"git checkout -f $tag".!
        createMicrositeVersion(
          publishingDir.getAbsolutePath,
          genDocsDir,
          micrositeBaseUrl.value,
          tag
        )
      }

      s"git checkout -f $currentBranchTag".!
    },
    moveMicrositeVersions := {
      val publishingDir = (makeSite / target).value
      val genDocsDir    = ".sbt-versioned-docs"

      micrositeVersionList.value.foreach { tag =>
        Files.move(
          Paths.get(s"$genDocsDir/$tag"),
          Paths.get(s"${publishingDir.getPath}/$tag"),
          StandardCopyOption.REPLACE_EXISTING
        )
      }
    },
    makeVersionedMicrosite :=
      Def.sequential(microsite, makeVersionsJson, makeMdoc, makeSite).value,
    makeMultiversionMicrosite :=
      Def
        .sequential(createMicrositeVersions, clean, makeVersionedMicrosite, moveMicrositeVersions)
        .value,
    ghpagesPrivateMappings := {
      sbt.Path.allSubpaths((makeSite / target).value).toList
    },
    pushMicrosite := Def.taskDyn {
      val siteDir: File                 = (makeSite / target).value
      val noJekyll: Boolean             = ghpagesNoJekyll.value
      val branch: String                = ghpagesBranch.value
      val pushSiteWith: PushWith        = micrositePushSiteWith.value
      val gitHosting: GitHostingService = micrositeGitHostingService.value
      val githubOwner: String           = micrositeGithubOwner.value
      val githubRepo: String            = micrositeGithubRepo.value
      val githubToken: Option[String]   = micrositeGithubToken.value

      lazy val log: Logger = streams.value.log

      (pushSiteWith.name, gitHosting.name) match {
        case (GHPagesPlugin.name, _) => ghpagesPushSite
        case (GitHub4s.name, GitHub.name) if githubToken.nonEmpty =>
          Def.task({
            val commitMessage = sys.env.getOrElse("SBT_GHPAGES_COMMIT_MESSAGE", "updated site")

            log.info(s"""Committing files from ${siteDir.getAbsolutePath} into branch '$branch'
                 | * repo: $githubOwner/$githubRepo
                 | * commitMessage: $commitMessage""".stripMargin)

            BlazeClientBuilder[IO].resource
              .use { client =>

                implicit val config: GithubConfig = if(micrositeGitHostingUrl.value.nonEmpty) {
                  val url = new URL(micrositeGitHostingUrl.value)
                  val replaceHost: String => String = s => s.replace("github.com", url.getHost)

                   GithubConfig.default
                    .copy(
                      baseUrl = replaceHost(s"${GithubConfig.default.baseUrl}"),
                      authorizeUrl = replaceHost(s"${GithubConfig.default.authorizeUrl}"),
                      accessTokenUrl = replaceHost(s"${GithubConfig.default.accessTokenUrl}")
                    )
                } else GithubConfig.default

                val ghOps = new GitHubOps[IO](client, githubOwner, githubRepo, githubToken)(implicitly, implicitly, config)

                if (noJekyll) FIO.touch(siteDir / ".nojekyll")

                ghOps
                  .commitDir(branch, commitMessage, siteDir)
                  .map(_ => log.info("Success committing files"))
                  .handleErrorWith { e =>
                    IO {
                      e.printStackTrace()
                      log.error(s"Error committing files")
                    }
                  }
              }
              .unsafeRunSync()
          })
        case (GitHub4s.name, GitHub.name) =>
          Def.task(
            log.error(
              s"You must provide a GitHub token through the `micrositeGithubToken` setting for pushing with github4s"
            )
          )
        case (GitHub4s.name, hosting) =>
          Def.task(log.warn(s"github4s doesn't have support for $hosting"))
        case _ =>
          Def.task(
            log.error(
              s"""Unexpected match case (pushSiteWith, gitHosting) = ("${pushSiteWith.name}", "${gitHosting.name}")"""
            )
          )
      }
    }.value,
    publishMicrosite :=
      Def.sequential(clean, makeMicrosite, pushMicrosite).value,
    publishMultiversionMicrosite :=
      Def.sequential(clean, makeMultiversionMicrosite, pushMicrosite).value
  )

  val pushMicrositeCommand: Command = Command(pushMicrositeCommandKey)(_ => OptNotSpace) {
    (st, _) =>
      val extracted = Project.extract(st)

      extracted.runTask(pushMicrosite, st)._1
  }

  val publishMicrositeCommand: Command = Command(publishMicrositeCommandKey)(_ => OptNotSpace) {
    (st, _) =>
      val extracted = Project.extract(st)

      extracted.runTask(publishMicrosite, st)._1
  }

  val publishMultiversionMicrositeCommand: Command =
    Command(publishMultiversionMicrositeCommandKey)(_ => OptNotSpace) { (st, _) =>
      val extracted = Project.extract(st)

      extracted.runTask(publishMultiversionMicrosite, st)._1
    }

  private[this] def validFile(extension: String)(file: File): Boolean =
    file.getName.endsWith(s".$extension")
}
