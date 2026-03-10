package app.controller.auth.account;
import app.controller.auth.account.Validate.AccountStatus;

import java.util.Scanner;

public class LoadAccount {
    public void SetAccountParam(Scanner scanner) {
    
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
}
