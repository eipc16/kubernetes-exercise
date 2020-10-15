#!/usr/bin/env bash

echo "Start VM initialization"

echo "Database variables..."
echo $DB_NAME
echo $DB_USERNAME
echo $DB_PASSWORD
echo $DB_HOST
echo $DB_PORT

# Update repository and Ubuntu
apt-get update -y

# Java installation script
chmod +x /vagrant/bootstrap/java.sh
/vagrant/bootstrap/java.sh

# Setup database
chmod +x /vagrant/bootstrap/database.sh
sudo /vagrant/bootstrap/database.sh -d $DB_NAME -u $DB_USERNAME -p $DB_PASSWORD

# Start application
chmod +x /vagrant/bootstrap/cinema-tickets-app.sh
sudo /vagrant/bootstrap/cinema-tickets-app.sh -d $DB_NAME -U $DB_USERNAME -P $DB_PASSWORD -h $DB_HOST -p $DB_PORT


# Add host
sudo echo "127.0.0.1 cinematicketsapp.com" >> /etc/hosts

# Default to main dir
echo "cd /vagrant" >> /home/vagrant/.bashrc
