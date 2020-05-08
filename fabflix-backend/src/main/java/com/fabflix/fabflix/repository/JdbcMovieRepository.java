package com.fabflix.fabflix.repository;

import com.fabflix.fabflix.*;
import com.fabflix.fabflix.repository.MovieRepository;
import org.apache.coyote.Response;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080"}, allowCredentials = "true")
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
            @RequestParam(required = false) String order2) throws SQLException {

        List<String> searchParams = new ArrayList<>();

        if (title != null) {
            searchParams.add("title LIKE \"%" + title + "%\"");
        }
        if (year != null) {
            searchParams.add("year = ?");
        }
        if (director != null) {
            searchParams.add("director LIKE \"%" + director + "%\"");
        }
        if (star != null) {
            searchParams.add("name LIKE \"%" + star + "%\"");
        }

        String searchQuery = String.join(" AND ", searchParams);
        String sql;
        if (sortBy1 == null) {
            sql = "SELECT DISTINCT m.id, m.title, m.year, m.director FROM movies m, stars_in_movies sim, stars s " +
                    "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ") " +
                    "ORDER BY m.id LIMIT " + perPage + " OFFSET " + (page-1)*perPage;
        }
        else {
            sql = "SELECT DISTINCT m.id, m.title, m.year, m.director, r.rating FROM stars_in_movies sim, stars s, (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                    "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ") " +
                    "ORDER BY " + sortBy1 + " " + order1 + ", " + sortBy2 + " " + order2 +
                    " LIMIT " + perPage + " OFFSET " + (page-1)*perPage;
        }

        Connection conn = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        if (year != null)
            stmt.setInt(1, year);

        return getMoviesBySearch(stmt, perPage, page, sortBy1, order1, sortBy2, order2);
    }

    @Override
    public List<MovieWithDetails> getMoviesBySearch(PreparedStatement stmt, int perPage, int page, String sortBy1, String order1, String sortBy2, String order2) {
        List<Movie> movies = new ArrayList<>();
        movies = jdbcTemplate.query(connection -> stmt,
                (rs, i) -> new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));

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
            searchParams.add("year = ?");
        }
        if (director != null) {
            searchParams.add("director LIKE \"%"+director+"%\"");
        }
        if (star != null) {
            searchParams.add("name LIKE \"%"+star+"%\"");
        }

        String searchQuery = String.join(" AND ", searchParams);
        String sql = "SELECT COUNT(DISTINCT m.id, m.title, m.year, m.director) FROM movies m, stars_in_movies sim, stars s " +
                "WHERE (sim.starId = s.id) AND (sim.movieId = m.id) AND (" + searchQuery + ") ";

        return jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);

            if (year != null)
                stmt.setInt(1, year);

            return stmt;
        }, rs -> {
            rs.next();
            return rs.getInt(1);
        });
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
        List<Movie> movies = new ArrayList<Movie>();
        String sql;
        if (sortBy1 == null) {
            sql = "SELECT m.id, m.title, m.year, m.director FROM movies m, genres_in_movies gim WHERE (gim.genreId = ?) " +
                    "AND (m.id = gim.movieId) ORDER BY m.id LIMIT " + perPage + " OFFSET " + (page-1)*perPage;
        }
        else {
            sql = "SELECT m.id, m.title, m.year, m.director FROM genres_in_movies gim, (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                    "WHERE (gim.genreId = ?) AND (m.id = gim.movieId) " +
                    "ORDER BY " + sortBy1 + " " + order1 + ", " + sortBy2 + " " + order2 +
                    " LIMIT " + perPage + " OFFSET " + (page-1)*perPage;
        }

        movies = jdbcTemplate.query(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
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
        String sql;
        if (sortBy1 == null) {
            if (startsWith.equals("*")) {
                sql = "SELECT id, title, year, director FROM movies WHERE title NOT regexp \"^[[:alnum:]]\" " +
                        "ORDER BY id LIMIT " + perPage + " OFFSET " + (page - 1) * perPage;
            }
            else {
                sql = "SELECT id, title, year, director FROM movies WHERE title LIKE  \"" + startsWith + "%\" " +
                        "ORDER BY id LIMIT " + perPage + " OFFSET " + (page - 1) * perPage;
            }
        }
        else {
            if (startsWith.equals("*")) {
                sql = "SELECT m.id, m.title, m.year, m.director FROM (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                        "WHERE (m.id = r.movieId) AND title NOT regexp \"^[[:alnum:]]\"" +
                        "ORDER BY " + sortBy1 + " " + order1 + ", " + sortBy2 + " " + order2 +
                        " LIMIT " + perPage + " OFFSET " + (page - 1) * perPage;
            }
            else {
                sql = "SELECT m.id, m.title, m.year, m.director FROM (movies m LEFT JOIN ratings r ON m.id = r.movieId) " +
                        "WHERE (m.id = r.movieId) AND title LIKE  \"" + startsWith + "%\" " +
                        "ORDER BY " + sortBy1 + " " + order1 + ", " + sortBy2 + " " + order2 +
                        " LIMIT " + perPage + " OFFSET " + (page - 1) * perPage;
            }
        }

        movies = jdbcTemplate.query(sql, (rs, rowNum)
                -> new Movie(rs.getString("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director")));

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
        System.out.println("BEFORE CACHE PARAMS: "+session.getAttribute("searchParams"));
        session.setAttribute("searchParams", payload);
        System.out.println("AFTER CACHE PARAMS: "+session.getAttribute("searchParams"));

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
            System.out.println("GETTING CACHE PARAMS: "+session.getAttribute("searchParams"));
            return (Map<String,Object>) session.getAttribute("searchParams");
        }
    }
}


