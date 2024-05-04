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
        service.setSalt(authenticator.generateSalt());
        String clearSecret = authenticator.generateSecret();

        service.setSecret(authenticator.encryptPassword(clearSecret,
                service.getSalt()));

        HashMap<String, String> result = new HashMap<>();


            int createId = repository.createApplicationService(
                    service.getUuid(),
                    service.getTitle(),
                    service.getSecret(),
                    service.getSalt()
            );
            System.out.println("Service registered");
            result.put("clientSecret", clearSecret);
            result.put("clientId", service.getUuid());
            return result;
    }
}
