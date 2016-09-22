---
layout: docs
title: Build the Microsite
section: docs
weight : 3
---

# Build the microsite

Once you have written down your documents you can build the microsite running this sbt task:

```
sbt> makeMicrosite
```

Internally, sequentially it'll run other tasks, among theirs, [`tut`](https://github.com/tpolecat/tut) and `makeSite` ([sbt-site](https://github.com/sbt/sbt-site)) tasks.

# View the microsite in your browser

1. In a shell, navigate to the generated site directory in `target/site`

2. Start jekyll with `jekyll serve`

3. Navigate to [http://localhost:4000/yourbase_url/](http://localhost:4000/yourbase_url/) in your browser, where `yourbase_url` depends on your own preferences (see `micrositeBaseUrl` setting).

# Publish the microsite

Before publishing, a couple of requirements should be satisfied:

1. Initializing the gh-pages branch, you can follow the instructions defined in the [sbt-ghpages](https://goo.gl/G0Ffv0) repository
2. Define `micrositeGithubOwner` and `micrositeGithubRepo` settings, you can see more details about them in the Microsite Settings section.

Once both requirements are satisfied, you can just run:

```
sbt> publishMicrosite
```
