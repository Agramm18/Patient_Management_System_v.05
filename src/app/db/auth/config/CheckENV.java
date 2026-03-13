package app.db.auth.config;
import java.util.Scanner;
import java.io.File;

import io.github.cdimascio.dotenv.Dotenv;

public class CheckENV {

    String DbHost;
    String DBPort;
    int DBPortINT;
    String DBName;
    String DBUser;
    String DbPWSD;
    String Admin_Auth_KEY;
    boolean EnvValid;
    
    public void sqlHeader() {
        System.out.println("\n==================================================");
        System.out.println("              Configuration & Database");
        System.out.println("--------------------------------------------------");
        System.out.println("   Validating .env configuration");
        System.out.println("   Establishing SQL connection");
        System.out.println("==================================================\n");
    }

    public void ValidateENV(Scanner scanner) {
        try {
            boolean env = false;
            int port;

            //Check if .env file exsist
            System.out.println("\nLoad .env files...........\n");

            File envFile = new File(".env");

            if (!envFile.exists()) {
                throw new IllegalArgumentException("It seems that the .env file do not exsist in this project");
            } else {
                System.out.println("It seems that the .env file exsists");
            }

            Dotenv dotenv = Dotenv.load();

            //Set the base values from the .env values
            String Host = dotenv.get("DB_HOST");
            String PortSTR = dotenv.get("DB_PORT");
            String Name = dotenv.get("DB_NAME");
            String User = dotenv.get("DB_USER");
            String PWSD = dotenv.get("DB_PWSD");
            String ADMIN_KEY = dotenv.get("Admin_Center_AUTH_KEY");

            //Check if env exsist if not throw error msg
            if (Host == null) {
                throw new IllegalArgumentException("It seems that your DB_HOST for the db host seems to be empty");
            }

            else if (PortSTR == null) {
                throw new IllegalArgumentException("It seems that your DB_PORT in the .env for the DB Port seems to be empty/0");
            }

            port = Integer.parseInt(PortSTR);

            if (Name == null) {
                throw new IllegalArgumentException("It seems that your DB_NAME in the .env for the db name seems to be empty");
            }

            else if (User == null) {
                throw new IllegalArgumentException("It seems that your the DB_USER in your .env seems to be empty");
            }

            else if (PWSD == null) {
                throw new IllegalArgumentException("It seems that the DB_PWSD in your .env seems to be empty");
            }

            else if (ADMIN_KEY == null) {
                throw new IllegalArgumentException("The Admin key can't be empty");
            }

            else {
                //If everything is valid set the .env values to the parameters
                System.out.println("\nSet the values");
                this.DbHost = Host;
                this.DBPortINT = port;
                this.DBName = Name;
                this.DBUser = User;
                this.DbPWSD = PWSD;
                this.Admin_Auth_KEY = ADMIN_KEY;
                env = true;
                this.EnvValid = env;
                System.out.println("Values setted\n");
                System.out.println(".env parameters loaded and setted sucsessfully\n");

                CheckDBConnection run = new CheckDBConnection(
                    this.DbHost,
                    this.DBPortINT,
                    this.DBName,
                    this.DBUser,
                    this.DbPWSD,
                    this.EnvValid,
                    this.Admin_Auth_KEY
                );

                run.SQLConnection(scanner);
            }

        } catch (IllegalArgumentException error) {
            System.out.println("\nSomething in your .env file is wrong or corrupted");
            System.out.println("Error: " + error + "\n");
        }
    }
    
}
