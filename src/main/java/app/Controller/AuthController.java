package app.Controller;

import app.Auth.LoginService;
import app.Auth.PasswordService;
import app.Auth.RegistrationService;

public class AuthController {

    public void VerifyUser() {
        System.out.println("\n[INFO] Verify the user");
        System.out.println("[INFO] Running through the login & registration process\n");

        LoginService login = new LoginService();
        RegistrationService register = new RegistrationService();
        PasswordService pwsd = new PasswordService();
    }
}
