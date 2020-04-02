pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toCharArray)

lazy val `sbt-microsites` = (project in file("."))
  .settings(moduleName := "sbt-microsites")
  .settings(pluginSettings: _*)
  .enablePlugins(JekyllPlugin)
  .enablePlugins(SbtPlugin)

lazy val docs = (project in file("docs"))
  .settings(moduleName := "docs")
  .settings(micrositeSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(
    Seq(
      buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
      buildInfoPackage := "microsites"
    ): _*
  )
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(TutPlugin)
  .enablePlugins(BuildInfoPlugin)

addCommandAlias("ci-test", "scalafmtCheck; scalafmtSbtCheck; docs/tut; compile; test; scripted")
addCommandAlias("ci-docs", "docs/tut")
