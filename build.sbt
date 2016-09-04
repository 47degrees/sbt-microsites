organization := "com.fortysevendeg"
name := "sbt-microsites"
version := "1.0.1-SNAPSHOT"
sbtPlugin := true

lazy val main = (project in file("."))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3"))
  .settings(addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.3"))
  .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.0.0"))
  .settings(tutSettings :_*)
  .settings(libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.6.0")
  .enablePlugins(JavaServerAppPackaging, UniversalDeployPlugin, JekyllPlugin)