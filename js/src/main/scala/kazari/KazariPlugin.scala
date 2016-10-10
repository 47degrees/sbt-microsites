package kazari

import org.scalajs.dom
import org.scalajs.dom._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import dom.ext.PimpedNodeList
import kazari.model.EvaluatorConfig
import org.denigma.codemirror.{CodeMirror, EditorConfiguration}
import org.denigma.codemirror.extensions.EditorConfig
import org.scalaexercises.evaluator.Dependency
import org.scalaexercises.evaluator.{EvalResponse, EvaluatorClient}
import org.scalaexercises.evaluator.EvaluatorClient._
import org.scalaexercises.evaluator.implicits._
import org.scalaexercises.evaluator.EvaluatorResponses.EvaluationResponse
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.{HTMLStyleElement, HTMLTextAreaElement}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.Defaults._
import scalacss.internal.{FontFace, NonEmptyVector}
import scalacss.ScalatagsCss._
import org.querki.jquery._

@JSExport
object KazariPlugin extends JSApp with DOMHelper {
  val codeExcludeClass = "code-exclude"
  lazy val codeSnippets = document.querySelectorAll(s"code.language-scala:not(.$codeExcludeClass)")
  val dependenciesMetaName = "evaluator-dependencies"
  val resolversMetaName = "evaluator-resolvers"
  val codeModalClass = "modalDialog"
  val codeModalCloseButtonClass = "closeButton"
  val codeModalInternalTextArea = "modalInternalTextArea"
  val codeModalButtonContainer = "modalButton"

  @JSExport
  def main(): Unit = { }

  @JSExport
  def decorateCode(config: EvaluatorConfig): Unit = {
    val textSnippets = generateCodeTextSnippets()
    lazy val evalClient = new EvaluatorClient(
      config.url,
      config.authToken)

    val modalDiv = createModalDiv(codeModalClass)
    document.body.appendChild(modalDiv)
    document
        .querySelector("." + codeModalCloseButtonClass)
        .addEventListener("click", { (e: dom.MouseEvent) =>
          $("#" + codeModalClass).toggleClass("ModalStyles-default").toggleClass("ModalStyles-active")
    })
    insertCSSStyleSheet("codemirror.css")
    insertCSSStyleSheet("monokai.css")
    val cmParams: EditorConfiguration = EditorConfig
        .mode("javascript")
        .lineNumbers(true)
        .theme("monokai")
    document.querySelector("#" + codeModalInternalTextArea) match {
      case el: HTMLTextAreaElement =>
        val m = CodeMirror.fromTextArea(el, cmParams)
        m.getDoc().setValue(textSnippets.last)
        val containerDiv = document.querySelector("#" + codeModalButtonContainer)
        appendButton(containerDiv, "Evaluate", onClickFunction = (e: dom.MouseEvent) => {
          val snippet = m.getDoc().getValue()
          val evalResponse = sendEvaluatorRequest(evalClient, snippet)
          evalResponse onComplete  {
            case Success(r) ⇒
              println(s"Connection to evaluator established: $r")
            case Failure(f) ⇒
              println(s"Error while connecting with remote evaluator: $f")
          }
        })
      case _ => console.error("Couldn't find text area to embed CodeMirror instance!")
    }

    codeSnippets.zipWithIndex foreach { case (node, i) =>
      appendButton(node, "Evaluate", onClickFunction = (e: dom.MouseEvent) => {
        val snippet = textSnippets.lift(i + 1)
        snippet foreach( (s: String) => {
          val evalResponse = sendEvaluatorRequest(evalClient, s)
          evalResponse onComplete  {
            case Success(r) ⇒
              println(s"Connection to evaluator established: $r")
            case Failure(f) ⇒
              println(s"Error while connecting with remote evaluator: $f")
          }
        })
      })
      appendButton(node, "Edit", onClickFunction = (e: dom.MouseEvent) => {
        $("#" + codeModalClass).toggleClass("ModalStyles-default").toggleClass("ModalStyles-active")
      })
    }
  }

  def generateCodeTextSnippets() = {
    codeSnippets.map(_.textContent)
        .scanLeft("")((currentItem, result) => currentItem + "\n" + result)
  }

  def getMetaContent(metaTagName: String): String = {
    val metaTag = document.querySelector(s"meta[property=" + """"""" + s"$metaTagName" + """"""" + "]")
    if (metaTag != null) {
      metaTag.getAttribute("content")
    } else {
      ""
    }
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

trait DOMHelper {
  def appendButton[T](targetNode: dom.Node, title: String, onClickFunction: Function[_, T], id: Option[String] = None): dom.Node = {
    val btnNode = document.createElement("button")
    btnNode.appendChild(document.createTextNode(title))
    btnNode.setAttribute("type", "button")
    btnNode.setAttribute("id", id.getOrElse(""))
    btnNode.addEventListener("click", onClickFunction)
    targetNode.appendChild(btnNode)
  }

  def createModalDiv(cssClass: String): Div = {
    import scalatags.JsDom.all._
    div(
      `id` := cssClass,
      `class` := cssClass,
      ModalStyles.render[scalatags.JsDom.TypedTag[HTMLStyleElement]],
      ModalStyles.default,
      div(
        ModalStyles.internalDiv,
        a(
          title := "Close",
          `class` := KazariPlugin.codeModalCloseButtonClass,
          ModalStyles.closeButton,
          "X"
        ),
        textarea(
          `id` := KazariPlugin.codeModalInternalTextArea
        ),
        div(
          `id` := KazariPlugin.codeModalButtonContainer
        )
      )
    ).render
  }

  def insertCSSStyleSheet(name: String) = {
    val link = document.createElement("link")
    link.setAttribute("rel", "stylesheet")
    link.setAttribute("type", "text/css")
    link.setAttribute("href", name)
    document.head.appendChild(link)
  }
}

object ModalStyles extends StyleSheet.Inline {
  import dsl._

  val common = style(
    position fixed,
    fontFamily(FontFace("Arial, Helvetica, sans-serif", NonEmptyVector(""))),
    top(0 px),
    bottom(0 px),
    right(0 px),
    left(0 px),
    backgroundColor(rgba(0, 0, 0, 0.8)),
    transition := "opacity 400ms ease-in"
  )

  val default = style(
    common,
    pointerEvents := none,
    zIndex(99999),
    opacity(0)
  )

  val active = style(
    common,
    opacity(1),
    pointerEvents := auto
  )

  val internalDiv = style(
    width(600 px),
    position relative,
    margin(10 %%, auto),
    padding(5 px, 20 px, 13 px, 20 px),
    borderRadius(10 px),
    backgroundColor(c"#fff")
  )

  val closeButton = style(
    backgroundColor(c"#606061"),
    color(c"#FFFFFF"),
    lineHeight(25 px),
    position absolute,
    right(-12 px),
    textAlign center,
    top(-10 px),
    width(24 px),
    textDecoration := ^.none,
    fontWeight bold,
    borderRadius(12 px),
    boxShadow := "1px 1px 3px #000",
    &.hover(
       backgroundColor(c"#00d9ff")
    )
  )
}