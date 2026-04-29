package app.Controller;

import app.Auth.LoginService;
import app.Auth.PasswordService;
import app.Auth.RegistrationService;

import java.util.Scanner;

public class AuthController {

    public void VerifyUser(Scanner scanner) {
        System.out.println("\n[INFO] Verify the user");
        System.out.println("[INFO] Running through the login & registration process\n");

        LoginService login = new LoginService();
        RegistrationService create = new RegistrationService();
        create.UserAccount(scanner);
        PasswordService pwsd = new PasswordService();
    }
}
