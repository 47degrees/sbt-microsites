# Changelog

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
