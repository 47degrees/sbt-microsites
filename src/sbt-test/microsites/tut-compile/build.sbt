import microsites._

enablePlugins(MicrositesPlugin)
scalaVersion := sys.props("scala.version")

micrositeCompilingDocsTool := WithTut

micrositeExtraMdFiles := Map(
  file("README.md") -> ExtraMdFileConfig(
    "index.md",
    "home"
  )
)
