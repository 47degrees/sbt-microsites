package microsites

import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.packager.NativePackagerKeys
import com.typesafe.sbt.packager.MappingsHelper._
import com.typesafe.sbt.packager.universal.UniversalPlugin
import com.typesafe.sbt.SbtNativePackager.Universal
import com.typesafe.sbt.site.jekyll.JekyllPlugin
import sbt.Keys._
import sbt._
import sbt.plugins.IvyPlugin
import tut.Plugin._
import microsites.FileHelper._

object MicrositesPlugin extends AutoPlugin with NativePackagerKeys {

  object autoImport extends MicrositeKeys

  import MicrositesPlugin.autoImport._
  import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._

  override def requires = IvyPlugin && SbtNativePackager && JekyllPlugin && UniversalPlugin

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = tutSettings ++
    micrositeSettings ++
    Seq(
    mappings in Universal ++= directory("src/main/resources/microsite"),
    microsite := createResources(
      resourcesDir = (resourceDirectory in Compile).value,
      resourceManagedDir = (resourceManaged in Compile).value,
      config = MicrositeConfig(
        name = micrositeName.value,
        description = micrositeDescription.value,
        author = micrositeAuthor.value,
        homepage = micrositeHomepage.value,
        twitter = micrositeTwitter.value,
        highlightTheme = micrositeHighlightTheme.value
      )),
    sourceDirectory in Jekyll := resourceManaged.value / "main" / "jekyll",
    tutSourceDirectory := sourceDirectory.value / "tut",
    tutTargetDirectory := resourceManaged.value / "main" / "jekyll"
  )

  lazy val micrositeSettings = Seq(
    micrositeName := name.value,
    micrositeDescription := description.value,
    micrositeAuthor := organizationName.value,
    micrositeHomepage := homepage.value.map(_.toString).getOrElse(""),
    micrositeTwitter := "",
    micrositeHighlightTheme := "tomorrow"
  )

  object Resolvers {
    val allResolvers = Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases"))
  }

  def createResources(resourcesDir: File, resourceManagedDir: File, config: MicrositeConfig): Seq[File] = {

    val sourceDir = getPathWithSlash(resourcesDir)
    val targetDir = getPathWithSlash(resourceManagedDir)

    copyPluginResources(getClass.getProtectionDomain.getCodeSource.getLocation, s"${targetDir}jekyll/")

    copyFilesRecursively(s"${sourceDir}microsite", s"${targetDir}jekyll/img/")

    Seq(createConfigYML(config, targetDir), createLayouts(config, targetDir))
  }

  def createConfigYML(config: MicrositeConfig, targetDir: String): File = {

    val targetFile = createFilePathIfNotExists(s"${targetDir}jekyll/_config.yml")

    IO.write(targetFile, s"""name: ${config.name}
                             |description: "${config.description}"
                             |github_owner: 47deg
                             |baseurl: /mylibrary
                             |highlight_theme: tomorrow
                             |docs: true
                             |
                             |markdown: kramdown
                             |collections:
                             |  tut:
                             |    output: true
                             |""".stripMargin)

    targetFile
  }

  def createLayouts(config: MicrositeConfig, targetDir: String): File = {

    val targetFile = createFilePathIfNotExists(s"${targetDir}jekyll/_layouts/home.html")

    IO.write(targetFile, Layouts.home(config).toString())
    targetFile
  }

}