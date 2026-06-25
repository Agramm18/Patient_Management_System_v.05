package app.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class SQLValidationService {
    private final EnvValidationService env;
    private String sqlUser;
    private String sqlPassword;
    private String sqlURL;

    //Constructor to load env values from EnvValidationService.java
    public SQLValidationService(EnvValidationService env) {
        this.env = env;
    }

    public boolean dbConnection() {
        String host = env.getHost();
        int port = env.getPort();
        String dbName = env.getDBName();
        String dbUser = env.getUser();
        String dbPassword = env.getPassword();

        System.out.println("[INFO] Building Global SQL Connection");
        LogManager.log(LogType.CONFIG_INFO, "Building Global importable SQL Connection");
        String URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName; //MySQL Connector URL

        System.out.println("[DEBUG] SQL URL: " + URL);
        LogManager.log(LogType.SQL_DEBUG, "URL: " + URL);

        //Routing Class variables to method variables
        this.sqlUser = dbUser;
        this.sqlPassword = dbPassword;
        this.sqlURL = URL;

        try (Connection connection = DriverManager.getConnection(URL, dbUser, dbPassword)) {
            System.out.println("[OK] SQL is connected successfully");
            LogManager.log(LogType.SQL_OK, "The Database connection established successfully ");
            return true;
        } catch (SQLException error) {
            System.out.println("[ERROR] Something wen wrong with the SQL");
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            return false;
        }
    }

    //Building getter to maintain a global connection
    public String getSqlUser() {
        return sqlUser;
    }

    public String getSqlPassword() {
        return sqlPassword;
    }

    public String getSqlURL() {
        return sqlURL;
    }
}
