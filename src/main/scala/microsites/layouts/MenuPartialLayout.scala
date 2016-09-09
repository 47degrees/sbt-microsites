package microsites.layouts

import microsites.MicrositeSettings

import scalatags.Text.TypedTag
import scalatags.Text.all._

trait MenuPartialLayout extends Layout {

  override def render(config: MicrositeSettings): TypedTag[String] = div("{% assign pages = site.pages | sort:'position'  %}",
    ul(cls := "horizontalNav", "{% for p in pages %} {% if p.position != null %}",
      li(
        a(cls := "{% if p.url == page.url %} active {% endif %}", href := "{{ site.baseurl }}{{ p.url }}", "{{ p.title }}")
      ), "{% endif %} {% endfor %}"
    )
  )

}


object MenuPartialLayout extends MenuPartialLayout