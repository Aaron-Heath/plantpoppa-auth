package com.plantpoppa.auth.services;

import com.plantpoppa.auth.dao.InternalClientRepository;
import com.plantpoppa.auth.models.InternalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InternalClientService {
    private final InternalClientRepository repository;
    private final AuthenticationService authenticator;

    @Autowired
    public InternalClientService(InternalClientRepository repository,
                                 AuthenticationService authenticator) {
        this.repository = repository;
        this.authenticator = authenticator;
    }

    /**
     * Creates and registers a new service follows a separate authorization and authentication flow.
     *
     * THIS IS THE ONLY TIME THE CLIENT SECRET IS ACCESSIBLE IN PLAINTEXT. IT MUST BE STORED FOR USE LATER.
     *
     * @param service as instance of InternalClient
     * @return HashMap with the clientSecret and clientId.
     */
    public HashMap<String, String> registerApplicationService(InternalClient service) {
        System.out.println("Registering new service");

        // Generate and set refresh token
        String refreshToken = authenticator.generateSecret();
        service.setRefreshToken(refreshToken);

        // Generate and store salt and salted and hashed secret
        service.setSalt(authenticator.generateSalt());
        String clearSecret = authenticator.generateSecret();
        service.setSecret(authenticator.encryptPassword(clearSecret,
                service.getSalt()));

        HashMap<String, String> result = new HashMap<>();


            int createId = repository.createApplicationService(
                    service.getUuid(),
                    service.getTitle(),
                    service.getSecret(),
                    service.getSalt(),
                    service.getRefreshToken()
            );
            System.out.println("Service registered");

            // Return map with clearSecret (for use in client system),
            // clientId (uuid of service)
            // refreshToken (for token expiration)
            result.put("clientSecret", clearSecret);
            result.put("clientId", service.getUuid());
            result.put("refreshToken", service.getRefreshToken());
            return result;
    }
}
