/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package microsites.layouts

import java.io.File

import microsites.MicrositeSettings
import microsites.util.MicrositeHelper
import sbtorgpolicies.io.FileReader

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{nav, title}

object Layout {
  val footer: TypedTag[String] =
    p(
      s"Website built with ",
      a(
        href := s"https://47degrees.github.io/sbt-microsites/",
        target := "_blank",
        rel := "noopener noreferrer",
        s"sbt-microsites"
      ),
      s" - © 2019 ",
      a(
        href := s"https://www.47deg.com/",
        target := "_blank",
        rel := "noopener noreferrer",
        s"47 Degrees"
      )
    )
}

abstract class Layout(config: MicrositeSettings) {
  implicitly(config)

  lazy val micrositeHelper = new MicrositeHelper(config)
  lazy val fr              = new FileReader

  def render: TypedTag[String]

  def commonHead: TypedTag[String] = {
    head(
      metas,
      favicons,
      styles
    )
  }

  val ganalytics: Option[TypedTag[String]] =
    if (config.identity.analytics.nonEmpty)
      Some(script(attr("async") := "async")(s"""
      |(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      |(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      |m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      |})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
      |
      |ga('create', '${config.identity.analytics}' , 'auto');
      |ga('send', 'pageview');
      """.stripMargin))
    else None

  val twitter: Option[TypedTag[String]] =
    if (config.identity.twitter.nonEmpty) {
      Some(meta(name := "twitter:site", content := config.identity.twitter))
    } else None

  val twitterCreator: Option[TypedTag[String]] =
    if (config.identity.twitterCreator.nonEmpty) {
      Some(meta(name := "twitter:creator", content := config.identity.twitterCreator))
    } else None

  def metas: List[TypedTag[String]] = {
    val pageTitle = s"${config.identity.name}{% if page.title %}: {{page.title}}{% endif %}"
    List(
      title(pageTitle),
      meta(charset := "utf-8"),
      meta(httpEquiv := "X-UA-Compatible", content := "IE=edge,chrome=1"),
      meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
      meta(name := "author", content := config.identity.author),
      meta(name := "description", content := config.identity.description),
      meta(name := "og:image", content := "{{site.url}}{{site.baseurl}}/img/poster.png"),
      // Linked-In requires this og:image tag format
      meta(
        name := "image",
        attr("property") := "og:image",
        content := "{{site.url}}{{site.baseurl}}/img/poster.png"
      ),
      meta(name := "og:title", content := pageTitle),
      // For Linked-In
      meta(name := "title", attr("property") := "og:title", content := pageTitle),
      meta(name := "og:site_name", content := config.identity.name),
      meta(name := "og:url", content := config.identity.homepage),
      meta(name := "og:type", content := "website"),
      meta(name := "og:description", content := config.identity.description),
      link(
        rel := "icon",
        `type` := "image/png",
        href := "{{site.url}}{{site.baseurl}}/img/favicon.png"
      ),
      meta(name := "twitter:title", content := pageTitle),
      meta(
        name := "twitter:image",
        // Twitter image URL must be the absolute path
        content := s"${config.urlSettings.micrositeUrl}{{site.baseurl}}/img/poster.png"
      ),
      meta(name := "twitter:description", content := config.identity.description),
      meta(name := "twitter:card", content := "summary_large_image")
    ) ++ twitter.toList ++ twitterCreator.toList
  }

  def favicons: List[TypedTag[String]] =
    (if (config.visualSettings.favicons.nonEmpty) {
       config.visualSettings.favicons
     } else {
       micrositeHelper.faviconDescriptions
     }).map { icon =>
      link(
        rel := "icon",
        `type` := "image/png",
        attr("sizes") := s"${icon.sizeDescription}",
        href := s"{{site.url}}{{site.baseurl}}/img/${icon.filename}"
      )
    }.toList

  def styles: List[TypedTag[String]] = {

    val customCssList =
      fr.fetchFilesRecursively(List(config.fileLocations.micrositeCssDirectory), validFile("css")) match {
        case Right(cssList) =>
          cssList.map(css =>
            link(rel := "stylesheet", href := s"{{site.baseurl}}/css/${css.getName}")
          )
        case _ => Nil
      }

    val customScssList =
      fr.fetchFilesRecursively(List(config.fileLocations.micrositeCssDirectory), validFile("scss")) match {
        case Right(scssList) =>
          scssList.map { scss =>
            val fileNameWithOutExt = scss.getName.replaceFirst("[.][^.]+$", "")
            link(rel := "stylesheet", href := s"{{site.baseurl}}/css/${fileNameWithOutExt}.css")
          }
        case _ => Nil
      }

    val customCDNList = config.fileLocations.micrositeCDNDirectives.cssList map { css =>
      link(rel := "stylesheet", href := css)
    }

    val cssStyles =
      if (config.visualSettings.theme == "pattern")
        List(
          link(
            rel := "stylesheet",
            href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          ),
          link(
            rel := "stylesheet",
            href := "https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
          ),
          link(
            rel := "stylesheet",
            href := s"{{site.url}}{{site.baseurl}}/highlight/styles/${config.visualSettings.highlightTheme}.css"
          ),
          link(
            rel := "stylesheet",
            href := s"{{site.baseurl}}/css/${config.visualSettings.theme}-style.css"
          )
        )
      else
        List(
          link(
            rel := "stylesheet",
            href := "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
          ),
          link(
            rel := "stylesheet",
            href := s"{{site.url}}{{site.baseurl}}/highlight/styles/${config.visualSettings.highlightTheme}.css"
          ),
          link(
            rel := "stylesheet",
            href := s"{{site.baseurl}}/css/${config.visualSettings.theme}-style.css"
          )
        )

    cssStyles ++ customCssList ++ customScssList ++ customCDNList ++ ganalytics.toList
  }

  def scripts: List[TypedTag[String]] = {

    val customJsList =
      fr.fetchFilesRecursively(List(config.fileLocations.micrositeJsDirectory), validFile("js")) match {
        case Right(jsList) =>
          jsList.map(js => script(src := s"{{site.url}}{{site.baseurl}}/js/${js.getName}"))
        case _ => Nil
      }

    val customCDNList = config.fileLocations.micrositeCDNDirectives.jsList map { js =>
      script(src := js)
    }

    val gitSidecar: List[TypedTag[String]] =
      if (config.gitSettings.gitSidecarChat) {
        List(
          script(s"""((window.gitter = {}).chat = {}).options = {
                    |room: '${config.gitSettings.gitSidecarChatUrl}'};""".stripMargin),
          script(src := s"https://sidecar.gitter.im/dist/sidecar.v1.js")
        )
      } else Nil

    val builtinLanguages = Set("scala", "java", "bash")

    val languages =
      config.visualSettings.highlightLanguages.map(lang => s"'$lang'").mkString("[", ",", "]")

    val languageScripts =
      config.visualSettings.highlightLanguages.filterNot(builtinLanguages.contains).map { lang =>
        script(
          src := s"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.15.10/languages/${lang}.min.js"
        )
      }

    val auxScripts =
      if (config.visualSettings.theme == "pattern")
        List(
          script(src := "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"),
          script(
            src := "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"
          ),
          script(src := "{{site.url}}{{site.baseurl}}/highlight/highlight.pack.js")
        )
      else
        List(
          script(src := "{{site.url}}{{site.baseurl}}/highlight/highlight.pack.js")
        )

    val highlightingScript = script(s"""
      |// For all code blocks, copy the language from the containing div
      |// to the inner code tag (where hljs expects it to be)
      |const langPrefix = 'language-';
      |document.querySelectorAll(`div[class^='$${langPrefix}']`).forEach(function(div) {
      |  div.classList.forEach(function(cssClass) {
      |    if (cssClass.startsWith(langPrefix)) {
      |      const lang = cssClass.substring(langPrefix.length);
      |      div.querySelectorAll('pre code').forEach(function(code) {
      |        code.classList.add(lang);
      |      });
      |    }
      |  });
      |});
      |
      |hljs.configure({languages:${languages}});
      |hljs.initHighlightingOnLoad();
      """.stripMargin)

    val message = script(
      """console.info('\x57\x65\x62\x73\x69\x74\x65\x20\x62\x75\x69\x6c\x74\x20\x77\x69\x74\x68\x3a\x0a\x20\x20\x20\x20\x20\x20\x20\x20\x20\x5f\x5f\x20\x20\x20\x20\x5f\x5f\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x5f\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x5f\x20\x5f\x5f\x0a\x20\x20\x20\x5f\x5f\x5f\x5f\x5f\x2f\x20\x2f\x5f\x20\x20\x2f\x20\x2f\x5f\x20\x20\x20\x20\x20\x20\x5f\x5f\x5f\x5f\x20\x5f\x5f\x5f\x20\x20\x28\x5f\x29\x5f\x5f\x5f\x5f\x5f\x5f\x5f\x5f\x5f\x5f\x5f\x5f\x5f\x20\x20\x5f\x5f\x5f\x5f\x5f\x28\x5f\x29\x20\x2f\x5f\x5f\x5f\x5f\x20\x20\x5f\x5f\x5f\x5f\x5f\x0a\x20\x20\x2f\x20\x5f\x5f\x5f\x2f\x20\x5f\x5f\x20\x5c\x2f\x20\x5f\x5f\x2f\x5f\x5f\x5f\x5f\x5f\x2f\x20\x5f\x5f\x20\x60\x5f\x5f\x20\x5c\x2f\x20\x2f\x20\x5f\x5f\x5f\x2f\x20\x5f\x5f\x5f\x2f\x20\x5f\x5f\x20\x5c\x2f\x20\x5f\x5f\x5f\x2f\x20\x2f\x20\x5f\x5f\x2f\x20\x5f\x20\x5c\x2f\x20\x5f\x5f\x5f\x2f\x0a\x20\x28\x5f\x5f\x20\x20\x29\x20\x2f\x5f\x2f\x20\x2f\x20\x2f\x5f\x2f\x5f\x5f\x5f\x5f\x5f\x2f\x20\x2f\x20\x2f\x20\x2f\x20\x2f\x20\x2f\x20\x2f\x20\x2f\x5f\x5f\x2f\x20\x2f\x20\x20\x2f\x20\x2f\x5f\x2f\x20\x28\x5f\x5f\x20\x20\x29\x20\x2f\x20\x2f\x5f\x2f\x20\x20\x5f\x5f\x28\x5f\x5f\x20\x20\x29\x0a\x2f\x5f\x5f\x5f\x5f\x2f\x5f\x2e\x5f\x5f\x5f\x2f\x5c\x5f\x5f\x2f\x20\x20\x20\x20\x20\x2f\x5f\x2f\x20\x2f\x5f\x2f\x20\x2f\x5f\x2f\x5f\x2f\x5c\x5f\x5f\x5f\x2f\x5f\x2f\x20\x20\x20\x5c\x5f\x5f\x5f\x5f\x2f\x5f\x5f\x5f\x5f\x2f\x5f\x2f\x5c\x5f\x5f\x2f\x5c\x5f\x5f\x5f\x2f\x5f\x5f\x5f\x5f\x2f\x0a\x0a\x68\x74\x74\x70\x73\x3a\x2f\x2f\x34\x37\x64\x65\x67\x2e\x67\x69\x74\x68\x75\x62\x2e\x69\x6f\x2f\x73\x62\x74\x2d\x6d\x69\x63\x72\x6f\x73\x69\x74\x65\x73')"""
    )

    auxScripts ++ languageScripts ++ List(highlightingScript) ++ customJsList ++ customCDNList ++ (message :: gitSidecar)
  }

  def versionScript: TypedTag[String] =
    script(src := "{{site.url}}{{site.baseurl}}/js/version-selector.js")

  def globalFooter: TypedTag[String] = {
    val divs: Seq[TypedTag[String]] =
      div(
        cls := "row",
        div(
          cls := "col-xs-6",
          p(
            "{{ site.name }} is designed and developed by ",
            a(
              href := s"${config.identity.organizationHomepage}",
              target := "_blank",
              rel := "noopener noreferrer",
              s"${config.identity.author}"
            )
          )
        ),
        div(
          cls := "col-xs-6",
          p(
            cls := "text-right",
            a(
              href := config.gitSiteUrl,
              target := "_blank",
              rel := "noopener noreferrer",
              span(cls := s"fa ${config.gitHostingIconClass}"),
              s"View on ${config.gitSettings.gitHostingService.name}"
            )
          )
        )
      ) +: {
        config.templateTexts.footer match {
          case Some(text) =>
            Seq(
              div(
                cls := "row",
                div(
                  cls := "col-xs-6",
                  raw(text)
                )
              )
            )
          case None => Nil
        }
      }

    footer(
      id := "site-footer",
      div(
        cls := "container",
        divs
      )
    )
  }

  def lightHomeNav: TypedTag[String] =
    nav(
      id := "navigation",
      aria.labelledby := "main-navigation",
      div(
        cls := "navbar-wrapper container",
        div(
          cls := "navigation-brand",
          a(
            href := "{{ site.baseurl }}/",
            cls := s"brand ${backgroundLogoCssMask}",
            div(cls := "icon-wrapper"),
            span(cls := "brand-title", config.identity.name)
          )
        ),
        div(cls := "navigation-menu", buildLightCollapseMenu)
      )
    )

  def lightFooter: TypedTag[String] = {
    val divs: Seq[TypedTag[String]] =
      div(
        cls := "row",
        p(
          "{{ site.name }} is designed and developed by ",
          a(
            href := s"${config.identity.organizationHomepage}",
            target := "_blank",
            rel := "noopener noreferrer",
            s"${config.identity.author}"
          )
        )
      ) +: {
        config.templateTexts.footer match {
          case Some(text) =>
            Seq(
              div(
                cls := "row",
                div(
                  raw(text)
                )
              )
            )
          case None => Nil
        }
      }

    footer(
      id := "site-footer",
      div(
        cls := "container",
        divs
      )
    )
  }

  def buildCollapseMenu: TypedTag[String] =
    nav(
      cls := "text-right",
      ul(
        cls := "",
        li(
          a(
            href := config.gitSiteUrl,
            i(cls := s"fa ${config.gitHostingIconClass}"),
            target := "_blank",
            rel := "noopener noreferrer",
            span(cls := "hidden-xs", config.gitSettings.gitHostingService.name)
          )
        ),
        if (!config.urlSettings.micrositeDocumentationUrl.isEmpty)
          li(
            a(
              href := s"${config.urlSettings.micrositeDocumentationUrl}",
              i(cls := "fa fa-file-text"),
              span(cls := "hidden-xs", config.urlSettings.micrositeDocumentationLabelDescription)
            )
          )
        else ()
      )
    )

  def buildLightCollapseMenu: TypedTag[String] =
    ul(
      "{% if site.data.versions %}",
      raw("""{% assign own_version = site.data.versions | where: "own", true | first %}"""),
      li(
        div(
          id := "version-dropdown",
          button(
            onclick := "displayToggle(event)",
            attr("title") := "Version {{ own_version.name }}",
            cls := "button link-like",
            i(cls := s"nav-item-icon fa fa-lg fa-caret-square-o-down", hidden := "true"),
            span(
              cls := "nav-item-text",
              id := "own-version",
              "Version {{ own_version.name }}"
            ),
            i(cls := "nav-item-text fa fa-caret-down")
          ),
          ul(
            cls := "dropdown dropdown-content",
            raw("""{% for item in site.data.versions offset: 1 %}"""),
            li(
              cls := "dropdown-item",
              a(
                attr("title") := "{{ item.name }}",
                cls := "dropdown-item-link",
                href := "{{ item.name | relative_url }}",
                span(
                  "{{ item.name }}"
                )
              )
            ),
            "{% endfor %}"
          )
        )
      ),
      "{% endif %}",
      li(
        a(
          href := config.gitSiteUrl,
          i(cls := s"nav-item-icon fa fa-lg ${config.gitHostingIconClass}", hidden := "true"),
          target := "_blank",
          rel := "noopener noreferrer",
          span(cls := "nav-item-text", config.gitSettings.gitHostingService.name)
        )
      ),
      if (!config.urlSettings.micrositeDocumentationUrl.isEmpty)
        li(
          a(
            href := s"${config.urlSettings.micrositeDocumentationUrl}",
            i(cls := "nav-item-icon fa fa-lg fa-file-text", hidden := "true"),
            span(cls := "nav-item-text", config.urlSettings.micrositeDocumentationLabelDescription)
          )
        )
    )

  val customFeatureImg =
    fr.fetchFilesRecursively(
      List(config.fileLocations.micrositeImgDirectory),
      validFeatureFile("feature-icon.svg")
    ) match {
      case Right(svgFeatureIconList) => svgFeatureIconList
      case _                         => Nil
    }

  val backgroundFeatureCssMask =
    if (customFeatureImg.nonEmpty) "custom-feature-icon" else "background-mask"

  val customLogoImg =
    fr.fetchFilesRecursively(
      List(config.fileLocations.micrositeImgDirectory),
      validFeatureFile("navbar-brand.svg")
    ) match {
      case Right(svgLogoIconList) => svgLogoIconList
      case _                      => Nil
    }

  val backgroundLogoCssMask =
    if (customLogoImg.nonEmpty) "custom-feature-icon" else "background-mask"

  private[this] def validFeatureFile(name: String)(file: File): Boolean =
    file.getName.endsWith(name)

  private[this] def validFile(extension: String)(file: File): Boolean =
    file.getName.endsWith(s".$extension")
}
