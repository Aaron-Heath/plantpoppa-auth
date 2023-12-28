package com.plantpoppa.auth.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Order(1)
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // Print timestamp, IP address, method, and endpoint for request
        System.out.printf("%s  %s %s request for %s\n",
                LocalDateTime.now(),req.getRemoteAddr(),req.getMethod(), req.getRequestURI());
    }
}
