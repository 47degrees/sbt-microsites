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

micrositeImgDirectory := (Compile / resourceDirectory).value / "images"
micrositeCssDirectory := (Compile / resourceDirectory).value / "styles"
micrositePluginsDirectory := (Compile / resourceDirectory).value / "plugins"
mdocIn := baseDirectory.in(ThisBuild).value / "src" / "main" / "mdoc"