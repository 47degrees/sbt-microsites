package kazari.domhelper

import org.scalajs.dom.html._
import scalatags.JsDom.all._

trait DOMTags {
  val codeModalClass = "modal-fade-screen"
  val codeModalCloseButtonClass = "modal-close"
  val codeModalInternalTextArea = "modal-text-area"
  val codeModalButtonContainer = "modalButton"
  val codeModalEditorMaxHeightPercent = 80.0
  val decoratorButtonRunClass = "kazari-decorator-run"
  val decoratorButtonEditClass = "kazari-decorator-edit"
  val decoratorButtonSaveGistClass = "kazari-decorator-gist"
  val decoratorButtonPlayClass = "fa-play-circle"
  val decoratorButtonSpinnerClass = "fa-spinner fa-spin"
  val decoratorButtonGithubClass = "fa-github-alt"
  val decoratorButtonDisableClass = "compiling"
  val decoratorAlertBarClass = "alert"
  val decoratorAlertBarHiddenClass = "alert-hidden"
  val decoratorAlertBarSuccessClass = "alert-success"
  val decoratorAlertBarErrorClass = "alert-error"
  val kazariUrl = "https://github.com/47deg/sbt-microsites"

  def createModalDiv(cssClass: String): Div = {
    div(
      `class` := "modal",
      input(
        `id` := "modal-1",
        `class` := "modal-state",
        `type` := "checkbox"
      ),
      div(
        `class` := "modal-fade-screen",
        div(
          `class` := "modal-inner",
          div(
            `class` := "modal-close",
            `for` := "modal-1",
            i(
              `class` := "fa fa-close"
            )
          ),
          div(
            div(
              `class` := "modal-content",
              textarea(
                `id` := codeModalInternalTextArea
              ),
              createCompiler(isFromModal = true)
            ),
            div(
              `class` := s"$decoratorAlertBarClass $decoratorAlertBarHiddenClass"
            )
          )
        )
      )
    ).render
  }

  def createDecoration(index: Int): Div = {
    div(
      `id` := s"snippet-$index",
      createCompiler(isFromModal = false),
      div(
        `class` := s"$decoratorAlertBarClass $decoratorAlertBarHiddenClass"
      )
    ).render
  }

  def createCompiler(isFromModal: Boolean) = {
    val (secondaryBtnClass, attrs, secondaryBtnText) =
      if (isFromModal) {
        (decoratorButtonSaveGistClass, "fa fa-github-alt", "Save as Gist")
      } else {
        (decoratorButtonEditClass, "fa fa-pencil", "Edit")
      }

    div(
      `class` := "compiler",
      ul(
        createButton(decoratorButtonRunClass, "fa fa-play-circle", "Run"),
        createButton(secondaryBtnClass, attrs, secondaryBtnText),
        li(
          a(
            href := kazariUrl,
            target := "_blank",
            "Kazari"
          )
        )
      )
    )
  }

  def createButton(anchorClass: String, imgClass: String, text: String) = {
    li(
      a(
        `class` := anchorClass,
        i(
          `class` := imgClass
        ),
        text
      )
    )
  }
}