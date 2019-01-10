import microsites._

enablePlugins(MicrositesPlugin)
scalaVersion := sys.props("scala.version")
micrositeCompilingDocsTool := "mdoc"
