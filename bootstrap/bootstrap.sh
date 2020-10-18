#!/usr/bin/env bash

# while [ ! -f .db_setup_completed ]
# do
#   sleep 2
#   echo "Waiting for database configuration..."
# done

echo "Start VM initialization"

# Add host
sudo echo "$APP_ID cinematicketsapp.com" >> /etc/hosts

# Hosts
sudo cat /etc/hosts

# Update repository and Ubuntu
apt-get update -y

# Java installation script
chmod +x /vagrant/bootstrap/java.sh
/vagrant/bootstrap/java.sh

# Start application
chmod +x /vagrant/bootstrap/cinema-tickets-app.sh
sudo /vagrant/bootstrap/cinema-tickets-app.sh -d $DB_NAME -U $DB_USERNAME -P $DB_PASSWORD -h $DB_HOST -p $DB_PORT

# Default to main dir
echo "cd /vagrant" >> /home/vagrant/.bashrc
