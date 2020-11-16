require 'json'

Vagrant.configure("2") do |config|
  config.vm.box = "gusztavvargadr/docker-linux-community-ubuntu-server"  
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox"
  config.vm.network "forwarded_port", guest: 8081, host: 8080

  config.vm.provision "ansible_local" do |ansible|
    ansible.playbook = "./setup.yml"
    ansible.galaxy_role_file = "requirements.yml"
    ansible.galaxy_command = "sudo ansible-galaxy install -r %{role_file}"
  end
  
end