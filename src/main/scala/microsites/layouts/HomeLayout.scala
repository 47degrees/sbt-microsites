package microsites.layouts

import microsites.MicrositeSettings

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{main, nav, section}

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
          a(href := "#", data.href := "{{ site.baseurl }}/", cls := "brand",
            div(cls := "icon-wrapper", style := "background:url('img/navbar_brand.png') no-repeat", span(config.name))
          )
        ),
        div(cls := "collapse navbar-collapse", id := "bs-example-navbar-collapse-1",
          ul(cls := "nav navbar-nav navbar-right",
            li(
              a(href := s"https://github.com/${config.githubOwner}/${config.githubRepo}", span("GitHub"))
            ),
            li(
              a(href := "{{ site.baseurl }}/docs", span("Documentation"))
            )
          )
        )
      )
    ),
    div(cls := "jumbotron", style := "background-image:url('img/jumbotron_pattern.png')",
      div(cls := "container",
        h1(cls := "text-center", config.description),
        h2(),
        p(cls := "text-center", a(href := s"https://github.com/${config.githubOwner}/${config.githubRepo}", cls := "btn btn-outline-inverse", "View on GitHub"))
      )
    )
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

  def globalFooter(config: MicrositeSettings) = footer( id:="site-footer",
    div( cls:="container",
      div( cls:="row",
        div( cls:="col-xs-6",
          p("{{ site.name }} is designed and developed by" ,a( href:="{{ site.organization.url }}", target:="_blank","{{ site.organization.name }}"))
        ),
        div( cls:="col-xs-6",
          p( cls:="text-right",
            a( href:="#", data.href:="https://github.com/{{ site.github_owner }}{{ site.baseurl }}", span( cls:="fa fa-github"), "View on Github" )
          )
        )
      )
    )
  )

  def scriptsMain(config:MicrositeSettings): Seq[TypedTag[String]] = scripts(config) ++
    Seq(script("jQuery(document).ready(function(){hljs.initHighlightingOnLoad();});"))

}

object HomeLayout extends HomeLayout
