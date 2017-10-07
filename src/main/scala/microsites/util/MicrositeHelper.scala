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

package microsites.util

import java.io.File

import com.sksamuel.scrimage._
import sbtorgpolicies.model.YamlFormats._
import microsites._
import microsites.layouts._
import net.jcazevedo.moultingyaml.{YamlObject, _}
import sbt._
import sbtorgpolicies.io._
import sbtorgpolicies.io.syntax._

import scala.io.Source

class MicrositeHelper(config: MicrositeSettings) extends MicrositeHelperSpecific {
  implicitly(config)

  val fw = new FileWriter

  val jekyllDir = "jekyll"

  // format: OFF
  val faviconHeights = List(16, 24, 32, 48, 57, 60, 64, 70, 72, 76, 96,
                           114, 120, 128, 144, 150, 152, 196, 310)
  val faviconSizes: List[(Int, Int)] = (faviconHeights zip faviconHeights) ++ List((310, 150))
  // format: ON

  lazy val faviconFilenames: List[String] =
    faviconSizes.foldLeft(List.empty[String]) { (list, size) =>
      val (width, height) = size
      list :+ s"favicon${width}x$height.png"
    }

  lazy val faviconDescriptions: List[MicrositeFavicon] = (faviconFilenames zip faviconSizes) map {
    case (filename, (width, height)) =>
      MicrositeFavicon(filename, s"${width}x$height")
  }

  def createResources(resourceManagedDir: File, tutSourceDirectory: File): List[File] = {

    val targetDir: String    = resourceManagedDir.getAbsolutePath.ensureFinalSlash
    val tutSourceDir: String = tutSourceDirectory.getAbsolutePath.ensureFinalSlash
    val pluginURL: URL       = getClass.getProtectionDomain.getCodeSource.getLocation

    fw.copyJARResourcesTo(pluginURL, s"$targetDir$jekyllDir/", "_sass")
    fw.copyJARResourcesTo(pluginURL, s"$targetDir$jekyllDir/", "css")
    fw.copyJARResourcesTo(pluginURL, s"$targetDir$jekyllDir/", "img")
    fw.copyJARResourcesTo(pluginURL, s"$targetDir$jekyllDir/", "js")
    fw.copyJARResourcesTo(pluginURL, s"$targetDir$jekyllDir/", "highlight")
    fw.copyJARResourcesTo(pluginURL, s"$targetDir$jekyllDir/", "plugins")

    fw.copyFilesRecursively(
      config.fileLocations.micrositeImgDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/img/")
    fw.copyFilesRecursively(
      config.fileLocations.micrositeCssDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/css/")
    fw.copyFilesRecursively(
      config.fileLocations.micrositeJsDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/js/")
    fw.copyFilesRecursively(
      config.fileLocations.micrositeExternalLayoutsDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_layouts/")
    fw.copyFilesRecursively(
      config.fileLocations.micrositeExternalIncludesDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_includes/")
    fw.copyFilesRecursively(
      config.fileLocations.micrositeDataDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_data/")
    fw.copyFilesRecursively(
      config.fileLocations.micrositePluginsDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_plugins/")

    List(createConfigYML(targetDir), createPalette(targetDir)) ++
      createLayouts(targetDir) ++ createPartialLayout(targetDir) ++ createFavicons(targetDir)
  }

  def buildAdditionalMd(): File = {
    val extraMdOutputDir = config.fileLocations.micrositeExtraMdFilesOutput
    extraMdOutputDir.mkdirs()

    config.fileLocations.micrositeExtraMdFiles foreach {
      case (sourceFile, targetFileConfig) =>
        println(
          s"Copying from ${sourceFile.getAbsolutePath} to ${extraMdOutputDir.getAbsolutePath}/$targetFileConfig")

        val targetFileContent =
          s"""---
             |layout: ${targetFileConfig.layout}
             |${targetFileConfig.metaProperties map {
               case (key, value) => "%s: %s" format (key, value)
             } mkString ("", "\n", "")}
             |---
             |${Source.fromFile(sourceFile.getAbsolutePath).mkString}
             |""".stripMargin

        val outFile = extraMdOutputDir / targetFileConfig.fileName

        IO.write(outFile, targetFileContent)
    }

    extraMdOutputDir
  }

  def createConfigYML(targetDir: String): File = {
    val targetPath = s"$targetDir$jekyllDir/_config.yml"
    fw.createFile(targetPath)

    val yaml             = config.configYaml
    val customProperties = yaml.yamlCustomProperties.toYaml.asYamlObject.fields
    val inlineYaml =
      if (yaml.yamlInline.nonEmpty)
        yaml.yamlInline.parseYaml.asYamlObject.fields
      else Map.empty[YamlValue, YamlValue]
    val fileYaml = yaml.yamlPath.fold(Map.empty[YamlValue, YamlValue])(f =>
      if (f.exists()) {
        Source.fromFile(f.getAbsolutePath).mkString.parseYaml.asYamlObject.fields
      } else Map.empty[YamlValue, YamlValue])

    fw.writeContentToFile(
      YamlObject(customProperties ++ fileYaml ++ inlineYaml).prettyPrint,
      targetPath)

    targetPath.toFile
  }

  def createPalette(targetDir: String): File = {
    val targetPath = s"$targetDir$jekyllDir/_sass/_variables_palette.scss"
    fw.createFile(targetPath)

    val content = config.visualSettings.palette
      .map {
        case (key, value) => s"""$$$key: $value;"""
      }
      .mkString("\n")
    fw.writeContentToFile(content, targetPath)

    targetPath.toFile
  }

  def createLayouts(targetDir: String): List[File] =
    List(
      "home" -> new HomeLayout(config),
      "docs" -> new DocsLayout(config),
      "page" -> new PageLayout(config)
    ) map {
      case (layoutName, layout) =>
        val targetPath = s"$targetDir$jekyllDir/_layouts/$layoutName.html"
        fw.createFile(targetPath)

        fw.writeContentToFile(layout.render.toString(), targetPath)
        targetPath.toFile
    }

  def createPartialLayout(targetDir: String): List[File] =
    List("menu" -> new MenuPartialLayout(config)) map {
      case (layoutName, layout) =>
        val targetPath = s"$targetDir$jekyllDir/_includes/$layoutName.html"
        fw.writeContentToFile(layout.render.toString(), targetPath)
        targetPath.toFile
    }

  def createFavicons(targetDir: String): List[File] = {
    val sourceFile = s"$targetDir$jekyllDir/img/navbar_brand2x.png"
    fw.createFile(sourceFile)

    (faviconFilenames zip faviconSizes)
      .map {
        case (name, size) =>
          (new File(s"$targetDir$jekyllDir/img/$name"), size)
      }
      .map {
        case (file, (width, height)) =>
          Image.fromFile(sourceFile.toFile).scaleTo(width, height).output(file)
      }
  }

  def copyConfigurationFile(sourceDir: File, targetDir: File): Unit = {

    val targetPath = s"$targetDir/_config.yml"
    fw.createFile(targetPath)

    fw.copyFilesRecursively(
      s"${sourceDir.getAbsolutePath}/_config.yml",
      targetPath.toFile.getAbsolutePath)
    ()
  }
}
