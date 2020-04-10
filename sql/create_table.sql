CREATE DATABASE moviedb;
USE moviedb;

CREATE TABLE movies (
    id          varchar(10) DEFAULT '' NOT NULL,
    title       varchar(100) DEFAULT '' NOT NULL,
    year        int NOT NULL,
    director    varchar(100) DEFAULT '' NOT NULL,
    PRIMARY KEY (id));

CREATE TABLE stars (
    id          varchar(10) DEFAULT '' NOT NULL,
    name        varchar(100) DEFAULT '' NOT NULL,
    birthYear   int,
    PRIMARY KEY (id));

CREATE TABLE stars_in_movies (
    starId      varchar(10) DEFAULT '' NOT NULL,
    movieId     varchar(10)  DEFAULT '' NOT NULL,
    FOREIGN KEY (starId) REFERENCES stars(id),
    FOREIGN KEY (movieId) REFERENCES movies(id));

CREATE TABLE genres (
    id          int NOT NULL AUTO_INCREMENT,
    name        varchar(32) DEFAULT '' NOT NULL,
    PRIMARY KEY (id));

CREATE TABLE genres_in_movies (
    genreId     int NOT NULL,
    movieId     varchar(10) DEFAULT '' NOT NULL,
    FOREIGN KEY (genreId) REFERENCES genres(id),
    FOREIGN KEY (movieId) REFERENCES movies(id));

CREATE TABLE creditcards (
    id          varchar(20) DEFAULT '' NOT NULL,
    firstName   varchar(50) DEFAULT '' NOT NULL,
    lastName    varchar(50) DEFAULT '' NOT NULL,
    expiration  date NOT NULL,
    PRIMARY KEY (id));

CREATE TABLE customers (
    id          int NOT NULL AUTO_INCREMENT,
    firstName   varchar(50) DEFAULT '' NOT NULL,
    lastName    varchar(50) DEFAULT '' NOT NULL,
    ccId        varchar(20) DEFAULT '' NOT NULL,
    address     varchar(200) DEFAULT '' NOT NULL,
    email       varchar(50) DEFAULT '' NOT NULL,
    password    varchar(20) DEFAULT '' NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (ccId) REFERENCES creditcards(id));

CREATE TABLE sales (
    id          int NOT NULL AUTO_INCREMENT,
    customerId  int NOT NULL,
    movieId     varchar(10) DEFAULT '' NOT NULL,
    saleDate    date NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customerId) REFERENCES customers(id));

CREATE TABLE ratings (
    movieId     varchar(10) DEFAULT '' NOT NULL,
    rating      float NOT NULL,
    numVotes    int NOT NULL,
    FOREIGN KEY (movieId) REFERENCES movies(id));
