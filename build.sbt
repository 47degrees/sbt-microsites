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

lazy val pluginSettings = Seq(
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
  micrositeDescription := "A sbt plugin to create awesome microsites for your project",
  micrositeBaseUrl := "sbt-microsites",
  micrositeDocumentationUrl := "/sbt-microsites/docs.html",
  micrositeGithubOwner := "47deg",
  micrositeGithubRepo := "sbt-microsites",
  micrositeExtratMdFiles := Map(file("README.md") -> "index.md"),
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val miscSettings = Seq(
  shellPrompt := { s: State =>
    val c     = scala.Console
    val blue  = c.RESET + c.BLUE + c.BOLD
    val white = c.RESET + c.BOLD

    val projectName = Project.extract(s).currentProject.id

    s"$blue$projectName$white>${c.RESET}"
  }
)

lazy val commonSettings = artifactSettings ++ miscSettings
lazy val allSettings    = pluginSettings ++ commonSettings ++ tutSettings

lazy val `sbt-microsites` = (project in file("."))
  .settings(moduleName := "sbt-microsites")
  .settings(allSettings: _*)
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3"))
  .settings(addSbtPlugin("org.tpolecat"     % "tut-plugin"          % "0.4.3"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-site"            % "1.0.0"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages"         % "0.5.4"))
  .enablePlugins(JavaServerAppPackaging, UniversalPlugin, JekyllPlugin, AutomateHeaderPlugin)

lazy val docs = (project in file("docs"))
  .settings(commonSettings: _*)
  .settings(micrositeSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(moduleName := "docs")
  .enablePlugins(MicrositesPlugin)
