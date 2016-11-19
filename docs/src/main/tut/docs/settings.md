---
layout: docs
title: Configuring the Microsite
---

# Configuring the Microsite

The following are the `sbt` settings that you can use to make adjustments to your microsite regarding deployment, configuration, and appearance. Not all of these settings are mandatory, since most of them have default values, as we'll see briefly.

Before beginning to detail the settings, the **sbt-microsites** plugin will use regular sbt configurations from your `build.sbt` file. In order to set the microsite up in a minimal manner, all of the configurations are used as default values:
   
## Regular SBT Settings
   
- `name`: default value for the microsite name.
- `description`: value by default used for the microsite description.
- `organizationName`: used as the microsite author by default.
- `homepage`: used as the default microsite homepage.

However, you can override these default settings by using the ones provided by the plugin, which we will describe in detail in the next section. 

## Microsite SBT Settings

We tried to bring all of the parameters that are potentially needed to configure any microsite. If you think that something additional could be added, please let us know as we are open to suggestions and contributions.  

- `micrositeName`: the microsite name. As we mentioned previously, by default, it's taken from the sbt setting `name`. Sometimes, it isn't the default behavior so you can override it like this:

```
micrositeName := "Your Awesome Library Name"
```

- `micrositeDescription`: the microsite description. If you don't configure this setting, the value is taken from the sbt setting `description` as we saw earlier. For instance:

```
micrositeDescription := "This is the description of my Awesome Library"
```

- `micrositeBaseUrl`: this setting brings the ability to set up a site base URL for your microsite. It's empty by default. However, you might need something like this:

```
micrositeBaseUrl := "/yoursite"
```

In this case, your microsite would be placed on: http://yourdomain.io/yoursite.

- `micrositeDocumentationUrl`: you might need documentation pages in a specific section of your microsite. This setting allows you to personalize this URL to fit your needs. As the **sbt-microsites** plugin provides a specific layout to improve the look of your documentation, we strongly recommend having a look at the [layouts](layouts.html) section. It's empty by default. Hence your `Documentation` link won't appear in the microsite in that case.

```
micrositeDocumentationUrl := "/yoursite/docs"
```

Therefore, considering the example above, your microsite documentation would be placed on: http://yourdomain.io/yoursite/docs. Moreover, the layouts supplied by the plugin will provide a link in the upper-right area pointing to this URL (if it isn't empty).

- `micrositeAuthor`: the author of the microsite is taken from this sbt setting. However, if nothing is specified, the default value will be `organizationName`, but you can override it, as we can see in this example:

```
micrositeAuthor := "47 Degrees"
```

- `micrositeHomepage`: this particular setting is used for the homepage url, similar to the way we've seen in other properties. This link is used in the footer-left link in the microsite. By default, the value is taken from the sbt setting `homepage`, but you can override it:

```
micrositeHomepage := "http://www.47deg.com"
```

- `micrositeGithubOwner` and `micrositeGithubRepo`: in order to add links to the `GitHub` repo, `micrositeGithubOwner` and `micrositeGithubRepo` are required:

```
micrositeGithubOwner := "47deg"
micrositeGithubRepo := "sbt-microsites"
```

- `micrositeHighlightTheme`: by default, the theme of Highlight.js is [default](https://highlightjs.org/static/demo/), however, you can configure it to a different theme thanks to this setting:

```
micrositeHighlightTheme := "monokai"
```
[Available themes: https://cdnjs.com/libraries/highlight.js/](https://cdnjs.com/libraries/highlight.js/)

- `micrositeImgDirectory`: the plugin provides some basic images, but you can add new images to personalize the microsite. This is the property where you can specify where they will be placed. The images in this folder will be automatically copied by the plugin, and they will be placed together with the rest of the Jekyll resources. By default, its value is `(resourceDirectory in Compile).value / "microsite" / "img"` but you can override it, for instance:

```
micrositeImgDirectory := (resourceDirectory in Compile).value / "site" / "images"
```

- `micrositeCssDirectory`: you can also override the styles through the `micrositeCssDirectory` setting, by using the same method. The css files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "css"` but you can override it like this:

```
micrositeCssDirectory := (resourceDirectory in Compile).value / "site" / "styles"
```

- `micrositeJsDirectory`: you can also introduce custom javascript files in the generated microsite through the `micrositeJsDirectory` setting, by using the same method. The javascript files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "js"` but you can override it like this:

```
micrositeJsDirectory := (resourceDirectory in Compile).value / "site" / "scripts"
```

There is a reserved filename that you cannot use in your personal microsite: `main.js`, which it's provided by the plugin.

- `micrositeExternalLayoutsDirectory`: you can also introduce custom html layouts in the generated microsite through the `micrositeExternalLayoutsDirectory` setting. The layout files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "layout"` but you can override it like this:

```
micrositeExternalLayoutsDirectory := (resourceDirectory in Compile).value / "site" / "layout"
```

These will be available to your pages by using the `layout` keyword in the YAML front matter block in each of your docs' markdown files. i.e. having included a `extra-layout.html` external layout file:

```
---
title: Foo Bar
layout: extra-layout
---
```

- `micrositeExternalIncludesDirectory`: you can also introduce custom html partial layouts in the generated microsite through the `micrositeExternalIncludesDirectory` setting. The layout files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "includes"` but you can override it like this:

```
micrositeExternalIncludesDirectory := (resourceDirectory in Compile).value / "site" / "includes"
```

These will be available to your pages by using `Jekyll`'s [include](https://jekyllrb.com/docs/templates/#includes) keyword in your layouts.

- `micrositeDataDirectory`: in addition, you can provide new data to your jekyll site through the `micrositeDataDirectory` setting. It's based on the idea of [Jekyll Data Files](https://jekyllrb.com/docs/datafiles/). It's important to keep in mind that if you are defining documentation in your microsite, you have to configure the menu through this setting. The default value is `(resourceDirectory in Compile).value / "microsite" / "data"` but you can override it like this:

```
micrositeDataDirectory := (resourceDirectory in Compile).value / "site" / "mydatafiles"
```

In the Documentation **Menu** case, as you can see in the [layouts](layouts.html) section, you need to create a file named as `menu.yml` under the `micrositeDataDirectory` setting.

- `micrositeExtraMdFiles`: this setting can be handy if you want to include additional markdown files in your site, and these files are not located in the same place in your `tut` directory. By default, the setting is set up as an empty map. You can override it, in this way:

```
micrositeExtraMdFiles := Map(file("README.md") -> "index.md", file("CONTRIBUTING.md") -> "contributing.md")
```

- `micrositePalette`: the default microsite style essentially uses eight colors. You can configure all of them, as seen below:

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
