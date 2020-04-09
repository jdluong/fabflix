package com.fabflix.fabflix.repository;

import java.util.List;

import com.fabflix.fabflix.*;
import org.springframework.web.bind.annotation.PathVariable;

public interface MovieListRepository {
    // find top 20 movie ratings
    List<Rating> findTopTwenty();

    // create top 20 movie list
    List<Movie> getTopTwentyList();

    // get 3 genres of movie
    List<Genre> getGenreById(String movieId);

    // get 3 stars in movie
    List<Star> getStarById(String movieId);

    // get rating of movie
    Rating getRatingById(String movieId);

    // get movie
    Movie getMovieById(String movieId);

    List<MovieWithDetails> getTopTwentyListWithDetails();

    // get movies by star id
    List<Movie> getMoviesByStarId(String starId);

    //get star by star id
    Star getStarByStarId(String starId);
}