# Laboratorium 4. Ansible (Technologie wspierające wytwarzanie oprogramowania)

## Konfiguracja

### Ogólna konfiguracja

Maszyny można konfigurować poprzez modyfikacje wartości w pliku `vm_config.json` ([Link](./vm_config.json)). Sam plik prezentuje się następująco:

```json
{
    "DB_NAME": "cinema_tickets_app_db",
    "DB_USERNAME": "test",
    "DB_PASSWORD": "test",
    "APP_IP": "10.0.0.33",
    "DB_IP": "10.0.0.34",
    "DB_PORT": 3306
}
```

Wyjaśnienia:

* *DB_NAME* - nazwa bazy danych
* *DB_USERNAME* - nazwa użytkownika w bazie danych
* *DB_PASSWORD* - hasło do bazy danych
* *DB_IP* - adres maszyny zawierającej bazę danych
* *DB_PORT* - port bazy danych
* *APP_IP* - adres maszyny zawierającej aplikacje (musi znajdować sie w tym samym subnecie co *DB_IP*)

Właściwa konfiguracja maszyn znajduje się w pliku `Vagrantfile` ([Link](./Vagrantfile)). W konfiguracji zdefiniowano dwie maszyny, które obierają się na tym samym obrazie `ubuntu/bionic64` oraz posiadają obustronną synchronizację folderów.

### Konfiguracja maszyny z bazą danych

Konfiguracja maszyny z bazą danych:

```ruby
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
```

W przypadku maszyny z bazą danych zdefiniowana została sieć prywatna, która pozwoli na komunikację ze strony aplikacji. W etapie `provisioning` uruchamiany jest skrypt konfigurujący, w którego wysyłane są: `nazwa bazy danych`, `nazwa użytkownika`, `hasło użytkownika`. Sam skrypt tak jak w poprzednim zadaniu tworzy bazę oraz użytkownika z pełnym dostepem. Do konfiguracji używany jest `Ansible`

### Konfiguracja maszyny z aplikacją

Konfiguracja maszyny z aplikacją:

```ruby
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
```

W przypadku maszyny z konfiguracją skonfigurowano sieć prywatną, tak aby była w tym samym subnecie co maszyna z bazą danych, w celu umożliwienia komunikacji. Dodatkowo dodano opcję `forward_port`, tak aby host mógł uzyskać dostęp do aplikacji z zewnątrz.

## Uruchomienie maszyn

Maszyny uruchamia się za pomocą komendy:

```bash
vagrant up
```

W przypadku działających maszyn możemy je przeładować za pomocą:

```bash
vagrant reload
```

lub ponowanie uruchomić skrypty konfigurujące poprzez wywołanie

```bash
vagrant provision
```

W celu usunięcia maszyn należy uruchomic polecenie:

```bash
vagrant destroy
```

## Weryfikacja działania maszyn

### Sprawdzenie serwisów

Aby sprawdzić czy serwisy uruchomiły się poprawnie należy zalogować się do maszyn wirtualnego, a następnie wykorzystać polecenie `lsof`.

W celu zalogowania się do maszyny z bazą danych należy wykonać polecenie:

```bash
vagrant ssh db
```

#### systemd

MariaDB uruchamiana jest jako serwis dlatego jej działanie można zweryfikować poprzez wykonanie komendy:

```bash
systemctl status mariadb
```

Jej rezultat powinien być następujący:

![db systemd](./screenshots/systemd_db.png)


#### lsof

Baza danych uruchamiana jest domyślnie na porcie `3306`. Efekt wywołania komendy `lsof` powinien być następujący:

![Db lsof](./screenshots/db_lsof.png)

W celu połaczenia się z maszyną backendu należy użyć komendy:

```bash
vagrant ssh app
```
#### systemctl

Aplikacja odpala się jako serwis, dlatego jej działanie można zweryfikować poprzez uruchomienie komendy:

```bash
systemctl status cinema_tickets_app
```

Jej rezultat powinien być następujący:

![App systemd](./screenshots/systemd_app.png)

#### lsof

Backend uruchamia się na porcie `8081`, aby zwalidować jego działanie należy wywołać komendę `lsof`, rezultat powinien być następujący:

![App lsof](./screenshots/app_lsof.png)

### Skrypt testujący

Skrypt sprawdzający czy aplikacja uruchomiła się poprawnie znajduje się w pliku `smoke_test.py` ([Link](./smoke_test.py)) i prezentuje się następująco:

```python
#!/bin/python
import json
import requests

url = 'http://localhost:8081/cinema-tickets-app/api/genres'
response_json = requests.get(url).json()

print('Found genres:')

for genre in response_json:
    id = genre['id']
    name = genre['name']
    print(f"Id: {id}, Name: {name}")
```

Skrypt wykorzystuje jeden z nielicznych endpointów niewymagających autoryzacji do pobrania listy gatunków znajdującej się w bazie danych. Efektem działania skryptu powinna być lista gatunków zaprezentowana poniżej:

![Script results](./screenshots/smoke_test.png)