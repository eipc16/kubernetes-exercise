require 'json'

vm_config = JSON.parse(File.read('vm_config.json'))

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox"

  config.vm.define "db" do |db|
    db.vm.network "private_network", ip: vm_config['DB_IP']

    db.vm.provision "shell",
    path: "bootstrap/database.sh",
      env: {
        "DB_NAME" => vm_config['DB_NAME'],
        "DB_USERNAME" => vm_config['DB_USERNAME'],
        "DB_PASSWORD" => vm_config['DB_PASSWORD']
      }
  end

  config.vm.define "app" do |app|
    app.vm.network "private_network", ip: vm_config['APP_IP']
    app.vm.network "forwarded_port", guest: 8081, host: 8081

    app.vm.provision "shell",
      path: "bootstrap/bootstrap.sh",
      env: {
          "DB_NAME" => vm_config['DB_NAME'],
          "DB_USERNAME" => vm_config['DB_USERNAME'],
          "DB_PASSWORD" => vm_config['DB_PASSWORD'],
          "APP_IP" => vm_config['APP_IP'],
          "DB_HOST" => vm_config['DB_IP'],
          "DB_PORT" => vm_config['DB_PORT']
      }
    
    app.vm.provider "virtualbox" do |vb|
      vb.memory = "4096"
      vb.cpus = "2"
    end
  end
end
