# Changelog

## 02/21/2020 - Version 1.1.2

Release changes:

* Setting version to 1.1.2-SNAPSHOT ([#429](https://github.com/47deg/sbt-microsites/pull/429))
* Remove hardcoded sbt version from Travis config ([#428](https://github.com/47deg/sbt-microsites/pull/428))
* Declare proper doctype on layouts ([#430](https://github.com/47deg/sbt-microsites/pull/430))
* Update scalactic to 3.1.1 ([#433](https://github.com/47deg/sbt-microsites/pull/433))
* Update scalatest to 3.1.1 ([#434](https://github.com/47deg/sbt-microsites/pull/434))
* Upgrade sbt-org-policies and modify sign key importing ([#431](https://github.com/47deg/sbt-microsites/pull/431))
* Trigger new patch version (1.1.2) ([#435](https://github.com/47deg/sbt-microsites/pull/435))


## 02/19/2020 - Version 1.1.1

Release changes:

* Update sbt-microsites to 1.1.0 ([#413](https://github.com/47deg/sbt-microsites/pull/413))
* Update sbt to 1.3.7 ([#414](https://github.com/47deg/sbt-microsites/pull/414))
* Update org-policies-core to 0.12.3 ([#415](https://github.com/47deg/sbt-microsites/pull/415))
* Update sbt-org-policies to 0.12.3 ([#416](https://github.com/47deg/sbt-microsites/pull/416))
* Update sbt to 1.3.8 ([#419](https://github.com/47deg/sbt-microsites/pull/419))
* Implement feature links ([#417](https://github.com/47deg/sbt-microsites/pull/417))
* Update scalatags to 0.8.5 ([#420](https://github.com/47deg/sbt-microsites/pull/420))
* Update scalafmt-core to 2.4.0 ([#422](https://github.com/47deg/sbt-microsites/pull/422))
* Update scalafmt-core to 2.4.1 ([#423](https://github.com/47deg/sbt-microsites/pull/423))
* Update scalatags to 0.8.6 ([#424](https://github.com/47deg/sbt-microsites/pull/424))
* Release 1.1.1 ([#425](https://github.com/47deg/sbt-microsites/pull/425))


## 01/13/2020 - Version 1.1.0

Release changes:

* Update sbt-microsites to 1.0.2 ([#395](https://github.com/47deg/sbt-microsites/pull/395))
* Tell hljs the language of code blocks so it doesn't have to guess ([#394](https://github.com/47deg/sbt-microsites/pull/394))
* Update sbt to 1.3.5 ([#396](https://github.com/47deg/sbt-microsites/pull/396))
* Update scalacheck to 1.14.3 ([#397](https://github.com/47deg/sbt-microsites/pull/397))
* Simplify deployment - Improve docs ([#399](https://github.com/47deg/sbt-microsites/pull/399))
* Update scalatags to 0.8.1 ([#400](https://github.com/47deg/sbt-microsites/pull/400))
* Update scalatags to 0.8.2 ([#401](https://github.com/47deg/sbt-microsites/pull/401))
* Please add sbt-kubeyml ([#402](https://github.com/47deg/sbt-microsites/pull/402))
* Airframe has graduated from sbt-microsite ([#403](https://github.com/47deg/sbt-microsites/pull/403))
* Update sbt to 1.3.6 ([#405](https://github.com/47deg/sbt-microsites/pull/405))
* Update scalatags to 0.8.3 ([#404](https://github.com/47deg/sbt-microsites/pull/404))
* Update sbt-mdoc to 2.1.0 ([#407](https://github.com/47deg/sbt-microsites/pull/407))
* Add a troubleshooting page ([#408](https://github.com/47deg/sbt-microsites/pull/408))
* Update sbt-mdoc to 2.1.1 ([#409](https://github.com/47deg/sbt-microsites/pull/409))
* Update scalatags to 0.8.4 ([#410](https://github.com/47deg/sbt-microsites/pull/410))
* Update transitive offered mdoc to same in deps ([#411](https://github.com/47deg/sbt-microsites/pull/411))
* Trigger new minor version (1.1.0) ([#412](https://github.com/47deg/sbt-microsites/pull/412))


## 12/05/2019 - Version 1.0.2

Release changes:

* Uses the new version 1.0.1 ([#390](https://github.com/47deg/sbt-microsites/pull/390))
* Make pushMicrosite a dynamic task and always return a task on it ([#392](https://github.com/47deg/sbt-microsites/pull/392))
* Release version 1.0.2 ([#393](https://github.com/47deg/sbt-microsites/pull/393))


## 12/04/2019 - Version 1.0.1

Release changes:

* Fixes safari styles ([#386](https://github.com/47deg/sbt-microsites/pull/386))
* Update sbt-mdoc to 2.0.3 ([#387](https://github.com/47deg/sbt-microsites/pull/387))
* publishMultiversionMicrosite and fix GHPagesPlugin publishing ([#388](https://github.com/47deg/sbt-microsites/pull/388))
* Release version 1.0.1 ([#389](https://github.com/47deg/sbt-microsites/pull/389))


## 12/02/2019 - Version 1.0.0 :rocket:

This release includes a completely redesigned style, which is offered through the use of a new feature: _themes_. The initial theme is named `light`, and features clean lines and a minimalistic design. This is the main feature of this new version, but there are many more changes, both small and large, to talk about.

### Layouts :nail_care: 

`sbt-microsites` is still totally customizable, and users can implement their personal style. Previous style hasn't been discarded, so users have the option of creating a microsite with the previous style, which is named `pattern` style.

These are the main aspects of the new `light` style, for which we have disposed of Bootstrap to use our own CSS styling.

##### Home layout

Default layout when setting the `index.md` as `layout: home`. It is similar to the previous `home` layout, but the main difference you'll notice is that we removed the background pattern from the header. The rest of the elements in this layout remain the same, but in a more stylized and attractive way.


##### Features layout

A completely new layout for v1.0.0, and its main characteristic is the _features_ section that is placed after the header, and where the user can highlight three main aspects of a library.

The example below shows how to display this layout:

```
---
layout: homeFeatures
features:
 - first:   ["Configuration", "sbt-microsites facilitates the creation of fancy microsites for your projects, with minimal tweaks"]
 - second:  ["Customize", "sbt-microsites provides a considerable scope for improvement and customization in terms of images and styles"]
 - third:   ["Documentation", "Writing documentation for your own microsites is fast and easy, so you don't have to deal with details"]
---
```

The features are identified for the set of keys (`first`, `second`, `third`), and you can add the title and a description for any feature in your library.

##### Docs layout

The default theme for the code highlighting has been changed to [vs](https://highlightjs.org/static/demo/), although this is configurable as described in the [sbt-microsites customize section](https://47deg.github.io/sbt-microsites/docs/customize.html#customize).

### Multiversion Support :1234: 

`sbt-microsites` now offers initial support to build different versions of your library documentation.

To start using this new feature, get a look at the new task `makeMultiversionMicrosite`. This will go through the new `micrositeVersionList` setting, where you can set a list of versions you want to build:

```scala
micrositeVersionList := List("0.1.0", "0.2.0")
```

And then the main microsite will now show you a selector of links to those versions microsites.


### Breaking changes :rotating_light: 

[mdoc](https://github.com/scalameta/mdoc) is now the default markdown code typechecking tool, currently updated to v2.0.2 [Release Notes/Changelog](https://github.com/scalameta/mdoc/releases/tag/v2.0.2). Because of this, sbt-microsites users who are still using `tut` in their microsites must follow the steps described [here](https://47deg.github.io/sbt-microsites/docs/customize.html#syntax-highlighting) to use `mdoc`, or adapt your build accordingly in order to keep using `tut`.


### Other Features ✨

- Custom Sass/SCSS support when overwriting styles.
- Complete jQuery removal for new default `light` theme, now using vanilla JavaScript.
- Use ES2015 syntax and newer browser APIs.
- Sidebar show/hide and current element activation behavior improvements.
- Update highlightjs to version 9.16.2.
- Complete streamline of web 3rd party dependencies.
- Various improvements on the site construction, getting rid of unused files and boilerplate content.
- Add more semantic tags, and include a11y attributes.


Additionally, you can read more information on the [sbt-microsites website](https://47deg.github.io/sbt-microsites/)



### Notable PRs :woman_technologist: 

- Update `sbt` version in Travis, change `micrositeDocumentationUrl` ([#383](https://github.com/47deg/sbt-microsites/pull/383))
- Update `scalatest` to 3.1.0 ([#381](https://github.com/47deg/sbt-microsites/pull/381))
- Update `scalactic` to 3.1.0 ([#380](https://github.com/47deg/sbt-microsites/pull/380))
- Multiversion support ([#379](https://github.com/47deg/sbt-microsites/pull/379))
- Update `sbt` to 1.3.4 ([#378](https://github.com/47deg/sbt-microsites/pull/378)) 
- Changes micrositeExtraMdFilesOutput default folder ([#377](https://github.com/47deg/sbt-microsites/pull/377))
- Custom Sass/SCSS support ([#373](https://github.com/47deg/sbt-microsites/pull/373))
- Update `sbt-mdoc` to 2.0.2 ([#371](https://github.com/47deg/sbt-microsites/pull/371))
- Set homepage setting to be GitHub repo URL explicitely for the project ([#370](https://github.com/47deg/sbt-microsites/pull/370)) 
- Site home change ([#368](https://github.com/47deg/sbt-microsites/pull/368))
- Streamline web 3rd party dependencies ([#367](https://github.com/47deg/sbt-microsites/pull/367))
- Modify `sbt-microsites` documentation folder ([#365](https://github.com/47deg/sbt-microsites/pull/365))
- Features layout section implementation ([#364](https://github.com/47deg/sbt-microsites/pull/364))
- Adds Java property `java.awt.headless=true` ([#363](https://github.com/47deg/sbt-microsites/pull/363))
- `mdoc` by default ([#362](https://github.com/47deg/sbt-microsites/pull/362))
- Use project scope for `mdoc` sources setting, not ThisBuild ([#357](https://github.com/47deg/sbt-microsites/pull/357)) by @pdalpra
- New style redesign ([#354](https://github.com/47deg/sbt-microsites/pull/354))


## 10/02/2019 - Version 0.9.7

Release changes:

* Update sbt-microsites to 0.9.6 ([#345](https://github.com/47deg/sbt-microsites/pull/345))
* Update scalacheck to 1.14.2 ([#346](https://github.com/47deg/sbt-microsites/pull/346))
* Update sbt-mdoc to 1.3.4 ([#347](https://github.com/47deg/sbt-microsites/pull/347))
* Update tut-plugin to 0.6.13 ([#348](https://github.com/47deg/sbt-microsites/pull/348))
* Releases 0.9.7 ([#349](https://github.com/47deg/sbt-microsites/pull/349))


## 09/25/2019 - Version 0.9.6

Release changes:

* Add {% raw %} directive to embedded {{page.path}} ([#326](https://github.com/47deg/sbt-microsites/pull/326))
* Update layouts.md ([#329](https://github.com/47deg/sbt-microsites/pull/329))
* Update customize.md ([#328](https://github.com/47deg/sbt-microsites/pull/328))
* Update README.md ([#327](https://github.com/47deg/sbt-microsites/pull/327))
* Added Mocked Streams library to list of projects using sbt-microsites ([#330](https://github.com/47deg/sbt-microsites/pull/330))
* Update publish-with-travis.md ([#331](https://github.com/47deg/sbt-microsites/pull/331))
* Update settings.md ([#332](https://github.com/47deg/sbt-microsites/pull/332))
* Add rel attribute to external links to avoid security problems ([#333](https://github.com/47deg/sbt-microsites/pull/333))
* Update moultingyaml to 0.4.1 ([#339](https://github.com/47deg/sbt-microsites/pull/339))
* Update sbt-microsites to 0.9.4 ([#337](https://github.com/47deg/sbt-microsites/pull/337))
* Update sbt to 1.3.2 ([#340](https://github.com/47deg/sbt-microsites/pull/340))
* Update scalacheck to 1.14.1 ([#341](https://github.com/47deg/sbt-microsites/pull/341))
* Update org-policies-core, ... to 0.12.0-M3 ([#336](https://github.com/47deg/sbt-microsites/pull/336))
* Update prerequisites versions doc info ([#342](https://github.com/47deg/sbt-microsites/pull/342))
* Release version 0.9.5 ([#343](https://github.com/47deg/sbt-microsites/pull/343))
* Supporting ExtraMDFiles with Mdoc ([#320](https://github.com/47deg/sbt-microsites/pull/320))
* Release version 0.9.6 ([#344](https://github.com/47deg/sbt-microsites/pull/344))


## 09/25/2019 - Version 0.9.5

Release changes:

* Add {% raw %} directive to embedded {{page.path}} ([#326](https://github.com/47deg/sbt-microsites/pull/326))
* Update layouts.md ([#329](https://github.com/47deg/sbt-microsites/pull/329))
* Update customize.md ([#328](https://github.com/47deg/sbt-microsites/pull/328))
* Update README.md ([#327](https://github.com/47deg/sbt-microsites/pull/327))
* Added Mocked Streams library to list of projects using sbt-microsites ([#330](https://github.com/47deg/sbt-microsites/pull/330))
* Update publish-with-travis.md ([#331](https://github.com/47deg/sbt-microsites/pull/331))
* Update settings.md ([#332](https://github.com/47deg/sbt-microsites/pull/332))
* Add rel attribute to external links to avoid security problems ([#333](https://github.com/47deg/sbt-microsites/pull/333))
* Update moultingyaml to 0.4.1 ([#339](https://github.com/47deg/sbt-microsites/pull/339))
* Update sbt-microsites to 0.9.4 ([#337](https://github.com/47deg/sbt-microsites/pull/337))
* Update sbt to 1.3.2 ([#340](https://github.com/47deg/sbt-microsites/pull/340))
* Update scalacheck to 1.14.1 ([#341](https://github.com/47deg/sbt-microsites/pull/341))
* Update org-policies-core, ... to 0.12.0-M3 ([#336](https://github.com/47deg/sbt-microsites/pull/336))
* Update prerequisites versions doc info ([#342](https://github.com/47deg/sbt-microsites/pull/342))
* Release version 0.9.5 ([#343](https://github.com/47deg/sbt-microsites/pull/343))


## 09/02/2019 - Version 0.9.4

Release changes:

* Consolidate two editButton href tags into one ([#324](https://github.com/47deg/sbt-microsites/pull/324))
* Release version 0.9.4 ([#325](https://github.com/47deg/sbt-microsites/pull/325))


## 08/28/2019 - Version 0.9.3

Release changes:

* Link mdoc into the credits section ([#317](https://github.com/47deg/sbt-microsites/pull/317))
* Remove ZIO from "microsites in the wild" ([#319](https://github.com/47deg/sbt-microsites/pull/319))
* Build Upgrade ([#321](https://github.com/47deg/sbt-microsites/pull/321))
* Release 0.9.3 ([#322](https://github.com/47deg/sbt-microsites/pull/322))


## 06/26/2019 - Version 0.9.2

Release changes:

* Home layout HighLighting ([#311](https://github.com/47deg/sbt-microsites/pull/311))
* Update to the latest stable Jekyll version ([#315](https://github.com/47deg/sbt-microsites/pull/315))
* Active doc page highlighted ([#314](https://github.com/47deg/sbt-microsites/pull/314))
* Release version 0.9.2 ([#316](https://github.com/47deg/sbt-microsites/pull/316))


## 05/22/2019 - Version 0.9.1

Release changes:

* Updates microsite docs ([#304](https://github.com/47deg/sbt-microsites/pull/304))
* add "includeFilter in makeSite" to docs ([#307](https://github.com/47deg/sbt-microsites/pull/307))
* Upgrade sbt-org-policies 0.11.3 applying Scala CoC ([#308](https://github.com/47deg/sbt-microsites/pull/308))
* Release version 0.9.1 ([#309](https://github.com/47deg/sbt-microsites/pull/309))


## 02/26/2019 - Version 0.9.0

Release changes:

Releasing 0.9.0


## 02/26/2019 - Version 0.9.0-M1

Release changes:

* Fix issue #229 ([#300](https://github.com/47deg/sbt-microsites/pull/300))
* Upgrades the project by using the latest sbt-org-policies version ([#302](https://github.com/47deg/sbt-microsites/pull/302))


## 01/15/2019 - Version 0.8.0

Release changes:

* fix markdown parsing ([#290](https://github.com/47deg/sbt-microsites/pull/290))
* fix typo with bumping ruby version to 2.3.0 in travis ([#291](https://github.com/47deg/sbt-microsites/pull/291))
* Update Ruby Version Script ([#294](https://github.com/47deg/sbt-microsites/pull/294))
* Add support to use mdoc ([#293](https://github.com/47deg/sbt-microsites/pull/293))
* Fixes and Updates Build ([#296](https://github.com/47deg/sbt-microsites/pull/296))
* Removes kazari from sbt-microsites ([#295](https://github.com/47deg/sbt-microsites/pull/295))
* Type safety at micrositeCompilingDocsTool Setting ([#297](https://github.com/47deg/sbt-microsites/pull/297))
* Releases 0.8.0 ([#298](https://github.com/47deg/sbt-microsites/pull/298))


## 11/29/2018 - Version 0.7.27

Release changes:

* upgrade tut to 0.6.9 ([#288](https://github.com/47deg/sbt-microsites/pull/288))
* Release with tut 0.6.9 ([#289](https://github.com/47deg/sbt-microsites/pull/289))


## 11/20/2018 - Version 0.7.26

Release changes:

* Use the site absolute url for twitter:image ([#285](https://github.com/47deg/sbt-microsites/pull/285))
* Release 0.7.26 ([#286](https://github.com/47deg/sbt-microsites/pull/286))


## 11/16/2018 - Version 0.7.25

Release changes:

* Some more Projects using Microsites. ([#280](https://github.com/47deg/sbt-microsites/pull/280))
* Improve logos in the README ([#281](https://github.com/47deg/sbt-microsites/pull/281))
* Changed position of Finch on README in order to see icon ([#282](https://github.com/47deg/sbt-microsites/pull/282))
* Fix twitter image URL ([#283](https://github.com/47deg/sbt-microsites/pull/283))
* Release 0.7.25 ([#284](https://github.com/47deg/sbt-microsites/pull/284))


## 10/09/2018 - Version 0.7.24

Release changes:

* add 'Edit Page' feature to doc pages ([#278](https://github.com/47deg/sbt-microsites/pull/278))
* Release sbt-microsites 0.7.24 ([#279](https://github.com/47deg/sbt-microsites/pull/279))


## 08/19/2018 - Version 0.7.23

Release changes:

* Add support for additional highlighted languages ([#276](https://github.com/47deg/sbt-microsites/pull/276))


## 07/17/2018 - Version 0.7.22

Release changes:

* gitSiteURL supports MicrositeKeys.GitHub and gitHostingUrl ([#272](https://github.com/47deg/sbt-microsites/pull/272))
* Releases sbt-microsites 0.7.22 ([#273](https://github.com/47deg/sbt-microsites/pull/273))
* Fix default layouts path ([#274](https://github.com/47deg/sbt-microsites/pull/274))


## 07/12/2018 - Version 0.7.21

Release changes:

* update tut 0.6.6 ([#270](https://github.com/47deg/sbt-microsites/pull/270))
* Release - Updating to tut 0.6.6 ([#271](https://github.com/47deg/sbt-microsites/pull/271))


## 06/19/2018 - Version 0.7.20

Release changes:

* #267 documentation label configurable ([#268](https://github.com/47deg/sbt-microsites/pull/268))


## 06/13/2018 - Version 0.7.19

Release changes:

* Add Scanamo to wild sbt-microsites ([#263](https://github.com/47deg/sbt-microsites/pull/263))
* Updated sbt-org-policies, and sbt to the latest versions. ([#265](https://github.com/47deg/sbt-microsites/pull/265))
* Release sbt-microsites v0.7.19 ([#266](https://github.com/47deg/sbt-microsites/pull/266))


## 03/30/2018 - Version 0.7.18

Release changes:

* docs typos ([#261](https://github.com/47deg/sbt-microsites/pull/261))
* Update tut to 0.6.4 for incomplete input fix ([#262](https://github.com/47deg/sbt-microsites/pull/262))


## 03/11/2018 - Version 0.7.17

Release changes:

* Bumps sbt-org-policies version ([#260](https://github.com/47deg/sbt-microsites/pull/260))


## 02/08/2018 - Version 0.7.16

Release changes:

* allows hide Github and Social links in `docs` layout ([#257](https://github.com/47deg/sbt-microsites/pull/257))


## 01/20/2018 - Version 0.7.15

Release changes:

* Add page title ([#255](https://github.com/47deg/sbt-microsites/pull/255))


## 01/11/2018 - Version 0.7.14

Release changes:

* Add micrositeStaticDirectory feature ([#253](https://github.com/47deg/sbt-microsites/pull/253))


## 12/14/2017 - Version 0.7.13

Release changes:

* Bump ruby version to 2.2.8 in docs ([#246](https://github.com/47deg/sbt-microsites/pull/246))
* Use settingKey for SettingKey definitions ([#247](https://github.com/47deg/sbt-microsites/pull/247))
* Releases 0.7.13 ([#249](https://github.com/47deg/sbt-microsites/pull/249))


## 12/01/2017 - Version 0.7.12

Release changes:

* Add anchor links handlers ([#245](https://github.com/47deg/sbt-microsites/pull/245))


## 11/30/2017 - Version 0.7.11

Release changes:

* Disabling kazari in DocsLayout and kazari styles if kazari is not required ([#242](https://github.com/47deg/sbt-microsites/pull/242))
* Change GA script parsing mode for smoother navigation jumps ([#243](https://github.com/47deg/sbt-microsites/pull/243))
* Releases 0.7.11 ([#244](https://github.com/47deg/sbt-microsites/pull/244))


## 11/28/2017 - Version 0.7.10

Release changes:

* Option disable kazari ([#241](https://github.com/47deg/sbt-microsites/pull/241))


## 11/16/2017 - Version 0.7.9

Release changes:

* Upgrades sbt org policies and scala version for Travis ([#240](https://github.com/47deg/sbt-microsites/pull/240))


## 11/16/2017 - Version 0.7.8

Release changes:

* Bump-ups versions ([#238](https://github.com/47deg/sbt-microsites/pull/238))


## 11/15/2017 - Version 0.7.7

Release changes:

* Upgrades to sbt-org-policies 0.8.12 and releases 0.7.7 ([#236](https://github.com/47deg/sbt-microsites/pull/236))
* Fixes travis release filter ([#237](https://github.com/47deg/sbt-microsites/pull/237))


## 11/06/2017 - Version 0.7.6

Release changes:

* Updates missed tut 0.5.6 for sbt 0.13.x ([#234](https://github.com/47deg/sbt-microsites/pull/234))


## 11/06/2017 - Version 0.7.5

Release changes:

* Updates tut 0.6.2 ([#233](https://github.com/47deg/sbt-microsites/pull/233))


## 10/09/2017 - Version 0.7.4

Release changes:

* Add support for custom Jekyll plugins ([#231](https://github.com/47deg/sbt-microsites/pull/231))
* Releases 0.7.4 with #231 ([#232](https://github.com/47deg/sbt-microsites/pull/232))


## 10/03/2017 - Version 0.7.3

Release changes:

* Bump sbt-org-policies ([#230](https://github.com/47deg/sbt-microsites/pull/230))


## 10/02/2017 - Version 0.7.2

Release changes:

* Fixes publishMicrosite dynamic task ([#229](https://github.com/47deg/sbt-microsites/pull/229))


## 09/20/2017 - Version 0.7.1

Release changes:

* Enables sbt-microsites through latest version of sbt-org-policies ([#226](https://github.com/47deg/sbt-microsites/pull/226))
* Upgrades sbt-org-policies ([#227](https://github.com/47deg/sbt-microsites/pull/227))
* Releases 0.7.1 ([#228](https://github.com/47deg/sbt-microsites/pull/228))


## 09/18/2017 - Version 0.7.0

Release changes:

* Port plugin to sbt {1.0.x, 0.13.16} ([#221](https://github.com/47deg/sbt-microsites/pull/221))
* Releases 0.7.0 - Cross-built for sbt 0.13 and sbt 1.0 ([#225](https://github.com/47deg/sbt-microsites/pull/225))


## 09/06/2017 - Version 0.6.3

Release changes:

* Update CHANGELOG.md ([#217](https://github.com/47deg/sbt-microsites/pull/217))
* Including console output in kazari ([#219](https://github.com/47deg/sbt-microsites/pull/219))
* User configurable footer ([#220](https://github.com/47deg/sbt-microsites/pull/220))


## 08/31/2017 - Version 0.6.2

Release changes:

* Added Freestyle and Libra ([#201](https://github.com/47deg/sbt-microsites/pull/201))
* Enriched version of `includeFilter` ([#203](https://github.com/47deg/sbt-microsites/pull/203))
* Move extra-md files to a target dir, instead of tut-sources ([#200](https://github.com/47deg/sbt-microsites/pull/200))
* Review css jumbotron_pattern retina ([#205](https://github.com/47deg/sbt-microsites/pull/205))
* Add airframe as an sbt-microsites example ([#207](https://github.com/47deg/sbt-microsites/pull/207))
* Remove unnecessary dependencies ([#209](https://github.com/47deg/sbt-microsites/pull/209))
* Added clarifying instructions for MacOS X ([#212](https://github.com/47deg/sbt-microsites/pull/212))
* Bump sbt org policies. Release 0.6.2 ([#215](https://github.com/47deg/sbt-microsites/pull/215))
* Update .gitignore ([#216](https://github.com/47deg/sbt-microsites/pull/216))


## 06/12/2017 - Version 0.6.1

Release changes:

* Bumps org-policies version to 0.5.0 ([#189](https://github.com/47deg/sbt-microsites/pull/189))
* Gitter Link Settings ([#197](https://github.com/47deg/sbt-microsites/pull/197))
* Releases sbt-microsites 0.6.1 ([#199](https://github.com/47deg/sbt-microsites/pull/199))


## 05/19/2017 - Version 0.6.0

Release changes:

* Update CHANGELOG.md with 0.5.7 release ([#177](https://github.com/47deg/sbt-microsites/pull/177))
* Bumps sbt-org-policies version ([#180](https://github.com/47deg/sbt-microsites/pull/180))
* Embed gitter channel ([#183](https://github.com/47deg/sbt-microsites/pull/183))
* Fixes metatags and solarized-dark issues ([#187](https://github.com/47deg/sbt-microsites/pull/187))
* Releases sbt-microsites 0.6.0 ([#188](https://github.com/47deg/sbt-microsites/pull/188))


## 04/25/2017 - Version 0.5.7

Release changes:

* Bumps sbt-org-policies plugin ([#176](https://github.com/47deg/sbt-microsites/pull/176))

## 04/25/2017 - Version 0.5.6

Release changes:

* Auto-update sbt-microsite version in doc files ([#174](https://github.com/47deg/sbt-microsites/pull/174))
* Adds task wrapper for the publishMicrosite Command ([#175](https://github.com/47deg/sbt-microsites/pull/175))


## 04/25/2017 - Version 0.5.5

Release changes:

* Add Google Analytics settings ([#172](https://github.com/47deg/sbt-microsites/pull/172))
* Upgrades sbt-org-policies and uses new method signature for fetching files ([#173](https://github.com/47deg/sbt-microsites/pull/173))


## 04/21/2017 - Version 0.5.4

Release changes:

* Use GitHub api to publish ghpages ([#169](https://github.com/47deg/sbt-microsites/pull/169))
* Fixes artifact publish ([#170](https://github.com/47deg/sbt-microsites/pull/170))


## 04/11/2017 - Version 0.5.3

Release changes:

* Migrates Old Changelog Format to the New format ([#167](https://github.com/47deg/sbt-microsites/pull/167))
* Fixes Kazari Build ([#168](https://github.com/47deg/sbt-microsites/pull/168)) 


## 04/07/2017 - Version 0.5.2

Release changes:

* Add new line to the footer ([#159](https://github.com/47deg/sbt-microsites/pull/159))
* Upgrades sbt-org-policies and its configuration ([#160](https://github.com/47deg/sbt-microsites/pull/160))
* Add new val "micrositeOrganisationHomepage"  ([#162](https://github.com/47deg/sbt-microsites/pull/162))
* Releases 0.5.2 ([#163](https://github.com/47deg/sbt-microsites/pull/163)) 

## 12/14/2016 - Version 0.4.0

* Provides the ability to import JS and CSS files from CDN services.
* Improves the `micrositeExtraMdFiles` setting. Now, you don't need to modify your markdown that are located in different path from tut. You can specify the layout version and the rest of the meta-properties when configuring this sbt setting. If your were using the previous version 0.3.x, and you are upgrading to 0.4.0, you need to rewrite this setting.
* Brings a new feature to make possible the total customization of the `_config.yml` Jekyll file, throug the new `micrositeConfigYaml` setting.
* Other minor improvements.

## 11/15/2016 - Version 0.3.3

* Adds feature to allow users to include custom layouts and partial layouts to their microsites, by specifying their location with the micrositeExternalLayoutsDirectory and micrositeExternalIncludesDirectory settings of the plugin.
* Fixes images loading for retina displays


## 11/09/2016 - Version 0.3.2

* Fixes https://github.com/47deg/sbt-microsites/issues/82: Scala 2.12 supported. Thanks @julien-truffaut!

## 11/07/2016 - Version 0.3.1

* Fixes https://github.com/47deg/sbt-microsites/issues/84: Allow custom javascript in generated microsite
* Fixes https://github.com/47deg/sbt-microsites/issues/70: Scala Code is not properly Highlighted
* Fixes https://github.com/47deg/sbt-microsites/issues/76: Improve table style. Thanks @jvican !

## 10/26/2016 - Version 0.3.0

 * Menu Configuration:

The docs Layout menu (https://47deg.github.io/sbt-microsites/docs/layouts.html) configuration, now it's slightly different respect to version `0.2.x`. In the previous version, it wasn't necessary to specify the docs folder where the different documents were placed.

For instance:

```
options:
  - title: Getting Started
    url: index.html
    section: intro

  - title: Configuring the Microsite
    url: settings.html
```

Internally the plugin considered that the links above were relative to the `micrositeDocumentationUrl` setting.

However, in the new version: `0.3.0`, you have to specify the inner folder in every case. For example:

```
options:
  - title: Getting Started
    url: docs/index.html
    section: intro

  - title: Configuring the Microsite
    url: docs/settings.html
```

In summary, now all the links are relative to the site base URL.

* Ability to create complex menus like this:

```
  - title: Type Classes
    url: typeclasses.html
    menu_type: typeclasses
    menu_section: typeclasses
```

`section` has been replaced by `menu_section`.

In addition, a new field `menu_type` property has been included in order to define different menus inside the `menu.yml` file.