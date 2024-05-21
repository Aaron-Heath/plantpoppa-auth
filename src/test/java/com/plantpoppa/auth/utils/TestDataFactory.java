package com.plantpoppa.auth.utils;

import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDataFactory {
    private final Random randomizer = new Random();
    private final List<String> firstNames = List.of(
            "Peter",
            "Samuel",
            "Chelsea",
            "Hector",
            "Courtney",
            "Rebecca",
            "Andrew",
            "Marie",
            "Hank"
    );

    private final List<String> lastNames = List.of(
            "Smith",
            "Brown",
            "Hughes",
            "Jackman",
            "Rabbit",
            "Swift",
            "Kelce",
            "Hopkins",
            "Williamson"
    );

    private final List<String> passwords = List.of(
            "badPassword",
            "worsePassword",
            "p4ssw0r6"
    );



    private TestDataFactory() {
    }

    public UserDto provideRequiredUserDto() {
        String firstName = provideRandomFirstName();
        String lastname = provideRandomLastName();
        String email = firstName + "." + lastname + "@email.com";
        String password = provideRandomPassword();

        return new UserDto.UserDtoBuilder()
                .firstname(firstName)
                .lastname(lastname)
                .password(password)
                .email(email)
                .build();
    }

    public List<UserDto> provideRequiredUserDtoList() {
        List<UserDto> userDtoList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            userDtoList.add(provideRequiredUserDto());
        }

        return userDtoList;
    }

    public List<UserDto> provideRequiredUserDtoList(int amount) {
        List<UserDto> userDtoList = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            userDtoList.add(provideRequiredUserDto());
        }

        return userDtoList;

    }

    public String provideRandomFirstName() {
        return firstNames.get(randomizer.nextInt(firstNames.size()));
    }

    public String provideRandomLastName() {
        return lastNames.get(randomizer.nextInt(lastNames.size()));
    }

    public String provideRandomPassword() {
        return lastNames.get(randomizer.nextInt(lastNames.size()));
    }

}
