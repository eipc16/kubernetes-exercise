Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"  
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox"
  # config.vm.network "forwarded_port", guest: 8081, host: 8081
  config.vm.network "private_network", ip: "10.0.0.33"

  config.vm.provider "virtualbox" do |vb|
    vb.gui = false
    vb.memory = '2048'
    vb.cpus = 2
  end

  config.vm.provision "ansible_local" do |ansible|
    ansible.playbook = "./setup.yml"

    ansible.install_mode = "pip3"
    ansible.extra_vars = { 
      ansible_python_interpreter: "/usr/bin/python3" 
    }
    
    ansible.galaxy_role_file = "requirements.yml"
    ansible.galaxy_command = "sudo ansible-galaxy install -r %{role_file}"
  end
end