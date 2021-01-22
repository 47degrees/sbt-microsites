---
layout: docs
title: Configuring the Microsite
permalink: docs/settings/
---

# Configuring the Microsite

The following are the `sbt` settings that you can use to make adjustments to your microsite regarding deployment, configuration, and appearance. Not all of these settings are mandatory, since most of them have default values, as we'll see briefly.

Before you begin to detail the settings, the **sbt-microsites** plugin will use regular sbt configurations from your `build.sbt` file. In order to set up the microsite with minimal effort, all of the configurations are used as default values:

## Regular SBT Settings

- `name`: Default value for the microsite name.
- `description`: Value by default used for the microsite description.
- `organizationName`: Used as the microsite author by default.
- `organizationHomepage`: Used as the default microsite homepage.

However, you can override these default settings by using the ones provided by the plugin, which we will describe in detail in the next section.

## Microsite SBT Settings

We tried to provide all of the parameters that are potentially needed to configure any microsite. If you think that something needs adding, please let us know! We're open to suggestions and contributions.

- `micrositeName`: The microsite name. As we mentioned previously, by default, it's taken from the sbt setting `name`. Sometimes, it isn't the default behavior, so you can override it like this:

```scala
micrositeName := "Your Awesome Library Name"
```

- `micrositeDescription`: The microsite description. If you don't configure this setting, the value is taken from the sbt setting `description` as we saw earlier. For instance:

```scala
micrositeDescription := "This is the description of my Awesome Library"
```

- `micrositeUrl`: The URL prefix of your site. This setting is necessary if you need to show a poster image `{micrositeUrl}{micrositeBaseUrl}/img/poster.png` of your site on Twitter. See also [Twitter Cards](https://cards-dev.twitter.com/validator) for more details.

```scala
micrositeUrl := "https://yourdomain.io"
```

- `micrositeBaseUrl`: This setting brings the ability to set up a site base URL for your microsite. It's empty by default. However, you might need something like this:

```scala
micrositeBaseUrl := "/yoursite"
```

In this case, your microsite would be placed on: https://yourdomain.io/yoursite.

- `micrositeDocumentationUrl`: You might need documentation pages in a specific section of your microsite. This setting allows you to personalize this URL to fit your needs. As the **sbt-microsites** plugin provides a specific layout to improve the look of your documentation, we strongly recommend having a look at the [layouts]({% link docs/layouts.md %}) section. It's empty by default. Hence your `Documentation` link won't appear in the microsite in that case.

```scala
micrositeDocumentationUrl := "/yoursite/docs"
```

Therefore, considering the example above, your microsite documentation would be placed on: https://yourdomain.io/yoursite/docs. Moreover, the layouts supplied by the plugin will provide a link in the upper-right area pointing to this URL (if it isn't empty).


- `micrositeDocumentationLabelDescription`: The default label description for the `micrositeDocumentationUrl` link in the homepage is `Documentation`. You can change it through this setting.

```scala
micrositeDocumentationLabelDescription := "Documentation"
```

- `micrositeAuthor`: The author of the microsite is taken from this sbt setting. However, if nothing is specified, the default value will be `organizationName`, but you can override it, as we can see in this example:

```scala
micrositeAuthor := "47 Degrees"
```

- `micrositeHomepage`: Used for the homepage URL, similar to the way we've seen in other properties. This link is used in the footer-left link in the microsite. By default, the value is taken from the sbt setting `homepage`.

```scala
micrositeHomepage := "https://47deg.github.io/sbt-microsites/"
```

- `micrositeOrganizationHomepage`: This particular setting is used for organizing the homepage URL, similar to the way we've seen in other properties. This link is used in the footer-left link in the microsite. By default, the value is taken from the sbt setting `organizationHomepage`. If you don´t provide any value for that setting, it will take it from the sbt setting `homepage`:

```scala
micrositeOrganizationHomepage := "https://www.47deg.com"
```

- `micrositeTwitter`: This setting is used for the Twitter integration. This generates a meta that Twitter uses in its [Twitter Cards](https://cards-dev.twitter.com/validator).

```scala
micrositeTwitter := "@sbt-microsites"
```

- `micrositeTwitterCreator`: This particular setting is used for the Twitter integration. This generates a meta that Twitter uses in its [Twitter Cards](https://cards-dev.twitter.com/validator).

```scala
micrositeTwitterCreator := "@47deg"
```


- `micrositeGithubOwner` and `micrositeGithubRepo`: Used to add links to the `GitHub` repo. It's also needed for publishing the site when `github4s` is chosen (see `micrositePushSiteWith` setting). Defaults to the information found in the 'origin' Git remote, if such remote exists; otherwise they must be set like:

```scala
micrositeGithubOwner := "47deg"
micrositeGithubRepo := "sbt-microsites"
```

- `micrositeGithubToken`: Used for publishing the site when `github4s` is enabled. A [token with repo scope](https://github.com/settings/tokens/new?scopes=repo&description=sbt-microsites) is needed. None by default, but you can override it in this way:

```scala
micrositeGithubToken := getEnvVar("GITHUB_TOKEN")
```

- `micrositePushSiteWith`: Determines how the site will be pushed. It accepts two values:

* `GHPagesPlugin`: The default value. The plugin will use the `sbt-ghpages` plugin for publishing the site.
* `GitHub4s`: The [GitHub4s](https://github.com/47deg/github4s) will be used for publishing the site. By now, only GitHub is supported. You need to specify a value for the `micrositeGithubToken` in order to use this publishing method.

```scala
micrositePushSiteWith := GitHub4s
```

- `micrositeGitHostingService` and `micrositeGitHostingUrl`: Used to specify a hosting service other than `GitHub`. If you are using a privately hosted GitHub instance, you can set the `micrositeGitHostingUrl` to override the default github.com and repo name configuration.

```scala
micrositeGitHostingService := GitLab
micrositeGitHostingUrl := "https://gitlab.com/gitlab-org/gitlab-ce"
```

- `micrositeAnalyticsToken`: Property id of Google Analytics. This is empty by default.

```scala
micrositeAnalyticsToken := 'UA-XXXXX-Y'
```

- `micrositeGithubLinks`: This setting defines whether to show/hide GitHub links for stars and forks in docs layout. By default, it is enabled.

```scala
micrositeGithubLinks := true
```

- `micrositeGitterChannel`: This setting is used to enabled the Gitter sidecar Channel functionality, and it's enabled by default. The chat room is taken from `micrositeGithubOwner` and `micrositeGithubRepo`.

```scala
micrositeGitterChannel := true
```

- `micrositeGitterChannelUrl`: This setting is used to set a custom URL to Gitter sidecar Channel. By default, this is `micrositeGithubOwner/micrositeGithubRepo`.

```scala
micrositeGitterChannelUrl := "47deg/sbt-microsites"
```

- `micrositeShareOnSocial`: This setting defines whether to show/hide the social media buttons in docs layout. By default, it is enabled.

```scala
micrositeShareOnSocial := true
```

- `micrositeHighlightTheme`: By default, the theme of Highlight.js is [vs](https://highlightjs.org/static/demo/). However, you can configure it to a different theme thanks to this setting:

```scala
micrositeHighlightTheme := "monokai"
```
[Available themes: https://cdnjs.com/libraries/highlight.js/](https://cdnjs.com/libraries/highlight.js/)

- `micrositeHighlightLanguages`: By default, Highlight.js is configured to support syntax highlighting for `java`, `scala`, and `bash`. You can add additional languages:

```scala
micrositeHighlightLanguages ++= Seq("protobuf", "thrift")
```

Then, use it as follows:

~~~
```protobuf
message MyMessage {
   optional int32 i = 1;
}
```
~~~

[Available languages: https://cdnjs.com/libraries/highlight.js/](https://cdnjs.com/libraries/highlight.js/)

- `micrositeImgDirectory`: The plugin provides some basic images, but you can add new images to personalize the microsite. This is the property where you can specify where they will be placed. The images in this folder will be automatically copied by the plugin, and they will be placed together with the rest of the Jekyll resources. By default, its value is `(resourceDirectory in Compile).value / "microsite" / "img"`, but you can override it. For instance:

```scala
micrositeImgDirectory := (resourceDirectory in Compile).value / "site" / "images"
```

- `micrositeCssDirectory`: You can also override the styles through the `micrositeCssDirectory` setting by using the same method. The css files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "css"`, but you can override it like this:

```scala
micrositeCssDirectory := (resourceDirectory in Compile).value / "site" / "styles"
```

- `micrositeSassDirectory`: If you want to use SCSS files, you might want to override the place to put the partials. This can be done through the `micrositeSassDirectory` setting. The main SCSS files need to go into the CSS directory, where they will be transformed into CSS files, and the partials will be loaded from this directory. The default value is `(resourceDirectory in Compile).value / "microsite" / "sass"`, but you can override it like this:

```scala
micrositeSassDirectory := (resourceDirectory in Compile).value / "site" / "partials"
```

- `micrositeJsDirectory`: You can also introduce custom javascript files in the generated microsite through the `micrositeJsDirectory` setting by using the same method. The javascript files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "js"`, but you can override it like this:

```scala
micrositeJsDirectory := (resourceDirectory in Compile).value / "site" / "scripts"
```

There is a reserved filename that you cannot use in your personal microsite: `main.js`, which is provided by the plugin.

- `micrositeCDNDirectives`: This setting provides the ability to include CDN imports (for js and css files) along the different layouts in this way:

```scala
micrositeCDNDirectives := CdnDirectives(
  jsList = List(
    "https://cdnjs.cloudflare.com/ajax/libs/ag-grid/7.0.2/ag-grid.min.js",
    "https://cdnjs.cloudflare.com/ajax/libs/ajaxify/6.6.0/ajaxify.min.js"
  ),
  cssList = List(
    "https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.css",
    "https://cdnjs.cloudflare.com/ajax/libs/cssgram/0.1.12/1977.min.css",
    "https://cdnjs.cloudflare.com/ajax/libs/cssgram/0.1.12/brooklyn.css"
  )
)
```

- `micrositeExternalLayoutsDirectory`: You can also introduce custom html layouts in the generated microsite through the `micrositeExternalLayoutsDirectory` setting. The layout files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "layouts"`, but you can override it like this:

```scala
micrositeExternalLayoutsDirectory := (resourceDirectory in Compile).value / "site" / "layouts"
```

These will be available to your pages by using the `layout` keyword in the YAML front matter block in each of your docs' markdown files (i.e., having included a `extra-layout.html` external layout file):

```markdown
---
title: Foo Bar
layout: extra-layout
---
```

- `micrositeExternalIncludesDirectory`: You can also introduce custom html partial layouts in the generated microsite through the `micrositeExternalIncludesDirectory` setting. The layout files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "includes"`, but you can override it like this:

```scala
micrositeExternalIncludesDirectory := (resourceDirectory in Compile).value / "site" / "includes"
```

These will be available to your pages by using `Jekyll`'s [include](https://jekyllrb.com/docs/templates/#includes) keyword in your layouts.

- `micrositeDataDirectory`: In addition, you can provide new data to your Jekyll site through the `micrositeDataDirectory` setting. It's based on the idea of [Jekyll Data Files](https://jekyllrb.com/docs/datafiles/). It's important to keep in mind that, if you are defining documentation in your microsite, you have to configure the menu through this setting. The default value is `(resourceDirectory in Compile).value / "microsite" / "data"`, but you can override it like this:

```scala
micrositeDataDirectory := (resourceDirectory in Compile).value / "site" / "mydatafiles"
```

In the Documentation **Menu** case, as you can see in the [layouts]({% link docs/layouts.md %}) section, you need to create a file named `menu.yml` under the `micrositeDataDirectory` setting.

- `micrositeStaticDirectory`: You can also provide a static directory to your Jekyll site through the `micrositeStaticDirectory` setting. It's based on the idea of [Jekyll Static Files](https://jekyllrb.com/docs/static-files/). The default value is `(resourceDirectory in Compile).value / "microsite" / "static"`, but you can override it like this:

```scala
micrositeStaticDirectory := (resourceDirectory in Compile).value / "site" / "mystaticfiles"
```

The directory will be copied as-is, but no process will be applied to any file on it. So the responsibility of loading/linking/using them on a layout is yours. Also, see the `includeFilter in makeSite` config setting for the allowed file types that will be copied.

- `micrositeExtraMdFiles`: This setting can be handy if you want to include additional markdown files in your site, and these files are not located in the same place in your `mdoc` directory. By default, the setting is set up as an empty map. You can override it in this way:

```scala
micrositeExtraMdFiles := Map(
  file("README.md") -> ExtraMdFileConfig(
    "readme.md",
    "home"
  ),
  file("CONSEQUAT.md") -> ExtraMdFileConfig(
    "consequat.md",
    "page",
    Map("title" -> "Consequat", "section" -> "consequat", "position" -> "5")
  )
)
```

Each file (the map key) can be related to a specific configuration through the `ExtraMdFileConfig` case class. This class allows you to specify three additional configurations:

1. The target file name. The plugin will copy the file, and it will put it in mdoc directory each time you build the microsite. Therefore, you might want to include this auto-copied file in the list of ignored files at the `.gitignore` file.
2. Jekyll `layout` property.
3. Other custom Jekyll properties that you might want to include in your document. A good point to highlight here is that through the `permalink` property you can control the place where `Jekyll` is going to move your extra file. The value of this property prevails over the folder where the file is copied.

- `micrositeExtraMdFilesOutput` this is an optional parameter when you are using `mdoc`, and refers to the microsite output location for extra-md files. Default is resourceManaged + `/jekyll/extra_md` although you can modify it.

- `micrositePluginsDirectory`: You can also introduce custom [Jekyll plugins](https://jekyllrb.com/docs/plugins/) in the generated microsite through the `micrositePluginsDirectory` setting. The plugin files in that folder will be automatically copied and imported by the plugin in your microsite. The default value is `(resourceDirectory in Compile).value / "microsite" / "plugins"`, but you can override it like this:

```scala
micrositePluginsDirectory := (resourceDirectory in Compile).value / "site" / "plugins"
```

- `micrositeTheme`: You can choose two different themes to generate your microsite. By default it will display the `light` theme but you have the option of choosing the classic `pattern` theme.

```scala
micrositeTheme := "pattern"
```
- `micrositePalette`: The default microsite style essentially uses three colors. You can configure all of them, as seen below.

Default palette used by the `light` theme:

```scala
micrositePalette := Map(
        "brand-primary"         -> "#013567",
        "brand-secondary"       -> "#009ADA",
        "white-color"           -> "#FFFFFF")
```

Palette used by the `pattern` theme:

```scala
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
- `micrositeFavicons`: List of filenames and sizes for the PNG/ICO files to be used as favicons for the generated site, located in the default image directory. The sizes should be described with a string (i.e.: \"16x16\"). If not provided, favicons with different sizes will be generated from the navbar_brand2x.png file for the `pattern` style and from the light_navbar_brand.png in case we are using the `light` style.

```scala
micrositeFavicons := Seq(MicrositeFavicon("favicon16x16.png", "16x16"), MicrositeFavicon("favicon32x32.png", "32x32"))
```

- `micrositeConfigYaml`: This setting brings the capability to customize the Jekyll `_config.yml` file in three different ways (not exclusive to each other):

1. Specifying a provided `_config.yml` as a part of your library resources.
2. Specifying a YAML string inline in the sbt configuration (you might want to consider the use of `stripMargin`).
3. Through custom liquid variables.

These three ways will be merged in order to generate the final and single `_config.yml` file.
This is possible thanks to the `ConfigYml` case class, which looks like the following:

```scala
case class ConfigYml(
      yamlCustomProperties: Map[String, Any] = Map.empty,
      yamlPath: Option[File] = None,
      yamlInline: String = ""
  )
```

Therefore, the next snippet represents an example that combines these three ways:

```scala
micrositeConfigYaml := ConfigYml(
  yamlCustomProperties = Map("org" -> "My Org"),
  yamlInline = """exclude: [package.json, grunt.js, Gruntfile.js, node_modules]
|""".stripMargin,
  yamlPath = Some((resourceDirectory in Compile).value / "microsite" / "myconfig.yml")
)
```

- `micrositeFooterText`: This setting allows the optional configuration of the second line in the footer. By default, it is set to `Some("""Website built with "Sbt-microsites © 2019 47 Degrees""")`. **This string is passed in unsanitized to the templating engine.**. If this setting is set to `None`, the second line is not displayed.

```scala
micrositeFooterText := Some("<b>Bob</b> the <i>Builder</i>")
```

- `micrositeEditButtonText`: This setting allows the optional inclusion and configuration of an edit button on pages
with the docs layout. The button links to the given path of the page in its repository.
By default, it is set to `None` and not visible. To enable, set the MicrositeEditButton with text
for the button and the basePath for the file. The basePath is comprised of the file URL excluding the top-level
repository URL, and should include the dynamic property `{% raw %}{{page.path}}{% endraw %}` that will be generated for each page when Jekyll
compiles the site.  **The strings are passed in unsanitized to the templating engine.**

```scala
{% raw %}micrositeEditButton := Some(MicrositeEditButton("Improve this Page", "/edit/master/docs/{{ page.path }}")){% endraw %}
```

- `includeFilter in makeSite`: Restrict the type of files that are included during the microsite build. The default value is `"*.html" | "*.css" | "*.png" | "*.jpg" | "*.jpeg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.webm" | "*.ico" | "CNAME" | "*.yml" | "*.svg" | "*.json"` but you can override it like this:

```scala
includeFilter in makeSite := "*.csv" | "*.pdf" | *.html" | "*.css" | "*.png" | "*.jpg" | "*.jpeg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.webm" | "*.ico" | "CNAME" | "*.yml" | "*.svg" | "*.json"
```
