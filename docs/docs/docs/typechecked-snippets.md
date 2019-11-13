---
layout: docs
title: Typechecked Snippets
permalink: docs/typechecked-snippets/
---

# Typechecked Snippets

As has been mentioned a couple of times throughout this tutorial, this plugin supports the compilation of Scala code written in markdown files. This process is now being delegated on two different sbt plugins: [**tut**](https://github.com/tpolecat/tut) and [**mdoc**](https://github.com/scalameta/mdoc).

## Tut

[tut](https://github.com/tpolecat/tut) has been deprecated, please switch to mdoc (see next section). If for some reason you want to keep using it, here there are some recommendations:

- Set this property accordingly: `micrositeCompilingDocsTool := WithTut`.
- Add your markdown files in this path by default: `src/main/tut/`.
- If you want to place your documments in a different path, just set the sbt property `tutSourceDirectory := your-path` being _your-path_ a `SettingKey[File]`.
- Mark the snippets that you want to compile, following this convention: **_```tut_** or any of its [modifiers](https://tpolecat.github.io/tut//modifiers.html)


## mdoc

`mdoc` is the option by default, so no special action is required from your side. See the scalameta site for more information [here](https://scalameta.org/mdoc/). Here is some advice for using this in your microsite:

- Add your markdown files in this path by default: `docs/`.
- By default, the setting `micrositeCompilingDocsTool` will be `WithMdoc`, so no action would be required.
- If you want to place your documents in a different path, just set the sbt property `mdocIn := your-path` being _your-path_ a `SettingKey[File]`.
- Mark the snippets that you want to compile, following this convention: **_```scala mdoc_** or any of its [modifiers](https://scalameta.org/mdoc/docs/modifiers.html)

## Migrating from tut to mdoc

You can migrate your microsite from _tut_ to _mdoc_ in 3 smooth steps:

- Set this property accordingly: `micrositeCompilingDocsTool := WithMdoc`
- Set the property `mdocIn := tutSourceDirectory`, thus we are setting the source for mdoc as it used to be for tut.
- Replace **_tut_** with **_mdoc_** in the snippets modifiers following this [migration guide](https://scalameta.org/mdoc/docs/tut.html).

There is also a [script](https://github.com/scalameta/mdoc/blob/5c732bc48eb88b8a416e5c61e945eac6d410b27b/bin/migrate-tut.sh) that does the replacement for you.
