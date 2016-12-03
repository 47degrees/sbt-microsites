addSbtPlugin("com.fortysevendeg" % "sbt-catalysts-extras" % "0.1.2")
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"         % "0.4.7")
libraryDependencies <+= sbtVersion("org.scala-sbt" % "scripted-plugin" % _)
// Plugin inception dependency to be able to generate the sbt-microsites' microsite
addSbtPlugin("com.fortysevendeg" % "sbt-microsites" % "0.3.3")
