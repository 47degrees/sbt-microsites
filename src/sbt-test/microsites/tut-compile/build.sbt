import microsites._

enablePlugins(MicrositesPlugin)
scalaVersion := sys.props("scala.version")

micrositeExtraMdFiles := Map(
  file("README.md") -> ExtraMdFileConfig(
    "index.md",
    "home"
  )
)
