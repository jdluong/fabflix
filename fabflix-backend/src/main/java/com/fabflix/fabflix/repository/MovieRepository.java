package com.fabflix.fabflix.repository;

import com.fabflix.fabflix.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MovieRepository {
    // LOGGING FUNCTION
    void logSearchTime() throws IOException;

    ResponseEntity<Boolean> logAuth(HttpSession session);

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

    // helper function to build sort and/or pagination sql
    String buildSortAndPagination(String sql, String sortBy1, String order1, String sortBy2, String order2);

    // endpoint to get autocomplete suggestions
    List<Object> getSuggestions(String title);

    // endpoint that searches
    List<MovieWithDetails> getMoviesSearch(
            String by, String title, Integer year, String director, String star, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2
    ) throws SQLException, IOException;

    // search for getMovies
    List<MovieWithDetails> getMoviesBySearch(List<Movie> movies);

    // get number of movies by search
    int getNumOfMoviesBySearch(String by, String title, Integer year, String director, String star);

    // ****************
    // BROWSE FUNCTIONS

    // endpoint that directs to genre or startsWith functions
    List<MovieWithDetails> getMoviesBrowseBy(
            String by, Integer id, String startsWith, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2
    );

    // genres for getMoviesBrowseBy
    List<MovieWithDetails> getMoviesByGenre(int id, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2);

    // startsWith for getMoviesBrowseBy
    List<MovieWithDetails> getMoviesByTitle(String startsWith, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2);

    // get number of movies by genre
    int getNumOfMoviesByGenre(@PathVariable String genreId);

    // get number of movies byt itle
    int getNumOfMoviesByTitle(@PathVariable String startsWith);

    // ***************
    // LOGIN FUNCTIONS
    ResponseEntity<Boolean> authenticate(Map<String, String> user, HttpSession session);

    Map<String, Boolean> isAuth(HttpSession session);

    // ************
    // SHOPPING

    // add quantity of movieId's to shopping cart
    public Map<String, Integer> addToCart(String movieId, int quantity, HttpSession session);

    // change quantity of movieId's in shopping cart
    public Map<String, Integer> changeItemQuantity(String movieId, int quantity, HttpSession session);

    // get cart contents
    public Map<String, Integer> getCartContents(HttpSession session);

    // delete movieId from shopping cart
    public Map<String, Boolean> deleteItem(String movieId, HttpSession session);

    // empty cart contents
    public Map<String, Boolean> emptyCart(HttpSession session);

    // validate credit card credentials
    ResponseEntity<Boolean> authenticateOrder(Map<String, String> orderInfo, HttpSession session);

    // add sales to db
    int addSale(String movieId, HttpSession session);

    // get movie id from sale
    String getMovieTitle(String saleId, HttpSession session);

    // get quantity
    int getQuantity(String saleId, HttpSession session);


    // ***********
    // various cACHING

    public Map<String, Object> cacheSearchParams(HttpSession session, Map<String, Object> payload);

    public Map<String, Object> getSearchParams(HttpSession session);


    // ***********
    // DASHBOARD

    public Map<String,Object> addMovie(Map<String, Object> payload);
    public Map<String, String> addStar(Map<String, Object> payload);
    public Map<String, Object> getTables();
  
  // ********** APP FUNCTIONS
    ResponseEntity<String> appAuthenticate(Map<String, String> user, HttpSession session);

    List<MovieWithDetails> appSearch(String text);

    List<MovieWithDetails> getMoviesBySearchApp(List<Movie> movies);
}