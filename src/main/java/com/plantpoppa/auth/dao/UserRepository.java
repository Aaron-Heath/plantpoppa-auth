package com.plantpoppa.auth.dao;

import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import jakarta.inject.Singleton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository  extends JpaRepository<User, Integer> {

//    @Query(value = "SELECT (user_id, uuid," +
//            "firstname," +
//            "lastname," +
//            "email," +
//            "phone," +
//            "zip)  FROM user_account",
    @Query(value="SELECT * FROM user_account",
    nativeQuery = true)
    List<User> fetchAll();

    @Query(value="SELECT * FROM user_account WHERE email = ?1",
    nativeQuery = true)
    User fetchOneByEmail(String email);

    @Query(value="SELECT * FROM user_account WHERE uuid = ?1",
    nativeQuery = true)
    User fetchOneByUuid(String uuid);
}
