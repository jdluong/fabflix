package com.fabflix.fabflix.repository;

import com.fabflix.fabflix.*;
import com.fabflix.fabflix.repository.MovieRepository;
import com.google.gson.JsonObject;
import org.apache.coyote.Response;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
// @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080"}, allowCredentials = "true")
//@CrossOrigin(origins = {"*"})
@Repository
public class JdbcMovieRepository implements MovieRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public List<Rating> findTopTwenty() {
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
    public List<Movie> getTopTwentyList() {
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
    public List<Genre> get3GenresByMovieId(@PathVariable String movieId) {
        String sql = "SELECT g.id, g.name FROM genres AS g INNER JOIN (SELECT genreId FROM genres_in_movies WHERE movieId = ? ) AS g2 ON g.id = g2.genreId ORDER BY g.name ASC LIMIT 3";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, movieId);
            return stmt;
        }, (resultSet, i) -> new Genre(resultSet.getInt("id"), resultSet.getString("name")));
    }

    @RequestMapping(
            value = "api/getAllGenresByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Genre> getAllGenresByMovieId(@PathVariable String movieId) {
        String sql = "SELECT g.id, g.name FROM genres AS g INNER JOIN (SELECT genreId FROM genres_in_movies WHERE movieId = ?) AS g2 ON g.id = g2.genreId ORDER BY g.name ASC";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, movieId);
            return stmt;
        }, (rs, i) -> new Genre(rs.getInt("id"), rs.getString("name")));
    }


    @RequestMapping(
            value = "api/get3StarsByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Star> get3StarsByMovieId(@PathVariable String movieId) {
        String sql = "SELECT s.id, s.name, IFNULL(s.birthYear,0) as birthYear FROM stars s, stars_in_movies sim, stars_in_movies sim1 WHERE (s.id = sim.starId) AND (sim.movieId = ?)" +
                " AND (s.id = sim1.starId) GROUP BY s.id, s.name, s.birthYear ORDER BY count(*) DESC, s.name ASC LIMIT 3";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, movieId);
            return stmt;
        }, (rs, i) -> new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));
    }

    @RequestMapping(
            value = "api/getAllStarsByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Star> getAllStarsByMovieId(@PathVariable String movieId) {
        String sql = "SELECT s.id, s.name, IFNULL(s.birthYear,0) as birthYear FROM stars s, stars_in_movies sim, stars_in_movies sim1 WHERE (s.id = sim.starId) AND (sim.movieId = ?)" +
                " AND (s.id = sim1.starId) GROUP BY s.id, s.name, s.birthYear ORDER BY count(*) DESC, s.name ASC";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, movieId);
            return stmt;
        }, (rs, i) -> new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));
    }

    @RequestMapping(
            value = "api/getRatingByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public Rating getRatingById(@PathVariable String movieId) {
        String sql = "SELECT m.id, r.rating, IFNULL(r.numVotes, 0) as numVotes FROM movies m LEFT JOIN ratings r ON m.id = r.movieId WHERE (m.id = ?)";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, movieId);
            return stmt;
        }, rs -> {
            if (rs.next())
                return new Rating(rs.getString("id"), rs.getDouble("rating"), rs.getInt("numVotes"));
            return null;
        });
    }

    @RequestMapping(
            value = "api/getMovieByMovieId/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public Movie getMovieById(@PathVariable String movieId) {
        String sql = "SELECT id, title, year, director FROM movies WHERE (id = ?)";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, movieId);
            return stmt;
        }, rs -> {
            if (rs.next())
                return new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director"));

            return null;
        });
    }


    @RequestMapping(
            value = "api/getMoviesByStarId/{starId}",
            method = RequestMethod.GET
    )
    @Override
    public List<Movie> getMoviesByStarId(@PathVariable String starId) {
        String sql = "SELECT m.id, m.title, m.year, m.director FROM movies AS m INNER JOIN (SELECT movieId FROM stars_in_movies WHERE starId = ?) AS m2 ON m.id = m2.movieId ORDER BY m.year DESC, m.title ASC";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, starId);
            return stmt;
        }, (rs, i) -> new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
    }

    @RequestMapping(
            value = "api/getStarByStarId/{starId}",
            method = RequestMethod.GET
    )
    @Override
    public Star getStarByStarId(@PathVariable String starId) {
        String sql = "SELECT id, name, IFNULL(birthYear,0) AS birthYear FROM stars WHERE (id = ?)";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, starId);
            return stmt;
        }, rs -> {
            if (rs.next())
                return new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear"));
            return null;
        });
    }


    @RequestMapping (
            value = "api/getAllGenres",
            method = RequestMethod.GET
    )
    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(
                "SELECT id, name FROM genres ORDER BY name ASC",
                (rs, rowNum) ->
                        new Genre(rs.getInt("id"), rs.getString("name")));
    }

    // *************** SEARCHING *****************

    @Override
    public String buildSortAndPagination(String sql, String sortBy1, String order1, String sortBy2, String order2) {
        if (sortBy1.equals("title")) {
            if (order1.equals("asc") && order2.equals("asc")) {
                sql += " ORDER BY title asc, rating asc LIMIT ? OFFSET ?";
            }
            else if (order1.equals("asc") && order2.equals("desc")) {
                sql += " ORDER BY title asc, rating desc LIMIT ? OFFSET ?";
            }
            else if (order1.equals("desc") && order2.equals("asc")) {
                sql  += " ORDER BY title desc, rating asc LIMIT ? OFFSET ?";
            }
            else if (order1.equals("desc") && order2.equals("desc")) {
                sql += " ORDER BY title desc, rating desc LIMIT ? OFFSET ?";
            }
        }
        else if (sortBy1.equals("rating")) {
            if (order1.equals("asc") && order2.equals("asc")) {
                sql += " ORDER BY rating asc, title asc LIMIT ? OFFSET ?";
            }
            else if (order1.equals("asc") && order2.equals("desc")) {
                sql += " ORDER BY rating asc, title desc LIMIT ? OFFSET ?";
            }
            else if (order1.equals("desc") && order2.equals("asc")) {
                sql += " ORDER BY rating desc, title asc LIMIT ? OFFSET ?";
            }
            else if (order1.equals("desc") && order2.equals("desc")) {
                sql += " ORDER BY rating desc, title desc LIMIT ? OFFSET ?";
            }
        }
        return sql;
    }

    @RequestMapping(
        value = "api/search/suggestions",
        method = RequestMethod.GET
    )
    @Override
    public List<Object> getSuggestions(@RequestParam String title) {
        
        String sql = "SELECT m.id, m.title FROM movies m " +
        "WHERE MATCH (m.title) AGAINST (? IN BOOLEAN MODE) " +
        "LIMIT 10";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            String[] tokens = title.split(" ");
            String againstClause = "";
            for (String token: tokens) {
                againstClause += ("+"+token+"*");
            }
            stmt.setString(1, againstClause);
            return stmt;
            }, (rs, i) -> this.buildSuggestions(rs.getString("id"), rs.getString("title"))
//                Collections.singletonMap(rs.getString("id"), rs.getString("title"))
        );
    }

    public Map<String, String> buildSuggestions(String id, String title) {
        Map<String,String> temp = new HashMap<>();
        temp.put("id", id);
        temp.put("title", title);
        return temp;
    }

    @RequestMapping(
            value = "api/search",
            method = RequestMethod.GET
    )
    @Override
    public List<MovieWithDetails> getMoviesSearch(
            @RequestParam String by,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String star,
            @RequestParam(defaultValue = "25") int perPage,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String sortBy1,
            @RequestParam(required = false) String order1,
            @RequestParam(required = false) String sortBy2,
            @RequestParam(required = false) String order2) throws SQLException {
        
        String sql = "";
        int paramsCount = 0;
        if (by.equals("advanced")) {
            List<String> searchParams = new ArrayList<>();
            if (title != null) {
                searchParams.add("title LIKE ?");
                ++paramsCount;
            }
            if (year != null) {
                searchParams.add("year = ?");
                ++paramsCount;
            }
            if (director != null) {
                searchParams.add("director LIKE ?");
                ++paramsCount;
            }
            if (star != null) {
                searchParams.add("name LIKE ?");
                ++paramsCount;
            }
            String searchQuery = String.join(" AND ", searchParams);
            if (sortBy1 == null) {
                sql = "SELECT DISTINCT m.id, m.title, m.year, m.director FROM movies m, stars_in_movies sim, stars s " +
                        "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ")";
            }
            else {
                sql = "SELECT DISTINCT m.id, m.title, m.year, m.director, r.rating FROM stars_in_movies sim, stars s, (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                        "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ")";
            }
        }
        else if (by.equals("fulltext")) {
            ++paramsCount;
            if (sortBy1 == null) {
                sql = "SELECT m.id, m.title, m.year, m.director FROM movies m " +
                        "WHERE MATCH (m.title) AGAINST (? IN BOOLEAN MODE)";
            }
            else {
                sql = "SELECT m.id, m.title, m.year, m.director FROM (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                        "WHERE MATCH (m.title) AGAINST (? IN BOOLEAN MODE)";
            }
        }
        
        if (sortBy1 == null) {
            sql += " ORDER BY m.id LIMIT ? OFFSET ?";
        }
        else {
            sql = this.buildSortAndPagination(sql, sortBy1, order1, sortBy2, order2);
        }

        String finalSql = sql;
        int finalParamsCount = paramsCount;
        List<Movie> movies = jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(finalSql);
            if (by.equals("advanced")) {
                boolean titleDone = false;
                boolean yearDone = false;
                boolean directorDone = false;
                boolean starDone = false;
                for (int i = 1; i <= finalParamsCount; ++i) {
                    if (title != null && !titleDone) {
                        stmt.setString(i, '%'+title+'%');
                    }
                    else if (year != null && !yearDone) {
                        stmt.setInt(i, year);
                    }
                    else if (director != null && !directorDone) {
                        stmt.setString(i, '%'+director+'%');
                    }
                    else if (star != null && !starDone) {
                        stmt.setString(i, '%'+star+'%');
                    }
                }
            }
            else if (by.equals("fulltext")) {
                String[] tokens = title.split(" ");
                String againstClause = "";
                for (String token: tokens) {
                    againstClause += ("+"+token+"*");
                }
                stmt.setString(1, againstClause);
            }
            stmt.setInt(finalParamsCount +1, perPage);
            stmt.setInt(finalParamsCount +2, (page-1)*perPage);
            return stmt;
            }, (rs, i) ->  new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director"))
        );

        return getMoviesBySearch(movies);
    }

    @Override
    public List<MovieWithDetails> getMoviesBySearch(List<Movie> movies) {
        List<MovieWithDetails> moviesWithDetails = new ArrayList<>();
        for (Movie m: movies) {
            MovieWithDetails movieWithDetails = new MovieWithDetails(m, get3GenresByMovieId(m.getId()), get3StarsByMovieId(m.getId()), getRatingById(m.getId()));
            moviesWithDetails.add(movieWithDetails);
        }
        
        return moviesWithDetails;
    }

    @RequestMapping (
            value = "api/search/getNumOfMovies",
            method = RequestMethod.GET
    )
    @Override
    public int getNumOfMoviesBySearch(
            @RequestParam String by,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String star) {
        
        String sql = "";
        int paramsCount = 0;
        if (by.equals("advanced")) {
            List<String> searchParams = new ArrayList<>();
            if (title != null) {
                searchParams.add("title LIKE ?");
                ++paramsCount;
            }
            if (year != null) {
                searchParams.add("year = ?");
                ++paramsCount;
            }
            if (director != null) {
                searchParams.add("director LIKE ?");
                ++paramsCount;
            }
            if (star != null) {
                searchParams.add("name LIKE ?");
                ++paramsCount;
            }
            String searchQuery = String.join(" AND ", searchParams);
            sql = "SELECT COUNT(DISTINCT m.id, m.title, m.year, m.director) FROM movies m, stars_in_movies sim, stars s " +
                    "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ")";
        }
        else if (by.equals("fulltext")) {
            ++paramsCount;
            sql = "SELECT COUNT(m.id) FROM movies m " +
                    "WHERE MATCH (m.title) AGAINST (? IN BOOLEAN MODE)";
        }

        String finalSql = sql;
        int finalParamsCount = paramsCount;
        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(finalSql);
            if (by.equals("advanced")) {
                boolean titleDone = false;
                boolean yearDone = false;
                boolean directorDone = false;
                boolean starDone = false;
                for (int i = 1; i <= finalParamsCount; ++i) {
                    if (title != null && !titleDone) {
                        stmt.setString(i, '%'+title+'%');
                    }
                    else if (year != null && !yearDone) {
                        stmt.setInt(i, year);
                    }
                    else if (director != null && !directorDone) {
                        stmt.setString(i, '%'+director+'%');
                    }
                    else if (star != null && !starDone) {
                        stmt.setString(i, '%'+star+'%');
                    }
                }
            }
            else if (by.equals("fulltext")) {
                String[] tokens = title.split(" ");
                String againstClause = "";
                for (String token: tokens) {
                    againstClause += ("+"+token+"*");
                }
                stmt.setString(1, againstClause);
            }
            return stmt;
            }, rs -> {
                if (rs.next())
                    return rs.getInt(1);
                return null;
            }
        );

    }


    // *************** BROWSING ******************

    @RequestMapping (
            value = "api/browse",
            method = RequestMethod.GET
    )
    @Override
    public List<MovieWithDetails> getMoviesBrowseBy(
            @RequestParam String by,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String startsWith,
            @RequestParam(defaultValue = "25") int perPage,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String sortBy1,
            @RequestParam(required = false) String order1,
            @RequestParam(required = false) String sortBy2,
            @RequestParam(required = false) String order2) {

        if (by.equals("genre")) {
            return getMoviesByGenre(id, perPage, page, sortBy1, order1, sortBy2, order2);
        }
        else {
            return getMoviesByTitle(startsWith, perPage, page, sortBy1, order1, sortBy2, order2);
        }
    }

    @Override
    public List<MovieWithDetails> getMoviesByGenre(int id, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2) {
        String sql = "";
        if (sortBy1 == null) {
            sql = "SELECT m.id, m.title, m.year, m.director FROM movies m, genres_in_movies gim WHERE (gim.genreId = ?) " +
                    "AND (m.id = gim.movieId) ORDER BY m.id LIMIT ? OFFSET ?";
        }
        else {
            sql = "SELECT m.id, m.title, m.year, m.director FROM genres_in_movies gim, (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                    "WHERE (gim.genreId = ?) AND (m.id = gim.movieId)";
            sql = this.buildSortAndPagination(sql, sortBy1, order1, sortBy2, order2);
        }

        String finalSql = sql;
        List<Movie> movies = jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(finalSql);
            stmt.setInt(1, id);
            stmt.setInt(2, perPage);
            stmt.setInt(3, (page-1)*perPage);
            return stmt;
        }, (rs, i) ->  new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));

        List<MovieWithDetails> moviesWithDetails = new ArrayList<>();

        for (Movie m: movies) {
            MovieWithDetails movieWithDetails = new MovieWithDetails(m, get3GenresByMovieId(m.getId()), get3StarsByMovieId(m.getId()), getRatingById(m.getId()));
            moviesWithDetails.add(movieWithDetails);
        }

        return moviesWithDetails;
    }

    @Override
    public List<MovieWithDetails> getMoviesByTitle(String startsWith, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2) {
        List<Movie> movies = new ArrayList<Movie>();
        String sql = "";
        if (sortBy1 == null) {
            if (startsWith.equals("*")) {
                sql = "SELECT id, title, year, director FROM movies WHERE title NOT regexp ? " +
                        "ORDER BY id LIMIT ? OFFSET ?";
            }
            else {
                sql = "SELECT id, title, year, director FROM movies WHERE title LIKE ? " +
                        "ORDER BY id LIMIT ? OFFSET ?";
            }
        }
        else {
            if (startsWith.equals("*")) {
                sql = "SELECT m.id, m.title, m.year, m.director FROM (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                        "WHERE (m.id = r.movieId) AND title NOT regexp ?";
                sql = this.buildSortAndPagination(sql, sortBy1, order1, sortBy2, order2);
            }
            else {
                sql = "SELECT m.id, m.title, m.year, m.director FROM (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                        "WHERE (m.id = r.movieId) AND title LIKE ?";
                sql = this.buildSortAndPagination(sql, sortBy1, order1, sortBy2, order2);
            }
        }

        String finalSql = sql;
        movies = jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(finalSql);
            if (startsWith.equals("*"))
                stmt.setString(1, "^[[:alnum:]]");
            else
                stmt.setString(1, startsWith+"%");
            stmt.setInt(2, perPage);
            stmt.setInt(3, (page-1)*perPage);
            return stmt;
        }, (rs, i) ->  new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));

        List<MovieWithDetails> moviesWithDetails = new ArrayList<>();

        for (Movie m: movies) {
            MovieWithDetails movieWithDetails = new MovieWithDetails(m, get3GenresByMovieId(m.getId()), get3StarsByMovieId(m.getId()), getRatingById(m.getId()));
            moviesWithDetails.add(movieWithDetails);
        }

        return moviesWithDetails;
    }


    @RequestMapping(
            value = "api/browse/getNumOfMovies/genre/{genreId}",
            method = RequestMethod.GET
    )
    @Override
    public int getNumOfMoviesByGenre(@PathVariable String genreId) {
        String sql = "SELECT COUNT(movieId) FROM genres_in_movies WHERE (genreId = ?)";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, genreId);
            return stmt;
        }, rs -> {
            if (rs.next())
                return rs.getInt(1);

            return null;
        });
    }

    @RequestMapping(
            value = "api/browse/getNumOfMovies/title/{startsWith}",
            method = RequestMethod.GET
    )
    @Override
    public int getNumOfMoviesByTitle(@PathVariable String startsWith) {
        if (startsWith.equals("*")) {
            return  jdbcTemplate.queryForObject(
                    "SELECT COUNT(id) FROM movies WHERE title NOT regexp \"^[[:alnum:]]\"",
                    Integer.class);
        }
        else {
            String sql = "SELECT COUNT(id) FROM movies WHERE title LIKE ?";

            return jdbcTemplate.query(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, startsWith+"%");
                return stmt;
            }, rs -> {
                if (rs.next())
                    return rs.getInt(1);

                return null;
            });

        }
    }


    // ~~~~~~~~~ LOGIN FUNCTIONS
    @RequestMapping(
            value = "api/auth",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public @ResponseBody ResponseEntity authenticate(@RequestBody Map<String, String> user, HttpSession session) {
        // get recaptcha response
        String recaptcha = user.get("g-recaptcha");
        // System.out.println(recaptcha);
        boolean recaptchaSuccess = false;

        // verify recaptcha
        try {
            recaptchaSuccess = RecaptchaService.verify(recaptcha);
        } catch (Exception e) {
            // return false if any exception occurred
            return ResponseEntity.ok(false);
        }

        // if recaptcha successful, continue with login
        if (recaptchaSuccess) {
            String email = user.get("username");
            String password = user.get("password");
            String userType = user.get("userType");
            // String sql = "SELECT EXISTS(SELECT email FROM customers WHERE email = ? AND password = ?)";
            String sql = "SELECT password FROM "+ userType.toLowerCase()+"s WHERE email = ?";
            System.out.println(sql);

            String encryptedPassword = jdbcTemplate.query(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, email);
                return stmt;
            }, resultSet -> {
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
                else {
                    return "";
                }
            });

            boolean userAuthenticated = false;
            if (encryptedPassword != "")
                userAuthenticated = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
            System.out.println(" auth: " + userAuthenticated);

            session.setAttribute("isAuth", userAuthenticated);

            if (userAuthenticated)
                session.setAttribute("user", userType);

            return ResponseEntity.ok(userAuthenticated);
        }

        // if unsuccessful, authentication failed, return false
        return ResponseEntity.ok(false);
    }

    @RequestMapping(
            value = "api/auth/isAuth",
            method = RequestMethod.GET
    )
    @Override
    public Map<String,Boolean> isAuth(HttpSession session) {
        Map<String,Boolean> json = new HashMap<>();
        if (session.getAttribute("isAuth") == null) {
            json.put("isAuth", false);
            return json;
        }
        else {
            json.put("isAuth", (Boolean) session.getAttribute("isAuth"));
            String userType = (String) session.getAttribute("user");
            json.put(userType, true);
        }
        System.out.println(json);
        return json;
    }



    // *************** SHOPPING CART ******************
    @RequestMapping(
            value = "api/shopping/addToCart/{movieId}/{quantity}",
            method = RequestMethod.GET
    )
    @Override
    public Map<String,Integer> addToCart(@PathVariable String movieId, @PathVariable int quantity, HttpSession session) {
//        HttpSession session = request.getSession(true);
        System.out.println("ID --- " + session.getId());
        if (session.getAttribute("cart") == null) {
            System.out.println("No items in cart yet");
            Map<String,Integer> cart = new HashMap<String,Integer>();
            cart.put(movieId, quantity);
            session.setAttribute("cart", cart);
            System.out.println("Cart contents: " + cart);

            Map<String, Integer> json = new HashMap<String, Integer>();
            json.put(movieId, quantity);
            return json;
        }
        else {
            Map<String,Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
            Integer curr = cart.get(movieId);
            if (curr == null) {
                cart.put(movieId, quantity);
            }
            else {
                cart.put(movieId, curr+quantity);
            }
            session.setAttribute("cart",cart);
            System.out.println("Cart contents: " + session.getAttribute("cart"));

            Map<String, Integer> json = new HashMap<String, Integer>();
            json.put(movieId, quantity);
            return json;
        }
    }

    @RequestMapping(
            value = "api/shopping/changeItemQuantity/{movieId}/{quantity}",
            method = RequestMethod.GET
    )
    @Override
    public Map<String,Integer> changeItemQuantity(@PathVariable String movieId, @PathVariable int quantity, HttpSession session) {
//        HttpSession session = request.getSession(true);
        System.out.println("ID --- " + session.getId());
        if (session.getAttribute("cart") == null) {
            System.out.println("No items in cart yet");
            Map<String,Integer> cart = new HashMap<String,Integer>();
            cart.put(movieId, quantity);
            session.setAttribute("cart", cart);
            System.out.println("Cart contents: " + session.getAttribute("cart"));

            Map<String, Integer> json = new HashMap<String, Integer>();
            json.put(movieId, quantity);
            return json;
        }
        else {
            Map<String,Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
            Integer curr = cart.get(movieId);
            cart.put(movieId, quantity);
            session.setAttribute("cart", cart);
            System.out.println("Cart contents: " + session.getAttribute("cart"));

            Map<String, Integer> json = new HashMap<String, Integer>();
            json.put(movieId, quantity);
            return json;
        }
    }

    @RequestMapping(
            value = "api/shopping/deleteItem/{movieId}",
            method = RequestMethod.DELETE
    )
    @Override
    public Map<String,Boolean> deleteItem(@PathVariable String movieId, HttpSession session) {
//        HttpSession session = request.getSession(true);
        System.out.println("ID --- " + session.getId());
        if (session.getAttribute("cart") == null) {
            System.out.println("No items in cart yet");

            Map<String, Boolean> json = new HashMap<String, Boolean>();
            json.put("success", true);
            return json;
        }
        else {
            Map<String,Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
            if (cart.get(movieId) == null) {
                System.out.println("Item was not in cart to begin with");
            }
            else {
                cart.remove(movieId);
            }
            System.out.println("Cart contents: " + session.getAttribute("cart"));

            Map<String, Boolean> json = new HashMap<String, Boolean>();
            json.put("success", true);
            return json;
        }
    }

    @RequestMapping(
            value = "api/shopping/getCartContents",
            method = RequestMethod.GET
    )
    @Override
    public Map<String,Integer> getCartContents(HttpSession session) {
//        HttpSession session = request.getSession(true);
        if (session.getAttribute("cart") == null) {
            System.out.println("No items in cart");

            Map<String, Integer> json = new HashMap<String,Integer>();
            return json;
        }
        else {
            Map<String,Integer> json = (Map<String, Integer>) session.getAttribute("cart");
            return json;
        }
    }

    @RequestMapping(
            value = "api/shopping/emptyCart",
            method = RequestMethod.DELETE
    )
    @Override
    public Map<String,Boolean> emptyCart(HttpSession session) {
        if (session.getAttribute("cart") == null) {
            System.out.println("Cart already empty");

            Map<String, Boolean> json = new HashMap<String,Boolean>();
            json.put("success",true);
            return json;
        }
        else {
            Map<String,Integer> cart = new HashMap<String,Integer>();
            session.setAttribute("cart", cart);
            System.out.println("After emptying: "+session.getAttribute("cart"));

            Map<String, Boolean> json = new HashMap<String,Boolean>();
            json.put("success",true);
            return json;
        }
    }

    @RequestMapping(
            value = "api/shopping/auth",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<Boolean> authenticateOrder(@RequestBody Map<String, String> credentials, HttpSession session) {
        String creditCard = credentials.get("number");
        String expiration = credentials.get("expiration");
        String firstName = credentials.get("firstName");
        String lastName = credentials.get("lastName");

        String authSql = "SELECT EXISTS(SELECT id FROM creditcards WHERE id = ? AND expiration = ? AND firstName = ? AND lastName = ?)";

        Boolean orderAuthenticated = jdbcTemplate.query((PreparedStatementCreator) connection -> {
            PreparedStatement stmt = connection.prepareStatement(authSql);
            stmt.setString(1, creditCard);
            stmt.setString(2, expiration);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);

            return stmt;
        }, rs -> {
            if (rs.next())
                return rs.getBoolean(1);

            return null;
        });

        if (orderAuthenticated) {
            String customerSql = "SELECT id FROM customers WHERE ccId = ?";
            Integer customerId = jdbcTemplate.query((PreparedStatementCreator) connection -> {
                PreparedStatement stmt = connection.prepareStatement(customerSql);
                stmt.setString(1, creditCard);
                return stmt;
            }, rs -> {
                if (rs.next())
                    return rs.getInt(1);

                return null;
            });

            session.setAttribute("customerId", customerId);
        }

        return ResponseEntity.ok(orderAuthenticated);
    }

    @RequestMapping(
            value = "api/shopping/addSale/{movieId}",
            method = RequestMethod.GET
    )
    @Override
    public int addSale(@PathVariable String movieId, HttpSession session) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
        int customerId = (int) session.getAttribute("customerId");
        int quantity = cart.get(movieId);

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO sales(customerId, movieId, quantity, saleDate) " +
                        "VALUES (" + customerId + ", \"" + movieId + "\", " + quantity +  ", CURRENT_DATE())", Statement.RETURN_GENERATED_KEYS);
                return stmt;
            }
        }, holder);

        System.out.println("holder: "+holder.getKey().intValue());

        return holder.getKey().intValue();
    }

    @RequestMapping(
            value="api/shopping/getMovieTitle/{saleId}",
            produces="text/plain",
            method = RequestMethod.GET
    )
    @Override
    public String getMovieTitle(@PathVariable String saleId, HttpSession session) {
        String sql = "SELECT m.title FROM movies as m INNER JOIN (SELECT movieId FROM sales WHERE id = ?) AS s ON m.id = s.movieId";

        return this.jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, saleId);
            return stmt;
        }, rs -> {
            if (rs.next())
                return rs.getString(1);

            return null;
        });
    }

    @RequestMapping(
            value="api/shopping/getQuantity/{saleId}",
            method = RequestMethod.GET
    )
    @Override
    public int getQuantity(@PathVariable String saleId, HttpSession session) {
        String sql = "SELECT quantity FROM sales WHERE id= ?";

        return this.jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, saleId);
            return stmt;
        }, rs -> {
            if (rs.next())
                return rs.getInt(1);

            return null;
        });
    }

    // *************** various caching stuff ******************
    @RequestMapping(
            value = "/api/cache/searchParams",
            method = RequestMethod.POST
    )
    @Override
    public Map<String,Object> cacheSearchParams(HttpSession session, @RequestBody Map<String, Object> payload) {
        session.setAttribute("searchParams", payload);

        Map<String, Boolean> json = new HashMap<String, Boolean>();
        json.put("success", true);
        return payload;
    }

    @RequestMapping(
            value = "/api/cache/searchParams",
            method = RequestMethod.GET
    )
    @Override
    public Map<String,Object> getSearchParams(HttpSession session) {
        if (session.getAttribute("searchParams") == null) {
            Map<String, Object> json = new HashMap<String,Object>();
            return json;
        }
        else {
            return (Map<String,Object>) session.getAttribute("searchParams");
        }
    }

    // ******************** EMPLOYEE FUNCTION ENDPOINTS *********************

    @RequestMapping(
            value = "/api/employee/addMovie",
            method = RequestMethod.POST
    )
    @Override
    public Map<String,Object> addMovie(@RequestBody Map<String, Object> payload) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("add_movie")
                .declareParameters(new SqlParameter("title", Types.VARCHAR),
                                    new SqlParameter("year", Types.INTEGER),
                                    new SqlParameter("director", Types.VARCHAR),
                                    new SqlParameter("star", Types.VARCHAR),
                                    new SqlParameter("genre", Types.VARCHAR),
                                    new SqlOutParameter("movie_message", Types.VARCHAR),
                                    new SqlOutParameter("star_message", Types.VARCHAR),
                                    new SqlOutParameter("genre_message",Types.VARCHAR)
                );
        SqlParameterSource parameters = new MapSqlParameterSource()
                                            .addValue("title", payload.get("title"))
                                            .addValue("year", payload.get("year"))
                                            .addValue("director", payload.get("director"))
                                            .addValue("star", payload.get("star"))
                                            .addValue("genre", payload.get("genre"));
        Map<String, Object> json = call.execute(parameters);

        return json;
    }

    @RequestMapping(
            value = "/api/employee/addStar",
            method = RequestMethod.POST
    )
    @Override
    public Map<String, String> addStar(@RequestBody Map<String, Object> payload) {
        String newId = jdbcTemplate.queryForObject(
                "SELECT CONCAT(SUBSTRING(max(id), 1, 2), SUBSTRING(max(id), 3) + 1) AS newId FROM stars;",
                String.class);
        String name = (String) payload.get("name");
        Integer birthYear = (Integer) payload.get("birthYear");

        if (birthYear == null) {
            this.jdbcTemplate.update(
                    "INSERT INTO stars(id, name) VALUES(?, ?)",
                    newId, name);
        }
        else {
            this.jdbcTemplate.update(
                    "INSERT INTO stars(id, name, birthYear) VALUES(?, ?, ?)",
                    newId, name, birthYear);
        }

        Map<String, String> json = new HashMap<>();
        json.put("id", newId);
        return json;
    }

    @RequestMapping(
            value = "/api/employee/getTables",
            method = RequestMethod.GET
    )
    @Override
    public Map<String, Object> getTables() {
        DataSource ds = jdbcTemplate.getDataSource();
        Map<String,Object> json = new HashMap<>();
        ArrayList<Map> tablesArray = new ArrayList<>();
        try {
            Connection conn = ds.getConnection();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet tables = md.getTables(null, "moviedb", null, new String[]{"TABLE"});
            while (tables.next()) {
                Map<String, Object> tableJson = new HashMap<>();
                ArrayList<Map> columnData = new ArrayList<Map>();
                String tableName = tables.getString("TABLE_NAME");
                if (!tableName.equals("sys_config")) {
//                    System.out.println(tableName);
                    tableJson.put("name", tableName);
                    ResultSet columns = md.getColumns(null, "moviedb", tableName, null);
                    while (columns.next()) {
                        Map<String, Object> columnJson = new HashMap<>();
                        String columnName = columns.getString("COLUMN_NAME");
//                        System.out.println("--- "+columnName);
                        columnJson.put("name", columnName);
                        String typeName = columns.getString("TYPE_NAME");
                        columnJson.put("type", typeName);
                        String columnSize = columns.getString("COLUMN_SIZE");
                        columnJson.put("size", columnSize);
                        String isNullable = columns.getString("IS_NULLABLE");
                        if (isNullable.equals("YES")) {
                            columnJson.put("required", "NO");
                        } else {
                            columnJson.put("required", "YES");
                        }
                        columnData.add(columnJson);
                    }
                    columns.close();
                    tableJson.put("columns", columnData);
                    tablesArray.add(tableJson);
                }
            }
            tables.close();
            conn.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        json.put("tables", tablesArray);
        return json;
    }

    // APP FUNCTIONS
    @RequestMapping(
            value = "api/app/auth",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public @ResponseBody ResponseEntity<String> appAuthenticate(@RequestParam Map<String, String> user, HttpSession session) {
        String email = user.get("username");
        String password = user.get("password");
        String userType = "customer";

        // String sql = "SELECT EXISTS(SELECT email FROM customers WHERE email = ? AND password = ?)";
        String sql = "SELECT password FROM "+ userType.toLowerCase() + "s WHERE email = ?";

        String encryptedPassword = jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            return stmt;
        }, resultSet -> {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            else {
                return "";
            }
        });

        boolean userAuthenticated = false;

        if (!encryptedPassword.equals(""))
            userAuthenticated = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);

        System.out.println(" auth: " + userAuthenticated);

        JsonObject response = new JsonObject();
        session.setAttribute("isAuth", userAuthenticated);

        if (userAuthenticated) {
            response.addProperty("status", "success");
            response.addProperty("message", "success");
        }
        else {
            response.addProperty("status", "fail");
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");

        return ResponseEntity.ok().headers(responseHeaders).body(response.toString());
    }
}


