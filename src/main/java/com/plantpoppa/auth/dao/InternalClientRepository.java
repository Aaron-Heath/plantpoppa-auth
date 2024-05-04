package com.plantpoppa.auth.dao;

import com.plantpoppa.auth.models.InternalClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface InternalClientRepository extends JpaRepository<InternalClient, Integer> {

    @Query("SELECT ic FROM InternalClient ic WHERE ic.uuid = :uuid")
    Optional<InternalClient> fetchOneByUuid(@Param("uuid") String uuid);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO internal_client (" +
            "uuid, title, secret, salt, refresh_token) VALUES (" +
            ":uuid, :title, :secret, :salt, :refresh_token)",
    nativeQuery = true)
    int createApplicationService(
            @Param("uuid") String uuid,
            @Param("title") String title,
            @Param("secret") String secret,
            @Param("salt") byte[] salt,
            @Param("refresh_token") String refreshToken);
}
