package com2027.software.group1.groupproject;

public class MemberItem{

    public String key = "";
    public String username = "";
    public String email = "";

    public MemberItem(String key, String username, String email) {
        this.key = key;
        this.username = username;
        this.email = email;

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {
        return key;
    }
}