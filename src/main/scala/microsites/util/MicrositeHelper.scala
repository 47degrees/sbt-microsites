/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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
import java.net.URL

import cats.syntax.either._
import com.sksamuel.scrimage.nio.ImageReader
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.implicits._
import microsites.util.YamlFormats._
import microsites._
import microsites.layouts._
import net.jcazevedo.moultingyaml._
import sbt._
import sbt.io.Path
import sbt.io.syntax.file
import microsites.ioops.FileWriter._
import microsites.ioops.syntax._

import scala.io.Source

class MicrositeHelper(config: MicrositeSettings) {
  implicitly(config)

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

  def copyJAROrFolder(
      jarUrl: URL,
      output: String,
      filter: String = ""
  ): Either[Exceptions.IOException, Any] =
    copyResourcesTo(jarUrl, output, filter).orElse(
      copyFilesRecursively(jarUrl.getFile + filter, output + filter)
    )

  def createResources(resourceManagedDir: File): List[File] = {

    val targetDir: String = resourceManagedDir.getAbsolutePath.ensureFinalSlash
    val pluginURL: URL    = getClass.getProtectionDomain.getCodeSource.getLocation

    copyJAROrFolder(pluginURL, s"$targetDir$jekyllDir/", "_sass")
    copyJAROrFolder(pluginURL, s"$targetDir$jekyllDir/", "css")
    copyJAROrFolder(pluginURL, s"$targetDir$jekyllDir/", "img")
    copyJAROrFolder(pluginURL, s"$targetDir$jekyllDir/", "js")
    copyJAROrFolder(pluginURL, s"$targetDir$jekyllDir/", "highlight/highlight.pack.js")
    copyJAROrFolder(pluginURL, s"$targetDir$jekyllDir/", "highlight/LICENSE")
    copyJAROrFolder(
      pluginURL,
      s"$targetDir$jekyllDir/",
      s"highlight/styles/${config.visualSettings.highlightTheme}.css"
    )
    copyJAROrFolder(pluginURL, s"$targetDir$jekyllDir/", "plugins")

    copyFilesRecursively(
      config.fileLocations.micrositeImgDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/img/"
    )
    copyFilesRecursively(
      config.fileLocations.micrositeCssDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/css/"
    )
    copyFilesRecursively(
      config.fileLocations.micrositeSassDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_sass_custom/"
    )
    copyFilesRecursively(
      config.fileLocations.micrositeJsDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/js/"
    )
    copyFilesRecursively(
      config.fileLocations.micrositeExternalLayoutsDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_layouts/"
    )
    copyFilesRecursively(
      config.fileLocations.micrositeExternalIncludesDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_includes/"
    )
    copyFilesRecursively(
      config.fileLocations.micrositeDataDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_data/"
    )
    copyFilesRecursively(
      config.fileLocations.micrositeStaticDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/${config.fileLocations.micrositeStaticDirectory.getName}/"
    )
    copyFilesRecursively(
      config.fileLocations.micrositePluginsDirectory.getAbsolutePath,
      s"$targetDir$jekyllDir/_plugins/"
    )

    List(createConfigYML(targetDir), createPalette(targetDir)) ++
      createLayouts(targetDir) ++ createPartialLayout(targetDir) ++ createFavicons(targetDir)
  }

  def buildAdditionalMd(): File = {
    val extraMdOutputDir = config.fileLocations.micrositeExtraMdFilesOutput
    extraMdOutputDir.mkdirs()

    config.fileLocations.micrositeExtraMdFiles foreach { case (sourceFile, targetFileConfig) =>
      println(
        s"Copying from ${sourceFile.getAbsolutePath} to ${extraMdOutputDir.getAbsolutePath}/$targetFileConfig"
      )

      val targetFileContent =
        s"""---
             |layout: ${targetFileConfig.layout}
             |${targetFileConfig.metaProperties map { case (key, value) =>
          "%s: %s" format (key, value)
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
    createFile(targetPath)

    val yaml             = config.configYaml
    val customProperties = yaml.yamlCustomProperties.toYaml.asYamlObject.fields
    val inlineYaml =
      if (yaml.yamlInline.nonEmpty)
        yaml.yamlInline.parseYaml.asYamlObject.fields
      else Map.empty[YamlValue, YamlValue]
    val fileYaml = yaml.yamlPath.fold(Map.empty[YamlValue, YamlValue])(f =>
      if (f.exists())
        Source.fromFile(f.getAbsolutePath).mkString.parseYaml.asYamlObject.fields
      else Map.empty[YamlValue, YamlValue]
    )

    writeContentToFile(
      YamlObject(customProperties ++ fileYaml ++ inlineYaml).prettyPrint,
      targetPath
    )

    targetPath.toFile
  }

  def createPalette(targetDir: String): File = {
    val targetPath = s"$targetDir$jekyllDir/_sass/_variables_palette.scss"
    createFile(targetPath)

    val content = config.visualSettings.palette
      .map { case (key, value) =>
        s"""$$$key: $value;"""
      }
      .mkString("\n")
    writeContentToFile(content, targetPath)

    targetPath.toFile
  }

  def createLayouts(targetDir: String): List[File] = {
    val layoutList =
      if (config.visualSettings.theme == "pattern")
        List(
          "home" -> new HomeLayout(config),
          "docs" -> new DocsLayout(config),
          "page" -> new PageLayout(config)
        )
      else
        List(
          "home"         -> new HomeLayout(config),
          "docs"         -> new DocsLayout(config),
          "homeFeatures" -> new FeaturesLayout(config),
          "page"         -> new PageLayout(config)
        )

    layoutList map { case (layoutName, layout) =>
      val targetPath = s"$targetDir$jekyllDir/_layouts/$layoutName.html"
      createFile(targetPath)

      writeContentToFile("<!DOCTYPE html>" + layout.render.toString(), targetPath)
      targetPath.toFile
    }
  }

  def createPartialLayout(targetDir: String): List[File] =
    List("menu" -> new MenuPartialLayout(config)) map { case (layoutName, layout) =>
      val targetPath = s"$targetDir$jekyllDir/_includes/$layoutName.html"
      writeContentToFile(layout.render.toString(), targetPath)
      targetPath.toFile
    }

  def createFavicons(targetDir: String): List[File] = {

    val sourceFile =
      if (config.visualSettings.theme == "pattern")
        s"$targetDir$jekyllDir/img/navbar_brand2x.png"
      else
        s"$targetDir$jekyllDir/img/light_navbar_brand.png"

    createFile(sourceFile)

    //This is a dirty classloader hack to allow the latest version of Scrimage to work.
    //This plugin's default classloader is limited and cannot load ImageReader instances.
    //We get a different ClassLoader that will work, replace it, and then set it back after we're done.
    //Will be fixed in Scrimage 4.1.0, see: https://github.com/sksamuel/scrimage/issues/217
    val desiredCL: ClassLoader = classOf[ImageReader].getClassLoader
    val currentCL              = Thread.currentThread.getContextClassLoader()

    try {
      Thread.currentThread.setContextClassLoader(desiredCL)

      (faviconFilenames zip faviconSizes)
        .map { case (name, size) =>
          (new File(s"$targetDir$jekyllDir/img/$name"), size)
        }
        .map { case (file, (width, height)) =>
          ImmutableImage.loader.fromFile(sourceFile.toFile).scaleTo(width, height).output(file)
        }
    } finally
    //Reset the classloader to what it was previously
    Thread.currentThread.setContextClassLoader(currentCL)
  }

  def copyConfigurationFile(sourceDir: File, targetDir: File): Unit = {

    val targetPath = s"$targetDir/_config.yml"
    createFile(targetPath)

    copyFilesRecursively(
      s"${sourceDir.getAbsolutePath}/_config.yml",
      targetPath.toFile.getAbsolutePath
    )
    ()
  }

  def directory(sourceDirPath: String): Seq[(File, String)] = {
    val sourceDir = file(sourceDirPath)
    Option(sourceDir.getParentFile)
      .map(parent => sourceDir.allPaths pair Path.relativeTo(parent))
      .getOrElse(sourceDir.allPaths pair Path.basic)
  }
}
