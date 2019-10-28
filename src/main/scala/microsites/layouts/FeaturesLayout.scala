/*
 * Copyright 2016-2019 47 Degrees, LLC. <http://www.47deg.com>
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
import scalatags.Text.tags2.{main, section}

class FeaturesLayout(config: MicrositeSettings) extends Layout(config) {

  override def render: TypedTag[String] = {
    html(
      commonHead,
      body(
        homeHeaderFeatures,
        homeMainFeatures,
        globalFooter
      )
    )
  }

  def homeHeaderFeatures: TypedTag[String] =
    header(
      id := "site-header",
      div(
        cls := "navbar-wrapper",
        div(
          cls := "container",
          div(
            cls := "row",
            div(
              cls := "col-xs-6",
              a(
                href := "{{ site.baseurl }}/",
                cls := "brand",
                div(cls := "icon-wrapper", span(config.identity.name)))),
            div(cls := "col-xs-6", buildCollapseMenu)
          )
        )
      ),
      div(
        cls := "jumbotron jumbotron-features",
        div(
          cls := "container",
          div(
            cls := "masthead-top",
            div(
              cls := "masthead-text",
              h1(config.identity.description),
              p(
                a(
                  href := config.gitSiteUrl,
                  cls := "btn btn-outline-inverse",
                  s"View on ${config.gitSettings.gitHostingService.name}"))
            ),
            div(
              cls := "masthead-brand",
              img(cls := "main-logo-wrapper")
            ),
          )
        )
      ),
      "{% include menu.html %}"
    )

  def homeMainFeatures: TypedTag[String] =
    main(
      id := "site-main",
      section(
        div(
          cls := "container",
          div(
            cls := "features",
            """{% for feature_hash in page.features %}
            {% for feature in feature_hash %}""",
            div(
              cls := "feature-item",
              img(cls := "{{ feature[0] }}-feature-icon-wrapper"),
              h4("{{ feature[1][0] }}"),
              p("{{ feature[1][1] }}")),
            """{% endfor %}
          {% endfor %}"""
          )
        )
      )
    )

}
