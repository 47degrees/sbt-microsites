---
layout: docs
title: Home
section: docs
weight : 0
---

# sbt-microsites Documentation

In this section, you can find everything you need to know about how **sbt-microsites** work and how they can be tweaked, review the following points to learn more: 

{% assign pages = site.pages | sort:"weight"  %}
{% for x in pages %}
  {% if x.section == 'docs' and x.title != 'Home' %}
- [{{x.title}}]({{site.baseurl}}{{x.url}})
  {% endif %}
{% endfor %}
