package com.plantpoppa.auth.services;

import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDto> findAllUsers(){
        List<User> users = this.repository.fetchAll();

        ArrayList<UserDto> userDtoList = new ArrayList<UserDto>();

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
        return userDtoList;

    }
}
