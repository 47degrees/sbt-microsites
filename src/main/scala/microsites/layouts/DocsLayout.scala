package microsites.layouts

import microsites.MicrositeSettings

import scalatags.Text.TypedTag
import scalatags.Text.tags2.section
import scalatags.Text.all._

trait DocsLayout extends Layout {

  override def render(config: MicrositeSettings): TypedTag[String] = {
    html(
      head(
        metas(config),
        styles(config)
      ),
      body(cls := "docs",
        sideBarAndContent(config),
        scriptsDocs(config)
      )
    )
  }

  def sideBarAndContent(config: MicrositeSettings): TypedTag[String] = {
    val text = s"${config.name} ${config.description}"
    div(id := "wrapper",
      div(id := "sidebar-wrapper",
        ul(id := "sidebar", cls := "sidebar-nav",
          li(cls := "sidebar-brand",
            a(href := "{{ site.baseurl }}/", cls := "brand",
              div(cls := "brand-wrapper", style := "background:url('{{site.baseurl}}/img/sidebar_brand.png') no-repeat", span(config.name))
            )
          ),
          "{% for x in site.pages %} {% if x.section == page.section %}",
          li(a(href := "{{ site.baseurl }}{{x.url}}", cls := "{% if x.title == page.title %} active {% endif %}", "{{x.title}}")),
          "{% endif %} {% endfor %}"
        )
      ),
      div(id := "page-content-wrapper",
        div(cls := "nav",
          div(cls := "container-fluid",
            div(cls := "row",
              div(cls := "col-lg-12",
                div(cls := "action-menu pull-left clearfix",
                  a(href := "#menu-toggle", id := "menu-toggle", i(cls := "fa fa-bars", aria.hidden := "true"))
                ),
                ul(cls := "pull-right",
                  li(cls := "hidden-xs",
                    a(href := s"https://github.com/${config.githubOwner}/${config.githubRepo}",
                      i(cls := "fa fa-eye"),
                      span("WATCH", span(id := "eyes", cls := "label label-default", "--"))
                    )
                  ),
                  li(cls := "hidden-xs",
                    a(href := s"https://github.com/${config.githubOwner}/${config.githubRepo}",
                      i(cls := "fa fa-star-o"),
                      span("STARS", span(id := "stars", cls := "label label-default", "--"))
                    )
                  ),
                  li(a(href := "#", onclick := s"shareSiteTwitter('$text');", i(cls := "fa fa-twitter"))),
                  li(a(href := "#", onclick := s"shareSiteFacebook('$text');", i(cls := "fa fa-facebook"))),
                  li(a(href := "#", onclick := "shareSiteGoogle();", i(cls := "fa fa-google-plus")))
                )
              )
            )
          )
        ),
        div(id := "content", data("github-owner") := config.githubOwner, data("github-repo") := config.githubRepo,
          div(cls := "content-wrapper",
            section("{{ content }}")
          )
        )
      )
    )
  }

  def scriptsDocs(config: MicrositeSettings): Seq[TypedTag[String]] = scripts(config) ++
    Seq(script(src := "{{ site.baseurl }}/js/main.js"))
}

object DocsLayout extends DocsLayout
