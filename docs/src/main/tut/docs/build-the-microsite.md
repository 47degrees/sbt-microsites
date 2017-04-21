---
layout: docs
title: Build the Microsite
---

# Build the microsite

Once you have written your documents, you can build the microsite running this sbt task:

```bash
sbt> makeMicrosite
```

Internally, it'll sequentially run other tasks including its own, [`tut`](https://github.com/tpolecat/tut), and `makeSite` ([sbt-site](https://github.com/sbt/sbt-site)) tasks.

# View the microsite in your browser

If you're running the microsite locally, you can follow these steps:

1. In a shell, navigate to the generated site directory in `target/site`.

2. Start Jekyll with `jekyll serve`.

3. Navigate to [http://localhost:4000/yourbase_url/](http://localhost:4000/yourbase_url/) in your browser, where `yourbase_url` depends on your own preferences (see `micrositeBaseUrl` setting). Note, if you haven't specified any `micrositeBaseUrl` setting, it'll be empty by default so you can navigate to the site following this url [http://localhost:4000_url/](http://localhost:4000/).  

# Publish the microsite

From version [`0.5.4`](https://github.com/47deg/sbt-microsites/releases/tag/v0.5.4), you have two options to publish the site:
 * **sbt-ghpages**: This is the default method. It uses the sbt-git plugin and the local ssh keys for pushing the changes.
 * **github4s**: Avoids using local ssh keys, publishing the site through the GitHub HTTP API and [Github4s](https://github.com/47deg/github4s). By contrast, you need to specify a token.

Before publishing, a couple of requirements should be satisfied:

1. Initialize the **gh-pages** branch, you can follow the instructions defined in the [sbt-ghpages](https://goo.gl/G0Ffv0) repository.
2. Define `micrositeGithubOwner` and `micrositeGithubRepo` settings and maybe the `micrositePushSiteWith` and `micrositeGithubRepo` settings.
You can see more details regarding this in the [Configuring the Microsite](settings.html) section.

Once both requirements are satisfied, you can just run:

```bash
sbt> publishMicrosite
```

And that's all. Behind the scenes, `makeMicrosite` and `pushSite` are invoked. 

By default, the second task uses the [`sbt-ghpages` plugin](https://github.com/sbt/sbt-ghpages).

If you don't have any domain names pointing to your site, you can see your microsite at:

[http://username.github.io/your-microsite](http://username.github.io/your-microsite)

Enjoy!