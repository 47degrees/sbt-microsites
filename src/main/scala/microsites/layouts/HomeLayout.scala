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

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{main, section}

class HomeLayout(config: MicrositeSettings) extends Layout(config) {

  override def render: TypedTag[String] = {
    html(
      commonHead,
      body(
        homeHeader,
        homeMain,
        globalFooter,
        scripts
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
            div(cls := "col-xs-6",
                a(href := "{{ site.baseurl }}/",
                  cls := "brand",
                  div(cls := "icon-wrapper",
                      style := "background:url('{{site.baseurl}}/img/navbar_brand.png') no-repeat",
                      span(config.name)))),
            div(cls := "col-xs-6", buildCollapseMenu)))
      ),
      div(cls := "jumbotron",
          style := "background-image:url('{{site.baseurl}}/img/jumbotron_pattern.png')",
          div(cls := "container",
              h1(cls := "text-center", config.description),
              h2(),
              p(cls := "text-center",
                a(href := s"https://github.com/${config.githubOwner}/${config.githubRepo}",
                  cls := "btn btn-outline-inverse",
                  "View on GitHub")))),
      "{% include menu.html %}")

  def homeMain: TypedTag[String] =
    main(id := "site-main",
         section(cls := "use", div(cls := "container", div(id := "content", "{{ content }}"))),
         section(cls := "technologies",
                 div(cls := "container",
                     div(cls := "row",
                         """{% for tech_hash in page.technologies %}
            {% for tech in tech_hash %}""",
                         div(cls := "col-md-4",
                             div(cls := "{{ tech[0] }}-icon-wrapper"),
                             h3("{{ tech[1][0] }}"),
                             p("{{ tech[1][1] }}")),
                         """{% endfor %}
          {% endfor %}"""))))

}
