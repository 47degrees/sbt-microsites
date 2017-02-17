package kazari

import kazari.domhelper.DOMHelper
import org.scalajs.dom._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalaexercises.evaluator.EvaluatorClient
import org.scalajs.dom.ext.PimpedNodeList

@JSExport
object KazariPlugin extends JSApp {
  import DOMHelper._
  import KazariUIBehavior._

  @JSExport
  def main(): Unit = {}

  @JSExport
  def decorateCode(url: String, scalaEvalToken: String, githubToken: String, theme: String): Unit = {
    lazy val evalClient           = new EvaluatorClient(url, scalaEvalToken)
    lazy val codeSnippetsSingle   = document.querySelectorAll(codeSingleSnippetSelector)
    lazy val codeSnippetsSequence = document.querySelectorAll(codeSnippetsSelectorById)

    val codeMirror = createModalWindow(evalClient, githubToken, theme)

    codeSnippetsSingle.zipWithIndex foreach {
      case (node, i) =>
        val decoration = createDecorationSingle(i)
        closestParentDiv(node).append(decoration)
        val snippet = node.textContent

        addRunButtonBehaviour(
          s"#${decoration.id} .$decoratorButtonRunClass",
          s"#${decoration.id}",
          evalClient,
          () => snippet
        )

        addEditButtonBehaviour(kazariSingleSnippetPrefix, i, codeMirror, () => snippet)
    }

    val snippetsGroupedById = codeSnippetsSequence.groupBy { node =>
      closestParentDiv(node)
        .get(0)
        .toOption
        .flatMap(
          classesFromElement(_).find(_.contains(snippetsWithId))
        )
    }

    snippetsGroupedById.foreach {
      case (key, snippetNodes) =>
        val kazariId = key.getOrElse("")
        val joinedSnippets = snippetNodes
          .map(_.textContent)
          .scanLeft("")((currentItem, result) =>
            if (currentItem == "") { result } else { currentItem + "\n" + result })
          .tail

        (snippetNodes zip joinedSnippets).zipWithIndex.foreach {
          case ((node, snippet), i) =>
            val decorationId = s"$kazariId-$i"
            val decoration   = createDecorationWithId(decorationId)
            closestParentDiv(node).append(decoration)

            addRunButtonBehaviour(
              s"#$decorationId .$decoratorButtonRunClass",
              s"#${decorationId}",
              evalClient,
              () => snippet
            )
            addEditButtonBehaviour(kazariId, i, codeMirror, () => snippet)
        }
    }

    applyColorThemes()
  }
}
