package com.example.bee;



public class UserProfile {

    private String username;
    private String password;
    private String phone;
    public UserProfile() {
        //firebase constructor
    }


    public UserProfile(String username, String phone, String password) {

        this.username = username;
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
