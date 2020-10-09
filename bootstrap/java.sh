#!/usr/bin/env bash

apt-get update

echo "Install Java..."
apt-get install -y openjdk-11-jdk-headless
echo "Java installation complete"

java --version