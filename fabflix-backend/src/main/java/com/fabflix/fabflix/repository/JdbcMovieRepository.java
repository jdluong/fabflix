package com.fabflix.fabflix.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fabflix.fabflix.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080"})
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
                "SELECT s.id, s.name, IFNULL(s.birthYear,0) as birthYear FROM stars s, stars_in_movies sim, stars_in_movies sim1 WHERE (s.id = sim.starId) AND (sim.movieId = \"" +
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
                "SELECT s.id, s.name, IFNULL(s.birthYear,0) as birthYear FROM stars s, stars_in_movies sim, stars_in_movies sim1 WHERE (s.id = sim.starId) AND (sim.movieId = \"" +
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
                "SELECT m.id, r.rating, IFNULL(r.numVotes, 0) as numVotes FROM movies m LEFT JOIN ratings r ON m.id = r.movieId " +
                        "WHERE (m.id = \"" + movieId + "\")",
                (rs, rowNum) ->
                        new Rating(rs.getString("id"), rs.getDouble("rating"), rs.getInt("numVotes")));
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

//    @Override
//    public Customer getCustomer(@PathVariable String username, @PathVariable String password) {
//            return jdbcTemplate.queryForObject(
//                    "SELECT id, firstName, lastName, ccId, address, email, password FROM customers WHERE (email = \""
//                    + username + "\" " + "AND password = \"" + password + "\")",
//                    (rs, rowNum) ->
//                        new Customer(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("ccId"), rs.getString("address"),
//                                     rs.getString("email"), rs.getString("password")));
//    }

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

    @RequestMapping (
            value = "api/search",
            method = RequestMethod.GET
    )
    @Override
    public List<MovieWithDetails> getMoviesSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String star,
            @RequestParam(defaultValue = "25") int perPage,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String sortBy1,
            @RequestParam(required = false) String order1,
            @RequestParam(required = false) String sortBy2,
            @RequestParam(required = false) String order2) {

        List<String> searchParams = new ArrayList<>();
        if (title != null) {
            searchParams.add("title LIKE \"%"+title+"%\"");
        }
        if (year != null) {
            searchParams.add("year = "+year);
        }
        if (director != null) {
            searchParams.add("director LIKE \"%"+director+"%\"");
        }
        if (star != null) {
            searchParams.add("name LIKE \"%"+star+"%\"");
        }
        String searchQuery = String.join(" AND ", searchParams);
        return getMoviesBySearch(searchQuery, perPage, page, sortBy1, order1, sortBy2, order2);
    }

    @Override
    public List<MovieWithDetails> getMoviesBySearch(String searchQuery, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2) {
        List<Movie> movies = new ArrayList<>();
        if (sortBy1 == null) {
            movies = jdbcTemplate.query(
                    "SELECT DISTINCT m.id, m.title, m.year, m.director FROM movies m, stars_in_movies sim, stars s " +
                         "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ") " +
                         "ORDER BY m.id LIMIT " + perPage + " OFFSET " + (page-1)*perPage,
                    (rs, rowNum) ->
                            new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
        }
        else {
            movies = jdbcTemplate.query(
                    "SELECT DISTINCT m.id, m.title, m.year, m.director, r.rating FROM stars_in_movies sim, stars s, (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                            "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ") " +
                            "ORDER BY " + sortBy1 + " " + order1 + ", " + sortBy2 + " " + order2 +
                            " LIMIT " + perPage + " OFFSET " + (page-1)*perPage ,
                    (rs, rowNum) ->
                            new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
        }
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
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String star) {

        List<String> searchParams = new ArrayList<>();
        if (title != null) {
            searchParams.add("title LIKE \"%"+title+"%\"");
        }
        if (year != null) {
            searchParams.add("year = "+year);
        }
        if (director != null) {
            searchParams.add("director LIKE \"%"+director+"%\"");
        }
        if (star != null) {
            searchParams.add("name LIKE \"%"+star+"%\"");
        }
        String searchQuery = String.join(" AND ", searchParams);
        return  jdbcTemplate.queryForObject(
                "SELECT COUNT(DISTINCT m.id, m.title, m.year, m.director) FROM movies m, stars_in_movies sim, stars s " +
                        "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ") ",
                Integer.class);
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
        List<Movie> movies = new ArrayList<>();
        if (sortBy1 == null) {
            movies = jdbcTemplate.query(
                    "SELECT m.id, m.title, m.year, m.director FROM movies m, genres_in_movies gim WHERE (gim.genreId = \""
                        + id + "\") AND (m.id = gim.movieId) ORDER BY m.id LIMIT " + perPage + " OFFSET " + (page-1)*perPage,
                    (rs, rowNum) ->
                            new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
        }
        else {
            movies = jdbcTemplate.query(
                    "SELECT m.id, m.title, m.year, m.director FROM genres_in_movies gim, (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                            "WHERE (gim.genreId = " + id + ") AND (m.id = gim.movieId) " +
                            "ORDER BY " + sortBy1 + " " + order1 + ", " + sortBy2 + " " + order2 +
                            " LIMIT " + perPage + " OFFSET " + (page-1)*perPage ,
                    (rs, rowNum) ->
                            new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
        }
        List<MovieWithDetails> moviesWithDetails = new ArrayList<>();
        for (Movie m: movies) {
            MovieWithDetails movieWithDetails = new MovieWithDetails(m, get3GenresByMovieId(m.getId()), get3StarsByMovieId(m.getId()), getRatingById(m.getId()));
            moviesWithDetails.add(movieWithDetails);
        }
        return moviesWithDetails;

    }

    @Override
    public List<MovieWithDetails> getMoviesByTitle(String startsWith, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2) {
        List<Movie> movies = new ArrayList<>();
        if (sortBy1 == null) {
            if (startsWith.equals("*")) {
                movies = jdbcTemplate.query(
                        "SELECT id, title, year, director FROM movies WHERE title NOT regexp \"^[[:alnum:]]\" " +
                                "ORDER BY id LIMIT " + perPage + " OFFSET " + (page - 1) * perPage,
                        (rs, rowNum) ->
                                new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
            }
            else {
                movies = jdbcTemplate.query(
                        "SELECT id, title, year, director FROM movies WHERE title LIKE  \"" + startsWith + "%\" " +
                                "ORDER BY id LIMIT " + perPage + " OFFSET " + (page - 1) * perPage,
                        (rs, rowNum) ->
                                new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
            }
        }
        else {
            if (startsWith.equals("*")) {
                movies = jdbcTemplate.query(
                        "SELECT m.id, m.title, m.year, m.director FROM (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                                "WHERE (m.id = r.movieId) AND title NOT regexp \"^[[:alnum:]]\"" +
                                "ORDER BY " + sortBy1 + " " + order1 + ", " + sortBy2 + " " + order2 +
                                " LIMIT " + perPage + " OFFSET " + (page - 1) * perPage,
                        (rs, rowNum) ->
                                new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
            }
            else {
                movies = jdbcTemplate.query(
                        "SELECT m.id, m.title, m.year, m.director FROM (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                                "WHERE (m.id = r.movieId) AND title LIKE  \"" + startsWith + "%\" " +
                                "ORDER BY " + sortBy1 + " " + order1 + ", " + sortBy2 + " " + order2 +
                                " LIMIT " + perPage + " OFFSET " + (page - 1) * perPage,
                        (rs, rowNum) ->
                                new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));
            }
        }
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
        return  jdbcTemplate.queryForObject(
                "SELECT COUNT(movieId) FROM genres_in_movies WHERE (genreId = \"" + genreId + "\")",
                        Integer.class);
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
            return  jdbcTemplate.queryForObject(
                    "SELECT COUNT(id) FROM movies WHERE title LIKE  \"" + startsWith + "%\"",
                    Integer.class);
        }
    }

    // ~~~~~~~~~ LOGIN FUNCTIONS
    @RequestMapping(
            value = "api/auth",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public @ResponseBody ResponseEntity authenticate(@RequestBody Map<String, String> user) {
        String email = user.get("username");
        String password = user.get("password");

        Boolean userAuthenticated = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT email FROM customers WHERE email=\"" + email + "\" " +
                        "AND password=\"" + password + "\")", Boolean.class);

        return ResponseEntity.ok(userAuthenticated);
    }


    // *************** SHOPPING CART ******************
    @RequestMapping(
            value = "api/shopping/addToCart/{movieId}/{quantity}",
            method = RequestMethod.GET
    )
    @Override
    public Map<String,Boolean> addToShoppingCart(@PathVariable String movieId, @PathVariable int quantity, HttpSession session) {
        if (session.getAttribute("cart") == null) {
            System.out.println("No items in cart yet");
            Map<String,Integer> cart = new HashMap< String,Integer>();
            cart.put(movieId, 1);
            session.setAttribute("cart", cart);
            System.out.println("Cart contents: " + cart);

            Map<String, Boolean> json = new HashMap<String, Boolean>();
            json.put("success", true);
            return json;
        }
        else {
            Map<String,Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
            Integer curr = cart.get(movieId);
            if (curr == null) {
                cart.put(movieId, 1);
            }
            else {
                cart.put(movieId, curr+quantity);
            }
            session.setAttribute("cart",cart);
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
            method = RequestMethod.GET
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

//    @RequestMapping(
//            value="/api/shopping/makePurchase",
//            method = RequestMethod.POST
//    )
//    public Map<String,Boolean> makePurchase(HttpSession session, @RequestBody Map<String, Object> payload) {
//
//
//    }

}
