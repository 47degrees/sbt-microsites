[![Build Status](https://travis-ci.org/47deg/sbt-microsites.svg?branch=master)](https://travis-ci.org/47deg/sbt-microsites) [![Join the chat at https://gitter.im/47deg/sbt-microsites](https://badges.gitter.im/47deg/sbt-microsites.svg)](https://gitter.im/47deg/sbt-microsites?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# sbt-microsites

**sbt-microsites** is an SBT plugin that facilitates the creation of fancy microsites for your projects, with minimal tweaks.

## What is a microsite?

A microsite is an instance of Jekyll, ready to publish a static web page for your new library. Some of the benefits of having these auto-generated web pages are:

- You can write documentation easily in markdown format.
- Templates, layouts, styles, and other resources will be able through the plugin at compile time.
- You don't have to deal with the styling.

# Installation

To Begin, add the following lines to the `project/plugins.sbt` file within your project or sbt module where you want to use the `sbt-microsites` plugin.

```
addSbtPlugin("com.fortysevendeg"  % "sbt-microsites" % "0.3.1")
```

Finally, to enable the plugin, add this to your `build.sbt` file:
```
enablePlugins(MicrositesPlugin)
```

We recommend taking a look at the `Getting Started` section at the [documentation](https://47deg.github.io/sbt-microsites/docs/) since there are some prerequisites that are needed to build your microsite.

# Documentation

Microsites comes with a range of options to customize and configure your project's site. Please visit the [documentation](https://47deg.github.io/sbt-microsites/docs/) section for more information.

# sbt-microsites in the wild

Many Scala projects use sbt-microsites to display documentation and project notes. If you wish to add your library here please consider a PR to include it in the list below.

★ | ★ | ★
--- | --- | ---
![Cats](http://typelevel.org/cats/img/navbar_brand.png) | [**Cats**](http://typelevel.org/cats/) | Lightweight, modular, and extensible library for functional programming
![Tuco](https://tpolecat.github.io/tuco/img/navbar_brand.png) | [**Tuco**](https://tpolecat.github.io/tuco/) | Tuco is a reasonable telnet server for Scala
![Atto](https://tpolecat.github.io/atto/img/navbar_brand.png) | [**Atto**](https://tpolecat.github.io/atto/) | Everyday parsers.



