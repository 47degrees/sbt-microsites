#!/bin/sh

function decipherKeys {
   echo $KEYS_PASSPHRASE | gpg --passphrase-fd 0 keys.tar.gpg
   tar xfv keys.tar
}

function publish {
   sbt publishSnapshot
}

echo "Master branch, releasing...";
decipherKeys;
publish;