package com.fabflix.fabflix;

import com.fabflix.fabflix.repository.MovieListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class FabflixApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(FabflixApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("jdbcMovieListRepository")
    private MovieListRepository movieRepository;

    public static void main(String[] args) {
        SpringApplication.run(FabflixApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("FabflixApplication...");
        runJDBC();
    }

    void runJDBC() {
        // find all
        log.info("[FIND_TOP_TWENTY] {}", movieRepository.findTopTwenty());
//
//        // find by id
//        log.info("[FIND_BY_ID] :2L");
//        Book book = bookRepository.findById(2L).orElseThrow(IllegalArgumentException::new);
//        log.info("{}", book);
//
//        // find by name (like) and price
//        log.info("[FIND_BY_NAME_AND_PRICE] : like '%Java%' and price <= 10");
//        log.info("{}", bookRepository.findByNameAndPrice("Java", new BigDecimal(10)));
//
//        // get name (string) by id
//        log.info("[GET_NAME_BY_ID] :1L = {}", bookRepository.getNameById(1L));
//
//        // update
//        log.info("[UPDATE] :2L :99.99");
//        book.setPrice(new BigDecimal("99.99"));
//        log.info("rows affected: {}", bookRepository.update(book));
//
//        // delete
//        log.info("[DELETE] :3L");
//        log.info("rows affected: {}", bookRepository.deleteById(3L));
//
//        // find all
//        log.info("[FIND_ALL] {}", bookRepository.findAll());
    }
}