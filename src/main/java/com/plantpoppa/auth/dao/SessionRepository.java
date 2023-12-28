package com.plantpoppa.auth.dao;

import com.plantpoppa.auth.models.Session;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface SessionRepository extends JpaRepository<Session, Integer> {

    @Query(value="SELECT * FROM session",
    nativeQuery = true)
    List<Session> fetchAll();

    @Query(value="SELECT * FROM session WHERE token = ?1",
    nativeQuery = true)
    Session fetchOneByToken(String token);

    @Transactional
    @Modifying
    @Query(value="INSERT INTO session (user_id, token, expiration) VALUES (?1, ?2, ?3)",
    nativeQuery = true)
    void createSession(int user_id, String token, LocalDate expiration);

}
