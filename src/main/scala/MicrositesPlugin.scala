
import sbt.{Keys, _}
import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging.autoImport.scriptClasspath
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport.Universal
import com.typesafe.sbt.packager.docker._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.NativePackagerKeys
import sbt.plugins.IvyPlugin

object MicrositesPlugin extends AutoPlugin with NativePackagerKeys {

  object autoImport {
    val buildinfo = taskKey[Seq[File]]("Add build info")
    val testing = settingKey[String]("Testing SBT setting")
  }

  import MicrositesPlugin.autoImport._

  override def requires = IvyPlugin && SbtNativePackager

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    testing := "local"
  )

  object Resolvers {
    val sonatypeSnapshots = "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
    val allResolvers = Seq(sonatypeSnapshots)
  }


}