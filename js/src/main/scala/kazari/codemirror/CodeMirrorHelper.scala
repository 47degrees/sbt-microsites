package kazari.codemirror

import org.denigma.codemirror.Position

import scala.scalajs.js.annotation.ScalaJSDefined

object PositionBuilder {
  def apply(_ch: Int, _line: Int): Position = {
    scalajs.js.Dynamic.literal(
      ch = _ch,
      line = _line
    ).asInstanceOf[Position]
  }
}

@ScalaJSDefined
trait CodeMirrorCharCords extends scalajs.js.Object {
  val left: Double
  val right: Double
  val top: Double
  val bottom: Double
}