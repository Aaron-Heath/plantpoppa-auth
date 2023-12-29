package com.plantpoppa.auth.services;


import com.plantpoppa.auth.dao.SessionRepository;
import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.models.Session;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import com.plantpoppa.auth.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@Component
public class AuthenticationService {
    public final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SecureRandom random = new SecureRandom();

    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder,
                                 UserRepository userRepository,
                                 SessionRepository sessionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public Optional<Session> basicAuth(UserDto userDto) {
        // Return empty if no user found
        Optional<User> validatedUser = this.validateBasicCredentials(userDto);

        if (validatedUser.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(this.createSession(validatedUser.get()));
//            // Encrypt input password with db salt
//            final String encryptedInput = passwordEncoder.encryptPassword(
//                    userDto.getPassword(),
//                    queriedUser.getSalt());
//            if (encryptedInput.equals(queriedUser.getPw_hash())) {
//                return Optional.of(this.createSession(queriedUser));
//            }
        }
    }

    public Optional<User> validateBasicCredentials(UserDto userDto) {
        User queriedUser = userRepository.fetchOneByEmail(userDto.getEmail());
        // Return empty if no user found
        if (queriedUser != null) {
            // Encrypt input password with db salt
            final String encryptedInput = passwordEncoder.encryptPassword(
                    userDto.getPassword(),
                    queriedUser.getSalt());
            if (encryptedInput.equals(queriedUser.getPw_hash())) {
                return Optional.of(queriedUser);
            }
        }
        return Optional.empty();

    }

    public boolean validateToken(String token) {
        System.out.println(token);
        Session validSession = sessionRepository.fetchOneValidToken(token);

        // Return false if no valid session found. Else return true.
        if(validSession == null) {
            return false;
        }
        return true;
    }

    Session createSession(User user) {
        //  Generate token
        // https://stackoverflow.com/questions/13992972/how-to-create-a-authentication-token-using-java
        byte[] randomBytes = new byte[24];
        random.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().encodeToString(randomBytes);

        // Generate expiration date (3 months later)
        LocalDate expiration = LocalDate.now().plusMonths(3);


        // Call SessionRepository.createSession();
        sessionRepository.createSession(user.getUser_id(), token, expiration);

        // return session
        return new Session(user.getUser_id(), token, expiration);
    }

    public int updateUserPassword(UserDto userDto, String newPassword) {
        User storedUser = userRepository.fetchOneByUuid(userDto.getUuid());

        byte[] newSalt = passwordEncoder.generateSalt();
        String encryptedNewPassword = passwordEncoder.encryptPassword(newPassword,
                newSalt);

        String storedPassword = storedUser.getPw_hash();
        byte[] storedSalt = storedUser.getSalt();

        return userRepository.updateUserPw(encryptedNewPassword,
                newSalt,
                storedPassword,
                storedSalt);
    }
}
