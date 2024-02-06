# PlantPoppa Authorization
## Description
This is a work-in-progress service that forms the centralized authentication and authorization layer from the PlantPoppa Application, ensuring secure user access management and data protection.

## Key Features
* **Centralized Authentication:** Handles authentication requests for both basic credentials (username/password) and token-based authentication.
* **User Data Management:** Provides CRUD (Create, Read, Update, Delete) functionality for managing user data.
* **Secure Password Storage:** Employs password encryption using randomly generated salts that are stored alongside encrypted password for effective authentication during future login requests.

**This service has the following base endpoints:**
* `/auth` - Handles authentication requests
* `/api` - Facilitates CRUD operations

## Technology Stack
<div>
    <img src="https://raw.githubusercontent.com/devicons/devicon/55609aa5bd817ff167afce0d965585c92040787a/icons/java/java-original-wordmark.svg" width="50" height="50" alt="Java" title="Java"/>
    <img  src="https://raw.githubusercontent.com/devicons/devicon/55609aa5bd817ff167afce0d965585c92040787a/icons/spring/spring-original-wordmark.svg" height="50" width="50" alt="Spring" title="Spring"/>
</div>

This application runs on the Spring Framework using Java. A previous implementation, built with Dropwizard can be found [here](https://github.com/Aaron-Heath/plantpoppa-auth/tree/dropwizard). The front end of the application is under development using ReactJS and can be found [here](https://github.com/Aaron-Heath/plantpoppa-ui).

## Work-In-Progress Status

Please note that this service is currently under active development and may not yet be suitable for production environments.


