package com2027.software.group1.groupproject;

import java.util.ArrayList;

public class User {

    public String username = "";
    public String email = "";

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}