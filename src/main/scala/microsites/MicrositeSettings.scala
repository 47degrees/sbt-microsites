package microsites

import sbt._

case class MicrositeSettings(
  name: String,
  description: String,
  author: String,
  homepage: String,
  twitter: String,
  highlightTheme: String,
  micrositeImgDirectory: File,
  micrositeCssDirectory: File,
  micrositeExtratMdFiles: Seq[File],
  micrositeBaseUrl: String,
  micrositeDocumentationUrl: String,
  palette: Map[String, String],
  githubOwner: String,
  githubRepo: String)
