import microsites._

enablePlugins(MicrositesPlugin)
scalaVersion := "2.12.1"

micrositeConfigYaml := ConfigYml(
  yamlCustomProperties = Map("org" -> "Test"),
  yamlInline =
    """exclude: [README.markdown, package.json, grunt.js, Gruntfile.js, Gruntfile.coffee, node_modules]
      |""".stripMargin,
  yamlPath = Some((resourceDirectory in Compile).value / "myconfig.yml")
)

def getLines(fileName: String) = {
  IO.readLines(file(fileName))
}

lazy val check = TaskKey[Unit]("check")

check := {
  val content = getLines("target/site/_config.yml").mkString

  if (!content.contains("org: Test"))
    sys.error("custom properties not found")
  if (!content.contains("exclude:"))
    sys.error("property defined inline not found")
  if (!content.contains("permalink: pretty"))
    sys.error("property defined in the provided file not found")
}
