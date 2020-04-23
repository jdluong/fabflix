package com.fabflix.fabflix.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fabflix.fabflix.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

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


    // ****************
    // SEARCH FUNCTIONS

    // endpoint that searches
    public List<MovieWithDetails> getMoviesSearch(
            String title, Integer year, String director, String star, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2
    );

    // search for getMovies
    public List<MovieWithDetails> getMoviesBySearch(String searchQuery, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2);

    // get number of movies by search
    public int getNumOfMoviesBySearch(String title, Integer year, String director, String star);


    // ****************
    // BROWSE FUNCTIONS

    // endpoint that directs to genre or startsWith functions
    public List<MovieWithDetails> getMoviesBrowseBy(
            String by, Integer id, String startsWith, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2
    );

    // genres for getMoviesBrowseBy
    public List<MovieWithDetails> getMoviesByGenre(int id, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2);

    // startsWith for getMoviesBrowseBy
    public List<MovieWithDetails> getMoviesByTitle(String startsWith, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2);

    // get number of movies by genre
    public int getNumOfMoviesByGenre(@PathVariable String genreId);

    // get number of movies byt itle
    public int getNumOfMoviesByTitle(@PathVariable String startsWith);

    // LOGIN FUNCTIONS
    public boolean authenticate(String email, String password);

    // ************
    // SHOPPING

    // add quantity of movieId's to shopping cart
    public Map<String,Boolean> addToShoppingCart(@PathVariable String movieId, @PathVariable int quantity, HttpSession session);

    // get cart contents
    public Map<String,Integer> getCartContents(HttpSession session);

    // empty cart contents
    public Map<String,Boolean> emptyCart(HttpSession session);



}