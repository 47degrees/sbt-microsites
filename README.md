#sbt-microsites

### Steps:

Add plugin in `project/plugins.sbt`:
```
addSbtPlugin("com.fortysevendeg"  % "sbt-microsites" % "1.0.1-SNAPSHOT")
```

Enable the plugin in `build.sbt`:
```
enablePlugins(MicrositesPlugin)
```

Create markdown documents called `index.md` in `src/tut` like [this one](https://gist.github.com/rafaparadela/9ccfcf1f52c5282c9a5e894b0ddf6508).


Create microsite
```
sbt> microsite
```

Compile:

```
sbt> tut
```

Make site:
```
sbt> makeSite
```

Preview site:
```
sbt> previewSite
```

## Microsite settings

- The name of the microsite is taken from the sbt setting `name`, but you can override it:
```
micrositeName := "Dummy"
```

- The description of the microsite is taken from the sbt setting `description`, but you can override it:
```
micrositeDescription := "This is my Dummy description"
```

- The author of the microsite is taken from the sbt setting `organizationName`, but you can override it:
```
micrositeAuthor := "Rafa Paradela"
```

- The homepage of the microsite is taken from the sbt setting `homepage`, but you can override it:
```
micrositeHomepage := "http://www.mywebpage.com"
```

- The theme of the  microsite is taken from the sbt setting `homepage`, but you can override it:
```
micrositeHomepage := "http://www.mywebpage.com"
```



