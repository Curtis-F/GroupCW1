package com2027.software.group1.groupproject;

import android.graphics.Bitmap;

import java.util.Date;

public class ProfileItem {

    private String Name = "";

    private Date Birthday = null;

    private Bitmap Picture = null;

    public String getName() {
        return Name;
    }

    public Date getBirthday() {
        return Birthday;
    }

    public Bitmap getPicture() {
        return Picture;
    }

    public ProfileItem(String name, Date birthday, Bitmap picture) {
        this.Name = name;
        this.Birthday = birthday;
        this.Picture = picture;
    }

    //TODO: List of badges
}