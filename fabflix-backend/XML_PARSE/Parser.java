package XML_PARSE;

import java.sql.SQLException;

public class Parser {
    public static void main(String[] args) {
        MovieParser movieParser = new MovieParser();
        StarParser starParser = new StarParser();
        CastParser castParser = new CastParser();

        try {
            movieParser.run();
            starParser.run();
            castParser.run();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
