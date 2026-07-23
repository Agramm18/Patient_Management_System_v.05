package app.Controller;

import app.Auth.Flow.CurrentSession;
import app.Auth.Flow.LoginFlow;
import app.Auth.Flow.RecoveryFlow;
import app.Auth.Flow.RegistrationFlow;
import app.CLIText.Menus.Program.AuthMenu;

import java.util.Scanner;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import app.Auth.Flow.Services.LoginService.LoginBehaviour.SessionAccount;

/*
    This section is a controller where the User has to choose what he wants to do

    1. Register a New account (Will redirect to  RegistrationFlow.java)
    2. Login an existent Account (Will redirect to LoginFlow.java)
    3. To exit the Program
*/

public class AuthController {

    public void verifyAccountStatus(Scanner scanner) {
        String accountStatusSTR;
        System.out.println("[INFO] Running through the Authentication process");
        LogManager.auth(AuthState.INFO, "Running through the Authentication process");

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
                        System.out.println("[INFO] Starting Registration process");
                        LogManager.auth(AuthState.INFO, "Starting Registration process");
                        RegistrationFlow register = new RegistrationFlow();
                        register.user(scanner);

                    } else if (userValue == 2) {
                        System.out.println("[INFO] Starting Login process");
                        LogManager.auth(AuthState.INFO, "Starting Login process");
                        LoginFlow login = new LoginFlow();
                        login.user(scanner);

                        SessionAccount user = CurrentSession.getCurrentAccount();

                        if (user != null && user.hasAccessToMenu() && user.accountStatus() == 1) {
                            LogManager.auth(AuthState.SUCCESS, "The Login was a success");
                            return;
                        }

                    } else if (userValue == 3) {
                        System.out.println("[INFO] Starting Recovery process");
                        LogManager.security(SecurityState.INFO, "Starting Recovery process");
                        RecoveryFlow recover = new RecoveryFlow();
                        recover.SystemAccounts(scanner);

                    } else {
                        System.out.println("[OK] The Program will end. Good bye!!");
                        LogManager.system(SystemState.SUCCESS, "The user have chosen to end this program");
                        System.exit(0);
                        break;
                    }
                }

            } catch (NumberFormatException numberError) {
                System.out.println("[ERROR] It seems that you didn't type in a number: " + numberError.getMessage());
                LogManager.other(OtherState.INVALID_INPUT, "The User typed not in a Number");

            } catch (IllegalArgumentException error) {
                System.out.println("[ERROR] Something went wrong: " + error.getMessage());
                LogManager.other(OtherState.INVALID_INPUT, error.getMessage());

            }
        }
    }
}
