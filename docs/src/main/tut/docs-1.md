---
layout: docs
title: Getting Started
section: docs
---

# Prerequisites

* [sbt](http://www.scala-sbt.org/) 0.13.8+
* [jekyll](https://jekyllrb.com/)

To satisfy the `jekyll` prerequisite, here a couple of hints for local and travis environments.

# Local Environment

Depending on your platform, you might do this with:

```bash
yum install jekyll

apt-get install jekyll

gem install jekyll
```

# Continuous Integration - Travis

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

# Setup

First of all, add the following lines to the `project/plugins.sbt` file, within your project or sbt module where you want to use the `sbt-microsites` plugin. Depending on the version:

Latest release:
```
addSbtPlugin("com.fortysevendeg"  % "sbt-microsites" % "0.2.0")
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
