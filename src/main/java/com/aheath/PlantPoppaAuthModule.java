package com.aheath;

import com.aheath.filters.TokenFilter;
import com.aheath.resources.UserResource;
import com.aheath.security.PasswordEncoder;
import com.aheath.services.AuthenticationService;
import com.aheath.services.UserService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import jakarta.inject.Named;
import org.jdbi.v3.core.Jdbi;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

import java.security.NoSuchAlgorithmException;

public class PlantPoppaAuthModule extends DropwizardAwareModule<PlantPoppaAuthConfiguration> {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public Jdbi provideUserJdbi() {
        // https://stackoverflow.com/questions/29203709/jdbi-guice-with-dropwizard
            return new JdbiFactory().build(environment(), configuration().getDataSourceFactory(), "jdbi");
    }

//    @Provides
//    public UserResource provideUserResource(@Named("authenticator") AuthenticationService authenticationService, @Named("userService") UserService userService) {
//        return new UserResource(authenticationService, userService);
//    }
//
//    @Provides
//    @Named("passwordEncoder")
//    public PasswordEncoder providePasswordEncoder(){
//        try {
//            return new PasswordEncoder();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Provides
//    @Named("authenticator")
//    public AuthenticationService provideAuthenticationService(@Named("user") Jdbi jdbi) {
//        return new AuthenticationService(jdbi);
//    }
//
//    @Provides
//    @Named("userService")
//    public UserService userService(@Named("user") Jdbi jdbi, @Named("passwordEncoder") PasswordEncoder passwordEncoder) {
//        return new UserService(jdbi, passwordEncoder);
//    }
//
//    @Provides
//    @Named("tokenFilter")
//    public TokenFilter TokenFilter(@Named("authenticator") AuthenticationService authenticationService) {
//        return new TokenFilter(authenticationService);
//    }

    // DAO Providers
//    @Provides
//    public UserDAO provideUserDao(@Named("user") Jdbi jdbi) {
//        return jdbi.onDemand(UserDAO.class);
//    }
}
