package app.Controller;

import app.Auth.Flow.LoginFlow;
import app.Auth.Flow.RecoveryFlow;
import app.Auth.Flow.RegistrationFlow;
import app.CLIText.Menus.Program.AuthMenu;

import java.util.Scanner;

import app.Config.LogManager;
import app.Config.LogManager.LogType;


/*
    This section is a controller where the User has to choose what he wants to do

    1. Register a New account (Will redirect to  RegistrationFlow.java)
    2. Login an existent Account (Will redirect to LoginFlow.java)
    3. To exit the Program
*/

public class AuthController {

    public void verifyAccountStatus(Scanner scanner) {
        String accountStatusSTR;
        LogManager.log(LogType.AUTH_INFO, "Running through the Registration or Login process");

        while (true) {
            try {

                AuthMenu show = new AuthMenu();
                show.authMenu();

                accountStatusSTR = scanner.nextLine().trim().toLowerCase();

                if (accountStatusSTR.isBlank()) {
                    throw new IllegalArgumentException("Please type in something to continue");
                } else {
                    int userValue = Integer.parseInt(accountStatusSTR);

                    if (userValue < 1 || userValue > 4) {
                        throw new IllegalArgumentException("The value can't be less than 1 or higher than 4");
                    } else if (userValue == 1) {
                        RegistrationFlow register = new RegistrationFlow();
                        register.user(scanner);

                    } else if (userValue == 2) {
                        LoginFlow login = new LoginFlow();
                        login.user(scanner);

                    } else if (userValue == 3) {
                        LogManager.log(LogType.SECURITY_INFO, "Starting Recovery");
                        RecoveryFlow recover = new RecoveryFlow();
                        recover.SystemAccounts(scanner);
                    } else {
                        LogManager.log(LogType.MESSAGE, "You have chosen to end this program");
                        LogManager.log(LogType.MESSAGE, "Have a nice day and Good Bye!!");
                        System.exit(0);
                        break;
                    }
                }

            } catch (NumberFormatException numberError) {
                LogManager.log(LogType.INVALID_INPUT, "Please type in a valid number");
            } catch (IllegalArgumentException error) {
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
            }
        }
    }
}
