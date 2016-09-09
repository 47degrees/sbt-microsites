package microsites

import sbt._

trait MicrositeKeys {
  val microsite = taskKey[Seq[File]]("Create microsite files")
  val micrositeName = settingKey[String]("Microsite name")
  val micrositeDescription = settingKey[String]("Microsite description")
  val micrositeAuthor = settingKey[String]("Microsite author")
  val micrositeHomepage = settingKey[String]("Microsite homepage")
  val micrositeTwitter = settingKey[String]("Microsite twitter")
  val micrositeBaseUrl = settingKey[String]("Microsite site base url")
  val micrositeDocumentationUrl = settingKey[String]("Microsite site documentation url")
  val micrositeHighlightTheme = settingKey[String]("Microsite Highlight Theme")
  val micrositeImgDirectory = settingKey[File]("Optional. Microsite images directory. By default, it'll be the resourcesDirectory + '/microsite/img'")
  val micrositeCssDirectory = settingKey[File]("Optional. Microsite CSS directory. By default, it'll be the resourcesDirectory + '/microsite/css'")
  val micrositeExtratMdFiles = settingKey[Seq[File]]("Optional. Additional document files located in a different place from the tutSourceDirectory. By default, it's empty")
  val micrositePalette = settingKey[Map[String, String]]("Microsite palette")
  val micrositeGithubOwner = settingKey[String]("Microsite Github owner")
  val micrositeGithubRepo = settingKey[String]("Microsite Github repo")
}
object MicrositeKeys extends MicrositeKeys