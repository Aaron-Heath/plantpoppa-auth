package com.aheath;

import com.aheath.filters.TokenFilter;
import com.aheath.filters.TokenFilterFeature;
import com.aheath.resources.UserResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class PlantPoppaAuthApplication extends Application<PlantPoppaAuthConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PlantPoppaAuthApplication().run(args);
    }

    @Override
    public String getName() {
        return "plantpoppa-auth";
    }

    @Override
    public void initialize(final Bootstrap<PlantPoppaAuthConfiguration> bootstrap) {
        //Allow for environment variables in the configuration
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(GuiceBundle.builder()
                .modules(new PlantPoppaAuthModule())
                .build());
    }

    @Override
    public void run(final PlantPoppaAuthConfiguration configuration,
                    final Environment environment) {
//        Injector injector = Guice.createInjector(new PlantPoppaAuthModule(configuration,environment));

        //retrieve user resource
        environment.jersey().register(UserResource.class);
//        environment.jersey().register(TokenFilterFeature.class);
    }


}
