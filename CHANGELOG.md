# Changelog

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