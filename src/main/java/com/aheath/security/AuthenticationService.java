package com.aheath.security;

import com.aheath.api.Session;
import com.aheath.api.User;
import com.aheath.db.SessionDAO;
import org.jdbi.v3.core.Jdbi;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;

public class AuthenticationService {

    // Store password encoder
    private final PasswordEncoder passwordEncoder;
    private final SessionDAO sessionDAO;
    private final SecureRandom random = new SecureRandom();

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
    //    Takes a successfully authenticated user and returns the user and session
    private Session createSession(User user) {
        //  Generate token
        // https://stackoverflow.com/questions/13992972/how-to-create-a-authentication-token-using-java
        byte[] randomBytes = new byte[24];
        random.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().encodeToString(randomBytes);

        // Generate expiration date (3 months later)
        LocalDate expiration = LocalDate.now().plusMonths(3);


        // Call SessionDAO.createSession();
        sessionDAO.createSession(user.getUser_id(), token, expiration);

        // return session
        return new Session(user.getUser_id(), token, expiration);
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
