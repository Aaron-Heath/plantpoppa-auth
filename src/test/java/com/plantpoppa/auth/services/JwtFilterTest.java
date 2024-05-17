package com.plantpoppa.auth.services;

import com.plantpoppa.auth.filters.JwtFilter;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtFilterTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;
    @InjectMocks
    private JwtFilter jwtFilter;
    private MockHttpServletRequest req;
    private MockHttpServletResponse res;
    private MockFilterChain chain;
    @Mock
    private JwtService jwtService;
    @BeforeEach
    public void setup() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.jwtFilter = new JwtFilter();
        this.req = new MockHttpServletRequest();
        this.res = new MockHttpServletResponse();
        this.chain = new MockFilterChain();
        this.jwtService = new JwtService();

    }


    @Test
    public void apiUserFilter_NoJwt_ReturnUnauthorized() throws ServletException, IOException {

        final String expected = "401 UNAUTHORIZED \"Authorization header missing\"";

        req.setRequestURI("/api/user");

        Exception exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            jwtFilter.doFilter(req, res, chain);
        });

        final String actual = exception.getMessage();

        Assertions.assertEquals(expected, actual);

    }

}
