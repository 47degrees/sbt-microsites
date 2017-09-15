import com.typesafe.sbt.site.SitePlugin.autoImport._
// import microsites.MicrositeKeys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import sbtorgpolicies.model.{sbtV, scalac}
import sbtorgpolicies.OrgPoliciesPlugin
import sbtorgpolicies.OrgPoliciesPlugin.autoImport._
import sbtorgpolicies.runnable.syntax._
import KazariBuild._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = OrgPoliciesPlugin

  object autoImport {

    lazy val pluginSettings = Seq(
      sbtPlugin := true,
      crossSbtVersions := Seq(sbtV.`0.13`, sbtV.`1.0`),
      resolvers ++= Seq(
        Resolver.sonatypeRepo("snapshots"),
        "jgit-repo" at "http://download.eclipse.org/jgit/maven"),
      addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.2"),
      addSbtPlugin("com.typesafe.sbt" % "sbt-site"    % "1.3.0"),
      libraryDependencies ++= Seq(
        "com.47deg" %% "org-policies-core" % "0.6.2",
        %%("moultingyaml"),
        %%("scalatags"),
        %%("scalactic"),
        %%("scalatest")  % "test",
        %%("scalacheck") % "test"
      ),
      libraryDependencies ++= {
        val sbtVersionValue       = (sbtVersion in pluginCrossBuild).value
        val sbtBinaryVersionValue = (sbtBinaryVersion in pluginCrossBuild).value

        val scalaBinaryVersionValue = (scalaBinaryVersion in update).value

        val (tutPluginVersion, scrimageVersion) = sbtVersionValue match {
          case sbtV.`0.13` => ("0.5.3", "2.1.7")
          case sbtV.`1.0`  => ("0.6.0", "2.1.8")
        }

        Seq(
          Defaults.sbtPluginExtra(
            "org.tpolecat" % "tut-plugin" % tutPluginVersion,
            sbtBinaryVersionValue,
            scalaBinaryVersionValue),
          "com.sksamuel.scrimage" %% "scrimage-core" % scrimageVersion
        )
      }
    )

    lazy val buildInfoSettings = Seq(
      buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
      buildInfoPackage := "microsites"
    )

    // lazy val micrositeSettings = Seq(
    //   micrositeName := "sbt-microsites",
    //   micrositeDescription := "An sbt plugin to create awesome microsites for your project",
    //   micrositeBaseUrl := "sbt-microsites",
    //   micrositeDocumentationUrl := "/sbt-microsites/docs/",
    //   micrositeGithubOwner := "47deg",
    //   micrositeGithubRepo := "sbt-microsites",
    //   micrositeGithubToken := sys.env.get(orgGithubTokenSetting.value),
    //   micrositeHighlightTheme := "color-brewer",
    //   micrositePushSiteWith := GitHub4s,
    //   includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
    // )

    lazy val jsSettings: Seq[Def.Setting[_]] = sharedJsSettings ++ Seq(
      scalaVersion := "2.11.8",
      crossScalaVersions := Seq("2.11.8"),
      skip in packageJSDependencies := false,
      libraryDependencies ++= Seq(
        %%%("github4s"),
        %%%("roshttp"),
        "org.scala-js"        %%% "scalajs-dom"       % "0.9.0",
        "be.doeraene"         %%% "scalajs-jquery"    % "0.9.0",
        "com.lihaoyi"         %%% "upickle"           % "0.4.1",
        "org.scala-exercises" %%% "evaluator-shared"  % "0.4.0-SNAPSHOT",
        "org.scala-exercises" %%% "evaluator-client"  % "0.4.0-SNAPSHOT",
        "com.lihaoyi"         %%% "scalatags"         % "0.6.5",
        "org.querki"          %%% "jquery-facade"     % "1.0-RC6",
        "org.denigma"         %%% "codemirror-facade" % "5.11-0.7"
      ),
      resolvers ++= Seq(
        Resolver.url(
          "bintray-sbt-plugin-releases",
          url("https://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
        Resolver.sonatypeRepo("snapshots"),
        Resolver.bintrayRepo("denigma", "denigma-releases")
      ),
      jsDependencies ++= Seq(
        "org.webjars" % "jquery" % "2.1.3" / "2.1.3/jquery.js",
        ProvidedJS / "codemirror.js",
        ProvidedJS / "clike.js" dependsOn "codemirror.js"
      )
    )

  }

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      name := "sbt-microsites",
      description := "An sbt plugin to create awesome microsites for your project",
      startYear := Some(2016),
      scalaVersion := scalac.`2.12`,
      crossScalaVersions := Seq(scalac.`2.12`),
      scalaOrganization := "org.scala-lang",
      orgScriptTaskListSetting := List(
        orgValidateFiles.asRunnableItem,
        (clean in Global).asRunnableItemFull,
        (compile in Compile).asRunnableItemFull,
        (test in Test).asRunnableItemFull,
        (publishLocal in Global).asRunnableItemFull,
        "tests/scripted".asRunnableItemFull,
        (jsFullOptGenerateTask in ProjectRef(file("."), "kazari")).asRunnableItem,
        "docs/tut".asRunnableItem
      )
    ) ++ shellPromptSettings
}
