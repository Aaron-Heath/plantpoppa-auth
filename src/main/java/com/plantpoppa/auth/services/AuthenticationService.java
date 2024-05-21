package com.plantpoppa.auth.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plantpoppa.auth.dao.InternalClientRepository;
import com.plantpoppa.auth.dao.SessionRepository;
import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.models.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.security.auth.login.CredentialException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

@Component
public class AuthenticationService {
    public final CredentialSecurityService credentialSecurityService;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final InternalClientRepository clientRepository;
    private final JwtService jwtService;
    private final SecureRandom random = new SecureRandom();
    private final JWTVerifier verifier;
    private final UserService userService;

    private final String secretKey;
    private final Algorithm algorithm;
    private static final int secretLength = 32;



    @Autowired
    public AuthenticationService(CredentialSecurityService credentialSecurityService,
                                 UserRepository userRepository,
                                 UserService userService,
                                 SessionRepository sessionRepository,
                                 InternalClientRepository clientRepository,
                                 JwtService jwtService) {
        this.credentialSecurityService = credentialSecurityService;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.clientRepository = clientRepository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.secretKey = System.getenv("JWT_SECRET");
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(algorithm)
                .withIssuer(System.getenv("JWT_ISSUER"))
                .build();
    }

    public Optional<JwtResponse> basicAuth(UserDto userDto) {
        // Return empty if no user found
        Optional<User> validatedUser;
        validatedUser = this.validateEmailPassword(userDto);

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
        return jwtService.createUserToken(userDto);
    }

    public String encryptPassword(String password, byte[] salt) {
        return credentialSecurityService.encryptPassword(password, salt);
    }

    public byte[] generateSalt() {
        return credentialSecurityService.generateSalt();
    }

    public String generateSecret() {
    byte[] bytes = new byte[secretLength];
    random.nextBytes(bytes);
    return Base64.getUrlEncoder().encodeToString(bytes);
}
/**
 * @param userDto must have an email AND a password to be validated.
 * @return Optional User if user is found AND passwords match. Else returns Optional.empty()
 * */
    public Optional<User> validateEmailPassword(UserDto userDto) {

        Optional<User> queriedUser = userService.loadByEmail(userDto.getEmail());
        // Return empty if no user found
        if (queriedUser.isPresent()) {
            User foundUser = queriedUser.get();
            // Encrypt input password with db salt
            final String encryptedInput = credentialSecurityService.encryptPassword(
                    userDto.getPassword(),
                    foundUser.getSalt());
            if (encryptedInput.equals(foundUser.getPw_hash())) {
                return Optional.of(foundUser);
            }
        }
        return Optional.empty();

    }

    public Optional<DecodedJWT> decodeToken(String token) {
        return jwtService.decodeJwt(token);
    }

    public Optional<User> validateTokenProvideUser(String token) {
        // If token is valid, query for user. Return user.
        Optional<DecodedJWT> decodedJwt = decodeToken(token);
        User validatedUser;
        if (decodedJwt.isPresent()) {
            String uuid = decodedJwt.get().getClaim("userId").asString();
            validatedUser = userRepository.fetchOneByUuid(uuid);

            return Optional.ofNullable(validatedUser);
        }
        return Optional.empty();
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
        Optional<User> validatedUser = this.validateEmailPassword(userDto);

        if(validatedUser.isEmpty()) {
            throw new Exception("User not authenticated");
        }

        // Generate new password only if user passed correct credentials
        byte[] newSalt = credentialSecurityService.generateSalt();
        String encryptedNewPassword = credentialSecurityService.encryptPassword(newPassword,
                newSalt);

        String storedPassword = validatedUser.get().getPw_hash();
        byte[] storedSalt = validatedUser.get().getSalt();

        return userRepository.updateUserPw(encryptedNewPassword,
                newSalt,
                storedPassword,
                storedSalt);
    }

    // --------- Internal Service Methods --------- \\

    public String createServiceToken(InternalClient service) {return jwtService.createServiceToken(service);}

    /**
     * @return HashMap with {
     *     newRefreshToken,
     *     newJwt
     * }
     * */
    public HashMap<String, String> refreshServiceToken(InternalClient service) throws CredentialException {
        HashMap<String, String> result = new HashMap<>();

        // Validate credentials provided by the service. Throws an error if invalid.
        InternalClient foundClient = this.validateServiceSecret(service);

        // If the refresh tokens don't match, throw CredentialException
        if(!service.getRefreshToken().equals(foundClient.getRefreshToken())) {
            throw new CredentialException("Invalid Refresh token");
        }

        // Generate new refresh token and jwt. Store in result
        String newRefreshToken = this.generateSecret();
        clientRepository.updateInternalClientRefreshToken(newRefreshToken, foundClient.getServiceId());
        String newJwt = this.createServiceToken(foundClient);

        result.put("refreshToken", newRefreshToken);
        result.put("jwt", newJwt);

        return result;

    }

    /**
     * Validates internal service credentials. Returns a client if credentials are valid.
     * Throws a credential exception if they are not.
     * @param service with String uuid and String secret.
     * */
    public InternalClient validateServiceSecret(InternalClient service) throws CredentialException{

        if(service.getUuid().isEmpty()) {
            throw new CredentialException("Missing service uuid");
        }

        if(service.getSecret().isEmpty()) {
            throw new CredentialException("Missing service secret");
        }

        final String clearSecret = service.getSecret();
        final Optional<InternalClient> queriedService = clientRepository.fetchOneByUuid(service.getUuid());
        // If no service found with uuid
        if(queriedService.isEmpty()) {
            throw new CredentialException("Invalid uuid provided");
        }

        final InternalClient foundService = queriedService.get();

        // hash clear secret and compare with queried secret
        final String hashedSecret = encryptPassword(clearSecret, foundService.getSalt());

        if(!hashedSecret.equals(foundService.getSecret())) {
            throw new CredentialException("Invalid Secret provided");
        }

        return foundService;
    }
}
