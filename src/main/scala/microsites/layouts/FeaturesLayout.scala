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

class FeaturesLayout(config: MicrositeSettings) extends Layout(config) {

  override def render: TypedTag[String] = {
    html(
      commonHead,
      body(
        cls := "home",
        lightHomeNav,
        homeHeaderFeatures,
        homeMainFeatures,
        lightFooter,
        scripts,
        versionScript
      )
    )
  }

  def homeHeaderFeatures: List[Frag] =
    List(
      header(
        id := "masthead",
        cls := "features-masthead",
        div(
          cls := "container feature-header",
          div(
            cls := "features-header-description",
            h1(cls := "masthead-title", config.identity.name),
            p(cls := "masthead-description", config.identity.description),
            a(
              href := config.gitSiteUrl,
              target := "_blank",
              rel := "noopener noreferrer",
              cls := "masthead-button",
              s"View on ${config.gitSettings.gitHostingService.name}"
            )
          ),
          div(cls := "features-image")
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

  def homeMainFeatures: TypedTag[String] =
    main(
      id := "site-main",
      section(
        cls := "container",
        div(
          cls := "features",
          """{% for feature_hash in page.features %}
              {% for feature in feature_hash %}""",
          div(
            cls := "feature-item",
            div(
              cls := s"feature-item-header ${backgroundFeatureCssMask}",
              div(cls := "{{ feature[0] }}-feature-icon-wrapper"),
              h4("{{ feature[1][0] }}"),
              p("{{ feature[1][1] }}")
            ),
            if (!config.urlSettings.micrositeDocumentationUrl.isEmpty)
              a(
                cls := "learn-more-button",
                href := s"${config.urlSettings.micrositeDocumentationUrl}/{{ feature[1][2] }}",
                span(cls := "learn-more", "Learn More")
              )
          ),
          """{% endfor %}
        {% endfor %}"""
        )
      )
    )

}
