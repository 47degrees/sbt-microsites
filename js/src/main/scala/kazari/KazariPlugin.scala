package kazari

import kazari.domhelper.DOMHelper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import org.scalajs.dom._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.denigma.codemirror.{CodeMirror, EditorConfiguration}
import org.denigma.codemirror.extensions.EditorConfig
import org.scalaexercises.evaluator.{Dependency, EvalResponse, EvaluatorClient}
import org.scalaexercises.evaluator.EvaluatorClient._
import org.scalaexercises.evaluator.implicits._
import org.scalaexercises.evaluator.EvaluatorResponses._
import org.scalajs.dom
import org.scalajs.dom.ext.PimpedNodeList
import org.scalajs.dom.raw.HTMLTextAreaElement
import org.querki.jquery._

@JSExport
object KazariPlugin extends JSApp with DOMHelper {
  lazy val codeSnippets = document.querySelectorAll(codeSnippetsSelector)

  @JSExport
  def main(): Unit = { }

  @JSExport
  def decorateCode(url: String, authToken: String, theme: String): Unit = {
    val textSnippets = generateCodeTextSnippets()
    lazy val evalClient = new EvaluatorClient(url, authToken)

    val modalDiv = createModalDiv(codeModalClass)
    document.body.appendChild(modalDiv)
    applyModalStyles()

    val cmParams: EditorConfiguration = EditorConfig
        .mode("javascript")
        .lineNumbers(true)
        .theme(theme)

    document.querySelector("#" + codeModalInternalTextArea) match {
      case el: HTMLTextAreaElement =>
        val m = CodeMirror.fromTextArea(el, cmParams)
        m.getDoc().setValue(textSnippets.last)
        m.setSize("100%", ($(window).height() * codeModalEditorMaxHeightPercent) / 100.0)

        addRunButtonBehaviour(
          s".$codeModalClass .$decoratorButtonRunClass",
          evalClient,
          () => m.getDoc().getValue(),
          { r => println(s"Connection to evaluator established: $r") },
          { e => println(s"Error connecting to evaluator: $e") }
        )
      case _ => console.error("Couldn't find text area to embed CodeMirror instance.")
    }

    codeSnippets.zipWithIndex foreach { case (node, i) =>
      val decoration = createDecoration(i)
      node.appendChild(decoration)

      val snippet = textSnippets.lift(i + 1)
      snippet foreach((s: String) => {
        addRunButtonBehaviour(
          s"#${decoration.id} .$decoratorButtonRunClass",
          evalClient,
          () => s,
          { r => println(s"Connection to evaluator established: $r") },
          { e => println(s"Error connecting to evaluator: $e") }
        )
      })

      addClickListenerToButton(s"#${decoration.id} .$decoratorButtonEditClass", (e: dom.MouseEvent) => {
        $(".modal-state").prop("checked", true).change()
      })
    }
  }

  def generateCodeTextSnippets() = {
    codeSnippets.map(_.textContent)
        .scanLeft("")((currentItem, result) => currentItem + "\n" + result)
  }

  def getDependenciesList(): List[Dependency] = {
    val content = getMetaContent(dependenciesMetaName)
    val elements = content.split(",")

    elements.foldRight(Seq[Dependency]()) { case (e, l) =>
      val split = e.split(";")
      if (split.length == 3) {
        l ++ Seq(Dependency(split(0), split(1), split(2)))
      } else {
        l
      }
    }.toList
  }

  def getResolversList(): List[String] = {
    val content = getMetaContent(resolversMetaName)
    content.split(",").toList
  }

  def sendEvaluatorRequest(evaluator: EvaluatorClient, codeSnippet: String): Future[EvaluationResponse[EvalResponse]] =
    evaluator.api.evaluates(
        dependencies = getDependenciesList(),
        resolvers = getResolversList(),
        code = codeSnippet).exec

  def addRunButtonBehaviour(btnSelector: String,
      evalClient: EvaluatorClient,
      codeSnippet: () => String,
      onSuccess: (EvaluationResponse[EvalResponse]) => Unit,
      onFailure: (Throwable) => Unit): Unit =

    addClickListenerToButton(btnSelector, (e: dom.MouseEvent) => {
      changeButtonIcon(btnSelector + " " + "i", decoratorButtonPlayClass, decoratorButtonSpinnerClass)
      toggleButtonActiveState(btnSelector, true)

      sendEvaluatorRequest(evalClient, codeSnippet()).onComplete {
        case Success(r) => {
          changeButtonIcon(btnSelector + " " + "i", decoratorButtonSpinnerClass, decoratorButtonPlayClass)
          toggleButtonActiveState(btnSelector, false)
          onSuccess(r)
        }
        case Failure(e) => {
          changeButtonIcon(btnSelector + " " + "i", decoratorButtonSpinnerClass, decoratorButtonPlayClass)
          toggleButtonActiveState(btnSelector, false)
          onFailure(e)
        }
      }
    })
}