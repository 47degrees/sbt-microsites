package kazari

import org.scalajs.dom
import org.scalajs.dom._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import dom.ext.PimpedNodeList
import kazari.model.EvaluatorConfig
import org.scalaexercises.evaluator.Dependency
import org.scalaexercises.evaluator.{EvalResponse, EvaluatorClient}
import org.scalaexercises.evaluator.EvaluatorClient._
import org.scalaexercises.evaluator.implicits._
import org.scalaexercises.evaluator.EvaluatorResponses.EvaluationResponse
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@JSExport
object KazariPlugin extends JSApp with DOMHelper {
  val codeExcludeClass = "code-exclude"
  lazy val codeSnippets = document.querySelectorAll(s"code.language-scala:not(.$codeExcludeClass)")
  val dependenciesMetaName = "scala-dependencies"
  val resolversMetaName = "scala-resolvers"

  @JSExport
  def main(): Unit = { }

  @JSExport
  def decorateCode(config: EvaluatorConfig): Unit = {
    val textSnippets = generateCodeTextSnippets()

    codeSnippets.zipWithIndex foreach { case (node, i) =>
      appendButton(node, "Evaluate", onClickFunction = (e: dom.MouseEvent) => {
        val snippet = textSnippets.lift(i + 1)

        val client = new EvaluatorClient(
          config.url,
          config.authToken)

        val evalResponse: Future[EvaluationResponse[EvalResponse]] =
          client.api.evaluates(
            dependencies = getDependenciesList(),
            resolvers = getResolversList(),
            code = snippet.getOrElse("")).exec

        evalResponse onComplete  {
          case Success(r) ⇒
            println(s"Connection to evaluator established: $r")
          case Failure(f) ⇒
            println(s"Error while connecting with remote evaluator: $f")
        }
      })
    }
  }

  def generateCodeTextSnippets() = {
    codeSnippets.map(_.textContent)
        .scanLeft("")((currentItem, result) => currentItem + result)
  }

  def getMetaContent(metaTag: String): String =
    document.querySelector(s"meta[property=" + """"""" + s"$metaTag" + """"""" + "]").getAttribute("content")

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