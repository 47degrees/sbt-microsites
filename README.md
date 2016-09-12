#sbt-microsites

## Prerequisites

* Install [Jekyll](https://jekyllrb.com/), locally, depending on your platform, you might do this with:

```bash
yum install jekyll

apt-get install jekyll

gem install jekyll
```

## Install

Add plugin in `project/plugins.sbt`:
```
addSbtPlugin("com.fortysevendeg"  % "sbt-microsites" % "1.0.1-SNAPSHOT")
```

Enable the plugin in `build.sbt`:
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

- `micrositeExtratMdFiles` setting could be handy if you want to include additional markdown files in your site, and these files are not located in the same place of your `tut` directory. By default, the setting is set up as a empty map. You could override it, in this way:
```
micrositeExtratMdFiles := Map(file("README.md") -> "index.md", file("CONTRIBUTING.md") -> "contributing.md")
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



