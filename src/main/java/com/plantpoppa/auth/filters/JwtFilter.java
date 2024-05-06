package com.plantpoppa.auth.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.plantpoppa.auth.services.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;

@Component
@Order(2)
public class JwtFilter implements Filter {
    private final List<String> adminPaths = List.of(
            "/api/user/",
            "/service/register"
    );

    private final List<String> noAuthPath = List.of(
            "/auth/basic",
            "/api/user/register",
            "/auth/service/refresh-token",
            "/auth/service/authenticate"
    );

    private final List<String> servicePaths = List.of(
            "/auth/service/validate-token"
    );

    @Autowired
    private JwtService jwtService;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)  request;

        // Skip filter if path does not require authentication
        if(noAuthPath.contains(req.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // Get authorization token from authorization header
        Optional<String> tokenParam = Optional.ofNullable(req.getHeader("AUTHORIZATION")); // Expecting Bearer <TOKEN>
        if(tokenParam.isEmpty()) {
            sendUnauthorizedResponse(response);
            return;
        }
        // Expecting
        final String token = StringUtils.replace(tokenParam.get(),"Bearer ","");
        // If decoded jwt returns an empty optional, send unauthorized response.
        Optional<DecodedJWT> decodedJwtOptional = jwtService.decodeJwt(token);
        if(decodedJwtOptional.isEmpty()) {
            sendUnauthorizedResponse(response);
            return;
        }

        DecodedJWT decodedToken = decodedJwtOptional.get();

        final String requesterRole = decodedToken.getClaim("role").asString();
        final String requestURI = req.getRequestURI();

        // Check if token is valid
        if(!jwtService.isTokenValid(token)) {
            sendUnauthorizedResponse(response);
            return;
        }

        // Check if token is Expired
        if(jwtService.isTokenExpired(token)) {
            sendExpiredResponse(response);
            return;
        }

        // Check if path is for admins and user is admin
        if(adminPaths.contains(requestURI) && !requesterRole.equals(System.getenv("JWT_ADMIN_ROLE"))) {
            sendForbiddenResponse(response);
            return;
        }
        //Check if path is for services only
        if(servicePaths.contains(requestURI) && !requesterRole.equals("service")) {
            sendForbiddenResponse(response);
            return;
        }

            chain.doFilter(request, response);

    }

    private void sendExpiredResponse(ServletResponse response) {
        HttpServletResponse res = (HttpServletResponse) response;

        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType(MediaType.TEXT_HTML_VALUE);

        try {
            res.sendError(401,"EXPIRED TOKEN");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendForbiddenResponse(ServletResponse response) {
        HttpServletResponse res = (HttpServletResponse) response;

        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.setContentType(MediaType.TEXT_HTML_VALUE);

        try {
            res.sendError(403,"NOT AUTHORIZED TO ACCESS THIS RESOURCE");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendUnauthorizedResponse(ServletResponse response) {

        HttpServletResponse res = (HttpServletResponse) response;

        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType(MediaType.TEXT_HTML_VALUE);

        try {
            res.sendError(401,"MISSING OR MALFORMED AUTHORIZATION HEADER");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
