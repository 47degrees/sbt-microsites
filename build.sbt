name := "sbt-microsites"
version := "1.0.0-SNAPSHOT"
sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3")

lazy val main = (project in file(".")).enablePlugins(JavaServerAppPackaging, UniversalDeployPlugin)
