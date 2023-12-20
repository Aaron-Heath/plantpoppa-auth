package com.aheath.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class TokenFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        // Log request
        System.out.printf("%s request heard for %s.\n",ctx.getMethod(),ctx.getUriInfo().toString());
        MultivaluedMap<String, String> headers = ctx.getHeaders();

        // Print out authentication token
        System.out.println(headers.get("Authentication"));
    }
}
