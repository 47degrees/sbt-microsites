package kazari.domhelper

import kazari.KazariPlugin
import kazari.styles.ModalStyles
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.HTMLStyleElement
import scalacss.ScalatagsCss._
import scalacss.Defaults._

trait DOMHelper {
  def appendButton[T](targetNode: dom.Node, title: String, onClickFunction: Function[_, T], id: Option[String] = None): dom.Node = {
    val btnNode = document.createElement("button")
    btnNode.appendChild(document.createTextNode(title))
    btnNode.setAttribute("type", "button")
    btnNode.setAttribute("id", id.getOrElse(""))
    btnNode.addEventListener("click", onClickFunction)
    targetNode.appendChild(btnNode)
  }

  def createModalDiv(cssClass: String): Div = {
    import scalatags.JsDom.all._
    div(
      `id` := cssClass,
      `class` := cssClass,
      ModalStyles.render[scalatags.JsDom.TypedTag[HTMLStyleElement]],
      ModalStyles.default,
      div(
        ModalStyles.internalDiv,
        a(
          title := "Close",
          `class` := KazariPlugin.codeModalCloseButtonClass,
          ModalStyles.closeButton,
          "X"
        ),
        textarea(
          `id` := KazariPlugin.codeModalInternalTextArea
        ),
        div(
          `id` := KazariPlugin.codeModalButtonContainer
        )
      )
    ).render
  }

  def getMetaContent(metaTagName: String): String = {
    val metaTag = document.querySelector(s"meta[property=" + """"""" + s"$metaTagName" + """"""" + "]")
    if (metaTag != null) {
      metaTag.getAttribute("content")
    } else {
      ""
    }
  }
}