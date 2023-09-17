package com.aheath.db;

import com.aheath.api.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(User.class)
public interface UserDAO {
    @SqlQuery("SELECT user_id,firstname,lastname,email,phone,zip FROM user_account")
    List<User> getAllUsers();
}
