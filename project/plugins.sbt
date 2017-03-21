libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin("com.47deg" % "sbt-org-policies" % "0.2.2-SNAPSHOT")
