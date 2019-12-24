
[comment]: # (Start Badges)

[![Build Status](https://travis-ci.org/47deg/sbt-microsites.svg?branch=master)](https://travis-ci.org/47deg/sbt-microsites) [![Maven Central](https://img.shields.io/badge/maven%20central-1.0.2-green.svg)](https://repo1.maven.org/maven2/com/47deg/sbt-microsites_2.12_1.0) [![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/47deg/sbt-microsites/master/LICENSE) [![Join the chat at https://gitter.im/47deg/sbt-microsites](https://badges.gitter.im/47deg/sbt-microsites.svg)](https://gitter.im/47deg/sbt-microsites?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![GitHub Issues](https://img.shields.io/github/issues/47deg/sbt-microsites.svg)](https://github.com/47deg/sbt-microsites/issues)

[comment]: # (End Badges)

# sbt-microsites

**sbt-microsites** is an SBT plugin that facilitates the creation of fancy microsites for your projects, with minimal tweaks.

## What is a microsite?

A microsite is an instance of Jekyll, ready to publish a static web page for your new library. Some of the benefits of having these auto-generated web pages are:

- You can write documentation easily in markdown format.
- Templates, layouts, styles, and other resources will be available through the plugin at compile time.
- You don't have to deal with the styling.

# Installation

To begin, add the following lines to the `project/plugins.sbt` file within your project or sbt module where you want to use the `sbt-microsites` plugin.

[comment]: # (Start Replace)

```
addSbtPlugin("com.47deg"  % "sbt-microsites" % "1.0.2")
```

[comment]: # (End Replace)

Finally, to enable the plugin, add this to your `build.sbt` file:
```
enablePlugins(MicrositesPlugin)
```

We recommend taking a look at the `Getting Started` section in the [documentation](https://47deg.github.io/sbt-microsites/docs/) since there are some prerequisites that are needed to build your microsite.

# Documentation

Microsites comes with a range of options to customize and configure your project's site. Please visit the [documentation](https://47deg.github.io/sbt-microsites/docs/) section for more information.

# sbt-microsites in the wild

Many Scala projects, libraries, and applications use sbt-microsites to display documentation and project notes. If you wish to add your library here, please consider a PR to include it in the list below.

★ | ★ | ★
--- | --- | ---
![Cats](http://typelevel.org/cats/img/navbar_brand.png) | [**Cats**](http://typelevel.org/cats/) | A library of typeclasses for functional programming, such as functors, monads, and arrows. 
![Scalaz](https://scalaz.github.io/8/img/navbar_brand.png) | [**Scalaz**](https://scalaz.github.io/7/) | A library of typeclasses for functional programming, such as functors, monads, and arrows. 
![Cats-Effect](http://typelevel.org/cats/img/navbar_brand.png) | [**Cats-Effect**](https://github.com/typelevel/cats-effect) | Extension of Cats for general effect types, with asynchronicity and concurrency
![FS2](http://fs2.io/img/navbar_brand.png) | [**fs2**](http://fs2.io/guide.html) | Functional Streams for Scala (FS2), based on `cats` and `cats-effect` 
![PureConfig](https://pureconfig.github.io/img/navbar_brand.png) | [**pureconfig**](https://pureconfig.github.io/) | A library for loading configuration files
![Ciris](https://cir.is/img/navbar_brand.png) | [**ciris**](https://cir.is/) | A _configuration as code_ library for compile-time safe configurations
![Tuco](https://tpolecat.github.io/tuco/img/navbar_brand.png) | [**Tuco**](https://tpolecat.github.io/tuco/) | Tuco is a reasonable telnet server for Scala
![Atto](https://tpolecat.github.io/atto/img/navbar_brand.png) | [**Atto**](https://tpolecat.github.io/atto/) | Everyday parsers.
![Typelevel Scala](http://typelevel.org/scala/img/navbar_brand.png) | [**Typelevel Scala**](http://typelevel.org/scala/) | Our fork of the Scala compiler
![Monocle](https://raw.githubusercontent.com/julien-truffaut/Monocle/master/image/black_icons/navbar_brand.png) | [**Monocle**](http://julien-truffaut.github.io/Monocle/) | Optics library for Scala
![ScalaCache](https://cb372.github.io/scalacache/img/navbar_brand.png) | [**ScalaCache**](https://cb372.github.io/scalacache/) | A facade for popular cache implementations, such as Caffeine, Redis, or Memcached
![scalacheck-toolbox](https://47deg.github.io/scalacheck-toolbox/img/navbar_brand.png) | [**scalacheck-toolbox**](https://47deg.github.io/scalacheck-toolbox/) | Generating sensible data with ScalaCheck
![Algebird](https://twitter.github.io/algebird/img/navbar_brand.png) | [**Algebird**](https://twitter.github.io/algebird/) | Algebraic typeclasses and data structures for big data
![Scalding](https://twitter.github.io/scalding/img/navbar_brand.png) | [**Scalding**](https://twitter.github.io/scalding/) | Scala API for Hadoop and Cascading
![Finch](https://finagle.github.io/finch/img/navbar_brand.png) | [**Finch**](https://finagle.github.io/finch/) | A combinator API over the Finagle HTTP services
![fetch](https://47deg.github.io/fetch/img/navbar_brand.png) | [**fetch**](https://47deg.github.io/fetch/) | Simple & Efficient data access for Scala and Scala.js
![github4s](https://47deg.github.io/github4s/img/navbar_brand.png) | [**github4s**](https://47deg.github.io/github4s/) | A GitHub API wrapper written in Scala
![hammock](https://pepegar.github.io/hammock/img/navbar_brand.png) | [**hammock**](https://pepegar.github.io/hammock/) | A purely functional HTTP client for Scala
![cron4s](https://alonsodomin.github.io/cron4s/img/navbar_brand.png) | [**cron4s**](https://alonsodomin.github.io/cron4s) | A CRON expression parser and AST for Scala
![freestyle](http://frees.io/img/navbar_brand.png) | [**freestyle**](http://frees.io/) | A cohesive & pragmatic framework of FP centric Scala libraries
![libra](https://to-ithaca.github.io/libra/img/navbar_brand.png) | [**libra**](https://to-ithaca.github.io/libra) |A dimensional analysis library based on dependent types
![Scanamo](http://www.scanamo.org/img/navbar_brand.png) | [**Scanamo**](http://www.scanamo.org) | Simpler DynamoDB access for Scala
![Mocked Streams](https://mockedstreams.madewithtea.com/img/navbar_brand.png) | [**Mocked Streams**](https://mockedstreams.madewithtea.com) | Scala DSL for Unit-Testing Kafka Streams Topologies
![sbt-kubeyml](http://sbt-kubeyml.vaslabs.org/img/navbar_brand.png) | [**sbt-kubeyml**](http://sbt-kubeyml.vaslabs.org) | Typesafe Kubernetes manifests to deploy Scala applications 

[comment]: # (Start Copyright)
# Copyright

sbt-microsites is designed and developed by 47 Degrees

Copyright (C) 2016-2019 47 Degrees. <http://47deg.com>

[comment]: # (End Copyright)
