package microsites

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{title, nav, main, section}


object Layouts {

  def home(config: MicrositeConfig): TypedTag[String] = {
    html(
      head(
        metas(config),
        styles(config)
      ),
      body(
        homeHeader(config),
        homeMain(config),
        scriptsMain(config)
      )
    )
  }

  def metas(config: MicrositeConfig): Seq[TypedTag[String]] = Seq(
    meta(charset := "utf-8"),
    meta(httpEquiv := "X-UA-Compatible", content := "IE=edge,chrome=1"),
    title(config.name),
    meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
    meta(name := "description", content := config.description),
    meta(name := "author", content := config.author),
    meta(name := "og:image", content := "img/poster.png"),
    meta(name := "og:title", content := config.name),
    meta(name := "og:site_name", content := config.name),
    meta(name := "og:url", content := config.homepage),
    meta(name := "og:type", content := "website"),
    meta(name := "og:description", content := config.description),
    meta(name := "twitter:image", content := "img/poster.png"),
    meta(name := "twitter:card", content := "summary_large_image"),
    meta(name := "twitter:site", content := config.twitter),
    link(rel := "icon", `type` := "image/png", href := "img/favicon.png"))

  def styles(config: MicrositeConfig): Seq[TypedTag[String]] = Seq(
    link(rel := "stylesheet", href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"),
    link(rel := "stylesheet", href := "https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"),
    link(rel := "stylesheet", href := s"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/styles/${config.highlightTheme}.min.css"),
    link(rel := "stylesheet", href := s"styles/style.scss")
  )

  def scripts(config: MicrositeConfig): Seq[TypedTag[String]] = Seq(
    script(src:="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js"),
    script(src:="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"),
    script(src:="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"),
    script(src:="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/languages/scala.min.js")
  )

  def homeHeader(config: MicrositeConfig) = header(id := "site-header",
    div(cls := "navbar-wrapper",
      div(cls := "container",
        div(cls := "row",
          div(cls := "col-xs-6",
            a(href := "{{ site.baseurl }}/", cls := "brand",
              div(cls := "icon-wrapper", style := "background: url('img/navbar_brand.png') no-repeat"),
              span("{{ site.name }}")
            )
          )
        ),
        div(cls := "col-xs-6",
          nav(cls := "text-right",
            ul(
              li(
                a(href := "https://github.com/{{ site.github_owner }}{{ site.baseurl }}",
                  i(cls := "fa fa-github"),
                  span(cls := "hidden-xs", "GitHub")
                )
              )
            )
          )
        )
      )
    ),
    div(cls := "jumbotron", style := "background-image:url('img/jumbotron_pattern.png')",
      div(cls := "container",
        h1(cls := "text-center", "{{ site.description }}"),
        h2,
        p(cls := "text-center",
          a(href := "https://github.com/{{ site.github_owner }}{{ site.baseurl }}", cls := "btn btn-outline-inverse", "View on GitHub")
        )
      )
    )
  )


  def homeMain(config: MicrositeConfig) = main(id := "site-main",
    section(cls := "use",
      div(cls := "container",
        div(id := "content", cls := "row", "{{ content }}")
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

  def scriptsMain(config:MicrositeConfig): Seq[TypedTag[String]] = scripts(config) ++
    Seq(script("jQuery(document).ready(function(){hljs.initHighlightingOnLoad();});"))



}
