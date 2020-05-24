package edu.uci.ics.fabflixmobile;

public class Genre {
    private int id;
    private String name;

    public Genre(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
