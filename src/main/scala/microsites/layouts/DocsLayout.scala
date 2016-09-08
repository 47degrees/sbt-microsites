package microsites.layouts

import microsites.MicrositeSettings

import scalatags.Text.TypedTag
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
        scriptsMain(config)
      )
    )
  }

  def sideBarAndContent(config: MicrositeSettings): TypedTag[String] = div(id := "wrapper",
    div(id := "sidebar-wrapper",
      ul(id := "sidebar", cls := "sidebar-nav",
        li(cls := "sidebar-brand",
          a(href := "#", data.href := "{{ site.baseurl }}/", cls := "brand",
            div(cls := "icon-wrapper", style := "background:url('img/navbar_brand.png') no-repeat", span(config.name))
          )
        )
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
                li(a(href := "#", onclick := "shareSiteTwitter();", i(cls := "fa fa-twitter"))),
                li(a(href := "#", onclick := "shareSiteFacebook();", i(cls := "fa fa-facebook"))),
                li(a(href := "#", onclick := "shareSiteGoogle();", i(cls := "fa fa-google-plus")))
              )
            )
          )
        )
      ),
      div(id := "content", "{{ content }}")
    )
  )


  def scriptsMain(config: MicrositeSettings): Seq[TypedTag[String]] = scripts(config) ++
    Seq(
      script(src := "js/main.js"),
      script(
        """jQuery(document).ready(function() {
          |activeLinks();
          |loadStyle();
          |hljs.initHighlightingOnLoad();
          |activeToggle();
          |organizeContent();loadGitHubStats();
          |});
        """.stripMargin)
    )
}

object DocsLayout extends DocsLayout
