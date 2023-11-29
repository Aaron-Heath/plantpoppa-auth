package com.aheath.db;

import com.aheath.api.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(User.class)
public interface UserDAO {
    @SqlQuery("SELECT user_id,firstname,lastname,email,phone,zip FROM user_account")
    List<User> getAllUsers();

    @SqlUpdate("INSERT INTO user_account (firstname, lastname, email, pw_hash, phone, zip, salt) VALUES" +
            "(:firstname, :lastname, :email, :pw_hash, :phone, :zip, :salt)" +
            "RETURNING user_id")
    @GetGeneratedKeys
    int createUser(@Bind("firstname") String firstname,@Bind("lastname") String lastname, @Bind("email") String email, @Bind("pw_hash") String pw_hash, @Bind("phone") String phone, @Bind("zip") String zip, @Bind("salt") byte[] salt);

    // Method for authentication

    /**
     *
     * @param email entered by user and passed by front end application
     * @return salt for user if the user exists
     */
    @SqlQuery("SELECT salt FROM user_account WHERE email = :email")
    String getSalt(@Bind("email") String email);

    @SqlQuery("SELECT * FROM user_account WHERE email = :email")
    User getUserByEmail(@Bind("email") String email);


    @SqlQuery("SELECT user_id, firstname, lastname, email, phone, zip FROM user_account WHERE email= :email pw_hash = :pw_hash AND salt = :salt")
    User checkCredentials(@Bind("email") String email, @Bind("pw_hash") String pw_hash, @Bind("salt") String salt);

    // update password using user_id
    @SqlUpdate("UPDATE user_account SET password = :pw_hash, salt = :salt WHERE user_id = :user_id")
    int changePassword(@Bind("pw_hash") String pw_hash, @Bind("salt") byte[] salt, @Bind("user_id") int user_id);
}
