package com.plantpoppa.auth.filters;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Order(2)
public class JwtFilter implements Filter {

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
        // Skip filter if path is /basic
        if(req.getMethod().equals("/auth/basic")) {
            chain.doFilter(request, response);
            return;
        }

        Optional<String> tokenParam = Optional.ofNullable(req.getHeader("AUTHORIZATION"));
        if(tokenParam.isEmpty()) {
            sendUnauthorizedResponse(response);
        }
        //TODO: Continue token validation

        // continue
        chain.doFilter(request, response);
    }

    private void sendUnauthorizedResponse(ServletResponse response) {
        HttpServletResponse res = (HttpServletResponse) response;
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "No Authorization Header");

        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try {
            res.sendError(401,"No Authorization Header");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
