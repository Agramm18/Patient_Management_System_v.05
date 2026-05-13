package app.Config;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLValidationService {
    private EnvValidationService env;
    private String SQLUser;
    private String sqlPWSD;
    private String sqlURL;

    //Konstruktor to load env values from EnvValidationService.java
    public SQLValidationService(EnvValidationService env) {
        this.env = env;
    }

    public void DBConnection() {
        String host = env.getHost();
        int port = env.getPort();
        String DBName = env.getDBName();
        String DBUser = env.getUser();
        String PWSD = env.getPWSD();

        String URL = "jdbc:mysql://" + host + ":" + port + "/" + DBName; //MySQL Connector URL

        System.out.println("[INFO] Trying to Build the MySQL Connection with the .env values");
        System.out.println("[DEBUG] URL: " + URL);

        //Routing Class variables to method variables
        this.SQLUser = DBUser;
        this.sqlPWSD = PWSD;
        this.sqlURL = URL;

        try {
            Connection connection = DriverManager.getConnection(URL, DBUser, PWSD);
            System.out.println("[OK] The Database connection established sucsessfully ");
        } catch (SQLException error) {
            System.out.println("[ERROR] Failed to connect to Database");
            System.out.println(error.getMessage());
        }
    }

    //Building getter to maintain a global connection
    public String getSQLUser() {
        return SQLUser;
    }

    public String getSqlPWSD() {
        return sqlPWSD;
    }

    public String getSqlURL() {
        return sqlURL;
    }
}
