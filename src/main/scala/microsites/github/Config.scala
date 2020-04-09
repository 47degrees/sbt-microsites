package microsites.github

object Config {

  val blobMode: String = "100644"
  val blobType: String = "blob"

  val defaultTextExtensions: Set[String] =
    Set(".md", ".css", ".html", ".properties", ".txt", ".scala", ".sbt")
  val defaultMaximumSize: Int = 4048

  case class BlobConfig(acceptedExtensions: Set[String], maximumSize: Int)

  val defaultBlobConfig: BlobConfig = BlobConfig(defaultTextExtensions, defaultMaximumSize)

}