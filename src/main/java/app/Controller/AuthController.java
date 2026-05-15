package app.Controller;

import app.Auth.LoginService;
import app.Auth.RegistrationService;

import java.util.Scanner;

public class AuthController {

    public void VerifyAccountStatus(Scanner scanner) {
        String AccountStatusSTR;

        System.out.println("\n[INFO] Verify the user");
        System.out.println("[INFO] Running through the login & registration process\n");

        while (true) {
            try {
                System.out.println("\n[INFO] Please select one of the following Options");
                System.out.println("[1] Login");
                System.out.println("[2] Registration");
                System.out.println("[3] Exit the Program\n");

                AccountStatusSTR = scanner.nextLine().trim().toLowerCase();

                if (AccountStatusSTR.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] Please type in something to continue");
                } else {
                    int UserValue = Integer.parseInt(AccountStatusSTR);

                    if (UserValue < 1 || UserValue > 3) {
                        throw new IllegalArgumentException("[ERROR] The value can't be less than 1 or higher than 3");
                    } else if (UserValue == 1) {
                        System.out.println("\n[INFO] You can now login to your Account");
                        LoginService login = new LoginService();
                        return;
                    } else if (UserValue == 2) {
                        System.out.println("\n[INFO] Welcome You can now Create a new Account");
                        RegistrationService register = new RegistrationService();
                        register.UserAccount(scanner);
                        return;
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
