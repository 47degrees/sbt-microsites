---
layout: docs
title: Configuring the Microsite
section: docs
weight : 2
---

# Configuring the Microsite

The following is the set of `sbt` settings that you can use to make some adjustments regarding the deployment, configuration and appearance of your microsite. Not all these settings are mandatory, since most of them have default values, as we'll see briefly.

Before starting to detail them, the **sbt-microsites** plugin will use some regular sbt configurations from your `build.sbt` file, in order to set the microsite up in a minimal way, all of them are used as default values:
   
## Regular SBT Settings
   
- `name`: default value for the microsite name.
- `description`: value by default used for the microsite description.
- `organizationName`: used as microsite author by default.
- `homepage`: used as default microsite homepage.

However, these default settings can be overridden by the following ones, provided by the plugin, keep reading the next section to know more. 

## Microsite SBT Settings

We tried to bring all the parameters that potentially are needed to configure any microsite. If you think that something additional could be added, please, we are open to suggestions and contributions :)

- `micrositeName`: the microsite name. As we said in the previous section, by default, it's taken from the sbt setting `name`. Sometimes, it isn't the default behaviour so you can override it in this way:

```
micrositeName := "Your Awesome Library Name"
```

- `micrositeDescription`: the microsite description. If you don't configure this setting, the value is taken from the sbt setting `description` as we saw beforehand. For instance:

```
micrositeDescription := "This is the description of my Awesome Library"
```

- `micrositeBaseUrl`: this setting brings the ability to setup a site base URL for your microsite. It's empty by default, however, you might need something like this:

```
micrositeBaseUrl := "/yoursite"
```

In this case, your microsite would be placed on: http://yourdomain.io/yoursite .

- `micrositeDocumentationUrl`: your microsite might need some documentation pages in an specific part of your microsite. This setting allows to personalize this URL to fit your needs. As **sbt-microsites** plugin provides an specific layout to have also a beautiful documentation, we strongly recommend to have a look to the [layouts](layouts.html) section. It's empty by default, hence your `Documentation` link won't appear in the microsite in that case.

```
micrositeDocumentationUrl := "/yoursite/docs"
```

Therefore, considering the example above, your microsite documentation would be placed on: http://yourdomain.io/yoursite/docs . Moreover, the layouts provided by the plugin will provide a link in the upper-right area pointing to this URL (if it isn't empty).

- `micrositeAuthor`: the author of the microsite is taken from this sbt setting. However, if nothing is specified, the default value will be `organizationName`, but you can override it, as we can see in this example:

```
micrositeAuthor := "47 Degrees"
```

- `micrositeHomepage`: in the same way as we are seeing for other properties, this particular setting is used for the homepage url. This link is used in the footer-left link in the microsite. By default, the value is taken from the sbt setting `homepage`, but you can override it:

```
micrositeHomepage := "http://www.47deg.com"
```

- `micrositeGithubOwner` and `micrositeGithubRepo`: in order to add links to the `GitHub` repo, `micrositeGithubOwner` and `micrositeGithubRepo` are required:

```
micrositeGithubOwner := "47deg"
micrositeGithubRepo := "sbt-microsites"
```

- `micrositeHighlightTheme`: by default, the theme of Highlight.js is [tomorrow](https://highlightjs.org/static/demo/), however, you could configure a different one thanks to this setting:

```
micrositeHighlightTheme := "monokai"
```
[Available themes: https://cdnjs.com/libraries/highlight.js/](https://cdnjs.com/libraries/highlight.js/)

- `micrositeImgDirectory`: the plugin provides some basic images, but you could add new images to personalize the microsite. This is the property where you can specify where they will be placed. The images in this folder will be automatically copied by the plugin, and they will be placed together with the rest of the jekyll resources. By default, its value is `(resourceDirectory in Compile).value / "microsite" / "img"` but you can override it, for instance:

```
micrositeImgDirectory := (resourceDirectory in Compile).value / "site" / "images"
```

- `micrositeCssDirectory`: in the same way, you could override the styles through the `micrositeCssDirectory` setting. The css files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "css"` but you can override it in this way:

```
micrositeCssDirectory := (resourceDirectory in Compile).value / "site" / "styles"
```

- `micrositeExtraMdFiles`: setting could be handy if you want to include additional markdown files in your site, and these files are not located in the same place of your `tut` directory. By default, the setting is set up as a empty map. You could override it, in this way:

```
micrositeExtraMdFiles := Map(file("README.md") -> "index.md", file("CONTRIBUTING.md") -> "contributing.md")
```

- `micrositePalette`: the default microsite style uses essentially 8 colors. All of them can be configured as below:

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
