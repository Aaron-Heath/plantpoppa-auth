package com.plantpoppa.auth.models;


public class JwtResponse {
    private String jwt;
//    private String id;
//    private String email;
//    private String firstname;
//    private String lastname;
//    private String role;

    public JwtResponse() {

    }

    public JwtResponse(String jwt) {
        this.jwt = jwt;
    }

    public JwtResponse(String jwt, UserDto userDto) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
