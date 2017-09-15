enablePlugins(MicrositesPlugin)

scalaVersion := sys.props("scala.version")

lazy val check = TaskKey[Unit]("check")

check := {
  // check resource_managed/main/jekyll folder

  val baseTargetPath = (crossTarget in Compile).value.getAbsolutePath + "/resource_managed/main/jekyll/"


  if(!file(baseTargetPath).exists())
    sys.error("base folder doesn't exist.")

  if(!file(baseTargetPath + "_config.yml").exists())
    sys.error("_config.yml doesn't exist.")

  if(!file(baseTargetPath + "_includes").exists())
    sys.error("_includes doesn't exist.")

  if(!file(baseTargetPath + "_layouts/docs.html").exists())
    sys.error("_layouts/docs.html doesn't exist.")

  if(!file(baseTargetPath + "_layouts/home.html").exists())
    sys.error("_layouts/home.html doesn't exist.")

  if(!file(baseTargetPath + "_layouts/page.html").exists())
    sys.error("_layouts/page.html doesn't exist.")

  if(!file(baseTargetPath + "_sass").exists())
    sys.error("_sass doesn't exist.")

  if(!file(baseTargetPath + "css").exists())
    sys.error("css doesn't exist.")

  if(!file(baseTargetPath + "js").exists())
    sys.error("js doesn't exist.")

}