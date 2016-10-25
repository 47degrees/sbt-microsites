/*
 * Copyright 2016 47 Degrees, LLC. <http://www.47deg.com>
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

import microsites.domain.MicrositeSettings
import microsites.util.FileHelper._

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{title, nav}

abstract class Layout(config: MicrositeSettings) {
  implicitly(config)

  def render: TypedTag[String]

  def commonHead: TypedTag[String] = {
    head(
      metas,
      styles
    )
  }

  def metas: List[TypedTag[String]] =
    List(meta(charset := "utf-8"),
         meta(httpEquiv := "X-UA-Compatible", content := "IE=edge,chrome=1"),
         title(config.name),
         meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
         meta(name := "description", content := config.description),
         meta(name := "author", content := config.author),
         meta(name := "og:image", content := "{{site.url}}{{site.baseurl}}/img/poster.png"),
         meta(name := "og:title", content := config.name),
         meta(name := "og:site_name", content := config.name),
         meta(name := "og:url", content := config.homepage),
         meta(name := "og:type", content := "website"),
         meta(name := "og:description", content := config.description),
         meta(name := "twitter:image", content := "{{site.url}}{{site.baseurl}}/img/poster.png"),
         meta(name := "twitter:card", content := "summary_large_image"),
         meta(name := "twitter:site", content := config.twitter),
         link(rel := "icon",
              `type` := "image/png",
              href := "{{site.url}}{{site.baseurl}}/img/favicon.png"))

  def styles: List[TypedTag[String]] = {

    val customCssList = fetchFilesRecursively(config.micrositeCssDirectory, List("css")) map {
      css =>
        link(rel := "stylesheet", href := s"{{site.baseurl}}/css/${css.getName}")
    }

    List(
      link(rel := "stylesheet",
           href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"),
      link(rel := "stylesheet",
           href := "https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"),
      link(
        rel := "stylesheet",
        href := s"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.7.0/styles/${config.highlightTheme}.min.css"),
      link(rel := "stylesheet", href := s"{{site.baseurl}}/css/style.css"),
      link(rel := "stylesheet", href := s"{{site.baseurl}}/css/palette.css")
    ) ++ customCssList
  }

  def scripts: List[TypedTag[String]] = List(
    script(src := "https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js"),
    script(
      src := "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"),
    script(src := "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.7.0/highlight.min.js"),
    script(
      src := "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.7.0/languages/scala.min.js")
  )

  def globalFooter =
    footer(
      id := "site-footer",
      div(cls := "container",
          div(cls := "row",
              div(cls := "col-xs-6",
                  p("{{ site.name }} is designed and developed by ",
                    a(href := s"${config.homepage}", target := "_blank", s"${config.author}"))),
              div(cls := "col-xs-6",
                  p(cls := "text-right",
                    a(href := s"https://github.com/${config.githubOwner}/${config.githubRepo}",
                      span(cls := "fa fa-github"),
                      "View on Github"))))))

  def buildCollapseMenu: TypedTag[String] =
    nav(cls := "text-right",
        ul(cls := "",
           li(
             a(href := s"https://github.com/${config.githubOwner}/${config.githubRepo}",
               i(cls := "fa fa-github"),
               span(cls := "hidden-xs", "GitHub"))
           ),
           if (!config.micrositeDocumentationUrl.isEmpty)
             li(
               a(href := s"${config.micrositeDocumentationUrl}",
                 i(cls := "fa fa-file-text"),
                 span(cls := "hidden-xs", "Documentation"))
             )
           else ()))
}
