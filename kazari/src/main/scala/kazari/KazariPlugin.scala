/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
  def decorateCode(
      url: String,
      scalaEvalToken: String,
      githubToken: String,
      theme: String): Unit = {
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
