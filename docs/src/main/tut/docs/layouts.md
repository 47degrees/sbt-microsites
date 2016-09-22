---
layout: docs
title: Layouts
section: docs
weight : 3
---

# Layouts

Currently sbt-microsites plugin includes a couple of includes 4 different layouts:

- `home`: The landing page, the public face of your library.
- `docs` (Optional): The page where the documentation of your library should be included. Probably you are watching the `Docs` page of this repo right now.
- `page` (Optional): Similar to `home` but reducing the jumbotron layer and taking into account the submenu.
- Menu Partial: this abstract layout read all the files in your project that fit some requirements and set up a menu under the jumbotron image. We'll see more details about it later on.

## Home Layout

Usually, the `home` layout is related to the `index.md` file. In this document you can put all the markdown content related with the landing page.

For instance:

```

---
layout: home
title:  "Home"
section: "home"
technologies:
 - scala: ["Scala", "sbt-microsites plugin is completely written in Scala"]
 - sbt: ["SBT", "sbt-microsites plugin uses SBT and other sbt plugins to generate microsites easily"]
 - jekyll: ["Jekyll", "Jekyll allows to transform plain text into static websites and blogs."]
---
```

The technology list is optional. These three technologies will be shown as a sub-footer in your home page.

## Docs Layout

All the markdown files that contains this `layout` and `section` as `docs` will be grouped in the Documentation page. They'll be shown on the left menu, one item per existing file, where you can easily navigate over.

Let's put as example, the documentation you're reading at this moment,  where we have several documentation files:

- `docs-1.md`
- `docs-2.md`
- `docs-3.md`
- `docs-4.md`
- ...

All these files contains as header something like this:

```

---
layout: docs
title: <Document Title>
section: docs
---
```

`<Document Title>` will be used as menu item name on the left.

## Page Layout and Menu Partial Layout

This layout is useful when we want to have web pages at the same `home` level, but under the menu of the microsite. 

One example:

```

---
layout: page
title:  "<page-menu-title>"
section: "<page_menu_title>"
position: 3
---
```

For each different `section` that the framework found in your source directory, it'll create a new menu option in the microsite.

Let's think in this example:

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

In this case, thanks to Jekyll and the MenuPartial Layout implemented as a part of this **sbt-microsites** plugin, automatically it will generate a menu with three items:
 
        Home | Section 3 | Section 2 
        
As you can see, the menu is ordered by the tag `position`.