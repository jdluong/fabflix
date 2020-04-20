package com.fabflix.fabflix.repository;

import java.util.List;

import com.fabflix.fabflix.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface MovieRepository {
    // find top 20 movie ratings
    List<Rating> findTopTwenty();

    // create top 20 movie list
    List<Movie> getTopTwentyList();

    // get 3 genres of movie
    List<Genre> get3GenresByMovieId(String movieId);

    // get all genres of movie
    List<Genre> getAllGenresByMovieId(String movieId);

    // get 3 stars in movie
    List<Star> get3StarsByMovieId(String movieId);

    // get all stars in movie
    List<Star> getAllStarsByMovieId(String movieId);

    // get rating of movie
    Rating getRatingById(String movieId);

    // get movie
    Movie getMovieById(String movieId);

    List<MovieWithDetails> getTopTwentyListWithDetails();

    // get movies by star id
    List<Movie> getMoviesByStarId(String starId);

    //get star by star id
    Star getStarByStarId(String starId);

    // get all genres
    List<Genre> getAllGenres();

    // get/authenticate user for login
//    Customer authenticateCustomer(String username, String password);

    // ****************
    // SEARCH AND BROWSE FUNCTIONS

    // endpoint that directs to genre or startsWith functions
    public List<MovieWithDetails> getMoviesBrowseBy(
            String by, Integer id, String startsWith, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2
    );

    // genres for getMoviesBrowseBy
    public List<MovieWithDetails> getMoviesByGenre(int id, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2);

    // startsWith for getMoviesBrowseBy
    public List<MovieWithDetails> getMoviesByStartsWith(String startsWith, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2);


}