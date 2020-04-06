package com.fabflix.fabflix.repository;

import java.util.List;
import com.fabflix.fabflix.Movie;
import com.fabflix.fabflix.Genre;
import com.fabflix.fabflix.Rating;
import com.fabflix.fabflix.Star;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    @Override
    public List<Movie> getTopTwentyList()
    {
        return jdbcTemplate.query(
                "SELECT m.id, m.title, m.year, m.director FROM movies AS m INNER JOIN (SELECT movieId FROM ratings ORDER BY rating DESC LIMIT 20) as m2 ON m.id = m2.movieId",
                (rs, rowNum) ->
                        new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
    }

    @Override
    public List<Genre> getGenreById(String movieId)
    {
        return jdbcTemplate.query(
                "SELECT g.id, g.name FROM genres AS g INNER JOIN (SELECT genreId FROM genres_in_movies WHERE movieId = \"" +
                        movieId + "\" LIMIT 3) AS g2 ON g.id = g2.genreId",
                (rs, rowNum) ->
                        new Genre(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public List<Star> getStarById(String movieId)
    {
        return jdbcTemplate.query(
                "SELECT s.id, s.name FROM stars AS s INNER JOIN (SELECT starId FROM stars_in_movies WHERE movieId = \"" +
                        movieId + "\" LIMIT 3) AS s2 ON s.id = s2.starId",
                (rs, rowNum) ->
                        new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));
    }
}