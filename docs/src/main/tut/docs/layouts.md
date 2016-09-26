---
layout: docs
title: Layouts
section: docs
weight : 3
---

# Layouts

Currently, the sbt-microsites plugin includes four different layouts:

- `home`: The landing page, the public face of your library or project.
- `docs` (Optional): The page where the documentation for your library should be included. Most likely, you are seeing the `Documentation` page of this repo right now. It's optional, depending on the `micrositeDocumentationUrl` setting, take a look at the [Configuring the Microsite](settings.html) section for an in-depth explanation.
- `page` (Optional): Similar to `home` but reducing the jumbotron layer and taking into account the submenu (jumbotron and other concepts related to style are explained in the [Customize](customize.html) section.
- Menu Partial: this abstract layout reads all the files in your project that fit a set of requirements, and sets up a menu under the jumbotron image. We'll see more details on this later on.

## Home Layout

Usually, the `home` layout is related to the `index.md` file. In this document, you can put all the markdown content that's related to the landing page.

For instance:

```

---
layout: home
title:  "Home"
section: "home"
technologies:
 - first: ["Scala", "sbt-microsites plugin is completely written in Scala"]
 - second: ["SBT", "sbt-microsites plugin uses SBT and other sbt plugins to generate microsites easily"]
 - third: ["Jekyll", "Jekyll allows for the transformation of plain text into static websites and blogs."]
---
```

The technology list is optional. These three technologies will be shown as a sub-footer in your home page. These technologies are identified for the set of keys (`first`, `second`, `third`). You can specify to include all of them or none of them. At this time there are no other choices.

## Docs Layout

All the markdown files that contain this `layout` and `section` as `docs` will be grouped in the Documentation page. Each markdown file with these characteristics will be shown on a collapsible left menu, with one item per existing file. From this menu, you can easily navigate all the docs.

To be able to access the documentation, you have to configure `micrositeDocumentationUrl` setting.

As an example, you can look at the sbt-microsites documentation at [Github](https://github.com/47deg/sbt-microsites/tree/master/docs/src/main/tut). We have several documentation files:

- `build-the-microsite.md`
- `customize.md`
- `getting-started.md`
- `index.md`
- ...

All these files contain as a header, something similar to this:

```

---
layout: docs
title: <Document Title>
section: docs
weight : <order>
---
```

`<Document Title>` will be used as a menu item name on the left. `<order>` is related to the order in which they will appear in the left menu.

## Page Layout and Menu Partial Layout

This layout is useful when we want to have different web pages at the same `home` level but under the menu of the microsite.

One example:

```

---
layout: page
title:  "<page-menu-title>"
section: "<page_menu_title>"
position: 3
---
```

For each different `section` the framework finds in your source directory, it'll create a new menu option in the microsite.

Let's review this in this example:

```

file1.md contents:

---
layout: home
title:  "Home"
section: "section_home"
position: 1
---

file2.md contents:

---
layout: page
title:  "Section 2"
section: "section2"
position: 3
---

file2.md contents:

---
layout: page
title:  "Section 3"
section: "section3"
position: 2
---
```

In this case, thanks to Jekyll and the MenuPartial Layout implemented as a part of the **sbt-microsites** plugin; it will automatically generate a menu with three items:
 
        Home | Section 3 | Section 2 
        
As you can see, the menu is ordered by the tag `position`.