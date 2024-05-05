package com.plantpoppa.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name="internal_client")
public class InternalClient {
    @Column(name="service_id")
    private @Id int serviceId;
    private String uuid = String.valueOf(UUID.randomUUID());
    private String title;
    private String secret;
    private byte[] salt;

    @Column(name="refresh_token")
    private String refreshToken;

    public InternalClient(int serviceId,
                          String uuid,
                          String title,
                          String secret,
                          byte[] salt,
                          String refreshToken) {
        this.serviceId = serviceId;
        this.refreshToken = refreshToken;
        if(uuid == null) {
            this.uuid = String.valueOf(UUID.randomUUID());
        } else {
            this.uuid = uuid;
        }
        this.title = title;
        this.secret = secret;
        this.salt = salt;
    }

    public InternalClient() {
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
