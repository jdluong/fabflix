package edu.uci.ics.fabflixmobile;

import java.util.List;

public class MovieWithDetails {
    private Movie movie;
    private List<Genre> genres;
    private List<Star> stars;
    private Rating rating;

    public MovieWithDetails(Movie m, List<Genre> genres, List<Star> stars, Rating rating) {
        this.movie = m;
        this.genres = genres;
        this.stars = stars;
        this.rating = rating;
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Star> getStars() {
        return stars;
    }

    public Rating getRating() {
        return rating;
    }
}
