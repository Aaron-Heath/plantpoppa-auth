package com.plantpoppa.auth.resources;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.plantpoppa.auth.models.*;
import com.plantpoppa.auth.services.AuthenticationService;
import com.plantpoppa.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthResource {
    private final AuthenticationService authenticator;
    private final UserService userService;

    @Autowired
    AuthResource(AuthenticationService authenticator, UserService userService) {
        this.authenticator = authenticator;
        this.userService = userService;

    }

    @GetMapping("/")
    int test() {
        return 1;
    }

    @PostMapping(value = "/basic",
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> basicAuth(@RequestBody UserDto userDto) {
        Optional<JwtResponse> authenticationResponse = authenticator.basicAuth(userDto);
        if (authenticationResponse.isPresent()) {
            return new ResponseEntity<>(authenticationResponse,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);

    }

    @PostMapping(value="/token",
    consumes=MediaType.APPLICATION_JSON_VALUE)
    Optional<DecodedJWT> tokenAuth(@RequestBody Session session) {
        return authenticator.decodeToken(session.getToken());
    }

    @PostMapping(value = "/service/validate-token",
    consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> validateToken(@RequestBody JwtBody jwt) {

        Optional<User> validatedUser = authenticator.validateTokenProvideUser(jwt.getJwt());

        HttpStatus status = validatedUser.isPresent() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

        HashMap<String, String> responseBody = new HashMap<>();

        if(validatedUser.isPresent()) {
            User user = validatedUser.get();
            responseBody.put("uuid", user.getUuid());
            responseBody.put("userId", String.valueOf(user.getUser_id()));
        } else {
            responseBody.put("Message", "Invalid Token");
        }

        return new ResponseEntity<>(responseBody, status);
    }

    @PostMapping(value="/password",
            consumes=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateUserPassword(@RequestBody Map<String, String> body) {
        UserDto userDto= new UserDto.UserDtoBuilder()
                .uuid(body.get("uuid"))
                .password(body.get("password")).build();

        String newPassword = body.get("newPassword");
    try {
        int response = authenticator.updateUserPassword(userDto, newPassword);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } catch(Exception e) {
        return new ResponseEntity<String>("Unauthorized",
                HttpStatus.UNAUTHORIZED);
        }
    }

//    @PostMapping(value="/service/validate-secret",
//    consumes = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<?> validateSecret(@RequestBody InternalClient service) {
//        InternalClient foundService;
//        Map<String, String> response;
//
//        // Try to get valid service based on credentials provided
//        try {
//            foundService = authenticator.validateServiceSecret(service);
//        } catch (CredentialException e) {
//            // Catch error thrown by authenticator if bad credentials are provided.
//            response = new HashMap<>();
//            response.put("message",
//                    e.getMessage());
//            return new ResponseEntity<>(response,
//                    HttpStatus.UNAUTHORIZED);
//        }
//
//        String serviceToken = authenticator.createServiceToken(foundService);
//        response = new HashMap<>();
//
//        response.put("jwt",serviceToken);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @PostMapping(value="/service/refresh-token",
    consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> refreshServiceToken(@RequestBody InternalClient service) {
        HashMap<String, String> body = new HashMap<>();
        try {
            body = authenticator.refreshServiceToken(service);
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (CredentialException e) {
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body,HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            body.put("message", "Something went wrong. Please check logs.");
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
