libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value
resolvers ++= Seq(Resolver.sonatypeRepo("snapshots"), Resolver.sonatypeRepo("releases"))
addSbtPlugin("com.47deg" % "sbt-org-policies" % "0.4.28" exclude("com.47deg", "sbt-microsites"))
addSbtPlugin("com.47deg" % "sbt-microsites" % "0.5.8-SNAPSHOT")
