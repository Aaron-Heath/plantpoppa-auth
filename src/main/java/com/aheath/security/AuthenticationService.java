package com.aheath.security;

import java.security.NoSuchAlgorithmException;

public class AuthenticationService {
    // Store password encoder
    private final PasswordEncoder passwordEncoder;

    {
        try {
            passwordEncoder = new PasswordEncoder();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Compare user provided credentials with those pulled from database
    public boolean authenticateUser(String passwordInput, final String validPassword, final byte[] validSalt) {
        // If user successfully validates, return true
        // Encrypt input password with same salt from db
        final String encryptedInput = this.passwordEncoder.encryptPassword(passwordInput,validSalt);

        // If input password validates to the same result, returns true. Otherwise, returns false.
        return encryptedInput.equals(validPassword);
    }
}
