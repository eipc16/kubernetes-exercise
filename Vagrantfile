Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"

  config.vm.synced_folder ".", "/vagrant"

  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.network "forwarded_port", guest: 8081, host: 8081
#   config.vm.network "private_network", ip: "192.168.33.10"

  config.vm.provision "shell",
    path: "bootstrap/bootstrap.sh",
    env: {
        "DB_NAME" => "cinema_tickets_app_db",
        "DB_USERNAME" => "test",
        "DB_PASSWORD" => "test",
        "DB_HOST" => "localhost",
        "DB_PORT" => 3306
    }
  
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "4096"
    vb.cpus = "2"
  end
end
