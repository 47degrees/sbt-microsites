package microsites


case class MicrositeConfig(
  name: String,
  description: String,
  author: String,
  homepage: String,
  twitter: String,
  highlightTheme: String,
  palette: Map[String, String],
  githubOwner: String,
  githubRepo: String)
