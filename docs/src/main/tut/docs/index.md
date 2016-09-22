---
layout: docs
title: Home
section: docs
weight : 0
---

# sbt-microsites Documentation

In this section, you can found everything about how **sbt-microsites** works and how can be tweaked, see next points to know more about it: 

{% assign pages = site.pages | sort:"weight"  %}
{% for x in pages %}
  {% if x.section == 'docs' and x.title != 'Home' %}
- [{{x.title}}]({{site.baseurl}}{{x.url}})
  {% endif %}
{% endfor %}
