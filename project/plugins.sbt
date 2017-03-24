libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin("com.47deg" % "sbt-org-policies" % "0.3.0")
addSbtPlugin("com.47deg" % "sbt-microsites"   % "0.5.0")
