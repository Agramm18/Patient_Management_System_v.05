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
        Console console = System.console();

        if (console == null) {
            throw new IllegalStateException("[WARNING] Please run the program only in the Terminal");
        }


        while (true) {
            try {

                this.invisibleInput = console.readPassword("Please type in your recovery key: ");

                if (this.invisibleInput == null || this.invisibleInput.length == 0) {
                    throw new IllegalArgumentException("[ERROR] The password can't be empty please try again");
                } else {
                    System.out.println("[OK] Convert your Password as String");
                    String convertedPassword = String.valueOf(this.invisibleInput);
                    System.out.println("[OK] Giving the Password to your Database");
                    this.passwordStringValue = convertedPassword;
                }

                this.enteredHashByUser = this.passwordStringValue;
                Arrays.fill(invisibleInput, '\0');
                break;

            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }

    }

    public String getEnteredHashByUser() {
        return this.enteredHashByUser;
    }

}
