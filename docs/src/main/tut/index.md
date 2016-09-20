---
layout: home
technologies:
 - scala: ["Scala", "sbt-microsites plugin is completely written in Scala"]
 - sbt: ["SBT", "sbt-microsites plugin uses SBT and other sbt plugins to generate microsites easily"]
 - jekyll: ["Jekyll", "Jekyll allows to transform plain text into static websites and blogs."]
---

[![Build Status](https://travis-ci.org/47deg/sbt-microsites.svg?branch=master)](https://travis-ci.org/47deg/sbt-microsites) [![Join the chat at https://gitter.im/47deg/sbt-microsites](https://badges.gitter.im/47deg/sbt-microsites.svg)](https://gitter.im/47deg/sbt-microsites?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# sbt-microsites

**sbt-microsites** is a sbt plugin tool that facilitates the creation of fancy microsites for your projects.

It's based in other awesome sbt plugins to make it posible, so basically, integrates everything in some basic steps to automatically create and publish the microsite to [GitHub Pages](https://pages.github.com/).

The plugin provides some basic free styles, css, and image resources by default, everything based [Bootstrap](http://getbootstrap.com/) framework. If you want to personalize color palette, styles and images you can do it in your project side in a easy way as you can see in the [documentation](https://47deg.github.io/sbt-microsites/docs.html).

In order to achieve the microsite creation, it uses directly these plugins and libraries:

* [tut-plugin](https://github.com/tpolecat/tut)
* [sbt-site](https://github.com/sbt/sbt-site)
* [sbt-ghpages](https://github.com/sbt/sbt-ghpages)
* [scalatags](https://github.com/lihaoyi/scalatags)

Additionally it's supported for other useful libraries and plugins like:

* [sbt-native-packager](https://github.com/sbt/sbt-native-packager)
* [sbt-scalafmt](https://github.com/olafurpg/scalafmt)
* [sbt-pgp](https://github.com/sbt/sbt-pgp)
* [sbt-header](https://github.com/sbt/sbt-header)

## Prerequisites

* [sbt](http://www.scala-sbt.org/) 0.13.8+
* [jekyll](https://jekyllrb.com/)

To satisfy the `jekyll` prerequisite, here a couple of hints for local and travis environments.

### Local Environment

Depending on your platform, you might do this with:

```bash
yum install jekyll

apt-get install jekyll

gem install jekyll
```

### Continuous Integration - Travis

If you have enabled [Travis](https://travis-ci.org/) for your project, you might have to tweak some parts of your `.travis.yml` file:

Potentially, your project is a scala project (`language: scala`), therefore you need to add the bundle gems vendro path in the `PATH` environment variable:

```
before_install:
 - export PATH=${PATH}:./vendor/bundle
```

This is needed in order to install and be able to use `jekyll` gem from other parts of your project. We have to do it in the Travis `install` section:

```
install:
 - ...
 - gem install jekyll -v 2.5
```

## Setup

First of all, add the following lines to the `project/plugins.sbt` file, within your project or sbt module where you want to use the `sbt-microsites` plugin. Depending on the version:

Latest release:
```
addSbtPlugin("com.fortysevendeg"  % "sbt-microsites" % "0.1.0")
```

Latest snapshot built from the `master` branch code:

```tut:evaluated
println("""resolvers += Resolver.sonatypeRepo("snapshots")""")
println(s"""addSbtPlugin("com.fortysevendeg"  % "sbt-microsites" % "${microsites.BuildInfo.version}"""")
```

Finally, to enable the plugin, add this to your `build.sbt` file:
```
enablePlugins(MicrositesPlugin)
```

## Build the microsite

Once you have written down your documents you can build the microste running this sbt task:

```
sbt> makeMicrosite
```

Internally, sequentially it'll run other tasks, among theirs, [`tut`](https://github.com/tpolecat/tut) and `makeSite` ([sbt-site](https://github.com/sbt/sbt-site)) tasks.

## View the microsite in your browser

1. In a shell, navigate to the generated site directory in `target/site`

2. Start jekyll with `jekyll serve`

3. Navigate to http://localhost:4000/yourbase_url/ in your browser, where `yourbase_url` depends on your own preferences (see `micrositeBaseUrl` setting).

## Publish the microsite

Before publishing, a couple of requirements should be satisfied:

1. Initializing the gh-pages branch, you can follow the instructions defined in the [sbt-ghpages](https://goo.gl/G0Ffv0) repository
2. Define `micrositeGithubOwner` and `micrositeGithubRepo` settings, you can see more details about them later on in this document

Once both requirements are satisfied, you can just run:

```
sbt> publishMicrosite
```

## Examples

Apart from the microsite for this plugin that you can finde in the `docs` sbt module in this repository, a quite complete example is located on https://github.com/47deg/dummy-sbt-microsite.

Please see the [documentation](https://47deg.github.io/sbt-microsites/docs.html) for more information.
