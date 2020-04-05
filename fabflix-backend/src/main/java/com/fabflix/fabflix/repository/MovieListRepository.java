package com.fabflix.fabflix.repository;

import java.util.List;
import com.fabflix.fabflix.Movie;
import com.fabflix.fabflix.Genre;
import com.fabflix.fabflix.Rating;
import com.fabflix.fabflix.Star;

public interface MovieListRepository {
    // find top 20 movie ratings
    List<Rating> findTopTwenty();

    // create top 20 movie list
    List<Movie> getTopTwentyList();

    // get 3 genres of movie
    List<Genre> getGenreById(String movieId);

    // get 3 stars in movie
    List<Star> getStarById(String movieId);
}