addSbtPlugin("com.typesafe.sbt"  % "sbt-native-packager" % "1.0.3")
addSbtPlugin("org.tpolecat"      % "tut-plugin"          % "0.4.3")
addSbtPlugin("com.typesafe.sbt"  % "sbt-site"            % "1.0.0")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"             % "1.0.1")
addSbtPlugin("de.heikoseeberger" % "sbt-header"          % "1.6.0")
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"        % "0.3.1")
resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")
// Plugin inception dependency to be able to generate the sbt-microsites' microsite
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin("com.fortysevendeg" % "sbt-microsites" % "0.2.0-SNAPSHOT" changing ())
