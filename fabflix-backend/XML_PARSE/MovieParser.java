package XML_PARSE;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieParser {
    private String USERNAME = "mytestuser";
    private String PASSWORD = "Password!123";

//    private String USERNAME = "root";
//    private String PASSWORD = "password";

    Map<Movie, List<String>> movies;
    List<String> genres;
    Document document;

    public MovieParser() {
        this.movies = new HashMap<>();
        this.genres = new ArrayList<>();
    }

    public void run() throws SQLException, ClassNotFoundException {
        parseXml();
        parseMovies();
        addToDatabase();
        //showData();
    }

    public void parseXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse("mains243.xml");
            //document = builder.parse("classes/mains243.xml");
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void parseMovies() {
        Element root = document.getDocumentElement();

        NodeList nl = root.getElementsByTagName("film");

        if (nl != null && nl.getLength() > 0) {
            int size = nl.getLength();

            for (int i = 0; i < size; ++i) {
                Element film = (Element) nl.item(i);
                getMovie(film);
            }
        }
    }

    private void getMovie(Element data) {
        String id = null;
        String title = null;
        int year = -1;
        String director = null;

        id = getTextValue(data, "fid");
        title = getTextValue(data, "t");
        year = getIntValue(data, "year");
        director = getTextValue(data, "dirn");

        ArrayList<String> g = getGenres(data);
        ArrayList<String> convertedGenres = new ArrayList<>();

        if (!g.isEmpty()) {
            for (String name : g) {
                name = name.trim();
                name = name.toLowerCase();

                if (name.length() > 0) {
                    for (String genre : convertGenre(name)) {
                        if (!genres.contains(genre))
                            genres.add(genre);

                        convertedGenres.add(genre);
                    }
                }
            }
        }

        if (id != null && title != null && year != -1 && director != null && convertedGenres.size() > 0)
            movies.put(new Movie(id, title, year, director), convertedGenres);
    }

    private ArrayList<String> getGenres(Element data) {
        ArrayList<String> genres = new ArrayList<>();
        genres = getTextValues(data, "cat");
        return genres;
    }

    private String getTextValue(Element data, String tag) {
        String val = null;
        NodeList nl = data.getElementsByTagName(tag);

        if (nl != null && nl.getLength() > 0) {
            Element e = (Element) nl.item(0);

            if (e.getTagName() != null) {
                if (e.hasChildNodes())
                    val = e.getFirstChild().getNodeValue();
            }

            if (val == null)
                System.out.println("Inconsistent Data Found: (Name) = " + e.getNodeName() + ", (Value) = " + e.getNodeValue());
        }

        return val;
    }

    private ArrayList<String> getTextValues(Element data, String tag) {
        ArrayList<String> values = new ArrayList<String>();
        String val = null;
        NodeList nl = data.getElementsByTagName(tag);

        if (nl != null && nl.getLength() > 0) {
            int size = nl.getLength();

            for (int i = 0; i < size; ++i) {
                Element e = (Element) nl.item(i);

                if (e.getTagName() != null) {
                    if (e.hasChildNodes())
                        val = e.getFirstChild().getNodeValue();

                    if (val != null)
                        values.add(val);
                    else
                        System.out.println("Inconsistent Data Found: (Name) = " + e.getNodeName() + ", (Value) = " + e.getNodeValue());
                }
            }
        }

        return values;
    }

    private int getIntValue(Element data, String tag) {
        try {
            return Integer.parseInt(getTextValue(data, tag).replaceAll("\\s", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private ArrayList<String> convertGenre(String raw) {
        ArrayList<String> converted = new ArrayList<>();

        if (raw.contains("susp"))
            converted.add("Thriller");

        if (raw.contains("cnr") || raw.contains("cart"))
            converted.add("Cops and Robbers");

        if (raw.contains("dram") || raw.contains("draam"))
            converted.add("Drama");

        if (raw.contains("west"))
            converted.add("Western");

        if (raw.contains("myst"))
            converted.add("Mystery");

        if (raw.contains("s.f.") || raw.contains("scif") || raw.contains("scfi") || raw.contains("sxfi"))
            converted.add("Sci-Fi");

        if (raw.contains("advt"))
            converted.add("Adventure");

        if (raw.contains("hor"))
            converted.add("Horror");

        if (raw.contains("romt"))
            converted.add("Romance");

        if (raw.contains("comd"))
            converted.add("Comedy");

        if (raw.contains("musc") || raw.contains("musical") || raw.contains("muusc"))
            converted.add("Musical");

        if (raw.contains("docu"))
            converted.add("Documentary");

        if (raw.contains("por"))
            converted.add("Pornography");

        if (raw.contains("noir"))
            converted.add("Black");

        if (raw.contains("bio"))
            converted.add("Biography");

        if (raw.equals("tv"))
            converted.add("TV Show");

        if (raw.equals("tvmini"))
            converted.add("TV Miniseries");

        if (raw.equals("act") || raw.equals("axtn"))
            converted.add("Action");

        if (raw.contains("fant"))
            converted.add("Fantasy");

        if (raw.contains("hist"))
            converted.add("History");

        if (raw.contains("crim"))
            converted.add("Crime");

        if (raw.contains("sur"))
            converted.add("Surreal");

        if (raw.equals("natu"))
            converted.add("Nature");

        if (raw.equals("avga") || raw.equals("avant garde") || raw.equals("expm") || raw.equals("weird"))
            converted.add("Avant Garde");

        if (raw.isEmpty())
            converted.add("Miscellaneous");

        return converted;
    }

    private void showData() {
        System.out.println("# Movies Parsed: " + movies.size());

        for (Map.Entry<Movie, List<String>> entry : movies.entrySet()) {
            System.out.println("Movie: " + entry.getKey());
            System.out.print("Genre(s): ");
            for (String g : entry.getValue())
                System.out.print(g + " ");

            System.out.println("\n");
        }

        System.out.println("# Genres Parsed: " + genres.size());
        for (String genre : genres)
            System.out.println(genre);
    }

    private void addNewGenres(Connection connection) throws SQLException {
        String querySql = "SELECT name FROM genres";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(querySql);

        List<String> currentGenres = new ArrayList<>();

        while (rs.next()) {
            currentGenres.add(rs.getString("name"));
        }

        List<String> newGenres = new ArrayList<>();

        for (String g : genres) {
            if (!currentGenres.contains(g))
                newGenres.add(g);
        }

        // add new genres, less time spent using data
        for (String g : newGenres) {
            String addSql = "INSERT INTO genres (name) VALUES (\"" + g + "\")";
            stmt.execute(addSql);
        }

        rs.close();
        stmt.close();
    }

    private String generateMovieId(Connection connection) throws SQLException {
        String generateIdSql = "SELECT CONCAT(SUBSTRING(max(id), 1, 2), SUBSTRING(max(id), 3) + 1) FROM movies";

        Statement stmt = connection.createStatement();
        stmt.execute(generateIdSql);

        ResultSet rs = stmt.getResultSet();

        if (rs.next()) {
            String id = rs.getString(1);
            rs.close();
            stmt.close();
            return id;
        }

        rs.close();
        stmt.close();
        return null;
    }

    private void addToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", USERNAME, PASSWORD);
            addNewGenres(connection);

            String movieSql = "INSERT INTO movies VALUES (?, ?, ?, ?)";
            String updateSql = "INSERT INTO genres_in_movies (genreId, movieId) VALUES ((SELECT id FROM genres WHERE name=?), (SELECT id FROM movies WHERE id=?))";

            for (Map.Entry<Movie, List<String>> entry : movies.entrySet()) {
                // add the movie to the db
                String movieId = generateMovieId(connection);
                
                if (movieId != null) {
                    PreparedStatement stmt = connection.prepareStatement(movieSql);
                    stmt.setString(1, movieId);
                    stmt.setString(2, entry.getKey().getTitle());
                    stmt.setInt(3, entry.getKey().getYear());
                    stmt.setString(4, entry.getKey().getDirector());
                    stmt.execute();

                    for (String genre : entry.getValue()) {
                        PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                        updateStmt.setString(1, genre);
                        updateStmt.setString(2, movieId);
                        updateStmt.execute();
                        updateStmt.close();
                    }

                    stmt.close();
                }
            }

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MovieParser parser = new MovieParser();
        try {
            parser.run();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

