# PlantPoppa Authorization
## Description
This is a work-in-progress service that is a part of the PlantPoppa application. This service is a centralized authentication and authorization layer for the rest of the application. 

This service has the following base endpoints:
* `/auth` - receives requests to authenticate either basic credentials or tokens and sessions.
* `/api` - receives requests to perform CRUD functions on user data.

PasswordEncoder can generate random salts and use those to encrypt user-provided credentials for storage. The salt is also stored in the database to allow for authentication after user creation. The AuthenticationService uses a user-provided email and password to query the database, pull the correct user credentials, and leverage the existing salt to encrpyt and compare the provided credentials against the stored credentials.



 
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/plantpoppa-auth-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
