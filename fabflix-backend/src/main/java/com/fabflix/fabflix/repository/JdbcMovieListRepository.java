package com.fabflix.fabflix.repository;

import java.util.ArrayList;
import java.util.List;

import com.fabflix.fabflix.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
// @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/"})
@CrossOrigin(origins = {"*"})
@Repository
public class JdbcMovieListRepository implements MovieListRepository {
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
            MovieWithDetails movieWithDetails = new MovieWithDetails(m, getGenreById(m.getId()), getStarById(m.getId()), getRatingById(m.getId()));
            top20WithDetails.add(movieWithDetails);
        }

        return top20WithDetails;
    }

    @RequestMapping(
            value = "api/getGenresByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Genre> getGenreById(@PathVariable String movieId)
    {
//        return jdbcTemplate.query(
//                "SELECT g.id, g.name FROM genres AS g INNER JOIN (SELECT genreId FROM genres_in_movies WHERE movieId = \"" +
//                        movieId + "\" LIMIT 3) AS g2 ON g.id = g2.genreId",
//                (rs, rowNum) ->
//                        new Genre(rs.getInt("id"), rs.getString("name")));
        return jdbcTemplate.query(
                "SELECT g.id, g.name FROM genres AS g INNER JOIN (SELECT genreId FROM genres_in_movies WHERE movieId = \"" +
                        movieId + "\") AS g2 ON g.id = g2.genreId",
                (rs, rowNum) ->
                        new Genre(rs.getInt("id"), rs.getString("name")));
    }

    @RequestMapping(
            value = "api/getStarsByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Star> getStarById(@PathVariable String movieId)
    {
//        return jdbcTemplate.query(
//                "SELECT s.id, s.name FROM stars AS s INNER JOIN (SELECT starId FROM stars_in_movies WHERE movieId = \"" +
//                        movieId + "\" LIMIT 3) AS s2 ON s.id = s2.starId",
//                (rs, rowNum) ->
//                        new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));
        return jdbcTemplate.query(
                "SELECT s.id, s.name, IFNULL(s.birthYear,0) as birthYear FROM stars s, stars_in_movies sim WHERE (s.id = sim.starId) AND (sim.movieId = \"" +
                        movieId + "\")",
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
                + starId + "\") AS m2 ON m.id = m2.movieId",
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

}
