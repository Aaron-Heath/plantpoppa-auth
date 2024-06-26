package com.plantpoppa.auth.resources;

import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.models.Session;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import com.plantpoppa.auth.services.UserService;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping(value="/register",
    consumes=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        // Check for missing required parameters
        if(
                userDto.getEmail() == null ||
                userDto.getPassword() == null ||
                userDto.getFirstname() == null ||
                userDto.getLastname() == null
        ) throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Request missing required fields"
        );
        Optional<UserDto> createdUser = userService.createUser(userDto);

        if(createdUser.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "A user with this email already exists.");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(createdUser,
                HttpStatus.OK);
    }

    @PutMapping(value="/firstname",
    consumes= MediaType.APPLICATION_JSON_VALUE)
    int updateUserFirstname(@RequestBody UserDto userDto) {
        return userService.updateUserFirstname(userDto);
    }

    @PutMapping(value="/lastname",
            consumes= MediaType.APPLICATION_JSON_VALUE)
    int updateUseLastname(@RequestBody UserDto userDto) {
        return userService.updateUserLastname(userDto);
    }

    @PutMapping(value="/phone",
            consumes= MediaType.APPLICATION_JSON_VALUE)
    int updateUserPhone(@RequestBody UserDto userDto) {
        return userService.updateUserPhone(userDto);
    }

    @PutMapping(value="/zip",
            consumes= MediaType.APPLICATION_JSON_VALUE)
    int updateUserZip(@RequestBody UserDto userDto) {
        return userService.updateUserZip(userDto);
    }

}
