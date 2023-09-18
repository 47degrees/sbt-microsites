ThisBuild / organization := "com.47deg"
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
  .settings(publish / skip := true)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(MdocPlugin)

lazy val documentation = project
  .settings(mdocOut := file("."))
  .settings(publish / skip := true)
  .enablePlugins(MdocPlugin)

lazy val pluginSettings: Seq[Def.Setting[_]] = Seq(
  addSbtPlugin("org.scalameta"    % "sbt-mdoc"    % "2.3.7"),
  addSbtPlugin("com.github.sbt"   % "sbt-ghpages" % "0.7.0"),
  addSbtPlugin("com.typesafe.sbt" % "sbt-site"    % "1.4.1"),
  libraryDependencies ++= Seq(
    "com.47deg"             %% "github4s"            % "0.32.0",
    "org.http4s"            %% "http4s-blaze-client" % "0.23.15",
    "net.jcazevedo"         %% "moultingyaml"        % "0.4.2",
    "com.lihaoyi"           %% "scalatags"           % "0.12.0",
    "com.sksamuel.scrimage" %% "scrimage-scala"      % "4.0.42",
    "com.github.marklister" %% "base64"              % "0.3.0",
    "org.scalatest"         %% "scalatest"           % "3.2.17"   % Test,
    "org.scalatestplus"     %% "scalacheck-1-16"     % "3.2.14.0" % Test
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
  micrositeName             := "sbt-microsites",
  micrositeDescription      := "An sbt plugin to create awesome microsites for your project",
  micrositeBaseUrl          := "sbt-microsites",
  micrositeDocumentationUrl := "docs",
  micrositeGithubToken      := sys.env.get("GITHUB_TOKEN"),
  micrositePushSiteWith     := GitHub4s,
  micrositeGitterChannelUrl := "47deg/sbt-microsites",
  makeSite / includeFilter := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.svg"
)
