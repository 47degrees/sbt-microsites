---
layout: docs
title: Intro
permalink: docs/
---

# sbt-microsites

**sbt-microsites** is an SBT plugin that facilitates the creation of fancy microsites for your projects, with minimal tweaks.

## What is a microsite?

A microsite is an instance of Jekyll that's ready to publish a static web page for your new library. Some of the benefits of having these auto-generated web pages are:

- You can write documentation easily in markdown format.
- Templates, layouts, styles and other resources will be available through the plugin at compile time.
- You don't have to deal with the styling.


## Credits

This plugin is based on and utilizes other awesome sbt plugins to make it possible. It integrates everything in a few basic steps to automatically create and publish the microsite to [GitHub Pages](https://pages.github.com/).

The plugin provides basic free styles, css, and image resources by default. Everything is based on the [Bootstrap](https://getbootstrap.com/) framework. If you want to personalize the color palette, styles, and images for your project, you can easily do so by viewing the steps in the [documentation]({% link docs/getting-started.md %}).

In order to create microsites, this plugin directly uses the following plugins and libraries:

* [sbt-mdoc](https://github.com/scalameta/mdoc)
* [sbt-site](https://github.com/sbt/sbt-site)
* [sbt-ghpages](https://github.com/sbt/sbt-ghpages)
* [scalatags](https://github.com/lihaoyi/scalatags)

Additionally, it depends on other useful libraries and plugins like:

* [sbt-scalafmt](https://github.com/olafurpg/scalafmt)
* [sbt-pgp](https://github.com/sbt/sbt-pgp)
* [sbt-header](https://github.com/sbt/sbt-header)
* [GitHub4s](https://github.com/47deg/github4s)
