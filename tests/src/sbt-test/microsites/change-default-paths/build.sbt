import microsites._

enablePlugins(MicrositesPlugin)

scalaVersion := sys.props("scala.version")

micrositeExtraMdFiles := Map(
  file("README.md") -> ExtraMdFileConfig(
    "index.md",
    "home"
  )
)

micrositeExtraMdFilesOutput := (target in Compile).value / "extra_md_override"

micrositeImgDirectory := (resourceDirectory in Compile).value / "images"
micrositeCssDirectory := (resourceDirectory in Compile).value / "styles"

lazy val check = TaskKey[Unit]("check")

check := {
  val baseTargetPath = (crossTarget in Compile).value.getAbsolutePath + "/resource_managed/main/jekyll/"

  // check images and styles at resource_managed folder

  if(!file(baseTargetPath + "img/scala.png").exists())
    sys.error("scala.png doesn't exist.")

  if(!file(baseTargetPath + "img/sbt.png").exists())
    sys.error("sbt.png doesn't exist.")

  if(!file(baseTargetPath + "css/override-1.css").exists())
    sys.error("override-1.css doesn't exist.")

  if(!file(baseTargetPath + "css/override-2.css").exists())
    sys.error("override-2.css doesn't exist.")

}