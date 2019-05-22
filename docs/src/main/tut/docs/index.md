---
layout: docs
title: Getting Started
---

# Prerequisites

* [sbt](http://www.scala-sbt.org/) 0.13.8+
* [jekyll](https://jekyllrb.com/) 3.2.1+

Here are a few hints for local and travis environments to satisfy the `jekyll` requirement.

## Local Environment

Depending on your platform, you might do this with:

```bash
yum install jekyll

apt-get install jekyll

gem install jekyll
```

*Note*: On MacOS X, `/usr/bin/gem` will install an incompatible version of `jekyll`. It is suggested that you use [Homebrew](https://brew.sh/) to install `ruby` (which provides `gem`) before running `gem install jekyll`.

## Continuous Integration - Travis

If you have [Travis](https://travis-ci.org/) enabled for your project, you might have to tweak parts of your `.travis.yml` file:

If you're working on a Scala project (`language: scala`), you need to add the bundle gems vendor path in the `PATH` environment variable:

```bash
before_install:
 - export PATH=${PATH}:./vendor/bundle
```

This is needed in order to install and use the `jekyll` gem from other parts of your travis descriptor file. Once we have the `/vendor/bundle` path in the Travis `PATH` env variable, we have to install the gem in the `install` travis section:

```bash
install:
  - rvm use 2.3.0 --install --fuzzy
  - gem update --system
  - gem install sass
  - gem install jekyll -v 3.2.1
```

# Set it up in your Project

To begin, add the following lines to the `project/plugins.sbt` file within your project or the sbt module where you want to use the `sbt-microsites` plugin. Depending on the version, you might want to use:

Latest release:

[comment]: # (Start Replace)

```bash
addSbtPlugin("com.47deg"  % "sbt-microsites" % "0.9.1")
```

[comment]: # (End Replace)

Finally, to enable the plugin, add this to your `build.sbt` file:
```bash
enablePlugins(MicrositesPlugin)
```

# Write your documentation `.md` files

Your docs can be placed wherever you want in your project since there are sbt settings to point to the sources of your microsite, however the plugin expects to find the sources where both [**tut**](https://github.com/tpolecat/tut) and [**mdoc**](https://github.com/scalameta/mdoc) have defined by default. They are:

- Tut: `src/main/tut/`
- mdoc: `docs/`