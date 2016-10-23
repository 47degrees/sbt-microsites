addSbtPlugin("com.fortysevendeg" % "sbt-catalysts-ext" % "0.0.2")
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"      % "0.4.7")
libraryDependencies <+= sbtVersion("org.scala-sbt" % "scripted-plugin" % _)
// Plugin inception dependency to be able to generate the sbt-microsites' microsite
addSbtPlugin("com.fortysevendeg" % "sbt-microsites" % "0.2.6")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.12")
