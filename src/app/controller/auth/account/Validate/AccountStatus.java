package app.controller.auth.account.Validate;

//Load other files
import app.controller.auth.account.login.Login;
import app.controller.auth.account.registration.Registration;
import app.menu.MenuUI.LocalAdminUIScreen;
import app.menu.MenuValidation.LocalAdminValidation;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Scanner;
import java.io.Console;

public class AccountStatus {

    private Connection connection;
    private Console console;

    public AccountStatus(Connection connection) {
        this.connection = connection;
    }

    public static void HeaderMSGAccount() {
        System.out.println("\n==================================================");
        System.out.println("                 Account System");
        System.out.println("--------------------------------------------------");
        System.out.println("   Login or create a new account to continue");
        System.out.println("==================================================\n");
    }

    public void SetBaseValues(Scanner scanner) {
        while (true) {
            Console console = System.console();

            if (console == null) {
                throw new IllegalStateException("Console not available run the system via the terminal....");
            }

            char[] UserInput;

            String StartValue;
            System.out.println("\nDo you have an Account or are you allready registerd/logged in (Y/N)?: ");
            StartValue = scanner.nextLine().trim().toLowerCase();

            //Check if the user have an account
            if (StartValue.isBlank()) {
                throw new IllegalArgumentException("This field can't be empty!");
            } else if (!StartValue.equals("y") && !StartValue.equals("n")) {
                throw new IllegalArgumentException("Only y or n are allowed as values please try again!");
            } else if (StartValue.equals("y")) {
                //Redirect User to Login
                System.out.println("Please follow the login");

                Login login = new Login(connection);

                //Throw collected errors and the user must start again
                while (true) {
                    try {
                        login.LoginUser(scanner);
                        break;

                    } catch (IllegalArgumentException invalidInput) {
                        System.out.println("\nThere is an error of the Login/Registration logic");
                        System.out.println("The error is: " + invalidInput.getMessage());
                        System.out.println("Please try again....\n");
                    }
                }

                break;
            } else if (StartValue.equals("n")) {
                //Redirect User to Registration
                System.out.println("Please follow the registration");

                Registration registration = new Registration(connection);

                //Throw collected errors and the user must start again
                while (true) {
                    try {
                        registration.RegisterUser(scanner);
                        break;

                    } catch (IllegalArgumentException invalidInput) {
                        System.out.println("\nThere is an error of the Login/Registration logic");
                        System.out.println("The error is: " + invalidInput.getMessage());
                        System.out.println("Please try again....\n");
                    }
                }

                break;

            }
            
            else {
                throw new IllegalStateException("It seems that the base value is corrupted");
            }
        }
    }
}