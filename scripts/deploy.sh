#!/bin/sh

echo "Master branch, releasing...";
sbt compile publishSigned;
