package microsites.layouts

import microsites.MicrositeSettings

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{main, section}

trait HomeLayout extends Layout {

  override def render(config: MicrositeSettings): TypedTag[String] = {
    html(
      head(
        metas(config),
        styles(config)
      ),
      body(
        homeHeader(config),
        homeMain(config),
        globalFooter(config),
        scriptsMain(config)
      )
    )
  }

  def homeHeader(config: MicrositeSettings): TypedTag[String] = header(id := "site-header",
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
            div(cls := "icon-wrapper", style := "background:url('{{site.baseurl}}/img/navbar_brand.png') no-repeat", span(config.name))
          )
        ),
        buildCollapseMenu(config)
      )
    ),
    div(cls := "jumbotron", style := "background-image:url('{{site.baseurl}}/img/jumbotron_pattern.png')",
      div(cls := "container",
        h1(cls := "text-center", config.description),
        h2(),
        p(cls := "text-center", a(href := s"https://github.com/${config.githubOwner}/${config.githubRepo}", cls := "btn btn-outline-inverse", "View on GitHub"))
      )
    ),
    "{% include menu.html %}"
  )

  def homeMain(config: MicrositeSettings): TypedTag[String] = main(id := "site-main",
    section(cls := "use",
      div(cls := "container",
        div(id := "content", "{{ content }}")
      )
    ),
    section(cls := "technologies",
      div(cls := "container",
        div(cls := "row",
          """{% for tech_hash in page.technologies %}
            {% for tech in tech_hash %}"""
          , div(cls := "col-md-4",
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

  def scriptsMain(config:MicrositeSettings): Seq[TypedTag[String]] = scripts(config) ++
    Seq(script("jQuery(document).ready(function(){hljs.initHighlightingOnLoad();});"))

}

object HomeLayout extends HomeLayout
