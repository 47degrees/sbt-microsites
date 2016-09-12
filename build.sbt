import sbt.Keys._
import de.heikoseeberger.sbtheader.license.Apache2_0

lazy val artifactSettings = Seq(
  name := "sbt-microsites",
  organization := "com.fortysevendeg",
  organizationName := "47 Degrees",
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

lazy val allSettings = commonSettings ++ artifactSettings ++ tutSettings

lazy val `sbt-microsites` = (project in file("."))
  .settings(moduleName := "sbt-microsites")
  .settings(allSettings: _*)
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3"))
  .settings(addSbtPlugin("org.tpolecat"     % "tut-plugin"          % "0.4.3"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-site"            % "1.0.0"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages"         % "0.5.4"))
  .enablePlugins(JavaServerAppPackaging, UniversalPlugin, JekyllPlugin, AutomateHeaderPlugin)
