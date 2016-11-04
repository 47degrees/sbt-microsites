# Kazari

Kazari is a JavaScript plugin developed on **Scala.Js** that allows to turn your project's existing documentation into interactive Scala worksheets. Basically it decorates your code snippets with extra functionality that allows users to run them in a [remote Scala evaluator](https://github.com/scala-exercises/evaluator) and edit it, by just including the script and some CSS files to your site.

# Installation

In order to use Kazari, you need to include the main `kazari.js` script in your site. Kazari is still in a WIP state so we haven't officially distributed it yet, but you can generate development versions following the steps you'll find in the "Building the plugin" section below. You'll also need to include one of the two Kazari CSS stylesheets (`style-dark.css` or `style-light.css`, depending on your color scheme preferences) which you will find bundled with our main JS script in our future releases, and in the resources folder of the project (`sbt-microsites/js/src/main/resources`).

To allow users to edit code interactively, Kazari relies on a Javascript code editor called [CodeMirror](http://codemirror.net). CodeMirror comes integrated with the scripts you add to your documentation, but you'll need to include the following CSS files to your site:

* `codemirror.css`, the main stylesheet used by CodeMirror.
* Theme CSS: CodeMirror can be configured with different themes, as you'll see in just a minute. In order for the themes to work, you'll need to provide their associated stylesheet too (i.e.: `monokai.css` for the `Monokai` theme).

Those files can be downloaded from the [CodeMirror official site](http://codemirror.net/doc/releases.html). Note that Kazari is compatible with CodeMirror version **5.19.0**.

Once you get all your scripts and stylesheets ready, you just need to include them in your project like this:

```html
<body>

<script type="text/javascript" src="kazari.js"></script>
<link rel="stylesheet" href="style-dark.css">
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

# Using Kazari

Once you got your scripts and stylesheets in your documentation, Kazari will take all your Scala code snippets (all those under <code class="language-scala"> tags) and decorate them with two buttons. One of them allows users to **run** each code snippets, and the other one will show a modal window that lets users **edit** your examples so they can play with them. You just need to take into account a couple of things about this:

* Snippets of code will be run in an **incremental basis**. That is, we assume that your snippets are more or less related to each other. So for instance, when users run the third snippet in your code, the plugin will send the contents of the first three snippets to the evaluator. This way, if your code depends on previously declared imports/instanced values, you won't have to specify them on every part of your documentation.
* It's usual that you use part of your documentation to show code that's not meant to run (i.e.: sbt commands, execution results...). Those snippets of code can be **excluded** from decoration by including the `code-exclude` class in your tags (i.e: <code class="language-scala code-exclude" data-lang="scala">). This is an important thing to do, as those pieces of code will also be included in the following ones as we explained in the previous point, and could make the code unable to compile or not to behave as expected.
* If you're creating a documentation for a library, or for almost any kind of Scala project, it's more than probable that your code needs some **dependencies** to rely on. The remote Scala evaluator needs to know those in order to compile your code. Both dependencies and resolvers can be made visible to Kazari by using the following meta tags:

```html
<meta property="evaluator-dependencies" content="groupId_1;artifactId_1;version_1,groupId_2;artifactId_2;version_2,...">
<!-- i.e.: "com.fortysevendeg;fetch_2.11;0.3.0-SNAPSHOT,com.fortysevendeg;mvessel-android;0.1" -->
<meta property="evaluator-resolvers" content="resolverUrl_1,resolverUrl_2,resolverUrl_3,...">
<!-- i.e.: "https://oss.sonatype.org/content/repositories/snapshots,https://dl.bintray.com/content/sbt/sbt-plugin-releases" -->
```

# Building the plugin

Kazari is a **Scala.JS** application. Even if it's part of the `sbt-microsites` project, it exists in its own independent module called `js`. Before being able to build it, you'll need to include the CodeMirror scripts that Kazari relies on. You can download the CodeMirror release from their [official site](http://codemirror.net/doc/releases.html), noting that Kazari is compatible with version `5.19.0`. Once you download the package, you'll be able to find the needed scripts in the following locations:

* lib/codemirror.js
* mode/javascript.js

Put these in the `resources` folder inside the `js` project of `sbt-microsites` (`sbt-microsites/js/src/main/scala/resources`) and you're good to go! In order to generate the scripts, please follow the next steps while in a sbt session inside the `sbt-microsites` project:

```scala
sbt-microsites> project js

js> fullOptGenerate
```

The `fullOptGenerate` task will compile the project, then generate and optimize Kazari's scripts, and finally combine the resulting scripts into a single file. `fullOptGenerate` is a slow command (as it relies on the `fullOptJS` task from Scala.JS, which perform several optimizations), so if you're planning to contribute to Kazari, please use the `fastOptGenerate` task in your development process instead. It will produce a larger script, but in more suitable build times. You'll be able to find the generated script (`kazari.js`) in the `target/scala-2.11` folder in the `js` project.