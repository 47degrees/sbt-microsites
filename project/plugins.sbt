import sbt.Resolver.sonatypeRepo

resolvers ++= Seq(sonatypeRepo("snapshots"), sonatypeRepo("releases"))

addSbtPlugin("com.47deg"        % "sbt-org-policies" % "0.13.3")
addSbtPlugin("com.47deg"        % "sbt-microsites"   % "1.1.4")
addSbtPlugin("com.eed3si9n"     % "sbt-buildinfo"    % "0.9.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-site"         % "1.4.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages"      % "0.6.3")
addSbtPlugin("org.tpolecat"     % "tut-plugin"       % "0.6.13")
addSbtPlugin("org.scalameta"    % "sbt-mdoc"         % "2.1.5")
