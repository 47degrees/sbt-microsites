package microsites

import sbt._

trait MicrositeKeys {
  val microsite = taskKey[Seq[File]]("Create microsite files")
  val micrositeName = settingKey[String]("Microsite name")
  val micrositeDescription = settingKey[String]("Microsite description")
  val micrositeAuthor = settingKey[String]("Microsite author")
  val micrositeHomepage = settingKey[String]("Microsite homepage")
  val micrositeTwitter = settingKey[String]("Microsite twitter")
  val micrositeHighlightTheme = settingKey[String]("Microsite Highlight Theme")
  val micrositePalette = settingKey[Map[String, String]]("Microsite palette")
  val micrositeGithubOwner = settingKey[String]("Microsite Github owner")
  val micrositeGithubRepo = settingKey[String]("Microsite Github repo")
}
object MicrositeKeys extends MicrositeKeys