package com.plantpoppa.auth.dao;

import com.plantpoppa.auth.models.ApplicationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ApplicationServiceRepository extends JpaRepository<ApplicationService, Integer> {

    @Query("SELECT as FROM ApplicationService as WHERE as.uuid = :uuid")
    ApplicationService fetchOneByUuid(@Param("uuid") String uuid);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO application_service (" +
            "uuid, title, secret, salt) VALUES (" +
            ":uuid, :title, :secret, :salt)",
    nativeQuery = true)
    int createApplicationService(
            @Param("uuid") String uuid,
          @Param("title") String title,
          @Param("secret") String secret,
          @Param("salt") byte[] salt);
}
