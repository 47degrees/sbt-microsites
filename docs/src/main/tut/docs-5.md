---
layout: docs
title: Customize
section: docs
---

# Customize

Although common resources are retrieved from a [CDN](https://github.com/47deg/microsites/tree/cdn), you can customize your microsite easily. Just create a directory in [CDN branch](https://github.com/47deg/microsites/tree/cdn) with the same name that you have set the parameter `style` in the `_config.yml` file. As you can see, the `style` parameter has been set to `default` value, so logos and palette of colours are retrieved from the `default` folder in the branch. Once your folder created, you have to provide these files within:

- `navbar_brand.png` and `sidebar_brand.png`: Logos for home and docs pages.
- `jumbotron_pattern.png`: Background pattern for home's jumbotron.
- `palette.css`: Color rules that overrides default ones.

The proper protocol to address these steps is, firstly create a issue for providing these resources and assign it to the design department, and then set the `style` parameter in your `_config.yml` file.