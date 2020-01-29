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

import microsites.MicrositeSettings

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{main, nav, section}

class PageLayout(config: MicrositeSettings) extends Layout(config) {

  override def render: TypedTag[String] = {
    val pageBodyBlock =
      if (config.visualSettings.theme == "pattern")
        List(pageHeader, pageMain, globalFooter)
      else
        (cls := "page") :: lightPageNav ++ List(pageMain, lightFooter)

    html(
      commonHead,
      body(
        pageBodyBlock,
        scripts,
        versionScript
      )
    )
  }

  def pageHeader: TypedTag[String] =
    header(
      id := "site-header",
      div(
        cls := "navbar-wrapper navbar-inverse",
        div(
          cls := "container",
          div(
            cls := "navbar-header",
            button(
              tpe := "button",
              cls := "navbar-toggle collapsed",
              data.toggle := "collapse",
              data.target := "#bs-example-navbar-collapse-1",
              aria.expanded := "false",
              span(cls := "sr-only", "Toggle navigation"),
              span(cls := "icon-bar"),
              span(cls := "icon-bar"),
              span(cls := "icon-bar")
            ),
            a(
              href := "{{ site.baseurl }}/",
              cls := "brand",
              div(cls := "icon-wrapper", span(config.identity.name))
            )
          ),
          buildCollapseMenu
        )
      ),
      div(
        cls := "jumbotron",
        style := "background-image:url('{{site.baseurl}}/img/jumbotron_pattern.png')"
      ),
      "{% include menu.html %}"
    )

  def lightPageNav: List[Frag] =
    List(
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
              div(cls := "page-icon-wrapper"),
              span(cls := "brand-title", config.identity.name)
            )
          ),
          div(cls := "navigation-menu", buildLightCollapseMenu)
        )
      ),
      "{% if page.position != null %}",
      nav(
        cls := "menu-container",
        aria.labelledby := "section-navigation",
        "{% include menu.html %}"
      ),
      "{% endif %}"
    )

  def pageMain: TypedTag[String] =
    main(
      id := "site-main",
      cls := "page-site-main",
      section(cls := "use", div(cls := "container", div(id := "content", "{{ content }}")))
    )

}
