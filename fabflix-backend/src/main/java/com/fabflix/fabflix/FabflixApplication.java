package com.fabflix.fabflix;

import com.fabflix.fabflix.repository.MovieListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Vector;

@SpringBootApplication
public class FabflixApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(FabflixApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("jdbcMovieListRepository")
    private MovieListRepository movieListRepository;

    public static void main(String[] args) {
        SpringApplication.run(FabflixApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("FabflixApplication...");
        runJDBC();
    }

    void runJDBC() {
        log.info("Getting Ratings of Top 20 Movies...\n");
        List<Rating> ratings = movieListRepository.findTopTwenty();

        log.info("\nResults:\n");
        for (Rating r : ratings)
            System.out.println(r.toString());

        log.info("\nGetting Top 20 Movies...\n");
        List<Movie> movies = movieListRepository.getTopTwentyList();

        log.info("\nResults:\n");
        for (Movie m : movies)
            System.out.println(m.toString());

        log.info("\nGetting genres of Top 20 Movies...\n");
        Vector<List<Genre>> genres = new Vector<List<Genre>>(20);

//        for (Movie m : movies)
//            genres.add(movieListRepository.getGenreById(m.getId()));

//        int i = 0;
//        log.info("\nResults:\n");
//        for (List<Genre> genreList : genres) {
//            System.out.println();
//            for (Genre g : genreList)
//                System.out.println("Movie " + genres.indexOf(genreList) + " " + g.toString());
//        }

//        log.info("\nGetting stars of Top 20 Movies...\n");
//        Vector<List<Star>> stars = new Vector<List<Star>>(20);
//
//        for (Movie m : movies)
//            stars.add(movieListRepository.getStarById(m.getId()));
//
//        log.info("Results:\n");
//        for (List<Star> starList : stars)
//            for (Star s : starList)
//                System.out.println("Star " + starList.indexOf(s) + " " + s.toString());
    }
}