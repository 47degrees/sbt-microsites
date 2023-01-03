/*
 * Copyright 2016-2022 47 Degrees Open Source <https://www.47deg.com>
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

class HomeLayout(config: MicrositeSettings) extends Layout(config) {

  override def render: TypedTag[String] = {
    val bodyBlock =
      if (config.visualSettings.theme == "pattern")
        List(homeHeader, homeMain, globalFooter)
      else
        List(cls := "home", lightHomeNav) ++ lightHomeHeader ++ List(lightHomeMain, lightFooter)

    html(
      commonHead,
      body(
        bodyBlock,
        scripts,
        versionScript,
        searchScript
      )
    )
  }

  def homeHeader: TypedTag[String] =
    header(
      id := "site-header",
      div(
        cls := "navbar-wrapper",
        div(
          cls := "container",
          div(
            cls := "row",
            div(
              cls := "col-xs-3",
              a(
                href := "{{ site.baseurl }}/",
                cls  := "brand",
                div(cls := "icon-wrapper", span(config.identity.name))
              )
            ),
            div(cls := "col-xs-9", buildCollapseMenu)
          )
        )
      ),
      div(
        cls := "jumbotron",
        div(
          cls := "container",
          h1(cls := "text-center", config.identity.description),
          h2(),
          p(
            cls := "text-center",
            ctaButton("btn btn-outline-inverse")
          )
        )
      ),
      "{% include menu.html %}"
    )

  def lightHomeHeader: List[Frag] =
    List(
      header(
        id := "masthead",
        div(
          cls := "container text-center",
          h1(cls := "masthead-description", config.identity.description),
          ctaButton("masthead-button")
        )
      ),
      "{% if page.position != null %}",
      nav(
        cls             := "menu-container",
        aria.labelledby := "section-navigation",
        "{% include menu.html %}"
      ),
      "{% endif %}"
    )

  def lightHomeMain: TypedTag[String] =
    main(
      id := "site-main",
      section(cls := "main-content", div(cls := "container", div(id := "content", "{{ content }}")))
    )

  def homeMain: TypedTag[String] =
    main(
      id := "site-main",
      section(cls := "use", div(cls := "container", div(id := "content", "{{ content }}"))),
      section(
        cls := "technologies",
        div(
          cls := "container",
          div(
            cls := "row",
            """{% for tech_hash in page.technologies %}
            {% for tech in tech_hash %}""",
            div(
              cls := "col-md-4",
              div(cls := "{{ tech[0] }}-icon-wrapper"),
              h3("{{ tech[1][0] }}"),
              p("{{ tech[1][1] }}")
            ),
            """{% endfor %}
          {% endfor %}"""
          )
        )
      )
    )
}
