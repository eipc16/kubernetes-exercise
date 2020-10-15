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

echo "Database name: $database"
echo "Database username: $username"
echo "Database password: $password"

# Update repo, install maria-db
apt-get -y update
apt-get -y install mariadb-server

# Check Status
systemctl status mariadb

# Restart (ensure it's running)
sudo systemctl restart mariadb

echo "GRANT ALL PRIVILEGES ON *.* TO '$username'@'localhost' IDENTIFIED BY '$password' WITH GRANT OPTION; FLUSH PRIVILEGES;"
sudo mysql -e "GRANT ALL PRIVILEGES ON *.* TO '$username'@'%' IDENTIFIED BY '$password' WITH GRANT OPTION; FLUSH PRIVILEGES;"

# Create database;
echo "DROP TABLE IF EXISTS $database;"
mysql -u $username -p$password -e "DROP DATABASE IF EXISTS ${database};"

echo "CREATE DATABASE $database;"
mysql -u $username -p$password -e "CREATE DATABASE ${database};"

for sql_file in `ls /vagrant/bootstrap/database_scripts/*.sql`
  do
    echo "Processing file $sql_file..."
    mysql -u $username -p$password $database < $sql_file
done

# Restart again
sudo systemctl restart mariadb