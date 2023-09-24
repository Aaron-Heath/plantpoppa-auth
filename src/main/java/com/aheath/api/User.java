package com.aheath.api;

import javax.security.auth.Subject;
import java.security.Principal;

public class User implements Principal {
    private int user_id;
    private String firstname;
    private String lastname;
    private String email;
    private String pw_hash;
    private String phone;
    private String zip;
    private byte[] salt;

    // Full constructor
    public User(int user_id, String email, String pw_hash, String firstname, String lastname, String phone, String zip, byte[] salt) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.pw_hash = pw_hash;
        this.phone = phone;
        this.zip = zip;
        this.salt = salt;
    }

    // Constructor without id
    public User(String email, String pw_hash, String firstname, String lastname, String phone, String zip, byte[] salt) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.pw_hash = pw_hash;
        this.phone = phone;
        this.zip = zip;
        this.salt = salt;
    }
    //Constructor without pw_hash and without salt
    public User(int user_id, String email, String firstname, String lastname, String phone, String zip) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.zip = zip;
    }

    //Constructor with email/password
    public User(String email, String pw_hash) {
        this.email = email;
        this.pw_hash = pw_hash;
    }
    // Empty Constructor
    public User() {
    }

    @Override
    public String getName() {
        return this.getFirstname() + " " + this.getLastname();
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw_hash() {
        return pw_hash;
    }

    public void setPw_hash(String pw_hash) {
        this.pw_hash = pw_hash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
