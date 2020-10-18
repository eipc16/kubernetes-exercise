rm -r .db_setup_completed

echo "Initializing Database..."
echo "Database variables..."
echo $DB_NAME
echo $DB_USERNAME
echo $DB_PASSWORD

# Update repo, install maria-db
apt-get -y update
apt-get -y install mariadb-server

# Check Status
systemctl status mariadb

# Restart (ensure it's running)
sudo systemctl restart mariadb

echo "GRANT ALL PRIVILEGES ON *.* TO '$DB_USERNAME'@'0.0.0.0' IDENTIFIED BY '$DB_PASSWORD' WITH GRANT OPTION; FLUSH PRIVILEGES;"
sudo mysql -e "GRANT ALL PRIVILEGES ON *.* TO '$DB_USERNAME'@'%' IDENTIFIED BY '$DB_PASSWORD' WITH GRANT OPTION; FLUSH PRIVILEGES;"

# Create database;
echo "DROP TABLE IF EXISTS $DB_NAME;"
mysql -u $DB_USERNAME -p$DB_PASSWORD -e "DROP DATABASE IF EXISTS ${DB_NAME};"

echo "CREATE DATABASE $DB_NAME;"
mysql -u $DB_USERNAME -p$DB_PASSWORD -e "CREATE DATABASE ${DB_NAME};"

for sql_file in `ls /vagrant/bootstrap/database_scripts/*.sql`
  do
    echo "Processing file $sql_file..."
    mysql -u $DB_USERNAME -p$DB_PASSWORD $DB_NAME < $sql_file
done

# Allow outside access
sudo ufw allow mysql/tcp
echo "bind-address = 0.0.0.0" | sudo tee -a /etc/mysql/my.cnf

# Restart again
sudo systemctl restart mariadb

touch .db_setup_completed