package com.fabflix.fabflix;

public class Star {
    private String id;
    private String name;
    private int birthYear;

    public Star(String id, String name, int birthYear)
    {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public int getBirthYear()
    {
        return this.birthYear;
    }
}