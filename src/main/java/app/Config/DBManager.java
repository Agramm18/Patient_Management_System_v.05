package app.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import app.Config.LogManager;
import app.Config.LogManager.LogType;
/*
 This section is the DB Manager who collects the Username, PWSD and Url from the SQL Connection

 The Idea behind this was to Maintain a global connection instead of injecting everytime the Connection so I can Import the connection


*/

public final class DBManager {
    private static String sqlUser;
    private static String sqlPassword;
    private static String sqlURL;

    public static boolean initialize(
            String user,
            String password,
            String url
    ) {
        LogManager.log(LogType.SQL_INFO, "Building global DB Connection");
        LogManager.log(LogType.SQL_INFO, "Save global DB runtime config");
        LogManager.log(LogType.SQL_INFO, "Set DB Config");

        sqlUser = user;
        sqlPassword = password;
        sqlURL = url;

        if (user == null || user.isBlank() || password == null || password.isBlank() || sqlURL == null || sqlURL.isBlank()) {
            LogManager.log(LogType.SQL_ERROR, "Something went wrong or is missing");
            return false;
        } else {
            System.out.println("[OK] Global SQL Connection was a success");
            LogManager.log(LogType.SQL_OK, "The Global SQL Connection was a success");
            return true;
        }

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                sqlURL,
                sqlUser,
                sqlPassword
        );
    }
}
