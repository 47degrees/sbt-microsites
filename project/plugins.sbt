import sbt.Resolver.sonatypeRepo

resolvers ++= Seq(sonatypeRepo("snapshots"), sonatypeRepo("releases"))
addSbtPlugin("com.47deg" % "sbt-org-policies" % "0.9.4")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")