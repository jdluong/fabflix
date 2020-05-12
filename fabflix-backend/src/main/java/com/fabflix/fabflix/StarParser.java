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

public class StarParser {
    List<Star> stars;
    Document document;

    public StarParser() {
        this.stars = new ArrayList<>();
    }

    public void run() {
        parseXml();
        parseStars();
        showData();
    }

    public void parseXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse("actors63.xml");
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        }
    }

    public void parseStars() {
        Element root = document.getDocumentElement();
        NodeList nl = root.getElementsByTagName("actor");

        if (nl != null && nl.getLength() > 0) {
            int size = nl.getLength();

            for (int i = 0; i < size; ++i) {
                Element star = (Element) nl.item(i);

                Star s = getStar(star);
                stars.add(s);
            }
        }
    }

    private Star getStar(Element data) {
        String name = null;
        int birthYear = -1;

        name = getTextValue(data, "stagename");
        birthYear = getIntValue(data, "dob");

        if (name != null)
            return new Star(null, name, birthYear);
        else
            return null;
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

    private int getIntValue(Element data, String tag) {
        try {
            return Integer.parseInt(getTextValue(data, tag).replaceAll("\\s", ""));
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }

    private void showData() {
        System.out.println("# Stars Parsed: " + stars.size());

        for (Star s : stars)
            System.out.println(s);
    }

    public static void main(String[] args) {
        StarParser parser = new StarParser();
        parser.run();
    }
}
