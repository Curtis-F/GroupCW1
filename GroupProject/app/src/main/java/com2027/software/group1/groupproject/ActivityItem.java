package com2027.software.group1.groupproject;

import java.io.Serializable;

public class ActivityItem implements Serializable {

    private String key = "";

    private String Name = "";

    private String Unit = null;

    private String Genre = null;

    public String getKey() { return key; }

    public String getName() { return Name; }

    public String getUnit() {
        return Unit;
    }

    public String getGenre() {
        return Genre;
    }

    @Override
    public String toString() {
        return this.Name;
    }

    public ActivityItem(String key, String name, String unit, String genre)
    {
        this.key = key;
        this.Name = name;
        this.Unit = unit;
        this.Genre = genre;
    }
}