package app.Auth.Flow.Services.AuthSecurityService;


import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class RecoveryCheck {
    private char[] invisibleInput;
    private String passwordStringValue;
    private String enteredHashByUser;

    public void keyValues(Scanner scanner) {

        while (true) {
            try {
                Console console = System.console();

                if (console != null) {
                    this.invisibleInput = console.readPassword("Please type in your recovery key: ");

                    if (this.invisibleInput == null || this.invisibleInput.length == 0) {
                        throw new IllegalArgumentException("[ERROR] The password can't be empty please try again");
                    } else {
                        System.out.println("[OK] Convert your Password as String");
                        String convertedPassword = String.valueOf(this.invisibleInput);
                        System.out.println("[OK] Giving the Password to your Database");
                        this.passwordStringValue = convertedPassword;
                    }

                    this.enteredHashByUser = BCrypt.hashpw(this.passwordStringValue, BCrypt.gensalt(12));
                    break;

                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }

    }

    public String getEnteredHashByUser() {
        return this.enteredHashByUser;
    }

}
