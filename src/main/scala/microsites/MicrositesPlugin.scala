package microsites

import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.packager.NativePackagerKeys
import com.typesafe.sbt.site.jekyll.JekyllPlugin
import microsites.layouts.Home
import sbt.Keys._
import sbt._
import sbt.plugins.IvyPlugin
import tut.Plugin._

object MicrositesPlugin extends AutoPlugin with NativePackagerKeys {

  object autoImport {
    val microsite = taskKey[Seq[File]]("Create microsite files")
    val micrositeName = settingKey[String]("Microsite name")
    val micrositeDescription = settingKey[String]("Microsite description")
  }

  import MicrositesPlugin.autoImport._
  import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._

  override def requires = IvyPlugin && SbtNativePackager && JekyllPlugin

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = tutSettings ++ Seq(
    micrositeName := name.value,
    micrositeDescription := description.value,
    microsite := createResources(
      dir = (resourceManaged in Compile).value,
      config = MicrositeConfig(
        name = micrositeName.value,
        description = micrositeDescription.value)),
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

    IO.write(targetFile, Home.html(config).toString())

    targetFile

  }


}