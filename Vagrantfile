require 'json'

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"  
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox"
  config.vm.network "forwarded_port", guest: 8081, host: 8080

  config.vm.provision "ansible_local" do |ansible|
    ansible.playbook = "./setup-compose.yml"
    # ansible.playbook = "./setup.yml"

    ansible.install_mode = "pip3"
    ansible.extra_vars = { 
      ansible_python_interpreter: "/usr/bin/python3" 
    }
    
    #ansible.galaxy_role_file = "requirements.yml"
    #ansible.galaxy_command = "sudo ansible-galaxy install -r %{role_file}"
  end
  
end