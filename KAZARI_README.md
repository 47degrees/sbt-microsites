# Kazari

Kazari is a script developed on **Scala.Js** that allows to turn your project's existing documentation into interactive Scala worksheets. Basically it decorates your code snippets with extra functionality that allows users to run them in a [remote Scala evaluator](https://github.com/scala-exercises/evaluator) and edit it, by just including the script and some CSS files to your site.

# Installation

Kazari comes integrated with `sbt-microsites` (version >0.4.1), and all sites built with it are able to use it right out the bat. To do so you need to add certain classes to the code snippets in your library documentation `tut` files. i.e.:

    ```tut:silent:decorate(.kazari-id-1)
    type UserId = Int
    case class User(id: UserId, username: String)
    ```
The `decorate` tag in `tut` allows you to add CSS classes at will to your code snippets. Kazari treats code snippets differently depending on the classes you assign to them:

* **Single snippets**: code snippets that should be evaluated alone should be decorated with the **kazari-single** CSS class.
* **Inter-related snippets**: code snippets that depend on others should be grouped with the **kazari-id-???** CSS class. The *???* can be anything (a name, number, etc...). Those snippets will be evaluated together sequentially. i.e.: if you hit "Run" on the first one, only that snippet will be evaluated. If you click on the second, the first and the second will be evaluated, and so on...

Once you have added these CSS classes to your code, just build your microsite as always and you'll see a bar with some buttons under each code block. The **Run** button will send a request to the configured remote Scala evaluator to evaluate that code snippet (or together with those before if it's part of a sequence of snippets). The **Edit** button will launch a modal window that will allow you to play with the code and run your own changes in the evaluator (or removing by using the **Reset** button).

`sbt-microsites` includes sbt settings to configure Kazari to suit your needs. Here's a list of those settings:

* **micrositeKazariEvaluatorUrl**: URL of the remote Scala Evaluator to be used by Kazari. Defaults to the [remote evaluator](https://github.com/scala-exercises/evaluator) used in [Scala Exercises](https://github.com/scala-exercises/scala-exercises).
* **micrositeKazariEvaluatorToken**: remote Scala Evaluator token to be used by Kazari. Check out the [README of the evaluator](https://github.com/scala-exercises/evaluator) for information on how to generate it.
* **micrositeKazariGithubToken**: optional GitHub token to be used by Kazari. Required for Kazari to perform certain actions (i.e. save Gists), which are still experimental.
* **micrositeKazariCodeMirrorTheme**: optional CodeMirror theme to be used by Kazari in its modal editor.
* **micrositeKazariDependencies**: optional list of dependencies needed to compile the code to be evaluated by Kazari (set of groupId, artifactId, and versionId).
* **micrositeKazariResolvers**: optional list of resolver urls needed for the provided dependencies to be fetched by Kazari.

Kazari will try to match its color scheme to the Microsite's highlight color theme. If the default colors don't quite match or you want to change them, we provide a few class for you to customize:

* **compiler-kazari-background**: background color of the button bar.
* **compiler-kazari-border**: foreground color of the button borders.
* **compiler-kazari-color**: foreground color of the button texts and icons.

# Using Kazari without sbt-microsites

Kazari comes integrated with `sbt-microsites` (and in fact is a part of the project), but you don't need to create your site with the plugin in order to use it.

To use Kazari in an existing documentation, you need to include the main `kazari.js` script in your site. You'll also need to include the Kazari CSS stylesheet (`kazari-style.css`) which you will find in the resources folder of the project (`sbt-microsites/kazari/src/main/resources`).

To allow users to edit code interactively, Kazari relies on a Javascript code editor called [CodeMirror](http://codemirror.net). CodeMirror comes integrated with the scripts you add to your documentation, but you'll need to include the following CSS files to your site:

* `codemirror.css`, the main stylesheet used by CodeMirror.
* Theme CSS: CodeMirror can be configured with different themes. In order for the themes to work, you'll need to provide their associated stylesheet too (i.e.: `monokai.css` for the `monokai` theme, set by the `micrositeKazariCodeMirrorTheme` sbt setting in `sbt-microsites`).

Those files can be downloaded from the [CodeMirror official site](http://codemirror.net/doc/releases.html). Note that Kazari is built with CodeMirror version **5.19.0**.

Once you get all your scripts and stylesheets ready, you just need to include them in your project like this:

```html
<body>

<script type="text/javascript" src="kazari.js"></script>
<link rel="stylesheet" href="kazari-style.css">
<link rel="stylesheet" href="codemirror.css">
<link rel="stylesheet" href="monokai.css">

<script type="text/javascript">
	$(document).ready(function() {
		kazari.KazariPlugin().decorateCode("http://localhost:8080/eval", "remote_evaluator_auth_token", "github_auth_token", "monokai")
	})	
</script>

<!-- Website contents -->
```

After including the scripts and stylesheets, we wait for the DOM to be ready, and we make a call to `decorateCode`. This function is the one handling the decoration process, and it takes several parameters:

* **Remote Scala evaluator URL**: it contains the URL of the [remote Scala evaluator](https://github.com/scala-exercises/evaluator) we want to connect to. In this case we're using a local instance by executing the project in our machine.
* **Auth token**: a security token needed to make requests to the evaluator. You can generate your own by following the steps in the "Authentication" section of the [evaluator documentation](https://github.com/scala-exercises/evaluator).
* **Github auth token**: a valid GitHub auth token with at least the following scopes: "user", "gist". This is required for Kazari to save edited snippets as Gists.
* **CodeMirror theme**: name of the CodeMirror theme to use when showing the interactive edit window.

Kazari expects your code to be set in `code` tags under `div` tags. These `div` tags should include the CSS class we aforementioned in the **Using Kazari without sbt-microsites** section.

Also note that if you're creating a documentation for a library, or for almost any kind of Scala project, it's more than probable that your code needs some **dependencies** to rely on. The remote Scala evaluator needs to know those in order to compile your code. Both dependencies and resolvers can be made visible to Kazari by using the following meta tags:

```html
<meta property="kazari-dependencies" content="groupId_1;artifactId_1;version_1,groupId_2;artifactId_2;version_2,...">
<!-- i.e.: "com.fortysevendeg;fetch_2.11;0.3.0-SNAPSHOT,com.fortysevendeg;mvessel-android;0.1" -->
<meta property="kazari-resolvers" content="resolverUrl_1,resolverUrl_2,resolverUrl_3,...">
<!-- i.e.: "https://oss.sonatype.org/content/repositories/snapshots,https://dl.bintray.com/content/sbt/sbt-plugin-releases" -->
```

# Building the plugin

Kazari is a **Scala.JS** application. Even if it's part of the `sbt-microsites` project, it exists in its own independent module called `kazari`. Before being able to build it, you'll need to include the CodeMirror scripts that Kazari relies on. You can download the CodeMirror release from their [official site](http://codemirror.net/doc/releases.html), noting that Kazari is built with version `5.19.0`. Once you download the package, you'll be able to find the needed scripts in the following locations:

* lib/codemirror.js
* mode/clike.js

Put these in the `resources` folder inside the `kazari` project of `sbt-microsites` (`sbt-microsites/kazari/src/main/scala/resources`) and you're good to go! In order to generate the scripts, please follow the next steps while in a sbt session inside the `sbt-microsites` project:

```scala
sbt-microsites> project kazari

js> fullOptGenerate
```

The `fullOptGenerate` task will compile the project, then generate and optimize Kazari's scripts, and finally combine the resulting scripts into a single file. `fullOptGenerate` is a slow command (as it relies on the `fullOptJS` task from Scala.JS, which perform several optimizations), so if you're planning to contribute to Kazari, please use the `fastOptGenerate` task in your development process instead. It will produce a larger script, but in more suitable build times. You'll be able to find the generated script (`kazari.js`) in the `target/scala-2.11` folder in the `kazari` project. This file contains both the javascript dependencies and the compiled Kazari code. If you want to use both files separately, use the `kazari-opt.js` and `kazari-jsdeps.js` instead.