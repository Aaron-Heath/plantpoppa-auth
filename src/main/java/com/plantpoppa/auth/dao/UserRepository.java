package com.plantpoppa.auth.dao;

import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserRepository  extends JpaRepository<User, Integer> {

//    @Query(value = "SELECT (user_id, uuid," +
//            "firstname," +
//            "lastname," +
//            "email," +
//            "phone," +
//            "zip)  FROM user_account",
    @Query(value="SELECT * FROM user_account",
    nativeQuery = true)
    List<User> findAllUsers();

    @Query("SELECT 0")
    int test();
}
