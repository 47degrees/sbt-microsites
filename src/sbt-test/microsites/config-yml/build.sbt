import microsites._

enablePlugins(MicrositesPlugin)
scalaVersion := sys.props("scala.version")

micrositeConfigYaml := ConfigYml(
  yamlCustomProperties = Map("org" -> "Test"),
  yamlInline =
    """exclude: [css, README.markdown, package.json, grunt.js, Gruntfile.js, Gruntfile.coffee, node_modules]
      |""".stripMargin,
  yamlPath = Some((Compile / resourceDirectory).value / "myconfig.yml")
)

def getLines(fileName: String) =
  IO.readLines(file(fileName))

lazy val check = TaskKey[Unit]("check")

check := {
  val content = getLines(s"${(Compile / resourceManaged).value}/jekyll/_config.yml").mkString

  if (!content.contains("org: Test"))
    sys.error("custom properties not found")
  if (!content.contains("exclude:"))
    sys.error("property defined inline not found")
  if (!content.contains("permalink: pretty"))
    sys.error("property defined in the provided file not found")
}
