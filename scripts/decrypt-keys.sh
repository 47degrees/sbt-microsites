#!/bin/sh

openssl aes-256-cbc -K $encrypted_0d40c3508e9d_key -iv $encrypted_0d40c3508e9d_iv -in keys.tar.enc -out keys.tar -d;
tar xvf keys.tar;
chmod 600 deploy_key;
cp deploy_key ~/.ssh/id_rsa;
