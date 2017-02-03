#!/bin/bash
set -e

git config --global user.email "developer@47deg.com"
git config --global user.name "47Deg (Travis CI)"
git config --global push.default simple

echo "Building Kazari..."

sbt "project kazari" "clean" "fullOptGenerate"

cp kazari/target/scala-2.11/kazari.js src/main/resources/js/
cp kazari/src/main/resources/*.js src/main/resources/js/
cp kazari/src/main/resources/*.css src/main/resources/css/

git add src/main/resources/*.js
git add src/main/resources/*.css
git commit -m "Travis CI - makes a new Kazari build and integrates it on sbt-microsites"
git push origin master