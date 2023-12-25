package com.aheath;

import com.aheath.filters.TokenFilter;
import com.aheath.resources.UserResource;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

public class PlantPoppaAuthModule extends DropwizardAwareModule<PlantPoppaAuthConfiguration> {

    @Override
    protected void configure() {
        binder().bind(TokenFilter.class);
        binder().bind(UserResource.class);
    }
}
