/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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

class MenuPartialLayout(config: MicrositeSettings) extends Layout(config) {

  override def render: TypedTag[String] =
    div(
      id := "horizontal-menu",
      "{% assign pages = site.pages | sort:'position' %}",
      ul(
        cls := "horizontal-nav",
        "{% for p in pages %}",
        "{% if p.position != null %}",
        li(
          a(
            cls := "{% if p.url == page.url %} active {% endif %}",
            href := "{{ site.baseurl }}{{ p.url }}",
            "{{ p.title }}"
          )
        ),
        "{% endif %}",
        "{% endfor %}"
      )
    )

}
