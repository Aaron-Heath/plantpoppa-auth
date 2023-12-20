package com.aheath.services;

import com.aheath.dao.UserDAO;
import com.aheath.models.Session;
import com.aheath.models.User;
import com.aheath.dao.SessionDAO;
import com.aheath.models.UserDto;
import com.aheath.security.PasswordEncoder;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jdbi.v3.core.Jdbi;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

public class AuthenticationService {

    // Store password encoder
    private final PasswordEncoder passwordEncoder;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;
    private final SecureRandom random = new SecureRandom();

    @Inject
    public AuthenticationService(@Named("user") Jdbi jdbi) {
        this.sessionDAO = jdbi.onDemand(SessionDAO.class);
        this.userDAO = jdbi.onDemand(UserDAO.class);
    }

    {
        try {
            passwordEncoder = new PasswordEncoder();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    //    Takes a successfully authenticated user and returns the user and session
    public Session createSession(User user) {
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
    public Optional<Session> authenticateUser(UserDto userDto) {
        // TODO: Handle when no user is found with email input
        User queriedUser = this.userDAO.getUserByEmail(userDto.getEmail());




        // Encrypt input password with same salt from db
        final String encryptedInput = this.passwordEncoder.encryptPassword(userDto.getPassword(), queriedUser.getSalt());

        // If input password validates to the same result, returns true. Otherwise, returns false.
        if(encryptedInput.equals(queriedUser.getPw_hash())){
            Session session = this.createSession(queriedUser);
            return Optional.of(session);
        }

        return Optional.empty();
    }
}
