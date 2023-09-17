package com.aheath.auth.api;

public class User {
    private int user_id;
    private String email;
    private String pw_hash;
    private String first;
    private String last;
    private String phone;
    private String zip;
    private String role;

    // Complete Constructor
    public User(int user_id, String email, String pw_hash, String first, String last, String phone, String zip, String role) {
        this.user_id = user_id;
        this.email = email;
        this.pw_hash = pw_hash;
        this.first = first;
        this.last = last;
        this.phone = phone;
        this.zip = zip;
        this.role = role;
    }
    // Constructor without ID (for new instances)
    public User(String email, String pw_hash, String first, String last, String phone, String zip, String role) {
        this.email = email;
        this.pw_hash = pw_hash;
        this.first = first;
        this.last = last;
        this.phone = phone;
        this.zip = zip;
        this.role = role;
    }

    // Constructor with only email and pw for authentication
    public User(String email, String pw_hash) {
        this.email = email;
        this.pw_hash = pw_hash;
    }

    // Constructor without pw_hash for returning information
    public User(int user_id, String email, String first, String last, String phone, String zip, String role) {
        this.user_id = user_id;
        this.email = email;
        this.first = first;
        this.last = last;
        this.phone = phone;
        this.zip = zip;
        this.role = role;
    }

    // blank constructor for JDBI
    public User() {

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
