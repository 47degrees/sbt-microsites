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

lazy val pluginSettings = Seq(
    sbtPlugin := true,
    scalaVersion in ThisBuild := "2.10.6",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "jgit-repo" at "http://download.eclipse.org/jgit/maven"
    ),
    libraryDependencies ++= Seq(
      "com.lihaoyi"   %% "scalatags" % "0.6.0",
      "org.scalactic" %% "scalactic" % "3.0.0"
    ),
    scalafmtConfig in ThisBuild := Some(file(".scalafmt"))
  ) ++ reformatOnCompileSettings ++ addTestLibs(vAll, "scalatest", "scalacheck")

lazy val micrositeSettings = Seq(
  micrositeName := "sbt-microsites",
  micrositeDescription := "An sbt plugin to create awesome microsites for your project",
  micrositeBaseUrl := "sbt-microsites",
  micrositeDocumentationUrl := "/sbt-microsites/docs/",
  micrositeGithubOwner := "47deg",
  micrositeGithubRepo := "sbt-microsites",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
)

lazy val jsSettings = Seq(
  scalaVersion := "2.11.8",
  scalaJSStage in Global := FastOptStage,
  parallelExecution := false,
  scalaJSUseRhino := false,
  requiresDOM := false,
  jsEnv := NodeJSEnv().value,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.9.0",
    "com.lihaoyi" %%% "upickle" % "0.4.1",
    "org.scala-exercises" %%% "evaluator-client" % "0.1.1-SNAPSHOT",
    "com.lihaoyi" %%% "scalatags"  % "0.6.0",
    "org.querki" %%% "jquery-facade" % "1.0-RC6",
    "org.denigma" %%% "codemirror-facade" % "5.11-0.7"
  ),
  resolvers ++= Seq(Resolver.url(
    "bintray-sbt-plugin-releases",
    url("https://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.bintrayRepo("denigma", "denigma-releases")),
  jsDependencies ++= Seq(
    "org.webjars" % "jquery" % "2.1.3" / "2.1.3/jquery.js",
    ProvidedJS / "codemirror.js",
    ProvidedJS / "javascript.js" dependsOn "codemirror.js"
  )
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
    pgpSettings ++
    credentialSettings ++
    sharedPublishSettings(gh, dev)

lazy val `sbt-microsites` = (project in file("."))
  .settings(moduleName := "sbt-microsites")
  .settings(allSettings: _*)
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3"))
  .settings(addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.5"))
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

lazy val js = (project in file("js"))
  .settings(moduleName := "sbt-microsites-js")
  .settings(commonSettings:_*)
  .settings(jsSettings:_*)
  .settings(KazariBuild.kazariTasksSettings:_*)
  .enablePlugins(ScalaJSPlugin)