//package com.fabflix.fabflix.controllers;
//
//import com.fabflix.fabflix.Movie;
//import com.fabflix.fabflix.repository.JdbcMovieListRepository;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class MovieController {
//
//    private JdbcMovieListRepository jdbc = new JdbcMovieListRepository();
//
//    @RequestMapping(
//            value = "api/getTopTwentyList",
//            method = RequestMethod.GET
//    )
//    private List<Movie> getTopTwentyMovies() {
//        return jdbc.getTopTwentyList();
//    }
//
//}
