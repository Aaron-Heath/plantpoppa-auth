package com.plantpoppa.auth.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class JwtFilter implements Filter {

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




        // continue
        chain.doFilter(request, response);

    }
}
