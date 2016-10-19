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
    applyModalStyles()

    val cmParams: EditorConfiguration = EditorConfig
        .mode("javascript")
        .lineNumbers(true)
        .theme(theme)

    document.querySelector("#" + codeModalInternalTextArea) match {
      case el: HTMLTextAreaElement =>
        val m = CodeMirror.fromTextArea(el, cmParams)
        m.getDoc().setValue(textSnippets.last)

        addClickListenerToButton(s".$codeModalClass .$decoratorButtonRunClass", (e: dom.MouseEvent) => {
          sendEvaluatorRequest(evalClient, m.getDoc().getValue()).onComplete {
            case Success(r) => println(s"Connection to evaluator established: $r")
            case Failure(e) => println(s"Error connecting to evaluator: $e")
          }
        })
      case _ => console.error("Couldn't find text area to embed CodeMirror instance!")
    }

    codeSnippets.zipWithIndex foreach { case (node, i) =>
      val decoration = createDecoration(i)
      node.appendChild(decoration)

      val snippet = textSnippets.lift(i + 1)
      snippet.foreach((s: String) => {
        addClickListenerToButton(s"#${decoration.id} .$decoratorButtonRunClass", (e: dom.MouseEvent) => {
          sendEvaluatorRequest(evalClient, s).onComplete {
            case Success(r) => println(s"Connection to evaluator established: $r")
            case Failure(e) => println(s"Error connecting to evaluator: $e")
          }
        })
      })

      addClickListenerToButton(s"#${decoration.id} .$decoratorButtonEditClass", (e: dom.MouseEvent) => {
        $(".modal-state").prop("checked", true).change()
      })
    }
  }

  def addClickListenerToButton(selector: String, function: (dom.MouseEvent) => Any) = {
    val btn = Option(document.querySelector(selector))
    btn.foreach { b =>
      b.addEventListener("click", function)
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
}