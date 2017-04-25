libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value
resolvers += Resolver.sonatypeRepo("releases")
addSbtPlugin("com.47deg" % "sbt-org-policies" % "0.4.16")
