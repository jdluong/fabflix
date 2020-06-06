package com.fabflix.fabflix;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class CastParser {
    private String USERNAME = "mytestuser";
    private String PASSWORD = "Password!123";

//    private String USERNAME = "root";
//    private String PASSWORD = "password";

    // map for inserting entries in stars_in_movies table
    // key = movie name, value = cast members
    Map<String, ArrayList<String>> starsInMovies;
    Document document;

    public CastParser() {
        this.starsInMovies = new HashMap<>();
    }

    public void run() throws SQLException {
        parseXml();
        parseCasts();
        addToDatabase();
        //showData();
    }

    public void parseXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //document = builder.parse("casts124.xml");
            document = builder.parse("classes/casts124.xml");
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseCasts() {
        Element root = document.getDocumentElement();
        NodeList nl = root.getElementsByTagName("m");

        if (nl != null && nl.getLength() > 0) {
            int size = nl.getLength();

            for (int i = 0; i < size; ++i) {
                Element cast = (Element) nl.item(i);

                String actor = null;
                String movie = null;

                movie = getTextValue(cast, "t");
                actor = getTextValue(cast, "a");

                if (actor != null && movie != null) {
                    if (starsInMovies.containsKey(movie))
                        starsInMovies.get(movie).add(actor);
                    else
                        starsInMovies.put(movie, new ArrayList<>());
                }
            }
        }
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

    private void showData() {
        System.out.println("# Stars in Movies Found: " + starsInMovies.size());

        for (Map.Entry<String, ArrayList<String>> entry : starsInMovies.entrySet()) {
            System.out.println("Movie: " + entry.getKey());
            System.out.println("\nStars: ");
            for (String s : entry.getValue())
                System.out.println(s);

            System.out.println();
        }
    }

    private String getStarId(Connection connection, String name) throws SQLException {
        name = name.replaceAll("\"", "'");
        String sql = "SELECT id FROM stars WHERE name=\"" + name + "\" LIMIT 1";
        Statement stmt = connection.createStatement();
        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String id = rs.getString(1);
                rs.close();
                stmt.close();
                return id;
            }
            else {
                rs.close();
                stmt.close();
                return null;
            }

        } catch (SQLException e) {
            return null;
        }
    }

    private String getMovieId(Connection connection, String name) throws SQLException {
        String sql = "SELECT id FROM movies WHERE title=\"" + name + "\" LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            String id = rs.getString(1);
            stmt.close();
            rs.close();
            return id;
        }

        stmt.close();
        rs.close();
        return null;
    }

    private void addToDatabase() throws SQLException {
        try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", USERNAME, PASSWORD);

          String sql = "INSERT INTO stars_in_movies (starId, movieId) VALUES (?, ?)";

          for (Map.Entry<String, ArrayList<String>> entry : starsInMovies.entrySet()) {
              String movieId = getMovieId(connection, entry.getKey());
              for (String star : entry.getValue()) {
                  String starId = getStarId(connection, star);
                  if (starId != null && movieId != null) {
                      PreparedStatement stmt = connection.prepareStatement(sql);
                      stmt.setString(1, starId);
                      stmt.setString(2, movieId);
                      stmt.execute();
                      stmt.close();
                  }
              }
          }
          connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CastParser parser = new CastParser();
        try {
            parser.run();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

