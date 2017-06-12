libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value
resolvers ++= Seq(Resolver.sonatypeRepo("snapshots"), Resolver.sonatypeRepo("releases"))
addSbtPlugin("com.47deg" % "sbt-org-policies" % "0.5.6")
