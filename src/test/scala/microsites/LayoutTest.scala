/*
 * Copyright 2016 47 Degrees, LLC. <http://www.47deg.com>
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

package microsites

import microsites.domain.MicrositeSettings
import microsites.layouts.Layout
import microsites.util.Arbitraries
import org.scalatest.prop.Checkers
import org.scalacheck.Prop._
import org.scalatest.{FunSuite, Matchers}

import scalatags.Text.TypedTag
import scalatags.Text.all._

class LayoutTest extends FunSuite with Checkers with Matchers with Arbitraries {

  def buildParentLayout(implicit settings: MicrositeSettings) = new Layout(settings) {
    override def render: TypedTag[String] = html
  }

  test("meta TypeTag list shouldn't be empty") {

    val property = forAll { implicit settings: MicrositeSettings ⇒
      val layout = buildParentLayout

      layout.metas should not be empty
      layout.metas.nonEmpty
    }

    check(property)
  }

  test("styles TypeTag list shouldn't be empty") {

    val property = forAll { implicit settings: MicrositeSettings ⇒
      val layout = buildParentLayout

      layout.styles should not be empty
      layout.styles.nonEmpty
    }

    check(property)
  }

  test("scripts TypeTag list shouldn't be empty") {

    val property = forAll { implicit settings: MicrositeSettings ⇒
      val layout = buildParentLayout

      layout.scripts should not be empty
      layout.scripts.nonEmpty
    }

    check(property)
  }

  test("globalFooter TypeTag should be `footer`") {

    val property = forAll { implicit settings: MicrositeSettings ⇒
      val layout = buildParentLayout

      layout.globalFooter.tag shouldBe "footer"
      !layout.globalFooter.void
    }

    check(property)
  }

  test("buildCollapseMenu TypeTag should be a `div`") {

    val property = forAll { implicit settings: MicrositeSettings ⇒
      val layout = buildParentLayout

      layout.buildCollapseMenu.tag shouldBe "nav"
      !layout.buildCollapseMenu.void
    }

    check(property)
  }
}
