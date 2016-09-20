addSbtPlugin("com.typesafe.sbt"  % "sbt-native-packager" % "1.0.3")
addSbtPlugin("org.tpolecat"      % "tut-plugin"          % "0.4.4")
addSbtPlugin("com.typesafe.sbt"  % "sbt-site"            % "1.0.0")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"             % "1.0.1")
addSbtPlugin("de.heikoseeberger" % "sbt-header"          % "1.6.0")
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"        % "0.3.1")
resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"
addSbtPlugin("com.typesafe.sbt"                    % "sbt-ghpages"     % "0.5.4")
libraryDependencies <+= sbtVersion("org.scala-sbt" % "scripted-plugin" % _)
// Plugin inception dependency to be able to generate the sbt-microsites' microsite
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin("com.fortysevendeg" % "sbt-microsites" % "0.2.3-SNAPSHOT" changing ())
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")
