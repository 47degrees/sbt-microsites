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

import microsites.domain.MicrositeSettings
import microsites.layouts._
import microsites.util.FileHelper._
import sbt._

class MicrositeHelper(config: MicrositeSettings) {
  implicitly(config)

  def createResources(resourceManagedDir: File): Seq[File] = {

    val targetDir: String = getPathWithSlash(resourceManagedDir)
    val pluginURL: URL    = getClass.getProtectionDomain.getCodeSource.getLocation

    copyPluginResources(pluginURL, s"${targetDir}jekyll/", "_sass")
    copyPluginResources(pluginURL, s"${targetDir}jekyll/", "css")
    copyPluginResources(pluginURL, s"${targetDir}jekyll/", "js")

    copyFilesRecursively(config.micrositeImgDirectory.getAbsolutePath, s"${targetDir}jekyll/img/")
    copyFilesRecursively(config.micrositeCssDirectory.getAbsolutePath, s"${targetDir}jekyll/css/")

    config.micrositeExtraMdFiles foreach {
      case (sourceFile, relativeTargetFile) =>
        copyFilesRecursively(sourceFile.getAbsolutePath, s"${targetDir}jekyll/$relativeTargetFile")
    }

    Seq(createConfigYML(targetDir), createPalette(targetDir)) ++
      createLayouts(targetDir) ++ createPartialLayout(targetDir)
  }

  def createConfigYML(targetDir: String): File = {
    val targetFile = createFilePathIfNotExists(s"${targetDir}jekyll/_config.yml")

    val baseUrl =
      if (!config.micrositeBaseUrl.isEmpty && !config.micrositeBaseUrl.startsWith("/"))
        s"/${config.micrositeBaseUrl}"
      else config.micrositeBaseUrl

    IO.write(targetFile,
             s"""name: ${config.name}
          |description: "${config.description}"
          |baseurl: $baseUrl
          |docs: true
          |
                             |markdown: kramdown
          |collections:
          |  tut:
          |    output: true
          |""".stripMargin)

    targetFile
  }

  def createPalette(targetDir: String): File = {
    val targetFile = createFilePathIfNotExists(s"${targetDir}jekyll/_sass/_variables_palette.scss")
    val content    = config.palette.map { case (key, value) => s"""$$$key: $value;""" }.mkString("\n")
    IO.write(targetFile, content)
    targetFile
  }

  def createLayouts(targetDir: String): Seq[File] =
    List(
      "home" -> new HomeLayout(config),
      "docs" -> new DocsLayout(config),
      "page" -> new PageLayout(config)
    ) map {
      case (layoutName, layout) =>
        val targetFile = createFilePathIfNotExists(s"${targetDir}jekyll/_layouts/$layoutName.html")
        IO.write(targetFile, layout.render.toString())
        targetFile
    }

  def createPartialLayout(targetDir: String): Seq[File] =
    List("menu" -> new MenuPartialLayout(config)) map {
      case (layoutName, layout) =>
        val targetFile =
          createFilePathIfNotExists(s"${targetDir}jekyll/_includes/$layoutName.html")
        IO.write(targetFile, layout.render.toString())
        targetFile
    }

  def copyConfigurationFile(sourceDir: File, targetDir: File) = {

    val targetFile = createFilePathIfNotExists(s"$targetDir/_config.yml")

    copyFilesRecursively(s"${sourceDir.getAbsolutePath}/_config.yml", targetFile.getAbsolutePath)
  }
}
