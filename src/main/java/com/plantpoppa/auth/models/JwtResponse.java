package com.plantpoppa.auth.models;


public class JwtResponse {
    private String jwt;
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String role;

    public JwtResponse() {

    }

    public JwtResponse(String jwt,
                       String id,
                       String email,
                       String firstname,
                       String lastname,
                       String role) {
        this.jwt = jwt;
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public JwtResponse(String jwt, UserDto userDto) {
        this.jwt = jwt;
        this.id = userDto.getUuid();
        this.email = userDto.getEmail();
        this.firstname = userDto.getFirstname();
        this.lastname = userDto.getLastname();
        this.role = userDto.getRole();
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
