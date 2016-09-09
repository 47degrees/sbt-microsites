package microsites.layouts

import microsites.MicrositeSettings

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{main, section}

trait PageLayout extends Layout {

  override def render(config: MicrositeSettings): TypedTag[String] = {
    html(
      head(
        metas(config),
        styles(config)
      ),
      body(
        pageHeader(config),
        pageMain(config),
        globalFooter(config),
        scriptsMain(config)
      )
    )
  }

  def pageHeader(config: MicrositeSettings): TypedTag[String] = header(id := "site-header",
    div(cls := "navbar-wrapper navbar-inverse",
      div(cls := "container",
        div(cls := "navbar-header",
          button(tpe := "button", cls := "navbar-toggle collapsed", data.toggle := "collapse", data.target := "#bs-example-navbar-collapse-1", aria.expanded := "false",
            span(cls := "sr-only", "Toggle navigation"),
            span(cls := "icon-bar"),
            span(cls := "icon-bar"),
            span(cls := "icon-bar")
          ),
          a(href := "{{ site.baseurl }}/", cls := "brand",
            div(cls := "icon-wrapper", style := "background:url('img/navbar_brand.png') no-repeat", span(config.name))
          )
        ),
        buildCollapseMenu(config)
      )
    ),
    div(cls := "jumbotron", style := "background-image:url('img/jumbotron_pattern.png')"),
    "{% include menu.html %}"
  )

  def pageMain(config: MicrositeSettings): TypedTag[String] = main(id := "site-main",
    section(cls := "use",
      div(cls := "container",
        div(id := "content", "{{ content }}")
      )
    )
  )

  def scriptsMain(config:MicrositeSettings): Seq[TypedTag[String]] = scripts(config) ++
    Seq(script("jQuery(document).ready(function(){hljs.initHighlightingOnLoad();});"))

}

object PageLayout extends PageLayout
