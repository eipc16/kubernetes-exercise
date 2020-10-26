require 'json'

vm_config = JSON.parse(File.read('./vm_config.json'))

LOCK_FILE_PATH = "/vagrant/bootstrap-ansible/.db_setup_finished"
SERVICE_SRC_PATH = "/vagrant/bootstrap-ansible/cinema_tickets_app.service"

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox"

  config.vm.define "db" do |db|
    db.vm.network "private_network", ip: vm_config['DB_IP']

    db.vm.provision "ansible_local" do |ansible|
      ansible.playbook = "./bootstrap-ansible/database-setup.yml"
      ansible.extra_vars = {
        lock_file_path: LOCK_FILE_PATH,
        db: {
          name: vm_config['DB_NAME'],
          username: vm_config['DB_USERNAME'],
          password: vm_config['DB_PASSWORD']
        }
      }
    end
  end

  config.vm.define "app" do |app|
    app.vm.network "private_network", ip: vm_config['APP_IP']
    app.vm.network "forwarded_port", guest: 8081, host: 8081

    app.vm.provision "ansible_local" do |ansible|
      ansible.playbook = "./bootstrap-ansible/backend-setup.yml"
      ansible.extra_vars = {
        lock_file_path: LOCK_FILE_PATH,
        service_src_path: SERVICE_SRC_PATH,
        db: {
          name: vm_config['DB_NAME'],
          username: vm_config['DB_USERNAME'],
          password: vm_config['DB_PASSWORD'],
          host: vm_config['DB_IP'],
          port: vm_config['DB_PORT']
        }
      }
    end
  end
end
