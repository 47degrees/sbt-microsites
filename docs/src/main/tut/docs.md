---
layout: docs
title: Introduction
section: docs
---

# Introduction

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
println(s"""addSbtPlugin("com.fortysevendeg"  % "sbt-microsites" % "${microsites.BuildInfo.version}")""")
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

## Microsite settings

- The name of the microsite is taken from the sbt setting `name`, but you can override it:
```
micrositeName := "Dummy"
```

- The description of the microsite is taken from the sbt setting `description`, but you can override it:
```
micrositeDescription := "This is my Dummy description"
```

- Site base URL could be configured through the sbt setting `micrositeBaseUrl` (empty by default):
```
micrositeBaseUrl := "/yoursite"
```

- Documentation URL could be configured through the sbt setting `micrositeDocumentationUrl` (empty by default):
```
micrositeDocumentationUrl := "/docs.html"
```

- The author of the microsite is taken from the sbt setting `organizationName`, but you can override it:
```
micrositeAuthor := "47 Degrees"
```

- The homepage of the microsite is taken from the sbt setting `homepage`, but you can override it:
```
micrositeHomepage := "http://www.mywebpage.com"
```

- In order to add links to GitHub repo, `micrositeGithubOwner` and `micrositeGithubRepo` are required:
```
micrositeGithubOwner := "47deg"
micrositeGithubRepo := "sbt-microsites"
```

- The theme of Highlight.js is [tomorrow](https://highlightjs.org/static/demo/) by default, however you set other theme:
```
micrositeHighlightTheme := "monokai"
```
[Available themes: https://cdnjs.com/libraries/highlight.js/](https://cdnjs.com/libraries/highlight.js/)

- You could add new images to personalize the microsite, you can specify them through the `micrositeImgDirectory` setting. The images in that folder will be automatically copied by the plugin and they will be placed together with the rest of the jekyll resources. By default, its value is `(resourceDirectory in Compile).value / "microsite" / "img"` but you can override it, for instance:
```
micrositeImgDirectory := (resourceDirectory in Compile).value / "site" / "images"
```

- At the same time, you could override the styles through the `micrositeCssDirectory` setting. The css files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "css"` but you can override it in this way:
```
micrositeImgDirectory := (resourceDirectory in Compile).value / "site" / "styles"
```

- `micrositeExtraMdFiles` setting could be handy if you want to include additional markdown files in your site, and these files are not located in the same place of your `tut` directory. By default, the setting is set up as a empty map. You could override it, in this way:
```
micrositeExtraMdFiles := Map(file("README.md") -> "index.md", file("CONTRIBUTING.md") -> "contributing.md")
```

- Style uses essentially 8 colors which palette can be set through the setting `micrositePalette` as below:
```
micrositePalette := Map(
        "brand-primary"     -> "#E05236",
        "brand-secondary"   -> "#3F3242",
        "brand-tertiary"    -> "#2D232F",
        "gray-dark"         -> "#453E46",
        "gray"              -> "#837F84",
        "gray-light"        -> "#E3E2E3",
        "gray-lighter"      -> "#F4F3F4",
        "white-color"       -> "#FFFFFF")
```



