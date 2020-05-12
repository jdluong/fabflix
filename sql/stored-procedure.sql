DELIMITER //
CREATE PROCEDURE `add_movie`(IN title VARCHAR(128), IN year INT, IN director VARCHAR(128), IN star VARCHAR(128), IN genre VARCHAR(128),
                           OUT movie_message VARCHAR (255), OUT star_message VARCHAR(255), OUT genre_message VARCHAR(255))
BEGIN
DECLARE movieId VARCHAR(128);
    DECLARE starId VARCHAR(128) DEFAULT NULL;
    DECLARE genreId INT DEFAULT NULL;

    -- check if movie exists
    DECLARE movieExists INT;
    SELECT COUNT(*) INTO movieExists 
    FROM movies m 
    WHERE (m.title = title) AND (m.year = year) AND (m.director = director);

    -- if movie exists
    IF (movieExists > 0) THEN 
        SET movie_message = 'ERROR: Movie already exists';   
    
    ELSE
        -- generate new movieId
        SELECT CONCAT(SUBSTRING(max(id), 1, 2), SUBSTRING(max(id), 3) + 1) INTO movieId
        FROM movies;
        -- insert new movie into movies table
        INSERT INTO movies VALUES(movieId, title, year, director);
        SET movie_message = CONCAT('(NEW) ', CONCAT(title, CONCAT(': ', movieId)));
        
        -- *** CHECK/ADD AND LINK STARS
        
        -- get starId
        SELECT s.id INTO starId
        FROM stars s 
        WHERE (s.name = star)
        LIMIT 1;
        -- if star does not exist
        IF (starId IS NULL) THEN
            -- generate new starId
            SELECT CONCAT(SUBSTRING(max(id), 1, 2), SUBSTRING(max(id), 3) + 1) INTO starId
            FROM stars;
            -- insert new star into stars table
            INSERT INTO stars VALUES(starId, star, NULL);
            SET star_message = CONCAT(CONCAT('(NEW) ', star), CONCAT(': ', starId));
        -- if star exists
        ELSE
            SET star_message = CONCAT(star, CONCAT(': ', starId));
        END IF;
        -- link star to movie
        INSERT INTO stars_in_movies VALUES(starId, movieId);


        -- *** CHECK/ADD AND LINK STARS
        
        -- get genreId
        SELECT g.id INTO genreId
        FROM genres g 
        WHERE (g.name = genre)
        LIMIT 1;
        -- if genre does not exist
        IF (genreId IS NULL) THEN
            -- generate new genreId
            SELECT (max(id) + 1) INTO genreId
            FROM genres;
            -- insert new genre into genres table
            INSERT INTO genres VALUES(genreId, genre);
            SET genre_message = CONCAT(CONCAT('(NEW) ', genre), CONCAT(': ', genreId)); 
        -- if genre exists
        ELSE
            SET genre_message = CONCAT(genre, CONCAT(': ', genreId));
        END IF;
        -- link genre to movie
        INSERT INTO genres_in_movies VALUES(genreId, movieId);

    END IF;

END //

DELIMITER ;