---
layout: docs
title: Customize
section: docs
weight : 4
---

# Customize

The sbt-microsites plugin brings different resources related to styles, images and colors, but it provides a considerable scope for improvement and customization in terms of images and styles.

## Images

As you can see in the [Configuring the Microsite](settings.html) section, everything that you put in the directory associated with `micrositeImgDirectory` setting  (`src/main/resources/microsite/img` by default), will be copied to the generated microsite. Therefore, you can add new images in that directory which you would reference from your markdown documents or, you could override the default ones.

If you create your own images (which makes sense) and override the default ones, you can do it with these names and properties, placing them at your image directory:

- Microsite logos in two different sizes for the home page:
    - `navbar_brand.png` -> **[44x44]**
    - `navbar_brand2x.png` -> **[88x88]**
- Microsite logos in two different sizes for the documentation page:
    - `sidebar_brand.png` -> **[36x36]**
    - `sidebar_brand2x.png` -> **[72x72]**
- Background pattern image for home's Jumbotron. Jumbotron is [Bootstrap](http://getbootstrap.com/) component, you can read more about it [here](http://getbootstrap.com/components/#jumbotron).
    - `jumbotron_pattern.png` -> In this case, there isn't size requirements for this image. The pattern is repeated according to the screen size.
- Icons used by the `technologies` meta tag that we saw in the [Layouts](layouts.html) section. These technologies and its icons will be shown in the sub-footer in the home page.
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

`sbt-microsites` is completely based on [Bootstrap](http://getbootstrap.com/), adding some extra styles that makes the microsites even more beautiful :P

Said that, you can personalize even more through your own css files. In the same fashion as we've just seen for images, all the css files that you place in the directory associated with `micrositeCssDirectory` setting  (`src/main/resources/microsite/css` by default), will be copied to the generated microsite. Therefore, you can add new styles or even, overriding existing ones.

## Colors

Colors can be customized through the `micrositePalette` settings (take a look at [Configuring the Microsite](settings.html) section for a deeper explanation).