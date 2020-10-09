#!/usr/bin/env bash

apt-get update

# Download node from NodeSource
curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -

# Install 
apt-get install nodejs

# verify version
node -v
