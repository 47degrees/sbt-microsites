package kazari.domhelper

import org.scalajs.dom
import org.scalajs.dom._
import org.querki.jquery._

object DOMHelper extends DOMTags {
  val codeExcludeClass = "code-exclude"
  val codeSnippetsSelector = s".language-scala:not(.$codeExcludeClass)"
  val dependenciesMetaName = "evaluator-dependencies"
  val resolversMetaName = "evaluator-resolvers"

  def getMetaContent(metaTagName: String): String = {
    val metaTag = Option(document.querySelector(s"meta[property=" + """"""" + s"$metaTagName" + """"""" + "]"))
    metaTag map { m =>
      m.getAttribute("content")
    } getOrElse ""
  }

  def applyModalStyles() = {
    $("#modal-1").on("change", { (e: JQueryEventObject, a: Any) =>
      if ($("#modal-1").is(":checked")) {
        $("body").addClass("modal-open")
      } else {
        $("body").removeClass("modal-open")
      }
    })

    $(".modal-fade-screen, .modal-close").on("click", { (e: JQueryEventObject, a: Any) =>
      $(".modal-state:checked").prop("checked", false).change()
    })

    $(".modal-inner").on("click", { (e: JQueryEventObject, a: Any) =>
      e.stopPropagation()
    })
  }

  def addClickListenerToButton(selector: String, function: (dom.MouseEvent) => Any) =
    Option(document.querySelector(selector)) foreach { b =>
      b.addEventListener("click", function)
    }

  def changeButtonIcon(selector: String, currentClass: String, nextClass: String) =
    Option(document.querySelector(selector)) foreach { $(_).removeClass(currentClass).addClass(nextClass) }

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
      val classToApply = if (isSuccess) { decoratorAlertBarSuccessClass } else { decoratorAlertBarErrorClass }
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
    def parseInt(src: String): Option[Int] = try {
      Some(src.toInt)
    } catch {
      case e: Throwable => None
    }
    parseInt(decorationId.split("-").toSeq.last)
  }

  def getHeightFromElement(selector: String): Option[Double] =
    Option($($(selector)).height())
}