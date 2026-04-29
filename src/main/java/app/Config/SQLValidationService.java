package app.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLValidationService {
    private EnvValidationService env;

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

        String URL = "jdbc:mysql://" + host + ":" + port + "/" + DBName;

        System.out.println("[INFO] Trying to Build the MySQL Connection with the .env values");
        System.out.println("[DEBUG] URL: " + URL);

        try {
            Connection connection = DriverManager.getConnection(URL, DBUser, PWSD);
            System.out.println("[OK] The Database connection established sucsessfully ");
        } catch (SQLException error) {
            System.out.println("[ERROR] Failed to connect to Database");
            System.out.println(error.getMessage());
        }
    }
}
