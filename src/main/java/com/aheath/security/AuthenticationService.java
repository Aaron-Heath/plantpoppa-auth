package com.aheath.security;

import com.aheath.api.Session;
import com.aheath.db.SessionDAO;
import org.jdbi.v3.core.Jdbi;

import java.security.NoSuchAlgorithmException;

public class AuthenticationService {

    // Store password encoder
    private final PasswordEncoder passwordEncoder;
    private final SessionDAO sessionDAO;

    public AuthenticationService(Jdbi jdbi) {
        this.sessionDAO = jdbi.onDemand(SessionDAO.class);
    }

    {
        try {
            passwordEncoder = new PasswordEncoder();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    //    Takes a successfully authenticated user and returns a session
    private Session createSession(int user_id) {
        //Generate token

        // Generate expiration date

        // Call SessionDAO.createSession();


        return null;
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
