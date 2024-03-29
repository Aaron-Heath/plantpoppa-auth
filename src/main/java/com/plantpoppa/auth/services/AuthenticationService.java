package com.plantpoppa.auth.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plantpoppa.auth.dao.SessionRepository;
import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.models.JwtResponse;
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
    private final JwtService jwtService;
    private final SecureRandom random = new SecureRandom();
    private final JWTVerifier verifier;

    private final String secretKey;
    private final Algorithm algorithm;



    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder,
                                 UserRepository userRepository,
                                 SessionRepository sessionRepository,
                                 JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtService = jwtService;
        this.secretKey = System.getenv("JWT_SECRET");
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(algorithm)
                .withIssuer(System.getenv("JWT_ISSUER"))
                .build();
    }

    public Optional<JwtResponse> basicAuth(UserDto userDto) {
        // Return empty if no user found
        Optional<User> validatedUser;
        try {
            validatedUser = this.validateBasicCredentials(userDto);
        } catch (Exception e) {
            validatedUser = Optional.empty();
        }

        if (validatedUser.isEmpty()) {
            return Optional.empty();
        } else {
            UserDto validUserDto = validatedUser.get().toDto();
            String jwt = createToken(validUserDto);

            // Build Response
            return Optional.of(new JwtResponse(jwt,validUserDto));
        }
    }

    public String createToken(UserDto userDto) {
        return jwtService.createToken(userDto);
    }

    public String encryptPassword(String password, byte[] salt) {
        return passwordEncoder.encryptPassword(password, salt);
    }
    public byte[] generateSalt() {
        return passwordEncoder.generateSalt();
    }

    public Optional<User> validateBasicCredentials(UserDto userDto) throws Exception {
        User queriedUser;

        // Check for email and/or uuid
        // UUID only validation
        if(userDto.getUuid() != null && userDto.getEmail() == null) {
            queriedUser = userRepository.fetchOneByUuid(userDto.getUuid());

        // Email only validation
        } else if (userDto.getEmail() != null && userDto.getUuid() == null) {
            queriedUser = userRepository.fetchOneByEmail(userDto.getEmail());

        // Email and UUID validation
        } else if (userDto.getEmail() != null && userDto.getUuid() != null ) {
            queriedUser= userRepository.fetchOneByEmailAndUuid(userDto.getUuid(), userDto.getEmail());

        // Exception if neither is provided
        } else {
            // Throw exception if neither is provided
            throw new Exception("Incomplete credentials.");
        }
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

    public Optional<DecodedJWT> decodeToken(String token) {
        return jwtService.decodeJwt(token);
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

    public int updateUserPassword(UserDto userDto, String newPassword) throws Exception {
        Optional<User> validatedUser = this.validateBasicCredentials(userDto);

        if(validatedUser.isEmpty()) {
            // FIXME:  Throw appropriate exception when user is not authenticated.
            throw new Exception("User not authenticated");
        }

        // Generate new password only if user passed correct credentials
        byte[] newSalt = passwordEncoder.generateSalt();
        String encryptedNewPassword = passwordEncoder.encryptPassword(newPassword,
                newSalt);

        String storedPassword = validatedUser.get().getPw_hash();
        byte[] storedSalt = validatedUser.get().getSalt();

        return userRepository.updateUserPw(encryptedNewPassword,
                newSalt,
                storedPassword,
                storedSalt);
    }
}
