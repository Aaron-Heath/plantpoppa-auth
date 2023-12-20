# PlantPoppa Authorization
## Description
This is a work-in-progress service that is a part of the PlantPoppa application. This particular service is responsible for security functions such as authorization and authentication.

This service has the following endpoints:
* `/user` - receives `GET` requests to return a list of all users. This endpoint will be protected and require administrative permissions to use.
* `/user/register` - receives `POST` requests to create users using an email, password, and any optional information the user decides to give (phone number, zip code, etc.) 
* `/user/authenticate` - receives `POST` requests that contain an email and password. If the user successfully authenticates, a token will be returned. That token should be used in any subsequent requests.

Future iterations will include endpoints for more CRUD functions for users. Some will need to be protected and for admin use only.

PasswordEncoder can generate random salts and use those to encrypt user-provided credentials for storage. The salt is also stored in the database to allow for authentication after user creation. The AuthenticationService uses a user-provided email and password to query the database, pull the correct user credentials, and leverage the existing salt to encrpyt and compare the provided credentials against the stored credentials.



 
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/plantpoppa-auth-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
