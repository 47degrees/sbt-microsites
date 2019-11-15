---
layout: docs
title: Customize
permalink: docs/customize/
---

# Customize

The sbt-microsites plugin brings in different resources related to styles, images, and colors. But it provides a considerable scope for improvement and customization in terms of images and styles.

## Images

As you can see in the [Configuring the Microsite]({% link docs/settings.md %}) section, everything that you put in the directory associated with the `micrositeImgDirectory` setting  (`src/main/resources/microsite/img` by default) will be copied to the generated microsite. Therefore, you can add new images in that directory, which you would reference from your markdown documents, or, you could override the default images.

If you create your own images (which makes sense) and override the default ones, you can do it with these names and properties, placing them in your image directory:

- Light style:
  - Microsite logo for the home page:
    - `light_navbar_brand.svg` -> **[36x36]**
  - Microsite logo for the documentation page:
    - `light_sidebar_brand.svg` -> **[36x36]**
  - Microsite logo for the masthead in the `Features` layout:
    - `features-header.svg` -> **[500x330]**

  - Icons used by the `features` meta tag that we saw in the [Layouts]({% link docs/layouts.md %}) section. These features and their icons will be shown in features section in the homeFeatures page.
    - First feature icon:
        - `first-feature-icon.svg` -> **[56x56]**
    - Second feature icon:
        - `second-feature-icon.svg` -> **[56x56]**
    - Third feature icon:
        - `third-feature-icon.svg` -> **[56x56]**

- Pattern style:
  - Microsite logos in two different sizes for the home page:
      - `navbar_brand.png` -> **[44x44]**
      - `navbar_brand2x.png` -> **[88x88]**
  - Microsite logos in two different sizes for the documentation page:
      - `sidebar_brand.png` -> **[36x36]**
      - `sidebar_brand2x.png` -> **[72x72]**
  - Background pattern image for home's Jumbotron. Jumbotron is a [Bootstrap](https://getbootstrap.com/) component. You can read more about it [here](https://getbootstrap.com/components/#jumbotron).
      - `jumbotron_pattern.png` -> In this case, there isn't a size requirement for this image. The pattern is repeated according to the screen size.
  - Icons used by the `technologies` meta tag that we saw in the [Layouts]({% link docs/layouts.md %}) section. These technologies and their icons will be shown in the sub-footer in the home page.
      - First icon:
          - `first_icon.png` -> **[40x40]**
          - `first_icon2x.png` -> **[80x80]**
      - Second icon:
          - `second_icon.png` -> **[40x40]**
          - `second_icon2x.png` -> **[80x80]**
      - Third icon:
          - `third_icon.png` -> **[40x40]**
          - `third_icon2x.png` -> **[80x80]**

- Optionally, you might want to specify your `favicon.png` image.

## Styles

`sbt-microsites` `pattern` style is completely based on [Bootstrap](https://getbootstrap.com/), adding some extra styles that make the microsites even more beautiful.

That being said, you can personalize your microsite even further by using your own css files. In the same manner, as we've just seen for images, all the css files that you place in the directory associated with the `micrositeCssDirectory` setting (`src/main/resources/microsite/css` by default) will be copied to the generated microsite. Therefore, you can add new styles, or even override existing ones.

## Colors

Colors can be customized through the `micrositePalette` setting (take a look at the [Configuring the Microsite]({% link docs/settings.md %}) section for a deeper explanation).

## Syntax Highlighting

As we mentioned in the [Configuring the Microsite]({% link docs/settings.md %}) section, `micrositeHighlightTheme` sbt setting allows you to specify the theme you want to use to highlight your code.

It's important to mention that the theme name should match with the one located at https://cdnjs.com/libraries/highlight.js/.

https://highlightjs.org/static/demo/ provides the ability to preview the different themes before setting up your microsite.

## Permalinks and GitHub integration

Users may want to navigate through your site docs, but they can also just use GitHub to do so.
In order to achieve linkable documents that work in both Jekyll and GitHub, follow a few
simple steps:

1. Follow a directory structure where each section corresponds with a folder and a README.md file inside.

```
README.md
/content/README.md
/content/whatever/README.md
```

This will make GitHub render `README.MD` files as if they were the index on each section when accesing them trough the GitHub website.

2. Add a permalink directive on each of your `README.md` files so that Jekyll understands that you want those to be served at the path of each folder.

For example:

```
/content/whatever/README.md
---
layout: docs
title: Whatever
permalink: /docs/whatever/
---
```

3. Link to content normally by using Markdown relative links that point to the folder.

```
[Link to Whatever Content](/content/whatever)
```
