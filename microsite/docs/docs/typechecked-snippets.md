---
layout: docs
title: Typechecked Snippets
permalink: docs/typechecked-snippets/
---

# Typechecked Snippets

As has been mentioned a couple of times throughout this tutorial, this plugin supports the compilation of Scala code written in markdown files. This process is now being delegated on [**mdoc**](https://github.com/scalameta/mdoc).

## mdoc

`mdoc` is the option by default, so no special action is required from your side. See the scalameta site for more information [here](https://scalameta.org/mdoc/). Here is some advice for using this in your microsite:

- Add your markdown files in this path by default: `docs/`.
- If you want to place your documents in a different path, just set the sbt property `mdocIn := your-path` being _your-path_ a `SettingKey[File]`.
- Mark the snippets that you want to compile, following this convention: **_```scala mdoc_** or any of its [modifiers](https://scalameta.org/mdoc/docs/modifiers.html)

## Migrating from tut to mdoc

If your project is still based on `tut`, you can migrate it from _tut_ to _mdoc_ can follow this [migration guide](https://scalameta.org/mdoc/docs/tut.html).

There is also a [script](https://github.com/scalameta/mdoc/blob/5c732bc48eb88b8a416e5c61e945eac6d410b27b/bin/migrate-tut.sh) that does the replacement for you.
