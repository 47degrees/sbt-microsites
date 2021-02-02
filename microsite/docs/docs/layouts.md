---
layout: docs
title: Layouts
permalink: docs/layouts/
---

# Layouts

Currently, the sbt-microsites plugin includes five different layouts:

- There are two different options for the home section:
    - `home`: The landing page--the public face of your library or project.
    - `homeFeatures`: Another option for the landing page. Choosing this option, you could highlight some of the special features your library offers. (This layout is not available for the classic pattern style)
- `docs` (Optional): The page where the documentation for your library should be included. You are most likely seeing the `Documentation` page of this repo right now. It's optional, depending on the `micrositeDocumentationUrl` setting. Take a look at the [Configuring the Microsite]({% link docs/settings.md %}) section for an in-depth explanation.
- `page` (Optional): Similar to `home`, but reducing the jumbotron layer and taking into account the submenu (jumbotron and other concepts related to style are explained in the [Customize]({% link docs/customize.md %}) section).
- Menu Partial: This abstract layout reads all the files in your project that fit a set of requirements, and sets up a menu under the jumbotron image. We'll see more details on this later.

## Home Layout

The `home` layout is usually related to the `index.md` file. In this document, you can put all the markdown content that's related to the landing page.

For instance:

```markdown
---
layout: home
title:  "Home"
section: "home"
technologies:
 - first:  ["Scala", "sbt-microsites plugin is completely written in Scala"]
 - second: ["SBT", "sbt-microsites plugin uses SBT and other sbt plugins to generate microsites easily"]
 - third:  ["Jekyll", "Jekyll allows for the transformation of plain text into static websites and blogs."]
---
```

The technology list is optional. These three technologies will be shown as a sub-footer in your home page. These technologies are identified for the set of keys (`first`, `second`, `third`). You can specify to include all of them or none of them. There are no other choices at this time.
This list is only available in the `pattern` theme, but, for the `light` theme, we have included the `homeFeatures` layout where you can highlight similar characteristics of your library.


## HomeFeatures Layout

Like the `home` layout, `homeFeatures` is related to the `index.md` file.
This layout is designed to show a main title beside a big logo, and then a series of features that can be highlighted from the library.

For instance:

```markdown
---
layout: homeFeatures
features:
  - first: ["Patterns", "Solutions to recurrent problems, in a purely Functional Programming manner.", "patterns"]
  - second: ["Typeclasses", "Enable ad-hoc polymorphism with protocols like Functor, Applicative, Monad, and many others.", "typeclasses"]
  - third: ["Data Types", "Take advantage of numerous data types based on algebraic properties.", "datatypes"]
---
```

This is basically all you´ll need to add to your `index.md` file using the `homeFeatures` layout.
You can add up to three different features, and sbt-microsites will dispose them after the masthead layer. Every feature will be accompained by an icon that can be overridden, as explained in the [Customize]({% link docs/customize.md %}) section.
These features are identified for the set of keys (`first`, `second`, `third`).
The last value of each feature is optional and will be used to set the link to the docs section. For instance, the first feature in the example: `patterns`, this will serve to compose `docs/patterns` link to the docs section.

## Docs Layout

All the markdown files that contain this `layout` and `section` as `docs` will be grouped in the Documentation page. Each markdown file with these characteristics will be shown on a collapsible left menu, with one item per existing file. From this menu, you can easily navigate all the docs.

To be able to access the documentation, you have to configure `micrositeDocumentationUrl` setting.

In order to change the default label description for the `micrositeDocumentationUrl` (the default value is `Documentation`), you have to change the `micrositeDocumentationLabelDescription`.

As an example, you can look at the sbt-microsites documentation at [GitHub](https://github.com/47degrees/sbt-microsites/tree/master/microsite/docs). We have several documentation files:

- `build-the-microsite.md`
- `customize.md`
- `getting-started.md`
- `index.md`
- ...

All these files contain as a header, something similar to this:

```markdown
---
layout: docs
title: <Document Title>
---
```

`<Document Title>` will be used as a menu item name on the left.

### How to setup the Docs Menu

Looking at the [Configuring the Microsite]({% link docs/settings.md %}) section, in the directory configured under the `micrositeDataDirectory` setting, you need to create a new file named `menu.yml`. This `YAML` file will be accessed by the `Docs Layout` in order to create the menu. Let's see an example:

```yaml
options:
  - title: Getting Started
    url: docs/index
    section: intro

  - title: Configuring the Microsite
    url: docs/settings

  - title: Layouts
    url: docs/layouts
    section: resources

  - title: Customize
    url: docs/customize

  - title: Build the microsite
    url: docs/build-the-microsite
```

* The `options` key is mandatory. It'll be the parent of all the options defined here. Each `option` or menu item will contain:
* `title`: the menu title. It should be the same as defined in the meta-property associated with the file (`<Document Title>`, where the layout is defined).
* `url`: relative path to the site URL.
* `menu_section`: this key is mandatory only when you have a nested submenu. It'll be useful to distinguish between sub-items with the same name in different menu options.
* `menu_type`: optional parameter. It brings the ability to configure different menus for different sets of documents, defining all the menu options in the same `menu.yml` file. For example, you might want to define two different places in your microsite where the menu might be different. This is the setting you can use in order to group the set of menu options.
* Optionally, we could define a second level of nested sub-items, thanks to the `nested_options` key, defined at the same level that `title` and `url` of the parent menu. For example:

```yaml
options:
  - title: Introduction
    url: index
    menu_section: intro

    nested_options:
     - title: Submenu 1
       url: subfolder/submenu1
     - title: Submenu 2
       url: subfolder/submenu2

  - title: Configuring the Microsite
    url: settings
```

In this example, `Submenu 1` and `Submenu 2` will be nested under the `Introduction` menu option. At the same time, `submenu1` and `submenu2` would have the same section name as the parent. For instance, `submenu1.md` would have a header like this, where the `section` field matches the one defined in `menu.yml`:

```markdown
---
layout: docs
title:  "Submenu 2"
section: "intro"
---
```

## Page Layout and Menu Partial Layout

This layout is useful when we want to have different web pages at the same `home` level, but under the menu of the microsite.

One example:

```markdown
---
layout: page
title:  "<page-menu-title>"
section: "<page_menu_title>"
position: 3
---
```

For each different `section` the framework finds in your source directory, it'll create a new menu option in the microsite.

Let's review this in this example:


`file1.md` contents:

```markdown
---
layout: home
title:  "Home"
section: "section_home"
position: 1
---
```

`file2.md` contents:

```markdown
---
layout: page
title:  "Section 2"
section: "section2"
position: 3
---
```

`file3.md` contents:

```markdown
---
layout: page
title:  "Section 3"
section: "section3"
position: 2
---
```

In this case, thanks to Jekyll and the MenuPartial Layout implemented as a part of the **sbt-microsites** plugin, it will automatically generate a menu with three items:

        Home | Section 3 | Section 2

As you can see, the menu is ordered by the tag `position`.
