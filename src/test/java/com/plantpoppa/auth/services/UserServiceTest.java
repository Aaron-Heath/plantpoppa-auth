package com.plantpoppa.auth.services;

import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    List<UserDto> provideTestDtos() {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(new UserDto.UserDtoBuilder()
                    .firstname("Sarah")
                    .lastname("Marshall")
                    .email("sarah.marshall@gmail.com")
                    .password("notasecurepassword")
                    .build());

        userDtos.add(new UserDto.UserDtoBuilder()
                .firstname("Michael")
                .lastname("Cunningham")
                .email("m.c@themail.com")
                .password("notasecurepassword")
                .phone("2225554499")
                .zip("20895")
                .build());
        return userDtos;
    }

    @ParameterizedTest
    @MethodSource("provideTestDtos")
    public void createUser_CreatesUserWithValidParameters(UserDto userDto) {
        Optional<UserDto> createdUser = userService.createUser(userDto);

        queriedUser = userService.


    }
}
