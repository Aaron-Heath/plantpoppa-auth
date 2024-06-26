package com.plantpoppa.auth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plantpoppa.auth.models.InternalClient;
import com.plantpoppa.auth.models.UserDto;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtService {
    private final JWTVerifier verifier;
    private final Algorithm algorithm;
    private final String secretKey = System.getenv("JWT_SECRET");
    private final String issuer = System.getenv("JWT_ISSUER");


    public JwtService() {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public String createUserToken(UserDto userDto) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY,24);

        Date expiration = calendar.getTime();

        return JWT.create()
                .withSubject(userDto.getEmail())
                .withClaim("userId", userDto.getUuid())
                .withClaim("role", userDto.getRole())
                .withClaim("firstName", userDto.getFirstname())
                .withClaim("lastName", userDto.getLastname())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String createServiceToken(InternalClient service) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        // Application token expires every 6 hours
        calendar.add(Calendar.HOUR_OF_DAY,6);

        Date expiration = calendar.getTime();

        return JWT.create()
                .withSubject(service.getTitle())
                .withClaim("serviceId", service.getUuid())
                .withClaim("role", "service")
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public Optional<DecodedJWT> decodeJwt(String token) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(decodedJWT);
    }

    /**
     *
     * @param token
     * @return true if token expiration date has passed.
     */
    public boolean isTokenExpired(String token) {
        Date now = new Date();
        Optional<DecodedJWT> decodedJWT = decodeJwt(token);

        // Throw exception if no valid jwt returned
        if(decodedJWT.isEmpty()) {
            throw new RuntimeException("No valid JWT found");
        }

        DecodedJWT jwt = decodedJWT.get();

        Date expiration = jwt.getExpiresAt();
        return expiration.before(now);
    }

    public boolean isTokenValid(String token) {
        return decodeJwt(token).isPresent();
    }
}
