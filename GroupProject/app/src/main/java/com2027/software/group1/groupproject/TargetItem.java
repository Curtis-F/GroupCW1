package com2027.software.group1.groupproject;

import java.io.Serializable;

public class TargetItem implements Serializable {

    private String key = "";

    private String activity_key = "";

    private String Name = "";

    private String Unit = null;

    private String Genre = null;

    private int BaseTarget = 0;

    private int StretchTarget = 0;

    public String getKey() { return key; }

    public String getActivity_key() { return activity_key; }

    public String getName() { return Name; }

    public String getUnit() {
        return Unit;
    }

    public String getGenre() {
        return Genre;
    }

    public int getBaseTarget() {
        return BaseTarget;
    }

    public int getStretchTarget() {
        return StretchTarget;
    }

    public void setBaseTarget(int baseTarget) {
        BaseTarget = baseTarget;
    }

    public void setStretchTarget(int stretchTarget) {
        StretchTarget = stretchTarget;
    }

    @Override
    public String toString() {
        return this.Name;
    }

    public TargetItem(String key, String activity_key, String name, String unit, String genre, int baseTarget, int stretchTarget)
    {
        this.key = key;
        this.activity_key = activity_key;
        this.Name = name;
        this.Unit = unit;
        this.Genre = genre;
        this.BaseTarget = baseTarget;
        this.StretchTarget = stretchTarget;
    }
}