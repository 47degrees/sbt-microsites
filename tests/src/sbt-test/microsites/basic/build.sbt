enablePlugins(MicrositesPlugin)

scalaVersion := sys.props("scala.version")

lazy val check = TaskKey[Unit]("check")

check := {
  val checkPathFile: File = file((crossTarget in Compile).value.getAbsolutePath + "/resource_managed/main/jekyll")

  if(!checkPathFile.exists())
    sys.error("Jekyll directory doesn't exist.")
}