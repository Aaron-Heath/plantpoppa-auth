package com.plantpoppa.auth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;
    private ObjectWriter objectWriter;
    @BeforeEach
    public void setup() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }


    static List<UserDto> provideTestDtos() {
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

//    @ParameterizedTest(name = "Creates, stores, and deletes users")
//    @MethodSource("provideTestDtos")
//    public void createUser_CreatesUserWithValidParameters(UserDto userDto) {
//        Optional<UserDto> optionalUserDto = userService.createUser(userDto);
//        UserDto expectedUser = optionalUserDto.get();
//
//        User queriedUser = userService.loadByEmail(expectedUser.getEmail());
//
//        Assertions.assertEquals(queriedUser.getUuid(), expectedUser.getUuid());
//        Assertions.assertEquals(queriedUser.getFirstname(), expectedUser.getFirstname());
//
//        userService.deleteOneByUuid(queriedUser.toDto());
//    }
}
