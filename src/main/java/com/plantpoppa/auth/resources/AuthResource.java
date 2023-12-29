package com.plantpoppa.auth.resources;

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
    Optional<Session> basicAuth(@RequestBody UserDto userDto) {
        return authenticator.basicAuth(userDto);
    }

    @PostMapping(value="/token",
    consumes=MediaType.APPLICATION_JSON_VALUE)
    boolean tokenAuth(@RequestBody Session session) {
        return authenticator.validateToken(session.getToken());
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
