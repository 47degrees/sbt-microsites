/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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

import github4s.GithubConfig
import microsites.util.Arbitraries
import org.scalacheck.Prop.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.Checkers
import sbt.Logger

import java.net.URL

class MicrositeAutoImportSettingsTest
    extends AnyFunSuite
    with Checkers
    with Matchers
    with Arbitraries
    with MicrositeAutoImportSettings {

  test("buildGithubConfig should return default GitHub config when invalid Git hosting URL") {
    val property = forAll(
      settingsArbitrary.arbitrary suchThat (!_.gitSettings.gitHostingUrl.startsWith("https://"))
    ) {
      settings: MicrositeSettings ⇒
        implicit val log: Logger = Logger.Null
        val githubConfig         = buildGithubConfig(settings.gitSettings.gitHostingUrl)

        githubConfig shouldBe GithubConfig.default
        githubConfig.baseUrl.nonEmpty
    }

    check(property)
  }

  test("buildGithubConfig should override GitHub config when valid Git hosting URL") {
    val property = forAll(
      settingsArbitrary.arbitrary suchThat (_.gitSettings.gitHostingUrl.startsWith("https://"))
    ) {
      settings: MicrositeSettings ⇒
        implicit val log: Logger = Logger.Null
        val gitSiteUrl           = new URL(settings.gitSettings.gitHostingUrl)
        val githubConfig         = buildGithubConfig(gitSiteUrl.toString)

        val actualGitBaseUrl = new URL(githubConfig.baseUrl)
        actualGitBaseUrl.getProtocol shouldBe "https"
        actualGitBaseUrl.getHost shouldBe gitSiteUrl.getHost
        actualGitBaseUrl.getPath shouldBe "/api/v3/"

        val actualGitAuthorizeUrl = new URL(githubConfig.authorizeUrl)
        actualGitAuthorizeUrl.getProtocol shouldBe "https"
        actualGitAuthorizeUrl.getHost shouldBe gitSiteUrl.getHost
        actualGitAuthorizeUrl.getPath shouldBe new URL(GithubConfig.default.authorizeUrl).getPath
        actualGitAuthorizeUrl.getQuery shouldBe new URL(GithubConfig.default.authorizeUrl).getQuery

        val actualGitAccessTokenUrl = new URL(githubConfig.accessTokenUrl)
        actualGitAccessTokenUrl.getProtocol shouldBe "https"
        actualGitAccessTokenUrl.getHost shouldBe gitSiteUrl.getHost
        actualGitAccessTokenUrl.getPath shouldBe new URL(
          GithubConfig.default.accessTokenUrl
        ).getPath
        actualGitAccessTokenUrl.getQuery shouldBe new URL(
          GithubConfig.default.accessTokenUrl
        ).getQuery

        githubConfig.baseUrl.nonEmpty
    }

    check(property)
  }
}
