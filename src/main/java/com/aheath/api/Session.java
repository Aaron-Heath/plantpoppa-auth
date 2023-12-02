package com.aheath.api;

import java.time.LocalDate;

// Session stores session specific data. For storage and validation server-side.
public class Session {
    private int user_id;
    private String token;
    private LocalDate expiration;

    public Session(int user_id, String token, LocalDate expiration) {
        this.user_id = user_id;
        this.token = token;
        this.expiration = expiration;
    }
    // Empty Constructor
    public Session() {
    }

    // Getters and Setters
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }
}
