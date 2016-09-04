package microsites.layouts

import microsites.MicrositeConfig
import scalatags.Text.TypedTag
import scalatags.Text.all._


object Home {

  def html(config: MicrositeConfig): TypedTag[String] = {
    div(
      h1("Header 1"),
      h2("Header 2"),
      h3("Name"),
      h4(config.name),
      h5("Description"),
      h6(config.description))
  }

}
