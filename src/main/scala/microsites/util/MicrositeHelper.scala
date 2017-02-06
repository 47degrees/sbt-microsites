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

package microsites.util

import java.io.File

import microsites._
import net.jcazevedo.moultingyaml.{YamlObject, _}
import ConfigYamlProtocol._
import microsites.layouts._
import microsites.util.FileHelper._
import sbt._

import scala.io.Source

class MicrositeHelper(config: MicrositeSettings) {
  implicitly(config)

  val jekyllDir = "jekyll"

  def createResources(resourceManagedDir: File, tutSourceDirectory: File): List[File] = {

    val targetDir: String    = getPathWithSlash(resourceManagedDir)
    val tutSourceDir: String = getPathWithSlash(tutSourceDirectory)
    val pluginURL: URL       = getClass.getProtectionDomain.getCodeSource.getLocation

    copyPluginResources(pluginURL, s"$targetDir$jekyllDir/", "_sass")
    copyPluginResources(pluginURL, s"$targetDir$jekyllDir/", "css")
    copyPluginResources(pluginURL, s"$targetDir$jekyllDir/", "img")
    copyPluginResources(pluginURL, s"$targetDir$jekyllDir/", "js")
    copyPluginResources(pluginURL, s"$targetDir$jekyllDir/", "highlight")

    copyFilesRecursively(config.fileLocations.micrositeImgDirectory.getAbsolutePath,
                         s"$targetDir$jekyllDir/img/")
    copyFilesRecursively(config.fileLocations.micrositeCssDirectory.getAbsolutePath,
                         s"$targetDir$jekyllDir/css/")
    copyFilesRecursively(config.fileLocations.micrositeJsDirectory.getAbsolutePath,
                         s"$targetDir$jekyllDir/js/")
    copyFilesRecursively(config.fileLocations.micrositeExternalLayoutsDirectory.getAbsolutePath,
                         s"$targetDir$jekyllDir/_layouts/")
    copyFilesRecursively(config.fileLocations.micrositeExternalIncludesDirectory.getAbsolutePath,
                         s"$targetDir$jekyllDir/_includes/")
    copyFilesRecursively(config.fileLocations.micrositeDataDirectory.getAbsolutePath,
                         s"$targetDir$jekyllDir/_data/")

    config.fileLocations.micrositeExtraMdFiles foreach {
      case (sourceFile, targetFileConfig) =>
        println(s"Copying from ${sourceFile.getAbsolutePath} to $tutSourceDir$targetFileConfig")

        val targetFileContent =
          s"""---
             |layout: ${targetFileConfig.layout}
             |${targetFileConfig.metaProperties map {
               case (key, value) => "%s: %s" format (key, value)
             } mkString ("", "\n", "")}
             |---
             |${Source.fromFile(sourceFile.getAbsolutePath).mkString}
             |""".stripMargin

        IO.write(s"$tutSourceDir${targetFileConfig.fileName}".toFile, targetFileContent)
    }

    List(createConfigYML(targetDir), createPalette(targetDir)) ++
      createLayouts(targetDir) ++ createPartialLayout(targetDir)
  }

  def createConfigYML(targetDir: String): File = {
    val targetFile = createFilePathIfNotExists(s"$targetDir$jekyllDir/_config.yml")

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

    IO.write(targetFile, YamlObject(customProperties ++ fileYaml ++ inlineYaml).prettyPrint)

    targetFile
  }

  def createPalette(targetDir: String): File = {
    val targetFile = createFilePathIfNotExists(
      s"$targetDir$jekyllDir/_sass/_variables_palette.scss")
    val content = config.visualSettings.palette.map {
      case (key, value) => s"""$$$key: $value;"""
    }.mkString("\n")
    IO.write(targetFile, content)
    targetFile
  }

  def createLayouts(targetDir: String): List[File] =
    List(
      "home" -> new HomeLayout(config),
      "docs" -> new DocsLayout(config),
      "page" -> new PageLayout(config)
    ) map {
      case (layoutName, layout) =>
        val targetFile =
          createFilePathIfNotExists(s"$targetDir$jekyllDir/_layouts/$layoutName.html")
        IO.write(targetFile, layout.render.toString())
        targetFile
    }

  def createPartialLayout(targetDir: String): List[File] =
    List("menu" -> new MenuPartialLayout(config)) map {
      case (layoutName, layout) =>
        val targetFile =
          createFilePathIfNotExists(s"$targetDir$jekyllDir/_includes/$layoutName.html")
        IO.write(targetFile, layout.render.toString())
        targetFile
    }

  def copyConfigurationFile(sourceDir: File, targetDir: File): Unit = {

    val targetFile = createFilePathIfNotExists(s"$targetDir/_config.yml")

    copyFilesRecursively(s"${sourceDir.getAbsolutePath}/_config.yml", targetFile.getAbsolutePath)
  }

  /*
   * This method has been extracted from the sbt-native-packager plugin:
   *
   * https://github.com/sbt/sbt-native-packager/blob/b5e2bb9027d08c00420476e6be0d876cf350963a/src/main/scala/com/typesafe/sbt/packager/MappingsHelper.scala#L21
   *
   */
  def directory(sourceDirPath: String): Seq[(File, String)] = {
    val sourceDir = file(sourceDirPath)
    Option(sourceDir.getParentFile)
      .map(parent => sourceDir.*** pair relativeTo(parent))
      .getOrElse(sourceDir.*** pair basic)
  }
}
