package com.plantpoppa.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_account")
public class User {
    private @Id int user_id;
    private String uuid = String.valueOf(UUID.randomUUID());
    private String firstname;
    private String lastname;
    private String email;
    private String pw_hash;
    private String phone;
    private String zip;
    private byte[] salt;
    private String role;


    // Full constructor
    public User(int user_id, String uuid, String email, String pw_hash, String firstname, String lastname, String phone, String zip, byte[] salt, String role) {
        this.user_id = user_id;
        // Add if condition for UserBuilder user creation
        if(uuid == null) {
            this.uuid = String.valueOf(UUID.randomUUID());
        } else {
            this.uuid = uuid;
        }
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.pw_hash = pw_hash;
        this.phone = phone;
        this.zip = zip;
        this.salt = salt;
        this.role = role;
    }

    // Constructor without id
    public User(String uuid, String email, String pw_hash, String firstname, String lastname, String phone, String zip, byte[] salt, String role) {
        this.uuid = uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.pw_hash = pw_hash;
        this.phone = phone;
        this.zip = zip;
        this.salt = salt;
        this.role = role;
    }
    //Constructor without pw_hash and without salt
    public User(int user_id, String email, String firstname, String lastname, String phone, String zip, String role) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.zip = zip;
        this.role = role;
    }

    //Constructor with email/password
    public User(String email, String pw_hash) {
        this.email = email;
        this.pw_hash = pw_hash;
    }
    // Empty Constructor
    public User() {
    }

    public UserDto toDto() {
        return new UserDto.UserDtoBuilder()
                .uuid(this.getUuid())
                .firstname(this.getFirstname())
                .lastname(this.getLastname())
                .email(this.getEmail())
                .phone(this.getPhone())
                .zip(this.getZip())
                .role(this.getRole()).build();
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserBuilder toBuilder() {
        return new UserBuilder()
                .user_id(this.user_id)
                .firstname(this.firstname)
                .lastname(this.lastname)
                .email(this.email)
                .pw_hash(this.pw_hash)
                .phone(this.phone)
                .zip(this.zip)
                .salt(this.salt);
    }


    public static class UserBuilder {
        private int user_id;
        private String uuid;
        private String firstname;
        private String lastname;
        private String email;
        private String pw_hash;
        private String phone;
        private String zip;
        private byte[] salt;
        private String role;

        // Empty Constructor
        public UserBuilder() {
        }

        public UserBuilder user_id(int user_id) {
            this.user_id = user_id;
            return this;
        }

        public UserBuilder uuid(String uuid){
            this.uuid = uuid;
            return this;
        }

        public UserBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public UserBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder pw_hash(String pw_hash) {
            this.pw_hash = pw_hash;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder zip(String zip) {
            this.zip = zip;
            return this;
        }

        public UserBuilder salt(byte[] salt) {
            this.salt = salt;
            return this;
        }

        public User build() {
            return new User(this.user_id,
                    this.uuid,
                    this.email,
                    this.pw_hash,
                    this.firstname,
                    this.lastname,
                    this.phone,
                    this.zip,
                    this.salt,
                    this.role);
        }


    }

}
