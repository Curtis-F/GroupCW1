package com2027.software.group1.groupproject;

import java.util.HashMap;
import java.util.Map;

public enum TargetType {
    ASCENDING("Do More"), DESCENDING("Lose");


    private String FriendlyName;

    private TargetType(String friendlyName) {
        this.FriendlyName = friendlyName;
    }

    @Override public String toString(){
        return this.FriendlyName;
    }

    private static final Map lookup = new HashMap<>();

    static {
        for(TargetType targetType : TargetType.values())
            lookup.put(targetType.toString(), targetType);
    }

    public static TargetType getTargetType(String targetType) {
        return (TargetType) lookup.get(targetType);
    }
}