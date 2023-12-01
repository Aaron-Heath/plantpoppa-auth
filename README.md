# PlantPoppa Authorization
## Description
This is a work-in-progress service that is a part of the PlantPoppa application. This particular service is responsible for security functions such as authorization and authentication. Currently, the PasswordEncoder can generate random salts and use those to encrypt user-provided credentials for storage. The salt is also stored in the database to allow for authentication after user creation. The AuthenticationService uses a user-provided email and password to query the database, pull the correct user credentials, and leverage the existing salt to encrpyt and compare the provided credentials against the stored credentials.

## Next Steps
*  Generate and store unique session token for further authorization after initial credential entry.
*  Create CSRF service that generates tokens for pass back and forth on from the front end to further validate requests.
*  Create CSRF filter to validate the tokens passed by the front end. 
 
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/plantpoppa-auth-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
