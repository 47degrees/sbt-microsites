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
}
object MicrositeKeys extends MicrositeKeys