BOX = "ubuntu/bionic64"
DB_NAME = "cinema_tickets_app_db"
DB_USERNAME = "test"
DB_PASSWORD = "test"
APP_IP="10.0.0.33"
DB_IP="10.0.0.34"
DB_HOST = "localhost"
DB_PORT = 3306


Vagrant.configure("2") do |config|
  config.vm.box = BOX
  config.vm.synced_folder ".", "/vagrant"

  config.vm.define "db" do |db|
    db.vm.network "private_network", ip: DB_IP

    db.vm.provision "shell",
    path: "bootstrap/database.sh",
      env: {
        "DB_NAME" => DB_NAME,
        "DB_USERNAME" => DB_USERNAME,
        "DB_PASSWORD" => DB_PASSWORD
      }
  end

  config.vm.define "app" do |app|
    app.vm.network "private_network", ip: APP_IP

    app.vm.provision "shell",
      path: "bootstrap/bootstrap.sh",
      env: {
          "DB_NAME" => DB_NAME,
          "DB_USERNAME" => DB_USERNAME,
          "DB_PASSWORD" => DB_PASSWORD,
          "APP_IP" => APP_IP,
          "DB_HOST" => DB_IP,
          "DB_PORT" => DB_PORT
      }
    
    app.vm.provider "virtualbox" do |vb|
      vb.memory = "4096"
      vb.cpus = "2"
    end
  end
end
