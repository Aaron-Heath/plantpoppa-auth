package com.aheath;

import com.aheath.resources.UserResource;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import jakarta.inject.Named;
import org.jdbi.v3.core.Jdbi;

public class PlantPoppaAuthModule extends AbstractModule {
    private final PlantPoppaAuthConfiguration configuration;
    private final Environment environment;

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
        final JdbiFactory factory = new JdbiFactory();
        return factory.build(environment, configuration.getDataSourceFactory(), "user");
    }

    @Provides
    public UserResource provideUserResource(@Named("user") Jdbi jdbi) {
        return new UserResource(jdbi);
    }
}
