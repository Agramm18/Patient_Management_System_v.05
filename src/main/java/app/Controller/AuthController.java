package app.Controller;

import app.Auth.Flow.LoginFlow;
import app.Auth.Flow.RegistrationFlow;
import app.CLIText.Menus.Program.AuthMenu;

import java.util.Scanner;

public class AuthController {

    public void VerifyAccountStatus(Scanner scanner) {
        String AccountStatusSTR;

        System.out.println("\n[INFO] Verify the user");
        System.out.println("[INFO] Running through the login & registration process\n");

        while (true) {
            try {

                AuthMenu show = new AuthMenu();
                show.authMenu();

                AccountStatusSTR = scanner.nextLine().trim().toLowerCase();

                if (AccountStatusSTR.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] Please type in something to continue");
                } else {
                    int UserValue = Integer.parseInt(AccountStatusSTR);

                    if (UserValue < 1 || UserValue > 3) {
                        throw new IllegalArgumentException("[ERROR] The value can't be less than 1 or higher than 4");
                    } else if (UserValue == 1) {
                        RegistrationFlow register = new RegistrationFlow();
                        register.user(scanner);

                    } else if (UserValue == 2) {
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
