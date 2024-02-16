// So the plugin is used in its own build
unmanagedSourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "scala"
unmanagedResourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "resources"

libraryDependencies ++= Seq(
  "com.47deg"             %% "github4s"            % "0.33.1",
  "org.http4s"            %% "http4s-blaze-client" % "0.23.16",
  "net.jcazevedo"         %% "moultingyaml"        % "0.4.2",
  "com.lihaoyi"           %% "scalatags"           % "0.12.0",
  "com.sksamuel.scrimage" %% "scrimage-scala"      % "4.1.0",
  "com.github.marklister" %% "base64"              % "0.3.0"
)
