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

import com.typesafe.sbt.sbtghpages.GhpagesPlugin.autoImport._
import com.typesafe.sbt.SbtGit.git
import com.typesafe.sbt.site.SitePlugin.autoImport._
import com.typesafe.sbt.site.jekyll.JekyllPlugin
import microsites.util.MicrositeHelper
import sbt.Keys._
import sbt._
import sbt.plugins.IvyPlugin
import tut.Plugin._

object MicrositesPlugin extends AutoPlugin {

  object autoImport extends MicrositeKeys

  import MicrositesPlugin.autoImport._
  import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._

  override def requires: Plugins = IvyPlugin && JekyllPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] =
    tutSettings ++
      micrositeDefaultSettings ++
      micrositeTasksSettings ++
      Seq(
        git.remoteRepo := s"git@github.com:${micrositeGithubOwner.value}/${micrositeGithubRepo.value}.git",
        mappings in Jekyll ++= micrositeHelper.value.directory("src/main/resources/microsite"),
        sourceDirectory in Jekyll := resourceManaged.value / "main" / "jekyll",
        tutSourceDirectory := sourceDirectory.value / "main" / "tut",
        tutTargetDirectory := resourceManaged.value / "main" / "jekyll"
      )

  lazy val micrositeDefaultSettings = Seq(
    micrositeName := name.value,
    micrositeDescription := description.value,
    micrositeAuthor := organizationName.value,
    micrositeHomepage := homepage.value.map(_.toString).getOrElse(""),
    micrositeBaseUrl := "",
    micrositeDocumentationUrl := "",
    micrositeTwitter := "",
    micrositeHighlightTheme := "default",
    micrositeConfigYaml := ConfigYml(
      yamlPath = Some((resourceDirectory in Compile).value / "microsite" / "_config.yml")),
    micrositeImgDirectory := (resourceDirectory in Compile).value / "microsite" / "img",
    micrositeCssDirectory := (resourceDirectory in Compile).value / "microsite" / "css",
    micrositeJsDirectory := (resourceDirectory in Compile).value / "microsite" / "js",
    micrositeCDNDirectives := CdnDirectives(),
    micrositeExternalLayoutsDirectory := (resourceDirectory in Compile).value / "microsite" / "layouts",
    micrositeExternalIncludesDirectory := (resourceDirectory in Compile).value / "microsite" / "includes",
    micrositeDataDirectory := (resourceDirectory in Compile).value / "microsite" / "data",
    micrositeExtraMdFiles := Map.empty,
    micrositePalette := Map(
      "brand-primary"   -> "#02B4E5",
      "brand-secondary" -> "#1C2C52",
      "brand-tertiary"  -> "#162341",
      "gray-dark"       -> "#453E46",
      "gray"            -> "#837F84",
      "gray-light"      -> "#E3E2E3",
      "gray-lighter"    -> "#F4F3F4",
      "white-color"     -> "#FFFFFF"
    ),
    micrositeFavicons := Seq(),
    micrositeGithubOwner := "47deg",
    micrositeGithubRepo := "sbt-microsites",
    micrositeKazariEvaluatorUrl := "https://scala-evaluator-212.herokuapp.com",
    micrositeKazariEvaluatorToken := "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.S2F6YXJp.Jl2eqMfw8IakJF93PjxTbrf-8YUJgX5OoOfy5JHE8Yw",
    micrositeKazariGithubToken := "",
    micrositeKazariCodeMirrorTheme := "solarized-dark",
    micrositeKazariDependencies := Seq(),
    micrositeKazariResolvers := Seq(),
    micrositeGitHostingService := GitHub,
    micrositeGitHostingUrl := "",
    includeFilter in Jekyll := ("*.html" | "*.css" | "*.png" | "*.jpg" | "*.jpeg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.webm" | "*.ico" | "CNAME")
  )

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
          twitter = micrositeTwitter.value
        ),
        visualSettings = MicrositeVisualSettings(
          highlightTheme = micrositeHighlightTheme.value,
          palette = micrositePalette.value,
          favicons = micrositeFavicons.value
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
          micrositeExtraMdFiles = micrositeExtraMdFiles.value
        ),
        urlSettings = MicrositeUrlSettings(
          micrositeBaseUrl = micrositeBaseUrl.value,
          micrositeDocumentationUrl = micrositeDocumentationUrl.value
        ),
        micrositeKazariSettings = KazariSettings(
          micrositeKazariEvaluatorUrl.value,
          micrositeKazariEvaluatorToken.value,
          micrositeKazariGithubToken.value,
          micrositeKazariCodeMirrorTheme.value,
          micrositeKazariDependencies.value,
          micrositeKazariResolvers.value
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
          gitHostingService = micrositeGitHostingService.value.name,
          gitHostingUrl = micrositeGitHostingUrl.value
        )
      ))
  }

  lazy val micrositeTasksSettings = Seq(
    microsite := micrositeHelper.value.createResources(
      resourceManagedDir = (resourceManaged in Compile).value,
      tutSourceDirectory = (tutSourceDirectory in Compile).value),
    micrositeConfig := micrositeHelper.value
      .copyConfigurationFile((sourceDirectory in Jekyll).value, siteDirectory.value),
    makeMicrosite := Def
      .sequential(
        microsite,
        tut,
        makeSite,
        micrositeConfig
      )
      .value,
    publishMicrosite := Def
      .sequential(
        clean,
        makeMicrosite,
        ghpagesPushSite
      )
      .value
  )
}
