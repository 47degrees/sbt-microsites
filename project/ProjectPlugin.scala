import com.typesafe.sbt.site.SitePlugin.autoImport._
import microsites.MicrositeKeys._
import sbt._
import sbt.Keys._
import sbt.ScriptedPlugin.autoImport._
import sbtorgpolicies.OrgPoliciesPlugin
import sbtorgpolicies.OrgPoliciesPlugin.autoImport._
import sbtorgpolicies.runnable.syntax._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = OrgPoliciesPlugin

  object autoImport {

    lazy val V = new {
      val mdoc: String         = "2.0.0"
      val moultingyaml: String = "0.4.1"
      val orgPolicies: String  = "0.12.0-M2"
      val scala: String        = "2.12.9"
      val scalactic: String    = "3.0.8"
      val scalatest: String    = "3.0.8"
      val scalacheck: String   = "1.14.2"
      val scalatags: String    = "0.7.0"
      val scrimage: String     = "2.1.8"
      val tut: String          = "0.6.13"
    }

    lazy val pluginSettings: Seq[Def.Setting[_]] = Seq(
      sbtPlugin := true,
      resolvers ++= Seq(
        Resolver.sonatypeRepo("snapshots"),
        "jgit-repo" at "https://download.eclipse.org/jgit/maven"),
      addSbtPlugin("org.tpolecat"  % "tut-plugin" % V.tut),
      addSbtPlugin("org.scalameta" % "sbt-mdoc"   % V.mdoc),
      addSbtPlugin(%("sbt-ghpages", isSbtPlugin = true)),
      addSbtPlugin(%("sbt-site", isSbtPlugin = true)),
      libraryDependencies ++= Seq(
        %%("org-policies-core", V.orgPolicies),
        %%("moultingyaml", V.moultingyaml),
        %%("scalatags", V.scalatags),
        %%("scalactic", V.scalactic),
        "com.sksamuel.scrimage"        %% "scrimage-core" % V.scrimage,
        %%("scalatest", V.scalatest)   % "test",
        %%("scalacheck", V.scalacheck) % "test"
      ),
      scriptedLaunchOpts := {
        scriptedLaunchOpts.value ++
          Seq(
            "-Xmx2048M",
            "-XX:ReservedCodeCacheSize=256m",
            "-XX:+UseConcMarkSweepGC",
            "-Dplugin.version=" + version.value,
            "-Dscala.version=" + scalaVersion.value
          )
      }
    )

    lazy val micrositeSettings: Seq[Def.Setting[_]] = Seq(
      micrositeName := "sbt-microsites",
      micrositeDescription := "An sbt plugin to create awesome microsites for your project",
      micrositeBaseUrl := "sbt-microsites",
      micrositeDocumentationUrl := "/sbt-microsites/docs/",
      micrositeGithubOwner := "47deg",
      micrositeGithubRepo := "sbt-microsites",
      micrositeGithubToken := sys.env.get(orgGithubTokenSetting.value),
      micrositePushSiteWith := GitHub4s,
      includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.svg"
    )

  }

  import autoImport.V

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      name := "sbt-microsites",
      description := "An sbt plugin to create awesome microsites for your project",
      startYear := Some(2016),
      scalaVersion := V.scala,
      crossScalaVersions := Seq(V.scala),
      scalaOrganization := "org.scala-lang",
      orgScriptTaskListSetting := List(
        orgValidateFiles.asRunnableItem,
        (clean in Global).asRunnableItemFull,
        (compile in Compile).asRunnableItemFull,
        (test in Test).asRunnableItemFull,
        (publishLocal in Global).asRunnableItemFull,
        "scripted".asRunnableItemFull,
        "docs/tut".asRunnableItem
      )
    ) ++ shellPromptSettings
}
