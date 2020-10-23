#!/usr/bin/env bash
while getopts "d:U:P:h:p:" flag;
do
  case $flag in
    d) database=${OPTARG};;
    U) username=${OPTARG};;
    P) password=${OPTARG};;
    h) host=${OPTARG};;
    p) port=${OPTARG};;
  esac
done

cd /vagrant

echo "Initializing Cinema Tickets App..."

# Cleanup
sudo rm /usr/bin/cinema_tickets_app.jar
#sudo rm -r /vagrant/backend/target
sudo rm /etc/systemd/system/cinema_tickets_app.service 
sudo rm /vagrant/cinema_tickets_app.log
sudo rm /etc/default/cinema_tickets_app.conf

sudo systemctl daemon-reload
sudo systemctl stop cinema_tickets_app

echo "Database name: $database"
echo "Database username: $username"
echo "Database password: $password"
echo "Database host: $host"
echo "Database port: $port"

echo "Installing app..."

./mvnw clean package -DskipTests

echo "Installation done..."
echo "Starting Service..."

# Prepare .jar file
sudo cp /vagrant/backend/target/cinema-tickets-core-0.0.1-SNAPSHOT.jar /usr/bin/cinema_tickets_app.jar
sudo chmod +x /usr/bin/cinema_tickets_app.jar

# Prepare service
echo -e "DB_NAME=${database}\nDB_USER=${username}\nDB_PASSWORD=${password}\nDB_HOST=${host}\nDB_PORT=${port}" | sudo tee -a /etc/default/cinema_tickets_app.conf
sudo cp /vagrant/bootstrap/cinema_tickets_app.service /etc/systemd/system/cinema_tickets_app.service
sudo systemctl daemon-reload
sudo systemctl enable cinema_tickets_app
sudo systemctl start cinema_tickets_app

echo "Finishing..."