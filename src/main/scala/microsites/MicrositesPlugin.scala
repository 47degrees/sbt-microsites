package microsites

import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.packager.NativePackagerKeys
import com.typesafe.sbt.packager.MappingsHelper._
import com.typesafe.sbt.packager.universal.UniversalPlugin
import com.typesafe.sbt.SbtNativePackager.Universal
import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.site.jekyll.JekyllPlugin
import sbt.Keys._
import sbt._
import sbt.plugins.IvyPlugin
import tut.Plugin._

object MicrositesPlugin extends AutoPlugin with NativePackagerKeys {

  object autoImport {
    val microsite = taskKey[Seq[File]]("Create microsite files")
    val micrositeName = settingKey[String]("Microsite name")
    val micrositeDescription = settingKey[String]("Microsite description")
    val micrositeAuthor = settingKey[String]("Microsite author")
    val micrositeHomepage = settingKey[String]("Microsite homepage")
    val micrositeTwitter = settingKey[String]("Microsite twitter")
    val micrositeHighlightTheme = settingKey[String]("Microsite Highlight Theme")
  }

  import MicrositesPlugin.autoImport._
  import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._

  override def requires = IvyPlugin && SbtNativePackager && JekyllPlugin && UniversalPlugin

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = tutSettings ++ Seq(
    mappings in Universal ++= directory("src/main/resources/microsite"),
    micrositeName := name.value,
    micrositeDescription := description.value,
    micrositeAuthor := organizationName.value,
    micrositeHomepage := homepage.value.map(_.toString).getOrElse(""),
    micrositeTwitter := "",
    micrositeHighlightTheme := "tomorrow",
    microsite := createResources(
      dir = (resourceManaged in Compile).value,
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

  object Resolvers {
    val sonatypeSnapshots = "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
    val allResolvers = Seq(sonatypeSnapshots)
  }


  def createResources(dir: File, config: MicrositeConfig): Seq[File] = {

    val targetDir = dir.getAbsolutePath + (if (dir.getAbsolutePath.endsWith("/")) "" else "/")

    mappings in Universal ++= (sourceDirectory map (src => directory(src / "main" / "resources" / "microsite")))
      .value.map{ fileString =>
        fileString._1 -> s"${targetDir}jekyll/img/${fileString._1.name}"
      }


    Seq(createConfigYML(config, targetDir), createLayouts(config, targetDir))
  }

  def createConfigYML(config: MicrositeConfig, targetDir: String): File = {

    val targetFile = new File(targetDir + "jekyll/" + "_config.yml")

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
    val layoutsDir = targetDir + "jekyll/_layouts/"
    val targetFile = new File(layoutsDir + "home.html")
    IO.write(targetFile, Layouts.home(config).toString())
    targetFile
  }


}