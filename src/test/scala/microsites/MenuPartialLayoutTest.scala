/*
 * Copyright 2016-2022 47 Degrees Open Source <https://www.47deg.com>
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

import microsites.layouts.MenuPartialLayout
import microsites.util.Arbitraries
import org.scalacheck.Prop._
import org.scalatestplus.scalacheck.Checkers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MenuPartialLayoutTest extends AnyFunSuite with Checkers with Matchers with Arbitraries {

  test("render should return a div container") {

    val property = forAll { settings: MicrositeSettings ⇒
      val layout = new MenuPartialLayout(settings)

      layout.render.tag shouldBe "div"
      !layout.render.void
    }

    check(property)
  }
}
