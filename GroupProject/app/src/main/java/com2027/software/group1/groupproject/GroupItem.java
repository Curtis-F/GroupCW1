package com2027.software.group1.groupproject;

public class GroupItem {

    private String Name = "";

    private ActivityItem Activity = null;

    private int BaseTarget = 0;

    private int StretchTarget = 0;

    public String getName() {
        return Name;
    }

    public ActivityItem getActivity() {
        return Activity;
    }

    public int getBaseTarget() {
        return BaseTarget;
    }

    public int getStretchTarget() {
        return StretchTarget;
    }

    public GroupItem(String name, ActivityItem activity, int baseTarget, int stretchTarget) {
        this.Name = name;
        this.StretchTarget = stretchTarget;
        this.BaseTarget = baseTarget;
        this.Activity = activity;
    }
}