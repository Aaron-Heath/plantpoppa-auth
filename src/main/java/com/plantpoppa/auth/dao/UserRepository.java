package com.plantpoppa.auth.dao;

import com.plantpoppa.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface UserRepository  extends JpaRepository<User, Integer> {

    @Query(value="SELECT * FROM user_account",
    nativeQuery = true)
    List<User> fetchAll();

    @Query(value="SELECT * FROM user_account WHERE email = ?1",
    nativeQuery = true)
    User fetchOneByEmail(String email);

    @Query(value="SELECT * FROM user_account WHERE uuid = ?1",
    nativeQuery = true)
    User fetchOneByUuid(String uuid);

    @Transactional
    @Modifying
    @Query(value="DELETE FROM user_account WHERE uuid = ?1",
    nativeQuery = true)
    int deleteOneByUuid(String uuid);

    @Transactional
    @Modifying
    @Query(value="Update user_account SET " +
            "firstname = ?1 " +
            "WHERE uuid = ?2 " +
            "AND firstname = ?3", nativeQuery = true)
    int updateUserFirstname(String firstname, String uuid, String storedName);

    @Transactional
    @Modifying
    @Query(value="Update user_account SET " +
            "lastname = ?1 " +
            "WHERE uuid = ?2 " +
            "AND lastname = ?3", nativeQuery = true)
    int updateUserLastname(String lastname, String uuid, String storedName);

    @Transactional
    @Modifying
    @Query(value="Update user_account SET " +
            "phone = ?1 " +
            "WHERE uuid = ?2 " +
            "AND (phone = ?3 OR phone IS NULL)", nativeQuery = true)
    int updateUserPhone(String phone, String uuid, String storedPhone);

    @Transactional
    @Modifying
    @Query(value="Update user_account SET " +
            "zip = ?1 " +
            "WHERE uuid = ?2 " +
            "AND (zip = ?3 OR zip IS NULL)", nativeQuery = true)
    int updateUserZip(String zip, String uuid, String storedZip);
}
