package microsites.layouts

import microsites.FileHelper._
import microsites.MicrositeSettings

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.title

trait Layout {

  def render(config: MicrositeSettings): TypedTag[String]

  def metas(config: MicrositeSettings): Seq[TypedTag[String]] = Seq(
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

  def styles(config: MicrositeSettings): Seq[TypedTag[String]] = {

    val customCssList = fetchFilesRecursively(config.micrositeCssDirectory) map { css =>
      link(rel := "stylesheet", href := s"/css/${css.getName}")
    }

    Seq(
      link(rel := "stylesheet", href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"),
      link(rel := "stylesheet", href := "https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"),
      link(rel := "stylesheet", href := s"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/styles/${config.highlightTheme}.min.css"),
      link(rel := "stylesheet", href := s"/css/style.css"),
      link(rel := "stylesheet", href := s"/css/palette.css")
    ) ++ customCssList
  }

  def scripts(config: MicrositeSettings): Seq[TypedTag[String]] = Seq(
    script(src:="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js"),
    script(src:="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"),
    script(src:="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"),
    script(src:="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/languages/scala.min.js")
  )
}
