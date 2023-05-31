ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)
addSbtPlugin("com.github.sbt"      % "sbt-ci-release"           % "1.5.12")
addSbtPlugin("org.scalameta"       % "sbt-scalafmt"             % "2.5.0")
addSbtPlugin("de.heikoseeberger"   % "sbt-header"               % "5.9.0")
addSbtPlugin("com.alejandrohdezma" % "sbt-github"               % "0.11.8")
addSbtPlugin("com.alejandrohdezma" % "sbt-github-header"        % "0.11.8")
addSbtPlugin("com.alejandrohdezma" % "sbt-github-mdoc"          % "0.11.8")
addSbtPlugin("com.alejandrohdezma" % "sbt-remove-test-from-pom" % "0.1.0")
addSbtPlugin("org.scalameta"       % "sbt-mdoc"                 % "2.3.7")
addSbtPlugin("com.github.sbt"      % "sbt-ghpages"              % "0.8.0")
addSbtPlugin("com.typesafe.sbt"    % "sbt-site"                 % "1.4.1")
