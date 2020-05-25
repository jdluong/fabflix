package edu.uci.ics.fabflixmobile;

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

    @java.lang.Override
    public java.lang.String toString() {
        return "Star{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", birthYear=" + birthYear +
                '}';
    }
}
