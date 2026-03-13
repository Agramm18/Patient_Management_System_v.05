package app.db.auth.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

import app.controller.auth.account.Validate.AccountStatus;
import app.controller.auth.account.LoadAccount;

public class CheckDBConnection {
    String Host;
    int Port;
    String Name;
    String User;
    String PWSD;
    boolean ENVConnection;
    String Admin_Auth_KEY;

    private Connection connection;

    public CheckDBConnection(String Host, int Port, String Name, String User, String PWSD, boolean ENVConnection, String Admin_Auth_KEY) {
        this.Host = Host;
        this.Port = Port;
        this.Name = Name;
        this.User = User;
        this.PWSD = PWSD;
        this.ENVConnection = ENVConnection;
        this.Admin_Auth_KEY = Admin_Auth_KEY;
    }
    
    public void SQLConnection(Scanner scanner) {
        try {
            boolean SQLValid = false;

            System.out.println("SQL Starting.....");
            //Set connection link with parameters
            String url = "jdbc:mysql://" + this.Host + ":" + this.Port + "/" + this.Name;

            //load connection
            this.connection = DriverManager.getConnection(url, this.User, this.PWSD);
            SQLValid = true;

            //validate if everything worked with base values
            if (!SQLValid || !ENVConnection) {
                throw new IllegalStateException("It seems that something unexpected happend");
            } else 
                {
                //if everything worked the account class will be called
                System.out.println("SQL Connected sucsessfully");
                System.out.println("\nEverything worked you will now be redirected to the Account validation\n");
                
                LoadAccount run = new LoadAccount(
                    this.connection,
                    this.Admin_Auth_KEY
                );
                run.SetAccountParam(scanner);
            }

        } catch (Exception error) {
            System.out.println("\nIt seems the SQL Connection this didn't work...");
            System.out.println("Error: " + error + "\n");
        }
    }
}
