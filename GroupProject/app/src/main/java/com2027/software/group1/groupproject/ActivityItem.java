package com2027.software.group1.groupproject;

import java.io.Serializable;

public class ActivityItem implements Serializable {

    private String Name = "";

    private TargetType Target = null;

    private UnitType Unit = null;

    private GenreType Genre = null;

    private int BaseTarget = 0;

    private int StretchTarget = 0;

    public String getName() { return Name; }

    public TargetType getTarget() {
        return Target;
    }

    public UnitType getUnit() {
        return Unit;
    }

    public GenreType getGenre() {
        return Genre;
    }

    public int getBaseTarget() {
        return BaseTarget;
    }

    public int getStretchTarget() {
        return StretchTarget;
    }


    @Override
    public String toString() {
        return this.Name;
    }

    public ActivityItem(String name, TargetType target, UnitType unit, GenreType genre, int baseTarget, int stretchTarget)
    {
        this.Name = name;
        this.Target = target;
        this.Unit = unit;
        this.Genre = genre;
        this.BaseTarget = baseTarget;
        this.StretchTarget = stretchTarget;
    }
}