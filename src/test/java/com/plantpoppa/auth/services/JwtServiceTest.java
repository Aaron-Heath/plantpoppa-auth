package com.plantpoppa.auth.services;


import com.plantpoppa.auth.models.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;

public class JwtServiceTest {
    private UserDto testUserDto = new UserDto
            .UserDtoBuilder()
            .uuid("2cc89d1d-ff26-4e2e-9069-77989e6a3ef2")
            .email("foo.bar@example.com")
            .firstname("Foo")
            .lastname("Bar")
            .phone("9999999999")
            .zip("19038")
            .build();
    private String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYWZmeS5kdWNrQGdtYWlsLmNvbSIsImlzcyI6InBwc2VjNTAwNSIsImV4cCI6MTcxMTU4NzYzNiwidXNlcklkIjoiNTE0MTYxZmUtYTE0MC00NzI2LWFlYmUtOGYxMmFkZDJhOTNiIiwiaWF0IjoxNzExNTAxMjM2fQ.ULlx3M_l9q61thsxpkEU4OibFcLY8ApBnCTfxi8TPm0";
    private String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private JwtService jwtService = new JwtService();

    @Test
    public void testIsTokenExpiredFalse() {
        String freshToken = jwtService.createToken(testUserDto);
        Assertions.assertFalse(jwtService.isTokenExpired(freshToken));
    }

    //Commented out until token expires
//    @Test
//    public void testIsTokenExpiredTrue() {
//        Assertions.assertTrue(jwtService.isTokenExpired(expiredToken));
//    }

    @Test
    public void testIsTokenExpiredException() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            jwtService.isTokenExpired(invalidToken);
        });

        String expectedMessage = "No valid JWT found";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

    }
}
