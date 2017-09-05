resolvers ++= Seq(Resolver.sonatypeRepo("snapshots"), Resolver.sonatypeRepo("releases"))
addSbtPlugin("com.47deg" % "sbt-org-policies" % "0.6.1-SNAPSHOT")

libraryDependencies += {
  lazy val sbtVersionValue = (sbtVersion in pluginCrossBuild).value

  scalaVersion.value match {
    case "2.10.6" => "org.scala-sbt" % "scripted-plugin"  % sbtVersionValue
    case _        => "org.scala-sbt" %% "scripted-plugin" % sbtVersionValue
  }
}
