# Laboratorium 6. Docker (Technologie wspierające wytwarzanie oprogramowania)

## Opis zadania
Celem ćwiczenia było przygotowanie konfiguracji do uruchomienia aplikacji webowej w środowisku Docker.

## Środowisko
Środowisko składa się z dwóch kontenerów. W pierwszym uruchamiana jest baza danych, natomiast w drugim uruchamiana jest aplikacja. Jako adresy kontenerów zostały wykorzystane ich nazwy. Poniższy rysunek obrazuje konfigurację środowiska:

## Uruchomienie bez docker-compose
Aby uruchomić aplikację bez wykorzystania polecenia docker-compose należy w pliku `Vagrant` wprowadzić w linii 7 (kod poniżej) wprowadzić ścieżke do pliku `setup.yml`, który zawiera skrypt Ansible konfigurujący środowisko. Następnie należy wywołać polecenie Vagrant up. Spowoduje to uruchomienie i skonfigurwanie maszyny.

`
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"  
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox"
  config.vm.network "forwarded_port", guest: 8081, host: 8080

  config.vm.provision "ansible_local" do |ansible|
    ansible.playbook = "./setup.yml"

    ansible.install_mode = "pip3"
    ansible.extra_vars = { 
      ansible_python_interpreter: "/usr/bin/python3" 
    }
  end
end
`

Poniżej zrzuty ekranu z działającą maszyną i kontenerami:

## Uruchomienie z docker-compose
Aby uruchomić aplikację z wykorzystaniem polecenia docker-compose należy w pliku `Vagrant` wprowadzić w linii 7 (kod powyżej) wprowadzić ścieżke do pliku `setup-compose.yml`, który zawiera skrypt Ansible konfigurujący środowisko. Następnie należy wywołać polecenie Vagrant up. Spowoduje to uruchomienie i skonfigurwanie maszyny.

Poniżej zrzuty ekranu z działającą maszyną i kontenerami:

## Podsumowanie
Praca z Dockerem nie obyła się bez małych problemów. Głównym problemem był tutaj obraz systemu wykorzystywany przez Vagranta. Obraz, który został wykorzystany w ramach poprzedniego laboratorium niestety nie sprawdził się w tym przypadku. Zamiast tego wykorzystany został zwyczajny obraz Ubuntu, a następnie zainstalowane zostały java i docker. Pozwoliło to na bezproblemowe uruchomienie aplikacji. Podsumowując pracę z Dockerem oceniamy pozytywnie.
