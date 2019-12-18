---
layout: docs
title: Getting Started
permalink: docs/getting-started/
---

# Prerequisites

* [sbt](https://www.scala-sbt.org/) 1.3.4+
* [jekyll](https://jekyllrb.com/) 4.0.0+

Here are a few hints for local and travis environments to satisfy the `jekyll` requirement.

## Local Environment

Depending on your platform, you might do this with:

```bash
yum install jekyll

apt-get install jekyll

gem install jekyll
```

*Note*: On MacOS X, `/usr/bin/gem` could install an incompatible version of `jekyll`. It is suggested that you use [Homebrew](https://brew.sh/) to install `ruby` (which provides `gem`) before running `gem install jekyll`. You can also manage your Ruby installation through [rvm](https://rvm.io/).

## Continuous Integration - Travis

If you have [Travis](https://travis-ci.org/) enabled for your project, you can install the gem in the Travis `install` section:

```bash
install:
  - rvm use 2.6.5 --install --fuzzy
  - gem install jekyll -v 4
```

# Set it up in your Project

To begin, add the following lines to the `project/plugins.sbt` file within your project or the sbt module where you want to use the `sbt-microsites` plugin. Depending on the version, you might want to use:

Latest release:

[comment]: # (Start Replace)

```bash
addSbtPlugin("com.47deg"  % "sbt-microsites" % "1.0.2")
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
