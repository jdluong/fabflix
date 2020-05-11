package com.fabflix.fabflix;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieParser {
    List<Movie> movies;
    List<String> genres;
    Document document;

    public MovieParser() {
        this.movies = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public void run() {
        parseXml();
        parseMovies();
        showData();
    }

    public void parseXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse("mains243.xml");
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

                Movie m = getMovie(film);
                movies.add(m);
            }
        }
    }

    private Movie getMovie(Element data) {
        String id = null;
        String title = null;
        int year = -1;
        String director = null;

        id = getTextValue(data, "fid");
        title = getTextValue(data, "t");
        year = getIntValue(data, "year");
        director = getTextValue(data, "dirn");

        ArrayList<String> g = getGenres(data);
        if (!g.isEmpty()) {
            for (String name : g) {
                name = name.trim();
                name = name.toLowerCase();
                if (name.length() > 0 && !genres.contains(name))
                    genres.add(name);
            }
        }

        if (id != null && title != null && year != -1 && director != null)
            return new Movie(id, title, year, director);
        else
            return null;
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

    private void showData() {
        System.out.println("# Movies Parsed: " + movies.size());

        for (Movie m : movies)
            System.out.println(m);

        System.out.println("\n# Genres Parsed: " + genres.size());

        for (String g : genres)
            System.out.println(g);
    }

    public static void main(String[] args) {
        MovieParser parser = new MovieParser();
        parser.run();
    }
}

