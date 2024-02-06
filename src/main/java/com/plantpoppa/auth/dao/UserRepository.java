package com.plantpoppa.auth.dao;

import com.plantpoppa.auth.models.User;
import com.plantpoppa.auth.models.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface UserRepository  extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying
    @Query(value="INSERT INTO user_account (uuid, firstname, lastname, email, phone, zip, pw_hash, salt) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)",
    nativeQuery = true)
    int createUser(String uuid,
                   String firstname,
                   String lastname,
                   String email,
                   String phone,
                   String zip,
                   String pw_hash,
                   byte[] salt);


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


    @Transactional
    @Modifying
    @Query(value = "UPDATE user_account SET " +
            "pw_hash = ?1," +
            "salt = ?2 " +
            "WHERE pw_hash = ?3 AND salt = ?4", nativeQuery = true)
    int updateUserPw(String pwHash, byte[] salt, String storedPwHash, byte[] storedSalt);

    @Query(value="SELECT * FROM user_account WHERE " +
            "uuid = ?1 AND email = ?2", nativeQuery = true)
    User fetchOneByEmailAndUuid(String uuid, String email);
}
