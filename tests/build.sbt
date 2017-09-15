scalaVersion := "2.10.6"

scriptedSettings

scriptedDependencies := (scriptedDependencies dependsOn (compile in Test)).value

scriptedLaunchOpts ++= Seq(
    "-Xmx2048M",
    "-XX:ReservedCodeCacheSize=256m",
    "-XX:+UseConcMarkSweepGC",
    "-Dplugin.version=" + version.value,
    "-Dscala.version=" + scalaVersion.value
)
