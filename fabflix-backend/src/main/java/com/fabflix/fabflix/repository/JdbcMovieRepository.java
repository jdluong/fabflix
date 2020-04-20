package com.fabflix.fabflix.repository;

import java.util.ArrayList;
import java.util.List;

import com.fabflix.fabflix.*;

import org.apache.catalina.startup.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
// @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/"})
// @CrossOrigin(origins = {"*"})
@Repository
public class JdbcMovieRepository implements MovieRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Rating> findTopTwenty()
    {
        return jdbcTemplate.query(
                "SELECT * FROM ratings ORDER BY rating DESC LIMIT 20",
                (rs, rowNum) ->
                        new Rating(rs.getString("movieId"), rs.getDouble("rating"), rs.getInt("numVotes")));
    }

    @RequestMapping(
            value = "api/getTopTwentyList",
            method = RequestMethod.GET
    )
    @Override
    public List<Movie> getTopTwentyList()
    {
        return jdbcTemplate.query(
                "SELECT m.id, m.title, m.year, m.director FROM movies AS m INNER JOIN (SELECT movieId FROM ratings ORDER BY rating DESC LIMIT 20) as m2 ON m.id = m2.movieId",
                (rs, rowNum) ->
                        new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
    }

    @RequestMapping(
            value = "api/getTopTwentyListWithDetails",
            method = RequestMethod.GET
    )
    @Override
    public List<MovieWithDetails> getTopTwentyListWithDetails() {
        List<Movie> top20 = getTopTwentyList();
        List<MovieWithDetails>  top20WithDetails = new ArrayList<MovieWithDetails>();
        for (Movie m: top20) {
            MovieWithDetails movieWithDetails = new MovieWithDetails(m, get3GenresByMovieId(m.getId()), get3StarsByMovieId(m.getId()), getRatingById(m.getId()));
            top20WithDetails.add(movieWithDetails);
        }

        return top20WithDetails;
    }

    @RequestMapping(
            value = "api/get3GenresByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Genre> get3GenresByMovieId(@PathVariable String movieId)
    {
        return jdbcTemplate.query(
                "SELECT g.id, g.name FROM genres AS g INNER JOIN (SELECT genreId FROM genres_in_movies WHERE movieId = \"" +
                        movieId + "\") AS g2 ON g.id = g2.genreId ORDER BY g.name ASC LIMIT 3",
                (rs, rowNum) ->
                        new Genre(rs.getInt("id"), rs.getString("name")));
    }

    @RequestMapping(
            value = "api/getAllGenresByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Genre> getAllGenresByMovieId(@PathVariable String movieId)
    {
        return jdbcTemplate.query(
                "SELECT g.id, g.name FROM genres AS g INNER JOIN (SELECT genreId FROM genres_in_movies WHERE movieId = \"" +
                        movieId + "\") AS g2 ON g.id = g2.genreId ORDER BY g.name ASC",
                (rs, rowNum) ->
                        new Genre(rs.getInt("id"), rs.getString("name")));
    }


    @RequestMapping(
            value = "api/get3StarsByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Star> get3StarsByMovieId(@PathVariable String movieId)
    {
        return jdbcTemplate.query(
                "SELECT s.id, s.name, IFNULL(s.birthYear,0) as birthYear FROM stars s, stars_in_movies sim, stars_in_movies_sim1 WHERE (s.id = sim.starId) AND (sim.movieId = \"" +
                        movieId + "\") AND (s.id = sim1.starId) GROUP BY s.id, s.name, s.birthYear ORDER BY count(*) DESC, s.name ASC LIMIT 3",
                (rs, rowNum) ->
                        new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));

    }

    @RequestMapping(
            value = "api/getAllStarsByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Star> getAllStarsByMovieId(@PathVariable String movieId)
    {
        return jdbcTemplate.query(
                "SELECT s.id, s.name, IFNULL(s.birthYear,0) as birthYear FROM stars s, stars_in_movies sim, stars_in_movies_sim1 WHERE (s.id = sim.starId) AND (sim.movieId = \"" +
                        movieId + "\") AND (s.id = sim1.starId) GROUP BY s.id, s.name, s.birthYear ORDER BY count(*) DESC, s.name ASC",
                (rs, rowNum) ->
                        new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));

    }

    @RequestMapping(
            value = "api/getRatingByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public Rating getRatingById(@PathVariable String movieId) {
        return jdbcTemplate.queryForObject(
                "SELECT r.movieId, r.rating, r.numVotes FROM ratings AS r, movies AS m WHERE (r.movieId = m.id) AND (m.id = \"" +
                        movieId + "\")",
                (rs, rowNum) ->
                        new Rating(rs.getString("movieId"), rs.getDouble("rating"), rs.getInt("numVotes")));
    }

    @RequestMapping(
            value = "api/getMovieByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public Movie getMovieById(@PathVariable String movieId) {
        return jdbcTemplate.queryForObject(
                "SELECT id, title, year, director FROM movies WHERE (id = \"" + movieId + "\")",
                (rs, rowNum) ->
                        new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
    }

    @RequestMapping(
            value = "api/getMoviesByStarId/{starId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Movie> getMoviesByStarId(@PathVariable String starId) {
        return jdbcTemplate.query(
                "SELECT m.id, m.title, m.year, m.director FROM movies AS m INNER JOIN (SELECT movieId FROM stars_in_movies WHERE starId = \""
                + starId + "\") AS m2 ON m.id = m2.movieId ORDER BY m.year DESC, m.title ASC",
                (rs, rowNum) ->
                        new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
    }

    @RequestMapping(
            value = "api/getStarByStarId/{starId}",
            method = RequestMethod.GET
    )
    @Override
    public Star getStarByStarId(@PathVariable String starId) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, IFNULL(birthYear,0) AS birthYear FROM stars WHERE (id = \""
                + starId + "\")",
                (rs, rowNum) ->
                        new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));
    }

    @Override
    public Customer getCustomer(@PathVariable String username, @PathVariable String password) {
            return jdbcTemplate.queryForObject(
                    "SELECT id, firstName, lastName, ccId, address, email, password FROM customers WHERE (email = \""
                    + username + "\" " + "AND password = \"" + password + "\")",
                    (rs, rowNum) ->
                        new Customer(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("ccId"), rs.getString("address"),
                                     rs.getString("email"), rs.getString("password")));
    }

    

        // ************* BROWSE ****************

// START put in one function, with a #browseBy param??
    // get movies by genreId
    // params: #genreId
    // --------
    // select movieId from genres_in_movies where (genreId = 10);


    // get movies starting with letter/number or non-alphanum
    // params: #char
    // --------
    // if (char == '*')
    //  select id, title from movies where title not regexp '^[[:alnum:]]';
    // else 
    //  select id, title, year, director from movies where title like '#char%';
// END


    // get stars by movieId, sorted by number of movies each star has been in
    // params: #movieId
    // --------
    // SELECT s.id, s.name
    // FROM stars s, stars_in_movies sim, stars_in_movies sim1
    // WHERE (s.id = sim.starId) AND (s.id = sim1.starId) AND (sim.movieId = #movieId)
    // GROUP BY s.id, s.name
    // ORDER BY count(*) DESC
    // LIMIT 3; <---- for movie list?

    
    // get number of movies by genreId
    // params: #genreId
    // ---------
    // SELECT count(movieId)
    // FROM genres_in_movies gim
    // WHERE 

    // pagination; sorting (default, by title/rating, asc/desc)
    // params: #perPage, #page, #sort='None', #order='None' <-- there's no def parameters in java :(
    // ---------
    // if (!sort) 
    //  SELECT # FROM # WHERE # 
    //  ORDER BY rating DESC, 'id';
    //  LIMIT #perPage
    //  OFFSET (#page-1)*#perPage
    //
    // if (sort == 'title')
    //  SELECT # FROM # WHERE # 
    //  ORDER BY title #order, rating #order, 'id';
    //  LIMIT #perPage
    //  OFFSET (#page-1)*#perPage
    //
    // if (sort == 'rating')
    //  SELECT # FROM # WHERE # 
    //  ORDER BY rating #order, title #order, 'id';
    //  LIMIT #perPage
    //  OFFSET (#page-1)*#perPage
}
