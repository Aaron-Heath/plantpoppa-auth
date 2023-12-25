package com.aheath.filters;

import com.aheath.services.AuthenticationService;
import com.google.inject.Singleton;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Singleton
public class TokenFilter implements ContainerRequestFilter {
    private final AuthenticationService authenticator;

    @Inject
    public TokenFilter(AuthenticationService authenticationService) {
        this.authenticator = authenticationService;
    }

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        // Log request
        System.out.printf("%s request heard for %s.\n", ctx.getMethod(), ctx.getUriInfo().toString());
        MultivaluedMap<String, String> headers = ctx.getHeaders();

        // Remove open and closing brackets from token.
        String token = headers.get("Authentication").toString().replaceAll("[\\[\\]]", "");


        // Print out authentication token
        System.out.println(token);
        boolean authenticated = authenticator.validateSession(token);

        if (!authenticated) {
            Exception cause = new NotAuthorizedException("Bad authentication credentials");
            throw new WebApplicationException(cause, Response.Status.UNAUTHORIZED);
        }

        System.out.println(authenticated);
    }
}
