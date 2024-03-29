package com.plantpoppa.auth.resources;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.plantpoppa.auth.models.JwtResponse;
import com.plantpoppa.auth.models.Session;
import com.plantpoppa.auth.models.UserDto;
import com.plantpoppa.auth.services.AuthenticationService;
import com.plantpoppa.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
