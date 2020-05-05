import sbt.Resolver.sonatypeRepo

resolvers ++= Seq(sonatypeRepo("snapshots"), sonatypeRepo("releases"))

addSbtPlugin("com.eed3si9n"         % "sbt-buildinfo"     % "0.9.0")
addSbtPlugin("com.geirsson"         % "sbt-ci-release"    % "1.5.3")
addSbtPlugin("org.scalameta"        % "sbt-scalafmt"      % "2.3.4")
addSbtPlugin("de.heikoseeberger"    % "sbt-header"        % "5.4.0")
addSbtPlugin("com.alejandrohdezma" %% "sbt-github"        % "0.6.0")
addSbtPlugin("com.alejandrohdezma"  % "sbt-github-header" % "0.6.0")
addSbtPlugin("com.alejandrohdezma"  % "sbt-github-mdoc"   % "0.6.0")
addSbtPlugin("com.alejandrohdezma"  % "sbt-mdoc-toc"      % "0.2")
addSbtPlugin("org.tpolecat"         % "tut-plugin"        % "0.6.13")
addSbtPlugin("org.scalameta"        % "sbt-mdoc"          % "2.2.0")
addSbtPlugin("com.typesafe.sbt"     % "sbt-ghpages"       % "0.6.3")
addSbtPlugin("com.typesafe.sbt"     % "sbt-site"          % "1.4.0")

unmanagedSourceDirectories in Compile ++= Seq(
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "scala"
)
libraryDependencies ++= Seq(
  "com.47deg"             %% "github4s"      % "0.24.0",
  "net.jcazevedo"         %% "moultingyaml"  % "0.4.2",
  "com.lihaoyi"           %% "scalatags"     % "0.9.0",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8"
)
