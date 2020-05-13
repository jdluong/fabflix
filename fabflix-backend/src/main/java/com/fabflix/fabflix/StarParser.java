package com.fabflix.fabflix;

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
import java.util.List;

public class StarParser {
    private String USERNAME = "mytestuser";
    private String PASSWORD = "Password!123";

    List<Star> stars;
    Document document;

    public StarParser() {
        this.stars = new ArrayList<>();
    }

    public void run() {
        parseXml();
        parseStars();
        //showData();
        addToDatabase();
    }

    public void parseXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse("classes/actors63.xml");
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

            if (val == null && !tag.equals("dob"))
                System.out.println("Inconsistent Data Found: (Name) = " + e.getNodeName() + ", (Value) = " + e.getNodeValue());
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

    private String generateStarId(Connection connection) throws SQLException {
        String sql = "SELECT CONCAT(SUBSTRING(max(id), 1, 2), SUBSTRING(max(id), 3) + 1) FROM stars";

        Statement stmt = connection.createStatement();
        stmt.execute(sql);

        ResultSet rs = stmt.getResultSet();

        if (rs.next()) {
           String id = rs.getString(1);
           rs.close();
           return id;
        }
        else
            return null;
    }

    private void addToDatabase() {
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", USERNAME, PASSWORD);

            String sql = "INSERT INTO stars VALUES (?, ?, ?)";

            for (Star s : stars) {
                String starId = generateStarId(connection);
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, starId);
                stmt.setString(2, s.getName());

                if (s.getBirthYear() != -1)
                    stmt.setInt(3, s.getBirthYear());
                else
                    stmt.setNull(3, java.sql.Types.INTEGER);

                stmt.execute();
                stmt.close();
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StarParser parser = new StarParser();
        parser.run();
    }
}
