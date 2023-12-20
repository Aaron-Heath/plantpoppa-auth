package com.aheath;

import com.aheath.resources.UserResource;
import com.aheath.security.PasswordEncoder;
import com.aheath.services.AuthenticationService;
import com.aheath.services.UserService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import jakarta.inject.Named;
import org.jdbi.v3.core.Jdbi;

import java.security.NoSuchAlgorithmException;

public class PlantPoppaAuthModule extends AbstractModule {
    private final PlantPoppaAuthConfiguration configuration;
    private final Environment environment;
    private Jdbi jdbi;

    public PlantPoppaAuthModule(PlantPoppaAuthConfiguration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {

    }

    @Provides
    @Named("user")
    public Jdbi provideUserJdbi() {
        // https://stackoverflow.com/questions/29203709/jdbi-guice-with-dropwizard
        if (jdbi == null) {
            final JdbiFactory factory = new JdbiFactory();
            jdbi = factory.build(environment, configuration.getDataSourceFactory(), "user");
        }
        return jdbi;
    }

    @Provides
    public UserResource provideUserResource(@Named("authenticator") AuthenticationService authenticationService, @Named("userService") UserService userService) {
        return new UserResource(authenticationService, userService);
    }

    @Provides
    @Named("passwordEncoder")
    public PasswordEncoder providePasswordEncoder(){
        try {
            return new PasswordEncoder();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Named("authenticator")
    public AuthenticationService provideAuthenticationService(@Named("user") Jdbi jdbi) {
        return new AuthenticationService(jdbi);
    }

    @Provides
    @Named("userService")
    public UserService userService(@Named("user") Jdbi jdbi, @Named("passwordEncoder") PasswordEncoder passwordEncoder) {
        return new UserService(jdbi, passwordEncoder);
    }

    // DAO Providers
//    @Provides
//    public UserDAO provideUserDao(@Named("user") Jdbi jdbi) {
//        return jdbi.onDemand(UserDAO.class);
//    }
}
