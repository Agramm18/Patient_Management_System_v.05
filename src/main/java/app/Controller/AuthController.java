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
                System.out.println("[INFO] Do you have already an account? Y/N?: ");
                AccountStatusSTR = scanner.nextLine().trim().toLowerCase();

                if (AccountStatusSTR.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] The Field can't be empty please try again");
                } else if (!AccountStatusSTR.equals("y") && !AccountStatusSTR.equals("n")) {
                    throw new IllegalArgumentException("[ERROR] Only Y or N are permitted values please try again");
                } else if (AccountStatusSTR.equals("y")) {
                    LoginService login = new LoginService();
                    System.out.println("[INFO] Not Implemented yet");
                    break;
                } else {
                    RegistrationService create = new RegistrationService();
                    create.UserAccount(scanner);
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }
}
