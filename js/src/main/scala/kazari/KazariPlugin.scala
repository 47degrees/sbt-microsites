package kazari

import kazari.domhelper.DOMHelper
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import org.scalajs.dom._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.denigma.codemirror.{CodeMirror, EditorConfiguration}
import org.denigma.codemirror.extensions.EditorConfig
import org.scalaexercises.evaluator.{Dependency, EvalResponse, EvaluatorClient}
import org.scalaexercises.evaluator.EvaluatorClient._
import org.scalaexercises.evaluator.implicits._
import org.scalaexercises.evaluator.EvaluatorResponses
import org.scalaexercises.evaluator.EvaluatorResponses._
import org.scalajs.dom
import org.scalajs.dom.ext.PimpedNodeList
import org.scalajs.dom.raw.HTMLTextAreaElement
import org.querki.jquery._

@JSExport
object KazariPlugin extends JSApp with DOMHelper {
  val codeExcludeClass = "code-exclude"
  lazy val codeSnippets = document.querySelectorAll(s"code.language-scala:not(.$codeExcludeClass)")
  val dependenciesMetaName = "evaluator-dependencies"
  val resolversMetaName = "evaluator-resolvers"

  @JSExport
  def main(): Unit = { }

  @JSExport
  def decorateCode(url: String, authToken: String, theme: String): Unit = {
    val textSnippets = generateCodeTextSnippets()
    lazy val evalClient = new EvaluatorClient(url, authToken)

    val modalDiv = createModalDiv(codeModalClass)
    document.body.appendChild(modalDiv)
    document
        .querySelector("." + codeModalCloseButtonClass)
        .addEventListener("click", { (e: dom.MouseEvent) =>
          $("#" + codeModalClass).toggleClass("ModalStyles-default").toggleClass("ModalStyles-active")
    })
    val cmParams: EditorConfiguration = EditorConfig
        .mode("javascript")
        .lineNumbers(true)
        .theme(theme)

    document.querySelector("#" + codeModalInternalTextArea) match {
      case el: HTMLTextAreaElement =>
        val m = CodeMirror.fromTextArea(el, cmParams)
        m.getDoc().setValue(textSnippets.last)
        val containerDiv = document.querySelector("#" + codeModalButtonContainer)
        createButtonEvaluate(evalClient,
          containerDiv,
          () => { m.getDoc().getValue() },
          (r) => println(s"Connection to evaluator established: $r"),
          (f) => println(s"Error connecting to evaluator: $f")
        )
      case _ => console.error("Couldn't find text area to embed CodeMirror instance!")
    }

    codeSnippets.zipWithIndex foreach { case (node, i) =>
      val snippet = textSnippets.lift(i + 1)
      snippet.foreach((s: String) => {
        createButtonEvaluate(evalClient,
          node,
          () => { s },
          (r) => println(s"Connection to evaluator established: $r"),
          (f) => println(s"Error connecting to evaluator: $f")
        )
      })

      appendButton(node, "Edit", onClickFunction = (e: dom.MouseEvent) => {
        $("#" + codeModalClass).toggleClass("ModalStyles-default").toggleClass("ModalStyles-active")
      })
    }
  }

  def createButtonEvaluate(evalClient: EvaluatorClient,
      targetNode: Node,
      snippetToEvaluate: () => String,
      onSuccess: EvaluatorResponses.EvaluationResponse[EvalResponse] => Unit,
      onFailure: Throwable => Unit) = {
    appendButton(targetNode, "Evaluate", onClickFunction = (e: dom.MouseEvent) => {
      val evalResponse = sendEvaluatorRequest(evalClient, snippetToEvaluate())
      evalResponse onComplete  {
        case Success(r) ⇒ onSuccess(r)
        case Failure(f) ⇒ onFailure(f)
      }
    })
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
}