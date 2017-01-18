---
layout: docs
title: Publish with Travis
---

# Publish with Travis

In this section, we are going to learn about how you can configure Travis in order to publish your microsite when you merge in master. You can follow these steps:

**1- Generate an SSH key pair**

Run this command in your console

```
$ ssh-keygen -t rsa -b 4096 -C "Travis Deploy Key" -f travis-deploy-key
```

After that, you'll see 2 new files in your folder: `travis-deploy-key` and `travis-deploy-key.pub`

_**Note:** If you are creating the keys in your project maybe you should add these files in your .gitignore file_

**2- Add key to GitHub Project**

Add the content of `travis-deploy-key.pub` in the `Deploy Key` section of the GitHub project's setting page.

Navigate to `Setting` > `Deploy Keys`. Make sure you allow write access in order to push in this repository

**3- Encrypt your keys**

Travis needs the keys for publishing the microsite in `gh-pages` branch. It's very important that you encrypts your keys using `travis` command line. You have to install travis

```
$ gem install travis
```

_**Note:** One important thing is that you only can to have 1 file encrypted in Travis, and if you need more keys you should use `tar` for joining your different keys. In our example, we only are going to use the key_

We have to encrypt `travis-deploy-key` file and we have to add to GitHub the encrypted file.

Firstly, we must be logged in travis:

```
$ travis login
```

Secondly, encrypt the file:

```
$ travis encrypt-file travis-deploy-key travis-deploy-key.enc
```

You are going to see the next response:

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

Finally, add the encrypted file to your git

```
$ git add travis-deploy-key.enc
```

**4- Configure your .travis.yml**

Our recommendation is that you should use `Bash Scripts` and run the script when you merge the changes in master

This is the `.travis.yml` in `sbt-microsite` project

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

You can see that before to install we run `decrypt-keys.sh`, then we install `jekyll` and finally, we are running `publishMicrosite.sh`

Next, you can see `decrypt-keys.sh` script

```
#!/bin/sh

openssl aes-256-cbc -K $encrypted_[your_number]_key -iv $encrypted_[your_number]_iv -in travis-deploy-key.enc -out travis-deploy-key -d;
chmod 600 travis-deploy-key;
cp travis-deploy-key ~/.ssh/id_rsa;
```

The most important thing here is the `openssl` command. You should use the command that you receive as response when you encrypt the file using `travis` command above

In this script, we decrypt the keys and copying to `.ssh` the folder in order to publish in GitHub later

Next, you can see `publishMicrosite.sh` script

```
#!/bin/bash
set -e

git config --global user.email "your_user_email"
git config --global user.name "your_user_name"
git config --global push.default simple

sbt docs/publishMicrosite
```

In this script, we are publishing the microsite in GitHub Pages. For that, we should add the user information for git