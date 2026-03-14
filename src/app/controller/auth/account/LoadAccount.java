package app.controller.auth.account;
import app.controller.auth.account.Validate.AccountStatus;

import java.sql.Connection;
import java.util.Scanner;

public class LoadAccount {

    private Connection connection;

    public LoadAccount(Connection connection) {
        this.connection = connection;
    }

    public void SetAccountParam(Scanner scanner) {
    
        AccountStatus account = new AccountStatus(connection);
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
