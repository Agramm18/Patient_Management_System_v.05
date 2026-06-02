package app.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/*
 This section is the DB Manager who collects the Username, PWSD and Url from the SQL Connection

 The Idea behind this was to Maintain a global connection instead of injecting everytime the Connection so I can Import the connection


*/

public final class DBManager {
    private static String sqlUser;
    private static String sqlPWSD;
    private static String sqlURL;

    private DBManager() {

    }

    public static boolean initialize(
            String user,
            String password,
            String url
    ) {
        System.out.println("\n[INFO] Building global DB Connection");
        System.out.println("[INFO] Save global DB runtime config");
        System.out.println("[INFO] Set DB Config\n");

        sqlUser = user;
        sqlPWSD = password;
        sqlURL = url;

        if (user == null || user.isBlank() || password == null || password.isBlank() || sqlURL == null || sqlURL.isBlank()) {
            return false;
        } else {
            return true;
        }

    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                sqlURL,
                sqlUser,
                sqlPWSD
        );
    }
}
