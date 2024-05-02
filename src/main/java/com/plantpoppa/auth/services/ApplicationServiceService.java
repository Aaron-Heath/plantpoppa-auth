package com.plantpoppa.auth.services;

import com.plantpoppa.auth.dao.ApplicationServiceRepository;
import com.plantpoppa.auth.models.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationServiceService {
    private final ApplicationServiceRepository repository;
    private final AuthenticationService authenticator;

    @Autowired
    public ApplicationServiceService(ApplicationServiceRepository repository,
                                     AuthenticationService authenticator) {
        this.repository = repository;
        this.authenticator = authenticator;
    }

    public Optional<ApplicationService> registerApplicationService(ApplicationService service) {
        System.out.println("Registering new service");
        service.setSalt(authenticator.generateSalt());
        String clearSecret = authenticator.generateSecret();

        service.setSecret(authenticator.encryptPassword(clearSecret,
                service.getSalt()));

        try {
            int result = repository.createApplicationService(
                    service.getUuid(),
                    service.getTitle(),
                    service.getSecret(),
                    service.getSalt()
            );
            System.out.println("Service registered");
            return Optional.of(service);
        } catch (Exception e) {
            System.out.println(e);
            return Optional.empty();
        }
    }
}
