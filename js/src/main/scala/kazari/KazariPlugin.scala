package kazari

import org.scalajs.dom
import org.scalajs.dom._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import dom.ext.PimpedNodeList
import kazari.model.{EvaluatorConfig, EvaluatorDependency, EvaluatorRequest}
import org.scalaexercises.evaluator.{EvalResponse, EvaluatorClient}
import upickle.default._
import cats.Eval
import cats.data.Xor
import org.scalaexercises.evaluator.EvaluatorClient._
import org.scalaexercises.evaluator.EvaluatorResponses.{EvaluationResponse, EvaluationResult}
import org.scalaexercises.evaluator.implicits._

import scala.concurrent.Future

object KazariPlugin extends JSApp with DOMHelper {
  val codeExcludeClass  = "code-exclude"
  lazy val codeSnippets = document.querySelectorAll(s"code.language-scala:not(.$codeExcludeClass)")

  @JSExport
  def main(): Unit = {}

  @JSExport
  def decorateCode(config: EvaluatorConfig): Unit = {
    val textSnippets = generateCodeTextSnippets()

    codeSnippets.zipWithIndex foreach {
      case (node, i) =>
        appendButton(node, "Evaluate", onClickFunction = (e: dom.MouseEvent) => {
          val snippet = textSnippets.lift(i + 1)
          val request =
            generateEvalRequest(snippet.getOrElse(""), Seq[String](), Seq[EvaluatorDependency]())

          requestEvaluationOfSnippet(request, config.url, config.authToken)
        });
    }
  }

  def generateCodeTextSnippets() = {
    codeSnippets.map(_.textContent).scanLeft("")((currentItem, result) => currentItem + result)
  }

  def requestEvaluationOfSnippet(snippet: String, url: String, token: String) = {
    import cats.implicits._
    import scala.concurrent.ExecutionContext.Implicits.global
    val response: EvaluationResponse[EvalResponse] = {
      EvaluatorClient(url, token).api.evaluates(Nil, Nil, snippet).exec[Future]
      ???
    }

    response match {
      case Xor.Left(msg) ⇒
      case Xor.Right(
          EvaluationResult(EvalResponse(EvalResponse.messages.ok, _, _, _),
                           statusCode,
                           headers)) =>
        println(s"Connection to evaluator established")
      case Xor.Right(
          EvaluationResult(EvalResponse(msg, value, valueType, compilationInfos),
                           statusCode,
                           headers)) =>
        println(s"Error while connecting with remote evaluator: $msg")
    }
  }

  def generateEvalRequest(snippet: String,
                          resolvers: Seq[String],
                          dependencies: Seq[EvaluatorDependency]): String =
    write(EvaluatorRequest(resolvers, dependencies, snippet))
}

trait DOMHelper {
  def appendButton[T](targetNode: dom.Node,
                      title: String,
                      onClickFunction: Function[_, T],
                      id: Option[String] = None): dom.Node = {
    val btnNode = document.createElement("button")
    btnNode.appendChild(document.createTextNode(title))
    btnNode.setAttribute("type", "button")
    btnNode.setAttribute("id", id.getOrElse(""))
    btnNode.addEventListener("click", onClickFunction)
    targetNode.appendChild(btnNode)
  }
}
