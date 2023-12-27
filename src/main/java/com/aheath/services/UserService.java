package com.aheath.services;

import com.aheath.dao.UserDAO;
import com.aheath.models.User;
import com.aheath.models.UserDto;
import com.aheath.security.PasswordEncoder;
import com.google.inject.Singleton;
import jakarta.inject.Inject;

import java.util.List;

@Singleton
public class UserService {
    private final UserDAO userDAO = null;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public UserService(
//            Jdbi jdbi,
            PasswordEncoder passwordEncoder
    ) {
//        this.userDAO = jdbi.onDemand(UserDAO.class);
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public UserDto createUser(UserDto userDto) {
        // Convert UserDto to User

        User user = new User.UserBuilder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .zip(userDto.getZip())
                .build();

        System.out.println(userDto.getLastname());

        // create user salt
        user.setSalt(this.passwordEncoder.generateSalt());

        // Encrypt and set password
        user.setPw_hash(this.passwordEncoder.encryptPassword(userDto.getPassword(), user.getSalt()));

        // Add user to db
        userDAO.createUser(
                user.getUuid(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPw_hash(),
                user.getPhone(),
                user.getZip(),
                user.getSalt()
        );
        return user.toDto();

    }
}
