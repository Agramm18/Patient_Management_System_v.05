package app.controller.auth.account.registration;
import java.util.Scanner;

import java.util.Arrays;
import java.io.Console;
import org.mindrot.jbcrypt.BCrypt;

import app.controller.auth.account.Password.SetPassword;

public class Registration {

    private boolean HasAccount;

    private boolean BasicCredntials;

    private String UserName;
    
    private String UserMail;
    private String UserPhone;
    
    public void RegisterUser(Scanner scanner) {

        Console console = System.console();

        //Registration Loop if the User didn't have an Account
        while (true) {
                String TempUserName;
                String TempEmail;
                String TempPhoneNumber;

                //Password credentials
                boolean HasUpper = false;
                boolean HasLower = false;
                boolean HasSpecial = false;
                boolean HasNumbers = false;
                boolean FitLength = false;

                System.out.println("Please set a user name for your account: ");
                TempUserName = scanner.nextLine();

                if (TempUserName.isBlank()) {
                    throw new IllegalArgumentException("Your Username can't be empty!");

                }

                System.out.println("Please set your email adress for your account: ");
                TempEmail = scanner.nextLine();

                if (TempEmail.isBlank()) {
                    throw new IllegalArgumentException("Your email adress can't be empty!");
                }

                System.out.println("Please set a phone number for your account: ");
                TempPhoneNumber = scanner.nextLine();

                if (TempPhoneNumber.isBlank()) {
                    throw new IllegalArgumentException("Your Phone Number can't be empty!");
                }
                
                    System.out.println("The base values for the Database are setted");
                    this.BasicCredntials = true;

                    this.UserName = TempUserName;
                    this.UserMail = TempEmail;
                    this.UserPhone = TempPhoneNumber;

                    if (this.BasicCredntials) {
                        SetPassword collect = new SetPassword();
                    
                while (true) {
                    try {
                        collect.PlainPWSD(scanner);
                        break;

                    } catch (IllegalArgumentException invalidInput) {
                        System.out.println("\nThere is an error of the Login/Registration logic");
                        System.out.println("The error is: " + invalidInput.getMessage() + "\n");
                    }
                }

                while (true) {
                    try {
                        collect.ValidatePWSD();
                        break;

                    } catch (IllegalArgumentException invalidInput) {
                        System.out.println("\nThere is an error of the Login/Registration logic");
                        System.out.println("The error is: " + invalidInput.getMessage() + "\n");
                    }
                }

                while (true) {
                    try {
                        collect.VerifyPWSD();
                        break;

                    } catch (IllegalArgumentException invalidInput) {
                        System.out.println("\nThere is an error of the Login/Registration logic");
                        System.out.println("The error is: " + invalidInput.getMessage() + "\n");
                    }
                }

                    System.out.println("Your registration where sucsessfull");
                    break;
                }
        }
    }
}
