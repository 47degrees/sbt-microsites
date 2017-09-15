import microsites._

enablePlugins(MicrositesPlugin)

scalaVersion := sys.props("scala.version")

micrositeExtraMdFiles := Map(
  file("README.md") -> ExtraMdFileConfig(
    "index.md",
    "home"
  )
)

lazy val check = TaskKey[Unit]("check")

check := {

  // check markdown resource_managed/main/jekyll folder

  val baseTargetPath = (crossTarget in Compile).value.getAbsolutePath + "/resource_managed/main/jekyll/"


  if(!file(baseTargetPath).exists())
    sys.error("base folder doesn't exist.")

  if(!file(baseTargetPath + "index.md").exists())
    sys.error("index.md doesn't exist.")

  if(!file(baseTargetPath + "docs.md").exists())
    sys.error("docs.md doesn't exist.")

}