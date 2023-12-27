package com.plantpoppa.auth.resources;

import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserRepository repository;

    @Inject
    UserResource(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @GetMapping("/")
    List<UserDto> all() {
        List<User> users = repository.findAllUsers();

        // Convert List of Users to list of UserDtos. Sanitize password.
        ArrayList<UserDto> userDtoList = new ArrayList<>();
        users.forEach(user -> {
            userDtoList.add(
                    new UserDto.UserDtoBuilder()
                            .uuid(user.getUuid())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .email(user.getEmail())
                            .phone(user.getPhone())
                            .zip(user.getZip())
                            .build()
            );
        });
//        return repository.test();

        return userDtoList;
    }
}
