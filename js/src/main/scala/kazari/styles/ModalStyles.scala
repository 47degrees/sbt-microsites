package kazari.styles

import scalacss.defaults.Exports.StyleSheet
import scalacss.internal.{FontFace, NonEmptyVector}
import scalacss.Defaults._
import scalacss.ScalatagsCss._

object ModalStyles extends StyleSheet.Inline {
  import dsl._

  val common = style(
    position fixed,
    fontFamily(FontFace("Arial, Helvetica, sans-serif", NonEmptyVector(""))),
    top(0 px),
    bottom(0 px),
    right(0 px),
    left(0 px),
    backgroundColor(rgba(0, 0, 0, 0.8)),
    transition := "opacity 400ms ease-in"
  )

  val default = style(
    common,
    pointerEvents := none,
    zIndex(99999),
    opacity(0)
  )

  val active = style(
    common,
    opacity(1),
    pointerEvents := auto
  )

  val internalDiv = style(
    width(600 px),
    position relative,
    margin(10 %%, auto),
    padding(5 px, 20 px, 13 px, 20 px),
    borderRadius(10 px),
    backgroundColor(c"#fff")
  )

  val closeButton = style(
    backgroundColor(c"#606061"),
    color(c"#FFFFFF"),
    lineHeight(25 px),
    position absolute,
    right(-12 px),
    textAlign center,
    top(-10 px),
    width(24 px),
    textDecoration := ^.none,
    fontWeight bold,
    borderRadius(12 px),
    boxShadow := "1px 1px 3px #000",
    &.hover(
      backgroundColor(c"#00d9ff")
    )
  )
}