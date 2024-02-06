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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<UserDto> createdUser = userService.createUser(userDto);

        if(createdUser.isEmpty()) {
            Map<String, String> response = null;
            response.put("message", "Something went wrong");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
