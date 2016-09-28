package kazari

import org.scalajs.dom
import org.scalajs.dom._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import dom.ext.PimpedNodeList
import kazari.model.{EvaluatorConfig, EvaluatorDependency, EvaluatorRequest}
import org.scalajs.jquery.{JQueryAjaxSettings, JQueryXHR, jQuery}

import scala.scalajs.js
import upickle.default._

object KazariPlugin extends JSApp with DOMHelper {
  val codeExcludeClass = "code-exclude"
  lazy val codeSnippets = document.querySelectorAll(s"code.language-scala:not(.$codeExcludeClass)")

  @JSExport
  def main(): Unit = { }

  @JSExport
  def decorateCode(config: EvaluatorConfig): Unit = {
    val textSnippets = generateCodeTextSnippets()

    codeSnippets.zipWithIndex foreach { case (node, i) =>
      appendButton(node, "Evaluate", onClickFunction = (e: dom.MouseEvent) => {
        val snippet = textSnippets.lift(i + 1)
        val request = generateEvalRequest(snippet.getOrElse(""), Seq[String](), Seq[EvaluatorDependency]())

        requestEvaluationOfSnippet(request,
          config.url,
          config.authToken,
          (data: js.Any, textStatus: String, jqHXR: JQueryXHR) => {
            println(s"Connection to evaluator established: $textStatus")
          },
          (jqHXR: JQueryXHR, status: String, error: String) => {
            println(s"Error while connecting with remote evaluator: $error")
          }
        )
      });
    }
  }

  def generateCodeTextSnippets() = {
    codeSnippets.map(_.textContent)
        .scanLeft("")((currentItem, result) => currentItem + result)
  }

  def requestEvaluationOfSnippet(snippet: String,
      url: String,
      token: String,
      success: (js.Any, String, JQueryXHR) => Unit,
      failure: (JQueryXHR, String, String) => Unit) = {
    val settings = js.Dynamic.literal(
      `type` = "POST",
      url = url,
      beforeSend = (xhrObj: JQueryXHR) => {
        xhrObj.setRequestHeader("X-Scala-Eval-Api-Token", token)
        xhrObj.setRequestHeader("Content-Type", "application/json")
      },
      data = snippet,
      success = success,
      error = failure
    ).asInstanceOf[JQueryAjaxSettings]

    jQuery.ajax(settings)
  }

  def generateEvalRequest(snippet: String, resolvers: Seq[String], dependencies: Seq[EvaluatorDependency]): String =
    write(EvaluatorRequest(resolvers, dependencies, snippet))
}

trait DOMHelper {
  def appendButton[T](targetNode: dom.Node, title: String, onClickFunction: Function[_, T], id: Option[String] = None): dom.Node = {
    val btnNode = document.createElement("button")
    btnNode.appendChild(document.createTextNode(title))
    btnNode.setAttribute("type", "button")
    btnNode.setAttribute("id", id.getOrElse(""))
    btnNode.addEventListener("click", onClickFunction)
    targetNode.appendChild(btnNode)
  }
}