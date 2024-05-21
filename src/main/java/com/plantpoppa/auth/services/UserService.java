package com.plantpoppa.auth.services;

import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.exceptions.EmailNotFoundException;
import com.plantpoppa.auth.exceptions.UserNotFoundException;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private final UserRepository repository;
//    private final AuthenticationService authenticator;
    private final CredentialSecurityService credentialSecurityService;

    @Autowired
    public UserService(UserRepository repository,
                       CredentialSecurityService credentialSecurityService) {
        this.repository = repository;
        this.credentialSecurityService = credentialSecurityService;
    }

    public User loadByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email not found"));
    }

    public User loadByUuid(String uuid) {
        return repository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException("User not found with given uuid"));
    }


    public Optional<UserDto> createUser(UserDto userDto) {
        System.out.println("User service creating new user");
        // Create salt and hashed password
        byte[] salt = credentialSecurityService.generateSalt(); //authenticator.generateSalt();
        String passwordHash = credentialSecurityService.encryptPassword(userDto.getPassword(), salt);// authenticator.encryptPassword(userDto.getPassword(), salt);

        // Convert to user object
        User newUser = new User.UserBuilder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .zip(userDto.getZip())
                .pw_hash(passwordHash)
                .salt(salt).build();
        // Insert newUser into DB
        try {
            int result = repository.createUser(
                    newUser.getUuid(),
                    newUser.getFirstname(),
                    newUser.getLastname(),
                    newUser.getEmail(),
                    newUser.getPhone(),
                    newUser.getZip(),
                    newUser.getPw_hash(),
                    newUser.getSalt());

            return Optional.of(newUser.toDto());
        } catch (DataIntegrityViolationException e) {
            System.out.println(e);
            return Optional.empty();
        }
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
                            .role(user.getRole())
                            .build()
            );
        });
        return userDtoList;
    }

    public int deleteOneByUuid(UserDto userDto) {
        return repository.deleteOneByUuid(userDto.getUuid());
    }

    public int updateUserFirstname(UserDto userDto) {
        // Convert dto to user
        User storedUser = this.repository.fetchOneByUuid(userDto.getUuid());
        String newName = userDto.getFirstname();
        String storedName = storedUser.getFirstname();
        String uuid = storedUser.getUuid();

        return repository.updateUserFirstname(newName,
                uuid,
                storedName);
    }

    public int updateUserLastname(UserDto userDto) {
        // Convert dto to user
        User storedUser = this.repository.fetchOneByUuid(userDto.getUuid());
        String newName = userDto.getLastname();
        String storedName = storedUser.getLastname();
        String uuid = storedUser.getUuid();

        return repository.updateUserLastname(newName,
                uuid,
                storedName);
    }

    public int updateUserPhone(UserDto userDto) {
        User storedUser = this.repository.fetchOneByUuid(userDto.getUuid());
        String newPhone = userDto.getPhone();
        String storedPhone = storedUser.getPhone();
        String uuid = storedUser.getUuid();

        return repository.updateUserPhone(newPhone,
                uuid,
                storedPhone);
    }

    public int updateUserZip(UserDto userDto) {
        User storedUser = this.repository.fetchOneByUuid(userDto.getUuid());
        String newZip = userDto.getZip();
        String storedZip = storedUser.getZip();
        String uuid = storedUser.getUuid();

        return repository.updateUserZip(newZip,
                uuid,
                storedZip);
    }
}
