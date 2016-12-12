import microsites._

enablePlugins(MicrositesPlugin)
scalaVersion := "2.12.0"

micrositeExtraMdFiles := Map(
  file("README.md") -> ExtraMdFileConfig(
    "index.md",
    "home"
  )
)
