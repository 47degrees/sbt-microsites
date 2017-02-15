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
  val decoratorButtonResetClass       = "kazari-decorator-reset"
  val decoratorButtonPlayClass        = "fa-play-circle"
  val decoratorButtonSpinnerClass     = "fa-spinner fa-spin"
  val decoratorButtonGithubClass      = "fa-github-alt"
  val decoratorButtonReloadIconClass  = "fa-refresh"
  val decoratorButtonDisableClass     = "compiling"
  val decoratorAlertBarClass          = "alert-kazari"
  val decoratorAlertBarHiddenClass    = "alert-hidden-kazari"
  val decoratorAlertBarSuccessClass   = "alert-success-kazari"
  val decoratorAlertBarErrorClass     = "alert-error-kazari"
  val kazariUrl                       = "https://github.com/47deg/sbt-microsites"
  val kazariSingleSnippetPrefix       = "snippet-single"
  val kazariModalClass                = "modal-kazari"
  val kazariModalId                   = "modal-1"
  val kazariModalStateClass           = "modal-kazari-state"
  val kazariModalFadeScreenClass      = "modal-kazari-fade-screen"
  val kazariModalCloseClass           = "modal-kazari-close"
  val kazariModalContentClass         = "modal-kazari-content"
  val kazariModalInnerClass           = "modal-kazari-inner"
  val compilerKazariClass             = "compiler-kazari"
  val compilerKazariBackgroundClass   = "compiler-kazari-background"
  val compilerKazariBorderClass       = "compiler-kazari-border"
  val compilingKazariClass            = "compiling-kazari"
  val compilerKazariColorClass        = "compiler-kazari-color"
  val compilerKazariLinkColorClass    = "compiler-kazari-link-color"
  val codeMirrorLinesClass            = "CodeMirror-line"

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

  def createDecorationSingle(index: Int): Div =
    createDecorationWithId(s"$kazariSingleSnippetPrefix-$index")

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
        // TODO: Removing Gist functionality for now until it's ready for launch
        //(decoratorButtonSaveGistClass, "fa fa-github-alt", "Save as Gist")
        (decoratorButtonResetClass, s"fa $decoratorButtonReloadIconClass", "Reset")
      } else {
        (decoratorButtonEditClass, "fa fa-pencil", "Edit")
      }

    div(
      `class` := s"$compilerKazariClass $compilerKazariBackgroundClass",
      ul(
        createButton(decoratorButtonRunClass, "fa fa-play-circle", "Run"),
        createButton(secondaryBtnClass, attrs, secondaryBtnText),
        li(
          a(
            `class` := compilerKazariLinkColorClass,
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
      `class` := s"$compilerKazariBorderClass",
      a(
        `class` := s"$anchorClass $compilerKazariColorClass",
        i(
          `class` := imgClass
        ),
        text
      )
    )
  }
}
