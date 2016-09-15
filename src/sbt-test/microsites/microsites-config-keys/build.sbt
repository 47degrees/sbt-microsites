enablePlugins(MicrositesPlugin)
scalaVersion := "2.11.8"

micrositeExtraMdFiles := Map(file("README.md") -> "index.md")
micrositeName := "test-microsite"
micrositeDescription := "Description of test-microsite"
micrositeAuthor := "Microsite author"
micrositeHomepage := "https://47deg.github.io/sbt-microsites/"
micrositeTwitter := "@47deg"
micrositeBaseUrl := "/test-microsite"
micrositeDocumentationUrl := "/api"
micrositeHighlightTheme := "just-another-theme"
micrositeGithubOwner := "47deg"
micrositeGithubRepo := "sbt-microsites"
micrositePalette := Map("brand-primary"   -> "#111111",
                        "brand-secondary" -> "#222222",
                        "brand-tertiary"  -> "#333333",
                        "gray-dark"       -> "#444444",
                        "gray"            -> "#555555",
                        "gray-light"      -> "#666666",
                        "gray-lighter"    -> "#777777",
                        "white-color"     -> "#888888")

lazy val check = TaskKey[Unit]("check")

check := {
  val configFiles = IO.readLines(file("target/site/_config.yml"))

  configFiles foreach {
    case s if s.toString.startsWith("name") && !s.contains("test-microsite") =>
      sys.error(s"Microsite Name doesn't match with $s")
    case s
        if s.toString.startsWith("description") && !s.contains("Description of test-microsite") =>
      sys.error(s"Microsite Description doesn't match with $s")
    case s if s.toString.startsWith("baseurl") && !s.contains("/test-microsite") =>
      sys.error(s"Microsite base URL doesn't match with $s")
    case _ =>
  }
}
