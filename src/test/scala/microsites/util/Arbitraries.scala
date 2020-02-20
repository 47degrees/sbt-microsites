/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
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

import java.io.File

import microsites.MicrositeKeys._
import microsites._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen._

trait Arbitraries {

  implicit def fileArbitrary: Arbitrary[File] = Arbitrary {
    uuid map (randomFileName => new File(randomFileName.toString))
  }

  implicit def paletteMapArbitrary: Arbitrary[Map[String, String]] = Arbitrary {
    for {
      stringList <- listOfN[String](6, Arbitrary.arbitrary[String])
      map        <- (stringList map (s => s -> s"value of $s")).toMap
    } yield map
  }

  implicit def configYamlArbitrary: Arbitrary[ConfigYml] = Arbitrary {
    for {
      yamlCustomProperties ← paletteMapArbitrary.arbitrary
      yamlPath             ← Arbitrary.arbitrary[Option[File]]
      yamlInline           ← Arbitrary.arbitrary[String]
    } yield ConfigYml(yamlCustomProperties, yamlPath, yamlInline)
  }

  implicit def extraMdConfigArbitrary: Arbitrary[ExtraMdFileConfig] = Arbitrary {
    for {
      file        ← Arbitrary.arbitrary[String]
      target      ← Arbitrary.arbitrary[String]
      mapArbValue ← paletteMapArbitrary.arbitrary
    } yield ExtraMdFileConfig(file, target, mapArbValue)
  }

  implicit def cdnDirectivesArbitrary: Arbitrary[CdnDirectives] = Arbitrary {
    for {
      jsList  <- listOf[String](Arbitrary.arbitrary[String])
      cssList <- listOf[String](Arbitrary.arbitrary[String])
    } yield CdnDirectives(jsList, cssList)
  }

  implicit def markdownMapArbitrary: Arbitrary[Map[File, ExtraMdFileConfig]] = Arbitrary {
    for {
      n        ← choose(1, 100)
      fileList <- listOfN[File](n, Arbitrary.arbitrary[File])
      config   ← extraMdConfigArbitrary.arbitrary
      map      <- (fileList map (f => f -> config)).toMap
    } yield map
  }

  implicit def hostingServiceArbitrary: Arbitrary[GitHostingService] = Arbitrary {
    oneOf(
      oneOf(GitHub, GitLab, Bitbucket),
      Arbitrary.arbitrary[String].map(Other(_))
    )
  }

  implicit def micrositeFaviconArbitrary: Arbitrary[MicrositeFavicon] = Arbitrary {
    for {
      filename <- Arbitrary.arbitrary[String]
      size     <- Arbitrary.arbitrary[String]
    } yield MicrositeFavicon(filename, size)
  }

  implicit def micrositeEditButtonArbitrary: Arbitrary[Option[MicrositeEditButton]] = Arbitrary {
    for {
      text     <- Arbitrary.arbitrary[String]
      basePath <- Arbitrary.arbitrary[String]
    } yield Some(MicrositeEditButton(text, basePath))
  }

  implicit def settingsArbitrary: Arbitrary[MicrositeSettings] = Arbitrary {
    for {
      name                                   ← Arbitrary.arbitrary[String]
      description                            ← Arbitrary.arbitrary[String]
      author                                 ← Arbitrary.arbitrary[String]
      homepage                               ← Arbitrary.arbitrary[String]
      organizationHomepage                   ← Arbitrary.arbitrary[String]
      twitter                                ← Arbitrary.arbitrary[String]
      twitterCreator                         ← Arbitrary.arbitrary[String]
      analytics                              ← Arbitrary.arbitrary[String]
      highlightTheme                         ← Arbitrary.arbitrary[String]
      highlightLanguages                     ← Arbitrary.arbitrary[Seq[String]]
      theme                                  ← Arbitrary.arbitrary[String]
      micrositeConfigYaml                    ← configYamlArbitrary.arbitrary
      micrositeImgDirectory                  ← Arbitrary.arbitrary[File]
      micrositeCssDirectory                  ← Arbitrary.arbitrary[File]
      micrositeSassDirectory                 ← Arbitrary.arbitrary[File]
      micrositeJsDirectory                   ← Arbitrary.arbitrary[File]
      micrositeCDNDirectives                 <- cdnDirectivesArbitrary.arbitrary
      micrositeExternalLayoutsDirectory      ← Arbitrary.arbitrary[File]
      micrositeExternalIncludesDirectory     ← Arbitrary.arbitrary[File]
      micrositeDataDirectory                 ← Arbitrary.arbitrary[File]
      micrositeStaticDirectory               ← Arbitrary.arbitrary[File]
      micrositeExtraMdFiles                  ← markdownMapArbitrary.arbitrary
      micrositeExtraMdFilesOutput            ← Arbitrary.arbitrary[File]
      micrositePluginsDirectory              ← Arbitrary.arbitrary[File]
      micrositeUrl                           ← Arbitrary.arbitrary[String]
      micrositeBaseUrl                       ← Arbitrary.arbitrary[String]
      micrositeDocumentationUrl              ← Arbitrary.arbitrary[String]
      micrositeDocumentationLabelDescription ← Arbitrary.arbitrary[String]
      palette                                ← paletteMapArbitrary.arbitrary
      favicon                                ← listOf[MicrositeFavicon](micrositeFaviconArbitrary.arbitrary)
      githubOwner                            ← Arbitrary.arbitrary[String]
      githubRepo                             ← Arbitrary.arbitrary[String]
      gitHostingService                      ← Arbitrary.arbitrary[GitHostingService]
      gitHostingUrl                          ← Arbitrary.arbitrary[String]
      githubLinks                            ← Arbitrary.arbitrary[Boolean]
      gitSidecarChat                         ← Arbitrary.arbitrary[Boolean]
      gitSidecarChatUrl                      ← Arbitrary.arbitrary[String]
      shareOnSocial                          ← Arbitrary.arbitrary[Boolean]
      micrositeFooterText                    ← Arbitrary.arbitrary[Option[String]]
      micrositeEditButton                    ← micrositeEditButtonArbitrary.arbitrary
      micrositeVersionList                   ← Arbitrary.arbitrary[Seq[String]]
    } yield MicrositeSettings(
      MicrositeIdentitySettings(
        name,
        description,
        author,
        homepage,
        organizationHomepage,
        twitter,
        twitterCreator,
        analytics
      ),
      MicrositeVisualSettings(
        highlightTheme,
        highlightLanguages,
        palette,
        favicon,
        shareOnSocial,
        theme
      ),
      MicrositeTemplateTexts(
        micrositeFooterText
      ),
      micrositeConfigYaml,
      MicrositeFileLocations(
        micrositeImgDirectory,
        micrositeCssDirectory,
        micrositeSassDirectory,
        micrositeJsDirectory,
        micrositeCDNDirectives,
        micrositeExternalLayoutsDirectory,
        micrositeExternalIncludesDirectory,
        micrositeDataDirectory,
        micrositeStaticDirectory,
        micrositeExtraMdFiles,
        micrositeExtraMdFilesOutput,
        micrositePluginsDirectory
      ),
      MicrositeUrlSettings(
        micrositeUrl,
        micrositeBaseUrl,
        micrositeDocumentationUrl,
        micrositeDocumentationLabelDescription
      ),
      MicrositeGitSettings(
        githubOwner,
        githubRepo,
        githubLinks,
        gitHostingService,
        gitHostingUrl,
        gitSidecarChat,
        gitSidecarChatUrl
      ),
      MicrositeEditButtonSettings(
        micrositeEditButton
      ),
      MicrositeMultiversionSettings(
        micrositeVersionList
      )
    )
  }
}
