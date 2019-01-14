---
layout: docs
title: Typechecked Snippets
---

# Typechecked Snippets

As has been mentioned a couple of times throughout this tutorial, this plugin supports the compilation of Scala code written in markdown files. This process is now being delegated on two different sbt plugins: [**tut**](https://github.com/tpolecat/tut) and [**mdoc**](https://github.com/scalameta/mdoc).

## Tut

Tut is the option by default, so no special action is required from your side, apart from following the guides defined in its [microsite](http://tpolecat.github.io/tut/). Some recommendations:

- Add your markdown files in this path by default: `src/main/tut/`.
- If you want to place your documments in a different path, just set the sbt property `tutSourceDirectory := your-path` being _your-path_ a `SettingKey[File]`.
- Mark the snippets that you want to compile, following this convention: **_```tut_** or any of its [modifiers](http://tpolecat.github.io/tut//modifiers.html)


## mdoc

This feature has been recently added and includes some amazing features that are [explained](https://scalameta.org/mdoc/) very well. Here is some advice for using this in your microsite:

- Add your markdown files in this path by default: `docs/`.
- Set this property accordingly: `micrositeCompilingDocsTool := mdoc`
- If you want to place your documments in a different path, just set the sbt property `mdocIn := your-path` being _your-path_ a `SettingKey[File]`.
- Mark the snippets that you want to compile, following this convention: **_```scala mdoc_** or any of its [modifiers](https://scalameta.org/mdoc/docs/modifiers.html)

## Migrating from tut to mdoc

You can migrate your microsite from _tut_ to _mdoc_ in 3 smooth steps:

- Set this property accordingly: `micrositeCompilingDocsTool := mdoc`
- Set the property `mdocIn := tutSourceDirectory`, thus we are setting the source for mdoc as it used to be for tut.
- Replace **_tut_** with **_scala mdoc_** in the snippets modifiers following this table of compatibilities

| tut                | mdoc                                                  |
| ------------------ | ----------------------------------------------------- |
| `:fail`            | `:fail` for compile error, `:crash` for runtime error |
| `:nofail`          | n/a                                                   |
| `:silent`          | `:silent`                                             |
| `:plain`           | n/a                                                   |
| `:invisible`       | `:invisible`                                          |
| `:book`            | can be removed, mdoc uses book mode by default        |
| `:evaluated`       | n/a                                                   |
| `:passthrough`     | `:passthrough`                                        |
| `:decorate(param)` | n/a                                                   |
| `:reset`           | `:reset`                                              |

There is also a [script](https://github.com/scalameta/mdoc/blob/master/bin/migrate-tut.sh) that does the replacement for you.
