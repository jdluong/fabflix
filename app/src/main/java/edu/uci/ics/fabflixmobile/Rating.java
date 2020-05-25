package edu.uci.ics.fabflixmobile;

public class Rating {
    private String movieId;
    private double rating;
    private int numVotes;

    public Rating(String movieId, double rating, int numVotes)
    {
        this.movieId = movieId;
        this.rating = rating;
        this.numVotes = numVotes;
    }

    public String getMovieId()
    {
        return this.movieId;
    }

    public double getRating()
    {
        return this.rating;
    }

    public int getNumVotes()
    {
        return this.numVotes;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Rating{" +
                "movieId='" + movieId + '\'' +
                ", rating=" + rating +
                ", numVotes=" + numVotes +
                '}';
    }
}
