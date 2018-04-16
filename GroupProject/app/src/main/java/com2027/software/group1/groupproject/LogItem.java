package com2027.software.group1.groupproject;

import java.util.Date;

public class LogItem {

    private String key = "";

    private String activity_key = "";

    private int value = 0;

    private MyDateTime time = null;

    public String getKey() {
        return key;
    }

    public String getActivity_key() {
        return activity_key;
    }

    public int getValue() {
        return value;
    }

    public MyDateTime getTime() {
        return time;
    }

    public LogItem(String key, String activity_key, int value, MyDateTime time) {
        this.key = key;
        this.activity_key = activity_key;
        this.value = value;
        this.time = time;
    }
}