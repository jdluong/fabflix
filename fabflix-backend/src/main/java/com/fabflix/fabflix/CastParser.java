package com.fabflix.fabflix;

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
    // map for inserting entries in stars_in_movies table
    // key = movie name, value = cast members
    Map<String, ArrayList<String>> starsInMovies;
    Document document;

    public CastParser() {
        this.starsInMovies = new HashMap<>();
    }

    public void run() {
        parseXml();
        parseCasts();
        showData();
    }

    public void parseXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse("casts124.xml");
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

    public static void main(String[] args) {
        CastParser parser = new CastParser();
        parser.run();
    }
}

