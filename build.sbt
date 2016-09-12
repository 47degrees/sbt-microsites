import sbt.Keys._
import de.heikoseeberger.sbtheader.license.Apache2_0

lazy val artifactSettings = Seq(
  name := "sbt-microsites",
  organization := "com.fortysevendeg",
  organizationName := "47 Degrees",
  homepage := Option(url("http://47deg.github.io/sbt-microsites/")),
  organizationHomepage := Some(new URL("http://47deg.com")),
  headers := Map(
    "scala" -> Apache2_0("2016", "47 Degrees, LLC. <http://www.47deg.com>")
  )
)

lazy val commonSettings = Seq(
    sbtPlugin := true,
    scalaVersion in ThisBuild := "2.10.6",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "jgit-repo" at "http://download.eclipse.org/jgit/maven"
    ),
    libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.6.0",
    scalafmtConfig in ThisBuild := Some(file(".scalafmt"))
  ) ++ reformatOnCompileSettings

lazy val micrositeSettings = Seq(
  micrositeName := "sbt-microsites",
  micrositeDescription := "An easy way to create your project's microsite",
  micrositeHighlightTheme := "atom-one-light",
  micrositeBaseUrl := "sbt-microsites",
  micrositeGithubOwner := "47deg",
  micrositeGithubRepo := "sbt-microsites",
  micrositeExtratMdFiles := Map(file("README.md") -> "index.md"),
  micrositePalette := Map("brand-primary"         -> "#FC4053",
                          "brand-secondary"       -> "#B92239",
                          "brand-tertiary"        -> "#8C192F",
                          "gray-dark"             -> "#464646",
                          "gray"                  -> "#7E7E7E",
                          "gray-light"            -> "#E8E8E8",
                          "gray-lighter"          -> "#F6F6F6",
                          "white-color"           -> "#FFFFFF")
)

lazy val allSettings = commonSettings ++ artifactSettings ++ tutSettings

lazy val `sbt-microsites` = (project in file("."))
  .settings(moduleName := "sbt-microsites")
  .settings(allSettings: _*)
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3"))
  .settings(addSbtPlugin("org.tpolecat"     % "tut-plugin"          % "0.4.3"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-site"            % "1.0.0"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages"         % "0.5.4"))
  .enablePlugins(JavaServerAppPackaging, UniversalPlugin, JekyllPlugin, AutomateHeaderPlugin)

lazy val docs = (project in file("docs"))
  .settings(artifactSettings)
  .settings(moduleName := "sbt-microsite-docs")
  .enablePlugins(MicrositesPlugin)
