package com.fabflix.fabflix;

import com.fabflix.fabflix.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpSession;

@SpringBootApplication
public class FabflixApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(FabflixApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("jdbcMovieRepository")
    private MovieRepository movieListRepository;

    public static void main(String[] args) {
        SpringApplication.run(FabflixApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("FabflixApplication...");
//        testLogging();
    }

//    void testLogging() {
//        log.info("Testing user login...\n");
//        Vector<Boolean> testResults = new Vector<Boolean>(10);
//
//        boolean test1 = movieListRepository.authenticate("dyoung@gmail.com", "disney392"); // true
//        boolean test2 = movieListRepository.authenticate("alexislaurenvu@gmail.com", "heybitch"); // false
//        boolean test3 = movieListRepository.authenticate("neepeefi@izuboogh.com", "aelohtho"); // true
//        boolean test4 = movieListRepository.authenticate("dumb@gmail.com", "poop"); // false
//        boolean test5 = movieListRepository.authenticate("ben.bchu@gmail.com", "peace"); // true
//        testResults.add(test1);
//        testResults.add(test2);
//        testResults.add(test3);
//        testResults.add(test4);
//        testResults.add(test5);
//
//        for (Boolean r : testResults)
//            System.out.println(r);
//    }
//
//    void runJDBC() {
//        log.info("Getting Ratings of Top 20 Movies...\n");
//        List<Rating> ratings = movieListRepository.findTopTwenty();
//
//        log.info("\nResults:\n");
//        for (Rating r : ratings)
//            System.out.println(r.toString());
//
//        log.info("\nGetting Top 20 Movies...\n");
//        List<Movie> movies = movieListRepository.getTopTwentyList();
//
//        log.info("\nResults:\n");
//        for (Movie m : movies)
//            System.out.println(m.toString());
//
//        log.info("\nGetting genres of Top 20 Movies...\n");
//        Vector<List<Genre>> genres = new Vector<List<Genre>>(20);

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
//    }
}