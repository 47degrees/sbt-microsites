package microsites.ioops

import java.io._
import scala.io.Source
import java.net.URL
import java.nio.charset.Charset

object IOUtils {

  def file(path: String): File = new File(path)

  def url(address: String): URL = new URL(address)

  def readLines(file: File): Iterator[String] =
    Source.fromFile(file).getLines()

  def readBytes(file: File): Array[Byte] = {
    val is: InputStream    = new FileInputStream(file)
    val array: Array[Byte] = Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
    is.close()
    array
  }

  def write(file: File, content: String, charset: Charset = Charset.forName("UTF-8")): Unit = {
    val writer = new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(file, false), charset)
    )
    writer.write(content)
    writer.close()
  }

  def relativize(base: File, file: File): Option[String] = {

    def ensureEndingSlash: Option[String] = {
      val path = base.getAbsolutePath
      path.lastOption.map {
        case c if c == File.separatorChar => path
        case _                            => path + File.separatorChar
      }
    }

    val baseFileString = if (base.isDirectory) ensureEndingSlash else None
    val pathString     = file.getAbsolutePath
    baseFileString flatMap {
      case baseString if pathString.startsWith(baseString) =>
        Some(pathString.substring(baseString.length))
      case _ => None
    }
  }

}
