package kazari.domhelper

import org.scalajs.dom
import org.scalajs.dom._
import org.querki.jquery._

object DOMHelper extends DOMTags {
  val codeExcludeClass          = "kazari-exclude"
  val codeSnippetsSelectorAll   = s".language-scala:not(.$codeExcludeClass)"
  val snippetsWithId            = "kazari-id-"
  val singleSnippets            = "kazari-single"
  val codeSnippetsSelectorById  = codeBlocksInDivsWithClass(snippetsWithId)
  val codeSingleSnippetSelector = codeBlocksInDivsWithClass(singleSnippets)
  val dependenciesMetaName      = "kazari-dependencies"
  val resolversMetaName         = "kazari-resolvers"

  def codeBlocksInDivsWithClass(className: String) = s"div[class*='$className'] code"

  def getMetaContent(metaTagName: String): String =
    Option($(s"meta[name=$metaTagName]").attr("content").get).getOrElse("")

  def classesFromElement(node: dom.Element): Seq[String] =
    node.attributes.getNamedItem("class").textContent.split(" ").toSeq

  def addClickListenerToButton(selector: String, function: (dom.MouseEvent) => Any) =
    Option(document.querySelector(selector)) foreach { b =>
      b.addEventListener("click", function)
    }

  def changeButtonIcon(selector: String, currentClass: String, nextClass: String) =
    Option(document.querySelector(selector)) foreach {
      $(_).removeClass(currentClass).addClass(nextClass)
    }

  def toggleButtonActiveState(selector: String, active: Boolean) =
    Option(document.querySelector(selector)).foreach { b =>
      val _ = if (active) {
        $(b).addClass(decoratorButtonDisableClass)
      } else {
        $(b).removeClass(decoratorButtonDisableClass)
      }
    }

  def showAlertMessage(parentSelector: String, message: String, isSuccess: Boolean) =
    Option(document.querySelector(s"$parentSelector .$decoratorAlertBarClass")) foreach { a =>
      val classToApply =
        if (isSuccess) { decoratorAlertBarSuccessClass } else { decoratorAlertBarErrorClass }
      $(a).removeClass(decoratorAlertBarHiddenClass).addClass(classToApply).text(message)
    }

  def hideAlertMessage(parentSelector: String) =
    Option(document.querySelector(s"$parentSelector .$decoratorAlertBarClass")) foreach { a =>
      $(a)
        .removeClass(decoratorAlertBarSuccessClass)
        .removeClass(decoratorAlertBarErrorClass)
        .addClass(decoratorAlertBarHiddenClass)
        .text("")
    }

  def decorationSnippetId(index: Int): String = s"snippet-$index"

  def snippetIndexFromDecorationId(decorationId: String): Option[Int] = {
    def parseInt(src: String): Option[Int] =
      try {
        Some(src.toInt)
      } catch {
        case e: Throwable => None
      }
    parseInt(decorationId.split("-").toSeq.last)
  }

  def getHeightFromElement(selector: String): Option[Double] =
    Option($($(selector)).height())

  def closestParentDiv(node: dom.Node): JQuery = $(node).parents("div").first()
}
