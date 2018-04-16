package com2027.software.group1.groupproject;

public class MyDateTime {
    private int day = 0;

    private int month = 0;

    private int year = 0;

    private long timestamp = 0;

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MyDateTime(int day, int month, int year, long timestamp) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.timestamp = timestamp;
    }
}