lazy val gpgFolder       = sys.env.getOrElse("GPG_FOLDER", ".")
lazy val publishSnapshot = taskKey[Unit]("Publish only if the version is a SNAPSHOT")
scalaVersion in ThisBuild := "2.10.6"
pgpPassphrase := Some(sys.env.getOrElse("GPG_PASSPHRASE", "").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")

credentials += Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env.getOrElse("PUBLISH_USERNAME", ""),
  sys.env.getOrElse("PUBLISH_PASSWORD", "")
)

scmInfo := Some(
  ScmInfo(url("https://github.com/47deg/sbt-microsites"),
          "https://github.com/47deg/sbt-microsites.git"))
licenses := Seq(
  "Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := Function.const(false)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("Snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("Releases" at nexus + "service/local/staging/deploy/maven2")
}
publishSnapshot := Def.taskDyn {
  if (isSnapshot.value) Def.task {
    PgpKeys.publishSigned.value
  } else Def.task(println("Actual version is not a Snapshot. Skipping publish."))
}.value

pomExtra :=
  <developers>
    <developer>
      <name>47 Degrees (twitter: @47deg)</name>
      <email>hello@47deg.com</email>
    </developer>
    <developer>
      <name>47 Degrees</name>
    </developer>
  </developers>
