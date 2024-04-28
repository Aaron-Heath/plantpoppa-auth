package com.plantpoppa.auth.models;

public class JwtBody {
    private String jwt;

    public JwtBody(String jwt) {
        this.jwt = jwt;
    }

    public JwtBody(){

    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
