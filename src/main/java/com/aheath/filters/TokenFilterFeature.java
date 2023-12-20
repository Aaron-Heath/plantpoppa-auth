package com.aheath.filters;

import com.aheath.annotations.AuthenticationRequired;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TokenFilterFeature implements DynamicFeature {
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if(resourceInfo.getResourceMethod().getAnnotation(AuthenticationRequired.class) != null){
            context.register(TokenFilter.class);
        }
    }
}
