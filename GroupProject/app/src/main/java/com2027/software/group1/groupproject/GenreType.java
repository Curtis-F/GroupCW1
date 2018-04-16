package com2027.software.group1.groupproject;

import java.util.HashMap;
import java.util.Map;

public enum GenreType {
    LEISURE("Leisure"), FITNESS("Fitness"), HABITS("Habits"), WORK("Work"), HOME("Home");

    private String FriendlyName;

    private GenreType(String friendlyName) {
        this.FriendlyName = friendlyName;
    }

    @Override public String toString(){
        return this.FriendlyName;
    }

    private static final Map lookup = new HashMap<>();

    static {
        for(GenreType genreType : GenreType.values())
            lookup.put(genreType.toString(), genreType);
    }

    public static GenreType getGenreType(String genre) {
        return (GenreType) lookup.get(genre);
    }


}