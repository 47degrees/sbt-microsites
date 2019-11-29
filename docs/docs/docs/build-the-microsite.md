---
layout: docs
title: Build the Microsite
permalink: docs/build-the-microsite/
---

# Build the microsite

Once you have written your documents, you can build the microsite running this sbt task:

```bash
sbt> makeMicrosite
```

Internally, it'll sequentially run other tasks including its own, [`tut`](https://github.com/tpolecat/tut) or [`mdoc`](https://scalameta.org/mdoc/), and `makeSite` ([sbt-site](https://github.com/sbt/sbt-site)) tasks.


# Multiversion support

Starting on version `1.0.0` you can build microsites including different versions of your library. This is available for the `light` theme, and you need to have [`git`](https://git-scm.com/) installed and available in your system. Most probably you already have it, but in other case please check your system docs, or go to the [git website](https://git-scm.com/download/) on how to proceed.

The feature is based on the concept of [`git` tags](https://git-scm.com/book/en/Git-Basics-Tagging). If you have different tags on your library repository, and they include `sbt-microsites` as part of their builds, specify the versions you want to serve in a list with their tag names, through the setting `micrositeVersionList`:

```
micrositeVersionList := List("0.1.0", "0.2.0")
```

Then, run the following task:

```
sbt> makeMultiversionMicrosite
```

And your `home`, `homeFeatures`, and `page` layouts will include a selector pointing to those version specific microsites main sites on their top navbars.



# View the microsite in your browser

If you're running the microsite locally, you can follow these steps:

1. In a shell, navigate to the generated site directory in `target/site`.

2. Start Jekyll with `jekyll serve`. Bear in mind that depending on your `micrositeBaseUrl` setting, you might need to serve the site [setting the base url](https://jekyllrb.com/docs/configuration/options/#serve-command-options). Execute Jekyll appending that value `jekyll serve -b /yourbase_url`

3. Navigate to [http://localhost:4000/](http://localhost:4000/) or [http://localhost:4000/yourbase_url/](http://localhost:4000/yourbase_url/) in your browser, where `yourbase_url` depends on your own preferences (see `micrositeBaseUrl` setting). Note, if you haven't specified any `micrositeBaseUrl` setting, it'll be empty by default.

# Publish the microsite

From version [`0.5.4`](https://github.com/47deg/sbt-microsites/releases/tag/v0.5.4), you have two options to publish the site:
 * **sbt-ghpages**: This is the default method. It uses the sbt-git plugin and the local ssh keys for pushing the changes.
 * **github4s**: Avoids using local ssh keys, publishing the site through the GitHub HTTP API and [Github4s](https://github.com/47deg/github4s). By contrast, you need to specify a token.

Before publishing, a couple of requirements should be satisfied:

1. Initialize the **gh-pages** branch, you can follow the instructions defined in the [sbt-ghpages](https://github.com/sbt/sbt-ghpages/blob/master/README.md#initializing-the-gh-pages-branch) repository.
2. Define `micrositeGithubOwner` and `micrositeGithubRepo` settings and maybe the `micrositePushSiteWith` and `micrositeGithubRepo` settings.
You can see more details regarding this in the [Configuring the Microsite]({% link docs/settings.md %}) section.

Once both requirements are satisfied, you can just run:

```bash
sbt> publishMicrosite
```

And that's all. Behind the scenes, `makeMicrosite` and `pushSite` are invoked.

By default, the second task uses the [`sbt-ghpages` plugin](https://github.com/sbt/sbt-ghpages).

If you don't have any domain names pointing to your site, you can see your microsite at:

[https://username.github.io/your-microsite](https://username.github.io/your-microsite)

Enjoy!
