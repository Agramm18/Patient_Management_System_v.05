package app.Controller;

import app.Auth.Flow.LoginFlow;
import app.Auth.Flow.RegistrationFlow;
import app.CLIText.Menus.Program.AuthMenu;

import java.util.Scanner;


/*
    This section is a controller where the User has to choose what he wants to do

    1. Register a New account (Will redirect to  RegistrationFlow.java)
    2. Login an existent Account (Will redirect to LoginFlow.java)
    3. To exit the Program
*/

public class AuthController {

    public void verifyAccountStatus(Scanner scanner) {
        String accountStatusSTR;

        System.out.println("\n[INFO] Verify the user");
        System.out.println("[INFO] Running through the login & registration process\n");

        while (true) {
            try {

                AuthMenu show = new AuthMenu();
                show.authMenu();

                accountStatusSTR = scanner.nextLine().trim().toLowerCase();

                if (accountStatusSTR.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] Please type in something to continue");
                } else {
                    int userValue = Integer.parseInt(accountStatusSTR);

                    if (userValue < 1 || userValue > 3) {
                        throw new IllegalArgumentException("[ERROR] The value can't be less than 1 or higher than 4");
                    } else if (userValue == 1) {
                        RegistrationFlow register = new RegistrationFlow();
                        register.user(scanner);

                    } else if (userValue == 2) {
                        LoginFlow login = new LoginFlow();
                        login.user(scanner);

                    } else {
                        System.out.println("\n[INFO] You have chosen to end this program");
                        System.out.println("[INFO] Have a nice day and Good Bye!!\n");
                        System.exit(0);
                        break;
                    }
                }

            } catch (NumberFormatException numberError) {
                System.out.println("[ERROR] Please type in a valid number");
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }
}
