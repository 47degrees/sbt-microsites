import sbt.Keys._
import de.heikoseeberger.sbtheader.license.Apache2_0
import catext.Dependencies._

val dev  = Seq(Dev("47 Degrees (twitter: @47deg)", "47 Degrees"))
val gh   = GitHubSettings("com.fortysevendeg", "sbt-microsites", "47 Degrees", apache)
val vAll = Versions(versions, libraries, scalacPlugins)

lazy val artifactSettings = Seq(
  name := gh.proj,
  organization := gh.org,
  organizationName := gh.publishOrg,
  homepage := Option(url("http://www.47deg.com")),
  organizationHomepage := Some(new URL("http://47deg.com")),
  headers := Map(
    "scala" -> Apache2_0("2016", "47 Degrees, LLC. <http://www.47deg.com>")
  )
)

pgpPassphrase := Some(sys.env.getOrElse("PGP_PASSPHRASE", "").toCharArray)
pgpPublicRing := file(s"${sys.env.getOrElse("PGP_FOLDER", ".")}/pubring.gpg")
pgpSecretRing := file(s"${sys.env.getOrElse("PGP_FOLDER", ".")}/secring.gpg")

lazy val pluginSettings = Seq(
    sbtPlugin := true,
    scalaVersion in ThisBuild := "2.10.6",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "jgit-repo" at "http://download.eclipse.org/jgit/maven"
    ),
    libraryDependencies ++= Seq(
      "com.lihaoyi"    %% "scalatags"    % "0.6.0",
      "org.scalactic"  %% "scalactic"    % "3.0.0",
      "net.jcazevedo"  %% "moultingyaml" % "0.4.0",
      "org.scalatest"  %% "scalatest"    % versions("scalatest") % "test",
      "org.scalacheck" %% "scalacheck"   % versions("scalacheck") % "test"
    ),
    scalafmtConfig in ThisBuild := Some(file(".scalafmt"))
  ) ++ reformatOnCompileSettings

lazy val micrositeSettings = Seq(
  micrositeName := "sbt-microsites",
  micrositeDescription := "An sbt plugin to create awesome microsites for your project",
  micrositeBaseUrl := "sbt-microsites",
  micrositeDocumentationUrl := "/sbt-microsites/docs/",
  micrositeGithubOwner := "47deg",
  micrositeGithubRepo := "sbt-microsites",
  micrositeHighlightTheme := "color-brewer",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
)

lazy val buildInfoSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
  buildInfoPackage := "microsites"
)

lazy val commonSettings = artifactSettings ++ miscSettings
lazy val allSettings = pluginSettings ++
    commonSettings ++
    tutSettings ++
    testScriptedSettings ++
    sharedReleaseProcess ++
    credentialSettings ++
    sharedPublishSettings(gh, dev)

lazy val `sbt-microsites` = (project in file("."))
  .settings(moduleName := "sbt-microsites")
  .settings(allSettings: _*)
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3"))
  .settings(addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.7"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.0.0"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4"))
  .enablePlugins(JekyllPlugin, AutomateHeaderPlugin)

lazy val docs = (project in file("docs"))
  .settings(commonSettings: _*)
  .settings(micrositeSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(buildInfoSettings: _*)
  .settings(moduleName := "docs")
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(BuildInfoPlugin)
