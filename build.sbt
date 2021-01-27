ThisBuild / organization := "com.47deg"
ThisBuild / scalaVersion := "2.12.12"
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/47degrees/sbt-microsites"),
    "scm:git:https://github.com/47degrees/sbt-microsites.git",
    Some("scm:git:git@github.com:47degrees/sbt-microsites.git")
  )
)

addCommandAlias(
  "ci-test",
  "scalafmtCheckAll; scalafmtSbtCheck; microsite/mdoc; compile; test; scripted"
)
addCommandAlias(
  "ci-docs",
  "github; documentation/mdoc; headerCreateAll; microsite/publishMicrosite"
)
addCommandAlias("ci-publish", "github; ci-release")

lazy val `sbt-microsites` = (project in file("."))
  .settings(moduleName := "sbt-microsites")
  .settings(pluginSettings: _*)
  .enablePlugins(JekyllPlugin)
  .enablePlugins(SbtPlugin)

lazy val microsite = project
  .settings(micrositeSettings: _*)
  .settings(skip in publish := true)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(MdocPlugin)

lazy val documentation = project
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)
  .enablePlugins(MdocPlugin)

lazy val pluginSettings: Seq[Def.Setting[_]] = Seq(
  addSbtPlugin("org.scalameta"    % "sbt-mdoc"    % "2.2.16"),
  addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.3"),
  addSbtPlugin("com.typesafe.sbt" % "sbt-site"    % "1.4.1"),
  libraryDependencies ++= Seq(
    "com.47deg"             %% "github4s"        % "0.24.0",
    "net.jcazevedo"         %% "moultingyaml"    % "0.4.2",
    "com.lihaoyi"           %% "scalatags"       % "0.9.3",
    "com.sksamuel.scrimage" %% "scrimage-scala"  % "4.0.15",
    "org.scalatest"         %% "scalatest"       % "3.2.3"   % Test,
    "org.scalatestplus"     %% "scalacheck-1-15" % "3.2.3.0" % Test
  ),
  scriptedLaunchOpts ++= Seq(
    "-Xmx2048M",
    "-XX:ReservedCodeCacheSize=256m",
    "-XX:+UseConcMarkSweepGC",
    "-Dplugin.version=" + version.value,
    "-Dscala.version=" + scalaVersion.value
  )
)

lazy val micrositeSettings: Seq[Def.Setting[_]] = Seq(
  micrositeName := "sbt-microsites",
  micrositeDescription := "An sbt plugin to create awesome microsites for your project",
  micrositeBaseUrl := "sbt-microsites",
  micrositeDocumentationUrl := "docs",
  micrositeGithubToken := sys.env.get("GITHUB_TOKEN"),
  micrositePushSiteWith := GitHub4s,
  micrositeGitterChannelUrl := "47deg/sbt-microsites",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.svg"
)
