# Cinema Tickets Sales System
## Build status: 
[![CircleCI](https://circleci.com/gh/pwr-piisw/piisw_app/tree/master.svg?style=svg&circle-token=a588a8588b64e62a101721eb308624765d91cc3e)](https://circleci.com/gh/pwr-piisw/piisw_app/tree/master)

## How to run it
1. Clone the repo and import it as maven project
2. Install MySql Server and set password as authentication method 
3. Go to  .../src/main/resources/application.properties. It's file containing server configuration.
```
jdbc:mysql://localhost:3306/cinema_tickets_app_db
spring.datasource.username= root
spring.datasource.password= root123
```
Create database named cinema_tickets_app_db with root's password root123.

4. Build project with maven and run it as Java Application

## Backend API endpoints manual
While the server is running, you may interact with it in the following ways:
1. sign up through URL `localhost:5000/api/auth/signup` with the following POST form:
```
{
	"name": "Johnny",
	"surname": "Smith",
	"username": "Jonny123",
	"password": "myweakpassword123",
	"email": "johnny@example.com",
	"phoneNumber": "12345678"
}
```
2. sign in through URL `localhost:5000/api/auth/signin` with the following POST form:
```
{
  "usernameOrEmail": "Jonny123",
  "password": "myweakpassword123"
}
```
3. check, whether an email is taken, through `localhost:5000/api/users/check/email/{GIVEN_EMAIL}` (GET); returns JSON:
```
{
  "isAvailable": "true"
}
```
4. check, whether a username is taken, through `localhost:5000/api/users/check/username/{GIVEN_USERNAME}` (GET); returns JSON:
```
{
  "isAvailable": "true"
}
```
5. get user's data suited for userpanel through `localhost:5000/api/user/current` (GET) (signed-in-user's session token in request's header); returns JSON:
```
{
  "id": 1,
  "username": "janek12",
  "name": "Jan Kowalski",
  "email": "janek@wp.pl"
}
```
