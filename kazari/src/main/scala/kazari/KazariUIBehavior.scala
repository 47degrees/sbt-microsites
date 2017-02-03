package kazari

import fr.hmil.roshttp.response.SimpleHttpResponse
import kazari.codemirror.{CodeMirrorCharCords, PositionBuilder}
import kazari.domhelper.DOMHelper
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import org.scalajs.dom._
import org.denigma.codemirror.{CodeMirror, Editor, EditorConfiguration}
import org.denigma.codemirror.extensions.EditorConfig
import org.scalaexercises.evaluator.{EvalResponse, EvaluatorClient}
import org.scalaexercises.evaluator.EvaluatorClient._
import org.scalaexercises.evaluator.implicits._
import org.scalaexercises.evaluator.EvaluatorResponses._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLTextAreaElement
import org.querki.jquery._
import github4s.free.domain.GistFile
import github4s.Github
import Github._
import github4s.js.Implicits._

object KazariUIBehavior {
  import DOMHelper._

  val errorMessagePrefix = "Compilation error:"
  val newGistPrompt      = "Choose a description for your Gist"
  val newGistDefaultDescription =
    "Gist created from Kazari (https://github.com/47deg/sbt-microsites)"
  val newGistFilename               = "KazariGist.scala"
  val kazariIdOriginalSnippetSuffix = "original"
  val kazariModalCurrentSnippetKey  = "kazari-modal-current-snippet"

  def addModalFunctionality(codeMirror: Editor) = {
    $("#modal-1").on("change", { (e: JQueryEventObject, a: Any) =>
      if ($("#modal-1").is(":checked")) {
        $("body").addClass("modal-kazari-open")
      } else {
        $("body").removeClass("modal-kazari-open")
      }
    })

    $(".modal-kazari-fade-screen, .modal-kazari-close").on("click", {
      (e: JQueryEventObject, a: Any) =>
        {
          $(".modal-kazari-state:checked").prop("checked", false).change()
          val currentModalSnippet =
            Option(window.sessionStorage.getItem(kazariModalCurrentSnippetKey))

          currentModalSnippet.foreach(s =>
            window.sessionStorage.setItem(s, codeMirror.getDoc().getValue()))
        }
    })

    $(".modal-kazari-inner").on("click", { (e: JQueryEventObject, a: Any) =>
      e.stopPropagation()
    })
  }

  def createModalWindow(evalClient: EvaluatorClient,
                        githubToken: String,
                        theme: String): Option[Editor] = {
    val modalDiv = createModalDiv(codeModalClass)
    document.body.appendChild(modalDiv)

    val cmParams: EditorConfiguration =
      EditorConfig.mode("javascript").lineNumbers(true).theme(theme)

    document.querySelector("#" + codeModalInternalTextArea) match {
      case el: HTMLTextAreaElement =>
        val m = CodeMirror.fromTextArea(el, cmParams)
        m.setSize("100%", ($(window).height() * codeModalEditorMaxHeightPercent) / 100.0)

        val codeSnippetsFromModal = () => m.getDoc().getValue()

        addRunButtonBehaviour(
          s".$codeModalClass .$decoratorButtonRunClass",
          s".$codeModalClass",
          evalClient,
          codeSnippetsFromModal
        )

        addResetButtonBehavior(s".$decoratorButtonResetClass", m)

        addGistButtonBehavior(s".$decoratorButtonSaveGistClass",
                              codeSnippetsFromModal,
                              githubToken)

        addModalFunctionality(m)

        Some(m)
      case _ => console.error("Couldn't find text area to embed CodeMirror instance."); None
    }
  }

  def codeMirrorScrollToLine(editor: Editor, line: Int): Unit = {
    val pos       = PositionBuilder(0, line)
    val scrollTop = editor.charCoords(pos, "local").asInstanceOf[CodeMirrorCharCords].top
    editor.scrollTo(0, scrollTop)
  }

  def createInitialState(decorationId: String, originalSnippet: String) = {
    window.sessionStorage.setItem(kazariModalCurrentSnippetKey, "")
    window.sessionStorage.setItem(decorationId, originalSnippet)
    window.sessionStorage.setItem(s"$decorationId-$kazariIdOriginalSnippetSuffix", originalSnippet)
  }

  def sendEvaluatorRequest(evaluator: EvaluatorClient,
                           codeSnippet: String): Future[EvaluationResponse[EvalResponse]] =
    evaluator.api
      .evaluates(dependencies = getDependenciesList(),
                 resolvers = getResolversList(),
                 code = codeSnippet)
      .exec

  def addRunButtonBehaviour(btnSelector: String,
                            parentSelector: String,
                            evalClient: EvaluatorClient,
                            codeSnippet: () => String,
                            onSuccess: (EvaluationResponse[EvalResponse]) => Unit = (_) => (),
                            onFailure: (Throwable) => Unit = (_) => ()): Unit =
    addClickListenerToButton(btnSelector, (e: dom.MouseEvent) => {
      def isEvaluationSuccessful(response: EvalResponse): Boolean =
        response.msg == EvalResponse.messages.ok

      changeButtonIcon(btnSelector + " " + "i",
                       decoratorButtonPlayClass,
                       decoratorButtonSpinnerClass)
      toggleButtonActiveState(btnSelector, true)
      hideAlertMessage(parentSelector)

      sendEvaluatorRequest(evalClient, codeSnippet()).onComplete {
        case Success(r) => {
          changeButtonIcon(btnSelector + " " + "i",
                           decoratorButtonSpinnerClass,
                           decoratorButtonPlayClass)
          toggleButtonActiveState(btnSelector, false)
          r.fold({ e =>
            showAlertMessage(parentSelector,
                             s"$errorMessagePrefix ${e.getCause.getMessage}",
                             false)
          }, {
            compilationResult =>
              {
                val isSuccess = isEvaluationSuccessful(compilationResult.result)
                val resultMsg = compilationResult.result.value.getOrElse("")
                val errorMsg = if (compilationResult.result.compilationInfos.nonEmpty) {
                  compilationResult.result.compilationInfos.mkString(" ")
                } else {
                  resultMsg
                }
                val compilationValue = if (isSuccess) { resultMsg } else { errorMsg }
                showAlertMessage(parentSelector,
                                 s"${compilationResult.result.msg} - $compilationValue",
                                 isSuccess)
              }
          })
          onSuccess(r)
        }
        case Failure(e) => {
          changeButtonIcon(btnSelector + " " + "i",
                           decoratorButtonSpinnerClass,
                           decoratorButtonPlayClass)
          toggleButtonActiveState(btnSelector, false)
          showAlertMessage(parentSelector,
                           "Error while connecting to the remote evaluator.",
                           false)
          onFailure(e)
        }
      }
    })

  def addEditButtonBehaviour(idPrefix: String,
                             id: Int,
                             codeMirror: Option[Editor],
                             snippet: () => String): Unit = {
    val decorationId = s"$idPrefix-${id.toString}"

    createInitialState(decorationId, snippet())

    addClickListenerToButton(
      s"#${decorationId} .$decoratorButtonEditClass",
      (e: dom.MouseEvent) => {
        window.sessionStorage.setItem(kazariModalCurrentSnippetKey, decorationId)
        codeMirror.foreach(
          m =>
            m.setSize(
              "100%",
              ($(s".$kazariModalInnerClass").height() * codeModalEditorMaxHeightPercent) / 100.0)
        )

        val modalSnippet =
          Option(window.sessionStorage.getItem(decorationId)).getOrElse(snippet())

        codeMirror foreach { c =>
          c.getDoc().setValue(modalSnippet)
          codeMirrorScrollToLine(c, 0)
        }
        hideAlertMessage(s".$codeModalClass")
        $(".modal-kazari-state").prop("checked", true).change()
      })
  }

  def addResetButtonBehavior(btnSelector: String, codeMirrorEditor: Editor): Unit = {
    addClickListenerToButton(btnSelector, (e: dom.MouseEvent) => {
      (for {
        snippetId <- Option(window.sessionStorage.getItem(kazariModalCurrentSnippetKey))
        snippet <- Option(
          window.sessionStorage.getItem(s"$snippetId-$kazariIdOriginalSnippetSuffix"))
      } yield (snippet)).foreach {
        case s =>
          codeMirrorEditor.getDoc().clearHistory()
          codeMirrorEditor.getDoc().setValue(s)
      }
    })
  }

  def addGistButtonBehavior(btnSelector: String,
                            codeSnippet: () => String,
                            accessToken: String,
                            onSuccess: (EvaluationResponse[EvalResponse]) => Unit = (_) => (),
                            onFailure: (Throwable) => Unit = (_) => ()): Unit = {
    addClickListenerToButton(btnSelector, (e: dom.MouseEvent) => {
      changeButtonIcon(btnSelector + " " + "i",
                       decoratorButtonGithubClass,
                       decoratorButtonSpinnerClass)
      toggleButtonActiveState(btnSelector, true)

      val description = window.prompt(newGistPrompt)
      val gistApi     = Github(Some(accessToken)).gists
      val files       = Map(newGistFilename -> GistFile(codeSnippet()))
      val request = gistApi
        .newGist(if (description.isEmpty) newGistDefaultDescription else description, true, files)

      request.execFuture[SimpleHttpResponse]().onComplete {
        result =>
          changeButtonIcon(btnSelector + " " + "i",
                           decoratorButtonSpinnerClass,
                           decoratorButtonGithubClass)
          toggleButtonActiveState(btnSelector, false)

          result match {
            case Failure(e) => println("failure creating gist -> " + e)
            case Success(r) =>
              r.fold(
                e => println("failure creating gist -> " + e),
                r => {
                  println("Success creating gist -> " + r)
                  println("Status code -> " + r.statusCode)
                }
              )
          }
      }
    })
  }

}
