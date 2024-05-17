package com.plantpoppa.auth.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.plantpoppa.auth.models.UserDto;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EndpointExceptionsTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;
    private ObjectWriter objectWriter;
    @BeforeEach
    public void setup() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesAuthResource() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
        Assertions.assertNotNull(webApplicationContext.getBean("authResource"));
    }

    @ParameterizedTest
    @CsvSource({"user1@email.com, password", "john.doe@example.com, pass", " , ", "user1@gmail.com, ", " , password"})
    public void basicAuth_BadCredentials_ReturnsUnauthorized(String email, String password) {
        HashMap<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", email);
        loginRequest.put("password", password);

        String body;
        try {
            body = objectWriter.writeValueAsString(loginRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            mvc.perform(post("/auth/basic")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isUnauthorized())
                    .andExpect(result -> Assertions.assertEquals("401 UNAUTHORIZED \"Invalid email or password\"", result.getResolvedException().getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    @ParameterizedTest
    @CsvSource({", mypass, fir,la",
            "password@missing.com, , FirstName, LastName",
            "foob.bar@baz.com, thisismysecurepassword, , lastNameOnly",
            "this@gmail.com, thisismysupersecurepassw0rd, First, "
    })
    public void apiUserRegister_MissingFields_ReturnBadRequest(String email, String password, String firstname, String lastname) {
        String expected = "400 BAD_REQUEST \"Request missing required fields\"";
        UserDto testUser = new UserDto.UserDtoBuilder()
                .email(email)
                .password(password)
                .firstname(firstname)
                .lastname(lastname)
                .build();

        String body = null;
        try {
            body = objectWriter.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            mvc.perform(post("/api/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> Assertions.assertEquals(expected, result.getResolvedException().getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    }
