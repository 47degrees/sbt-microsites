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
micrositePluginsDirectory := (resourceDirectory in Compile).value / "plugins"
