package app.controller.auth.account;
import app.controller.auth.account.Validate.AccountStatus;

import java.sql.Connection;
import java.util.Scanner;

public class LoadAccount {

    private Connection connection;
    private String Admin_Auth_KEY; 

    public LoadAccount(Connection connection, String Admin_Auth_KEY) {
        this.connection = connection;
        this.Admin_Auth_KEY = Admin_Auth_KEY;

    }

    public void SetAccountParam(Scanner scanner) {
    
        AccountStatus account = new AccountStatus(connection, this.Admin_Auth_KEY);
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
}
