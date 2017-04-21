---
layout: docs
title: Publish with Travis
---

# Publish with Travis

In this section, we’re going to learn how to configure Travis so you can publish your microsite when you merge in master.

There are two options for publishing the site. See [Configuring the Microsite](settings.html) section for more information.

## Publish using sbt-ghpages

When the property `micrositePushSiteWith` is set to `GHPagesPlugin` (by default), the site is pushed using the sbt-ghpages plugins. The steps for this approach are:

**1- Generate an SSH key pair**

Run this command in your console

```
$ ssh-keygen -t rsa -b 4096 -C "Travis Deploy Key" -f travis-deploy-key
```

After that, you'll see two new files in your folder: `travis-deploy-key` and `travis-deploy-key.pub`

_**Note:** If you are creating the keys in your project maybe you should add these files to your .gitignore file_.

**2- Add key to GitHub Project**

Add the content of `travis-deploy-key.pub` in the `Deploy Key` section of the GitHub project's setting page.

Navigate to `Setting` > `Deploy Keys`. Make sure you allow write access in order to push in this repository.

**3- Encrypt your keys**

Travis needs the keys for publishing the microsite in `gh-pages` branch. It's very important that you encrypt your keys using the `travis` command line. You have to install travis:

```
$ gem install travis
```

_**Note:** An important thing to remember is that you can only have one file encrypted in Travis. If you need more keys you should use `tar` for joining your different keys. In our example, we only are going to use the key_

We have to encrypt the `travis-deploy-key` file and we have to add the encrypted file to GitHub.

First, we must be logged into travis:

```
$ travis login
```

Secondly, encrypt the file:

```
$ travis encrypt-file travis-deploy-key travis-deploy-key.enc
```

You are going to see the following response:

```
encrypting travis-deploy-key for [org]/[project]
storing result as travis-deploy-key.enc

Please add the following to your build script (before_install stage in your .travis.yml, for instance):

    openssl aes-256-cbc -K $encrypted_[your_number]_key -iv $encrypted_[your_number]_iv -in travis-deploy-key.enc -out travis-deploy-key -d

Pro Tip: You can add it automatically by running with --add.

Make sure to add keys.tar.enc to the git repository.
Make sure not to add keys.tar to the git repository.
Commit all changes to your .travis.yml.

```

Finally, add the encrypted file to your git:

```
$ git add travis-deploy-key.enc
```

**4- Configure your .travis.yml**

Our recommendation is that you should use `Bash Scripts` and run the script when you merge the changes in master.

This is the `.travis.yml` in `sbt-microsite` project:

```
language: scala
scala:
- 2.10.6
jdk:
- oraclejdk8
before_install:
- if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; then bash
  scripts/decrypt-keys.sh; fi
- export PATH=${PATH}:./vendor/bundle
install:
- rvm use 2.2.3 --install --fuzzy
- gem update --system
- gem install sass
- gem install jekyll -v 3.2.1
script:
- sbt ++$TRAVIS_SCALA_VERSION test scripted
after_success:
- if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; then bash
  scripts/publishMicrosite.sh; fi
- if [ "$TRAVIS_PULL_REQUEST" = "true" ]; then echo "Not in master branch, skipping
  deploy and release"; fi
```

You can see that before we install, we run `decrypt-keys.sh`, then we install `jekyll` and finally, we are running `publishMicrosite.sh`.

Next, you can see the `decrypt-keys.sh` script:

```
#!/bin/sh

openssl aes-256-cbc -K $encrypted_[your_number]_key -iv $encrypted_[your_number]_iv -in travis-deploy-key.enc -out travis-deploy-key -d;
chmod 600 travis-deploy-key;
cp travis-deploy-key ~/.ssh/id_rsa;
```

The most important thing here is the `openssl` command. You should use the command that you receive as a response when you encrypt the file using the `travis` command above.

In this script, we decrypt the keys and copy `.ssh` to the folder in order to publish it in GitHub later.

Next, you can see the `publishMicrosite.sh` script:

```
#!/bin/bash
set -e

git config --global user.email "your_user_email"
git config --global user.name "your_user_name"
git config --global push.default simple

sbt docs/publishMicrosite
```

In this script, we are publishing the microsite in GitHub Pages. To do this, we should add the user information for git.

## Publish using GitHub4s

With this method, you don't need to generate any ssh keys or encrypt any files. We'll use the GitHub API for publishing the site. The steps are:

**1- Create a token for your project**

You need to create a GitHub token with `repo` scope. You can create it in the [GitHub settings](https://github.com/settings/tokens/new?scopes=repo&description=sbt-microsites) page.

Copy the token in a safe place, we'll use this token through an environment variable as we'll see shortly.

**2- Configure your project build**

You need to set these two sbt settings:

```
micrositePushSiteWith := GitHub4s
```

```
micrositeGithubToken := getEnvVar("GITHUB_TOKEN")
```

**3- Add the environment variable in Travis**

The final step is to define the `GITHUB_TOKEN` environment variable in Travis Repository Settings. You can do it following [the documentation](https://docs.travis-ci.com/user/environment-variables/#Defining-Variables-in-Repository-Settings).
