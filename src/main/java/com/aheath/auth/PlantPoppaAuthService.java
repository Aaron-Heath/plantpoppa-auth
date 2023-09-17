package com.aheath.auth;

import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class PlantPoppaAuthService extends Application {
    public static void main(String[] args) throws Exception {
        new PlantPoppaAuthService().run(args);
    }

    @Override
    public void initialize(Bootstrap<PlantPoppaAuthConfiguration> bootstrap) {
        // nothing to do yet
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    @Override
    public void run(PlantPoppaAuthConfiguration configuration, Environment environment) {
        // nothing to do yet
    }

}
