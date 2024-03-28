package com.plantpoppa.auth.models;

public class UserDto {
    private String uuid;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private String zip;
    private String role;

    public UserDto(String uuid, String firstname, String lastname, String email, String password, String phone, String zip, String role) {
        this.uuid = uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.zip = zip;
        this.role = role;
    }

    // Default constructor
    public UserDto(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public static class UserDtoBuilder{
        private String uuid;
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private String phone;
        private String zip;
        private String role;

        public UserDtoBuilder() {
        }

        public UserDtoBuilder uuid(String uuid){
            this.uuid = uuid;
            return this;
        }

        public UserDtoBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public UserDtoBuilder lastname(String lastname){
            this.lastname = lastname;
            return this;
        }

        public UserDtoBuilder email(String email){
            this.email = email;
            return this;
        }

        public UserDtoBuilder password(String password){
            this.password = password;
            return this;
        }

        public UserDtoBuilder phone(String phone){
            this.phone = phone;
            return this;
        }

        public UserDtoBuilder zip(String zip){
            this.zip = zip;
            return this;
        }

        public  UserDtoBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserDto build() {
            return new UserDto(
                    this.uuid,
                    this.firstname,
                    this.lastname,
                    this.email,
                    this.password,
                    this.phone,
                    this.zip,
                    this.role
            );
        }

    }
}
