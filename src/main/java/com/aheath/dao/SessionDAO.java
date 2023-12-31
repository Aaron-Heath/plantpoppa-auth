package com.aheath.dao;

import com.aheath.models.Session;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;

@RegisterBeanMapper(Session.class)
public interface SessionDAO {
    //Get Session by token and user_id
    @SqlQuery("SELECT * FROM session WHERE token = :token AND user_id = :user_id")
    Session getSession();

    //Get Session By ID
    @SqlQuery("SELECT * FROM session WHERE token = :token")
    Session getSessionByToken(@Bind("token") String token);

    // Create session
    @SqlUpdate("INSERT INTO session (user_id, token, expiration) VALUES (:user_id, :token, :expiration)")
    void createSession(@Bind("user_id") int user_id, @Bind("token") String token, @Bind("expiration") LocalDate expiration);



}
