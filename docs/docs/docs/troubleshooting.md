---
layout: docs
title: Troubleshooting
permalink: docs/troubleshooting/
---

# Troubleshooting

## Site has no styling

**Problem:** After upgrading from sbt-microsites 0.x to 1.x, my microsite has no
styling at all. The `target/site/css/` directory is not being generated.

**Solution:** You probably have the wrong version of Jekyll installed.
sbt-microsites 0.x had a dependency on Jekyll 3.x, but sbt-microsites 1.x
requires Jekyll 4.0.0 or newer.
