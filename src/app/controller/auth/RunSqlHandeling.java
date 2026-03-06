package app.controller.auth;
//Load SQL Connection libaries
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

import app.controller.auth.account.AccountStatus;
//load dotenv libaries
import io.github.cdimascio.dotenv.Dotenv;

//load filehandler
import java.io.File;

public class RunSqlHandeling {
    String DbHost;
    String DBPort;
    int DBPortINT;
    String DBName;
    String DBUser;
    String DbPWSD;
    boolean EnvValid;
    boolean ConnectionValid;

    public void sqlHeader() {
        System.out.println("\n==================================================");
        System.out.println("              Configuration & Database");
        System.out.println("--------------------------------------------------");
        System.out.println("   Validating .env configuration");
        System.out.println("   Establishing SQL connection");
        System.out.println("==================================================\n");
    }

    public void ValidateENV() {
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

            else {
                //If everything is valid set the .env values to the parameters
                System.out.println("\nSet the values");
                this.DbHost = Host;
                this.DBPortINT = port;
                this.DBName = Name;
                this.DBUser = User;
                this.DbPWSD = PWSD;
                env = true;
                this.EnvValid = env;
                System.out.println("Values setted\n");

                System.out.println(".env parameters loaded and setted sucsessfully\n");
            }

        } catch (IllegalArgumentException error) {
            System.out.println("\nSomething in your .env file is wrong or corrupted");
            System.out.println("Error: " + error + "\n");
        }
    }

    public void SQLConnection(Scanner scanner) {
        try {
            boolean SQLValid = false;

            System.out.println("SQL Starting.....");
            //Set connection link with parameters
            String url = "jdbc:mysql://" + this.DbHost + ":" + this.DBPortINT + "/" + this.DBName;

            //load connection
            Connection connection = DriverManager.getConnection(url, this.DBUser, this.DbPWSD);
            SQLValid = true;

            //validate if everything worked with base values
            if (!SQLValid || !EnvValid) {
                throw new IllegalStateException("It seems that something unexpected happend");
            } else 
                {
                //if everything worked the account class will be called
                System.out.println("SQL Connected sucsessfully");
                System.out.println("\nEverything worked you will now be redirected to the Account validation\n");

                AccountStatus account = new AccountStatus();
                account.HeaderMSGAccount();
                
                //Throw collected errors and the user must start again
                while (true) {
                    try {
                        account.SetBaseValues(scanner);
                        break;

                    } catch (IllegalArgumentException invalidInput) {
                        System.out.println("\nThere is an error of the Login/Registration logic");
                        System.out.println("The error is: " + invalidInput.getMessage() + "\n");
                    }
                }
            }

        } catch (Exception error) {
            System.out.println("\nIt seems the SQL Connection this didn't work...");
            System.out.println("Error: " + error + "\n");
        }
    }
}