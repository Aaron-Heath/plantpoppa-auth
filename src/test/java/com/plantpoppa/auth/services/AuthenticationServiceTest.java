package com.plantpoppa.auth.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticator;


    @Test
    public void testGenerateSecret() {
        final String randomString1 = authenticator.generateSecret();
        final String randomString2 = authenticator.generateSecret();
        System.out.println(randomString1);
        System.out.println(randomString2);

        Assertions.assertFalse(randomString1.equals(randomString2));
    }
}
