#!/usr/bin/env bash
while getopts "d:u:p:" flag;
do
  case $flag in
    d) database=${OPTARG};;
    u) username=${OPTARG};;
    p) password=${OPTARG};;
  esac
done

echo "Initializing Database..."

echo "Database name: $DB_NAME"
echo "Database username: $username"
echo "Database password: $password"

# Update repo, install maria-db
apt-get -y update
apt-get -y install mariadb-server

# Check Status
systemctl status mariadb

# Restart (ensure it's running)
systemctl restart mariadb

# Set root password
/usr/bin/mysqladmin -u$username password $password

# Enable remote access
sudo mysql -u$username -p$password -e "GRANT ALL PRIVILEGES ON *.* TO '$username'@'%' IDENTIFIED BY '$password' WITH GRANT OPTION; FLUSH PRIVILEGES;"

# Create database;
echo "DROP TABLE IF EXISTS $database;"
sudo mysql -u$username -p$password -e "DROP DATABASE IF EXISTS ${database};"

echo "CREATE DATABASE $database;"
sudo mysql -u$username -p$password -e "CREATE DATABASE ${database};"

# Restart again
systemctl restart mariadb