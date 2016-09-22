---
layout: home
technologies:
 - first: ["Scala", "sbt-microsites plugin is completely written in Scala"]
 - second: ["SBT", "sbt-microsites plugin uses SBT and other sbt plugins to generate microsites easily"]
 - third: ["Jekyll", "Jekyll allows to transform plain text into static websites and blogs."]
---

# sbt-microsites

**sbt-microsites** is a SBT plugin that facilitates the creation of fancy microsites for your projects, with minimal tweaks.

## What is a microsite?

A microsite is an instance of Jekyll, ready to publish a static web page for your new library. Some of the benefits of having these auto-generated web pages are:

- You can write documentation easily in markdown format.
- Templates, layouts, styles and other resources will be able through the plugin at compile time
- You don't have to deal with the styling.


## Credits

This plugin is based in other awesome sbt plugins to make it possible, so basically, integrates everything in some basic steps to automatically create and publish the microsite to [GitHub Pages](https://pages.github.com/).

The plugin provides some basic free styles, css, and image resources by default, everything based [Bootstrap](http://getbootstrap.com/) framework. If you want to personalize the color palette, styles and images you can do it in your project side in a easy way as you can see in the [documentation](docs/).

In order to achieve the microsite creation, it uses directly these plugins and libraries:

* [tut-plugin](https://github.com/tpolecat/tut)
* [sbt-site](https://github.com/sbt/sbt-site)
* [sbt-ghpages](https://github.com/sbt/sbt-ghpages)
* [scalatags](https://github.com/lihaoyi/scalatags)

Additionally it depends on other useful libraries and plugins like:

* [sbt-scalafmt](https://github.com/olafurpg/scalafmt)
* [sbt-pgp](https://github.com/sbt/sbt-pgp)
* [sbt-header](https://github.com/sbt/sbt-header)
* [sbt-native-packager](https://github.com/sbt/sbt-native-packager)
