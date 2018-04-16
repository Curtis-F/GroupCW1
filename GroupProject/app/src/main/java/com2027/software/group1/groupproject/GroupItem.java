package com2027.software.group1.groupproject;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupItem implements Serializable {

    private String key = "";

    private String name = "";

    private String activity_key = null;

    private String activity_name = null;

    private String unit = "";

    private String genre = "";

    private int baseTarget = 0;

    private int stretchTarget = 0;

    private ArrayList<String> userKeys = null;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getActivity_key() {
        return activity_key;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public String getUnit() {
        return unit;
    }

    public String getGenre() {
        return genre;
    }

    public int getBaseTarget() {
        return baseTarget;
    }

    public int getStretchTarget() {
        return stretchTarget;
    }

    public ArrayList<String> getUserKeys() { return userKeys; }

    public void setBaseTarget(int baseTarget) {
        this.baseTarget = baseTarget;
    }

    public void setStretchTarget(int stretchTarget) {
        this.stretchTarget = stretchTarget;
    }

    public GroupItem(String key, String name, String activity_key, String activity_name, String unit, String genre, int baseTarget, int stretchTarget, ArrayList<String> userKeys) {
        this.key = key;
        this.name = name;
        this.stretchTarget = stretchTarget;
        this.baseTarget = baseTarget;
        this.activity_key = activity_key;
        this.activity_name = activity_name;
        this.userKeys = userKeys;
        this.unit = unit;
    }


}