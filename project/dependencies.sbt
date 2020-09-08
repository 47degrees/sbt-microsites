// So the plugin is used in its own build
unmanagedSourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "scala"
unmanagedResourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "resources"

libraryDependencies ++= Seq(
  "com.47deg"             %% "github4s"      % "0.24.1",
  "net.jcazevedo"         %% "moultingyaml"  % "0.4.2",
  "com.lihaoyi"           %% "scalatags"     % "0.9.1",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8"
)
