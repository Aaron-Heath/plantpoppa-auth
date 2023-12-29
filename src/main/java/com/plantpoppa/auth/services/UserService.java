package com.plantpoppa.auth.services;

import com.plantpoppa.auth.dao.UserRepository;
import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

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
