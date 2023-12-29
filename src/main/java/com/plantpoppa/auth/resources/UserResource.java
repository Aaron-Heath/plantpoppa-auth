package com.plantpoppa.auth.resources;

import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import com.plantpoppa.auth.services.UserService;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    // FIXIT: Not working. May have to do with null phone values
    @PutMapping(value="/phone",
            consumes= MediaType.APPLICATION_JSON_VALUE)
    int updateUsePhone(@RequestBody UserDto userDto) {
        return userService.updateUserPhone(userDto);
    }


}
