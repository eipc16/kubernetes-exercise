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

ls -a

echo "Initializing Cinema Tickets App..."

echo "Database name: $database"
echo "Database username: $username"
echo "Database password: $password"
echo "Database host: $host"
echo "Database port: $port"

echo "Installing app..."

./mvnw clean install -DskipTests

echo "Installation done..."
echo "Starting..."

./mvnw -pl backend spring-boot:run -DskipTests -Ddb.name=$database -Ddb.user=$username -Ddb.password=$password -Ddb.host=$host -Ddb.port=$port &
./mvnw -pl ui spring-boot:run &

echo "Finishing..."