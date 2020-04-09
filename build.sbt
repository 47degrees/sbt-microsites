inThisBuild(
  List(
    name := "sbt-microsites",
    organization := "com.47deg",
    organizationName := "47 Degrees",
    organizationHomepage := Some(url("https://www.47deg.com")),
    homepage := Option(url("https://47degrees.github.io/sbt-microsites/")),
    description := "An sbt plugin to create awesome microsites for your project",
    startYear := Some(2016),
    scalaVersion := V.scala
  )
)

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
addCommandAlias("ci-docs", "docs/tut; headerCreateAll")

lazy val V = new {
  val ghPages: String             = "0.6.3"
  val github4s: String            = "0.23.0"
  val mdoc: String                = "2.1.1"
  val moultingyaml: String        = "0.4.2"
  val sbtSite: String             = "1.4.0"
  val scala: String               = "2.12.11"
  val scalatestScalacheck: String = "3.1.1.1"
  val scalatags: String           = "0.8.6"
  val scrimage: String            = "2.1.8"
  val tut: String                 = "0.6.13"
}

lazy val noPublishSettings = Seq(
  publish := ((): Unit),
  publishLocal := ((): Unit),
  publishArtifact := false,
  publishMavenStyle := false // suppress warnings about intransitive deps (not published anyway)
)

lazy val pluginSettings: Seq[Def.Setting[_]] = Seq(
  sbtPlugin := true,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("snapshots"),
    "jgit-repo" at "https://download.eclipse.org/jgit/maven"
  ),
  addSbtPlugin("org.tpolecat"     % "tut-plugin"  % V.tut),
  addSbtPlugin("org.scalameta"    % "sbt-mdoc"    % V.mdoc),
  addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % V.ghPages),
  addSbtPlugin("com.typesafe.sbt" % "sbt-site"    % V.sbtSite),
  libraryDependencies ++= Seq(
    "com.47deg"             %% "github4s"        % V.github4s,
    "net.jcazevedo"         %% "moultingyaml"    % V.moultingyaml,
    "com.lihaoyi"           %% "scalatags"       % V.scalatags,
    "com.sksamuel.scrimage" %% "scrimage-core"   % V.scrimage,
    "org.scalatestplus"     %% "scalacheck-1-14" % V.scalatestScalacheck % Test
  ),
  scriptedLaunchOpts := {
    scriptedLaunchOpts.value ++
      Seq(
        "-Xmx2048M",
        "-XX:ReservedCodeCacheSize=256m",
        "-XX:+UseConcMarkSweepGC",
        "-Dplugin.version=" + version.value,
        "-Dscala.version=" + scalaVersion.value
      )
  }
)

lazy val micrositeSettings: Seq[Def.Setting[_]] = Seq(
  micrositeName := "sbt-microsites",
  micrositeDescription := "An sbt plugin to create awesome microsites for your project",
  micrositeBaseUrl := "sbt-microsites",
  micrositeDocumentationUrl := "docs",
  micrositeGithubOwner := "47degrees",
  micrositeGithubRepo := "sbt-microsites",
  micrositeGithubToken := sys.env.get("GITHUB_TOKEN"),
  micrositePushSiteWith := GitHub4s,
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.svg"
)
