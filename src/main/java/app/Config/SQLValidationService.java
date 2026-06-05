package app.Config;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class SQLValidationService {
    private EnvValidationService env;
    private String sqlUser;
    private String sqlPassword;
    private String sqlURL;

    //Konstruktor to load env values from EnvValidationService.java
    public SQLValidationService(EnvValidationService env) {
        this.env = env;
    }

    public boolean DBConnection() {
        String host = env.getHost();
        int port = env.getPort();
        String DBName = env.getDBName();
        String DBUser = env.getUser();
        String DBPassword = env.getPassword();

        String URL = "jdbc:mysql://" + host + ":" + port + "/" + DBName; //MySQL Connector URL

        LogManager.log(LogType.MESSAGE, "Trying to Build the MySQL Connection with the .env values");
        LogManager.log(LogType.SQL_DEBUG, "URL: " + URL);

        //Routing Class variables to method variables
        this.sqlUser = DBUser;
        this.sqlPassword = DBPassword;
        this.sqlURL = URL;

        try (Connection connection = DriverManager.getConnection(URL, DBUser, DBPassword)) {
            LogManager.log(LogType.SQL_OK, "The Database connection established successfully ");
            return true;
        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            return false;
        }
    }

    //Building getter to maintain a global connection
    public String getSQLUser() {
        return sqlUser;
    }

    public String getSqlPWSD() {
        return sqlPassword;
    }

    public String getSqlURL() {
        return sqlURL;
    }
}
