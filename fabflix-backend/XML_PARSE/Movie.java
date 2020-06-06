package XML_PARSE;

public class Movie {
    private String id;
    private String title;
    private int year;
    private String director;

    public Movie(String id, String title, int year, String director)
    {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
    }

    public String getId()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public int getYear()
    {
        return this.year;
    }

    public String getDirector()
    {
        return this.director;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", director='" + director + '\'' +
                '}';
    }
}