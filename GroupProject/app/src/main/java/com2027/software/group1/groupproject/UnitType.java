package com2027.software.group1.groupproject;

import java.util.HashMap;
import java.util.Map;

public enum UnitType {
    HOURS("Hours"),KILOMETERS("Kilometers"), MILES("Miles"),
        STONES("Stones/Pounds"), KILOGRAMS("Kilograms"), NUMBER("None");


    private String FriendlyName;

    private UnitType(String friendlyName) {
        this.FriendlyName = friendlyName;
    }

    @Override public String toString(){
        return this.FriendlyName;
    }

    private static final Map lookup = new HashMap<>();

    static {
        for(UnitType unitType : UnitType.values())
            lookup.put(unitType.toString(), unitType);
    }

    public static UnitType getUnitType(String unit) {
        return (UnitType) lookup.get(unit);
    }

}