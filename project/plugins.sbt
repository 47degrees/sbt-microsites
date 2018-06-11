import sbt.Resolver.sonatypeRepo

resolvers ++= Seq(sonatypeRepo("snapshots"), sonatypeRepo("releases"))
addSbtPlugin("com.47deg" % "sbt-org-policies" % "0.9.1")

libraryDependencies += {
  lazy val sbtVersionValue = (sbtVersion in pluginCrossBuild).value

  scalaBinaryVersion.value match {
    case "2.10" => "org.scala-sbt" % "scripted-plugin"  % sbtVersionValue
    case _      => "org.scala-sbt" %% "scripted-plugin" % sbtVersionValue
  }
}
