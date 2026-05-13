package app.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBManager {
    private static String sqlUser;
    private static String sqlPWSD;
    private static String sqlURL;

    private DBManager() {

    }

    public static void initialize(
            String user,
            String password,
            String URL
    ) {
        System.out.println("\n[INFO] Building global DB Connection");
        System.out.println("[INFO] Save global DB runtime config");
        System.out.println("[INFO] Set DB Config\n");

        sqlUser = user;
        sqlPWSD = password;
        sqlURL = URL;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                sqlURL,
                sqlUser,
                sqlPWSD
        );
    }
}
