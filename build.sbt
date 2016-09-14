import sbt.Keys._
import de.heikoseeberger.sbtheader.license.Apache2_0

lazy val artifactSettings = Seq(
  name := "sbt-microsites",
  organization := "com.fortysevendeg",
  organizationName := "47 Degrees",
  homepage := Option(url("http://47deg.github.io/sbt-microsites/")),
  organizationHomepage := Some(new URL("http://47deg.com"))
)

lazy val commonSettings = Seq(
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "jgit-repo" at "http://download.eclipse.org/jgit/maven"
  ),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "scalatags" % "0.6.0",
    "org.scalactic" %% "scalactic" % "3.0.0",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.2" % "test"
  ),
  scalafmtConfig in ThisBuild := Some(file(".scalafmt")),
  headers := Map(
    "scala" -> Apache2_0("2016", "47 Degrees, LLC. <http://www.47deg.com>")
  )
) ++ artifactSettings ++ miscSettings ++ reformatOnCompileSettings

lazy val testSettings =
  ScriptedPlugin.scriptedSettings ++ Seq(
    publishLocal := (),
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq(
          "-Xmx2048M",
          "-XX:MaxPermSize=512M",
          "-XX:ReservedCodeCacheSize=256m",
          "-XX:+UseConcMarkSweepGC",
          "-Dproject.version=" + version.value,
          "-Dplugin.version=" + version.value,
          "-Dscala.version=" + scalaVersion.value
        )
    }
  )

lazy val pluginSettings = Seq(
  sbtPlugin := true,
  scalaVersion in ThisBuild := "2.10.6"
) ++ testSettings

lazy val micrositeSettings = Seq(
  micrositeName := "sbt-microsites",
  micrositeDescription := "A sbt plugin to create awesome microsites for your project",
  micrositeBaseUrl := "sbt-microsites",
  micrositeDocumentationUrl := "/sbt-microsites/docs.html",
  micrositeGithubOwner := "47deg",
  micrositeGithubRepo := "sbt-microsites",
  micrositeExtraMdFiles := Map(file("README.md") -> "index.md"),
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val miscSettings = Seq(
  shellPrompt := { s: State =>
    val c = scala.Console
    val blue = c.RESET + c.BLUE + c.BOLD
    val white = c.RESET + c.BOLD

    val projectName = Project.extract(s).currentProject.id

    s"$blue$projectName$white>${c.RESET}"
  }
)

lazy val root = (project in file("."))
  .settings(miscSettings: _*)
  .aggregate(docs, core, `sbt-microsites`)

lazy val docs = (project in file("docs"))
  .enablePlugins(MicrositesPlugin)
  .settings(commonSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(micrositeSettings: _*)

lazy val core = (project in file("core"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(commonSettings: _*)

lazy val `sbt-microsites` = (project in file("sbt-microsites"))
  .settings(moduleName := "sbt-microsites")
  .settings(commonSettings: _*)
  .settings(pluginSettings: _*)
  .settings(tutSettings: _*)
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3"))
  .settings(addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.3"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.0.0"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4"))
  .enablePlugins(JavaServerAppPackaging, UniversalPlugin, JekyllPlugin, AutomateHeaderPlugin)
  .dependsOn(core)

addCommandAlias("testAll", ";core/test;sbt-microsites/scripted")
