//Load SQL Connection libaries
import java.sql.Connection;
import java.sql.DriverManager;


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

    public void sqlHeader() {
        System.out.println("\n==============================================");
        System.out.println("=== Running .env validation and connect to sql ===");
        System.out.println("====================================================\n");
    }

    public void ValidateENV() {
        try {
            //Set env parameter
            System.out.println("\nLoad .env files...........\n");

            File envFile = new File(".env");

            if (!envFile.exists()) {
                throw new IllegalArgumentException("It seems that the .env file do not exsist in this project");
            } else {
                System.out.println("It seems that the .env file exsists");
            }

            Dotenv dotenv = Dotenv.load();

            //Collect .env parameters
            String Host = dotenv.get("DB_HOST");
                                
            String PortSTR = dotenv.get("DB_PORT");
            int PortINT = Integer.parseInt(PortSTR);

            String Name = dotenv.get("DB_NAME");

            String User = dotenv.get("DB_USER");
            
            String PWSD = dotenv.get("DB_PWSD");

            if (Host == null) {
                throw new IllegalArgumentException("It seems that your DB_HOST for the db host seems to be empty");
            }

            else if (PortINT == 0) {
                throw new IllegalArgumentException("It seems that your DB_PORT in the .env for the DB Port seems to be empty/0");
            }

            else if (Name == null) {
                throw new IllegalArgumentException("It seems that your DB_NAME in the .env for the db name seems to be empty");
            }

            else if (User == null) {
                throw new IllegalArgumentException("It seems that your the DB_USER in your .env seems to be empty");
            }

            else if (PWSD == null) {
                throw new IllegalArgumentException("It seems that the DB_PWSD in your .env seems to be empty");
            }

            else {
                System.out.println("\nSet the values");
                this.DbHost = Host;
                this.DBPortINT = PortINT;
                this.DBName = Name;
                this.DBUser = User;
                this.DbPWSD = PWSD;
                System.out.println("Values setted\n");

                System.out.println(".env parameters loaded and setted sucsessfully");
            }

        } catch (IllegalArgumentException error) {
            System.out.println("Something in your .env file is wrong or corrupted");
            System.out.println("Error: " + error);
        }
    }

}