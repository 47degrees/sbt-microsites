package kazari.domhelper

import org.scalajs.dom.html._
import scalatags.JsDom.all._

trait DOMTags {
  val codeModalClass                  = "modal-kazari-fade-screen"
  val codeModalCloseButtonClass       = "modal-kazari-close"
  val codeModalInternalTextArea       = "modal-kazari-text-area"
  val codeModalButtonContainer        = "modalButton"
  val codeModalEditorMaxHeightPercent = 80.0
  val decoratorButtonRunClass         = "kazari-decorator-run"
  val decoratorButtonEditClass        = "kazari-decorator-edit"
  val decoratorButtonSaveGistClass    = "kazari-decorator-gist"
  val decoratorButtonPlayClass        = "fa-play-circle"
  val decoratorButtonSpinnerClass     = "fa-spinner fa-spin"
  val decoratorButtonGithubClass      = "fa-github-alt"
  val decoratorButtonDisableClass     = "compiling"
  val decoratorAlertBarClass          = "alert"
  val decoratorAlertBarHiddenClass    = "alert-hidden"
  val decoratorAlertBarSuccessClass   = "alert-success"
  val decoratorAlertBarErrorClass     = "alert-error"
  val kazariUrl                       = "https://github.com/47deg/sbt-microsites"

  def createModalDiv(cssClass: String): Div = {
    div(
      `class` := "modal-kazari",
      input(
        `id` := "modal-1",
        `class` := "modal-kazari-state",
        `type` := "checkbox"
      ),
      div(
        `class` := "modal-kazari-fade-screen",
        div(
          `class` := "modal-kazari-inner",
          div(
            `class` := "modal-kazari-close",
            `for` := "modal-1",
            i(
              `class` := "fa fa-close"
            )
          ),
          div(
            div(
              `class` := "modal-kazari-content",
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

  def createDecoration(index: Int): Div = createDecorationWithId(s"snippet-$index")

  def createDecorationSingle(index: Int): Div = createDecorationWithId(s"snippet-single-$index")

  def createDecorationWithId(decorationId: String): Div = {
    div(
      `id` := decorationId,
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
