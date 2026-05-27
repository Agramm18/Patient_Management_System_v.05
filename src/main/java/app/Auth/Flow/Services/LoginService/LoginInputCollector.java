package app.Auth.Flow.Services.LoginService;

import java.util.Scanner;
import java.io.Console;


/*
    In this Section The User and PWSD will be collected and Stored via the getter
*/

public class LoginInputCollector {
    private String enteredUserName;
    private String enteredPWSD;


    public void user(Scanner scanner) {
            enterUsername(scanner);
            enterPWSD();
    }

    private void enterUsername(Scanner scanner) {
        System.out.println("\n[INFO] Please enter your username");

        while (true) {
            try {
                String userName = scanner.nextLine();

                if (userName.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be emtpy please try again");
                } else {
                    this.enteredUserName = userName;
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }

    }

    private void enterPWSD() {

        while (true) {
            try {
                Console console = System.console();

                if (console != null) {
                    char[] pwsdCHAR = console.readPassword("\n[INFO] Please enter your Password: ");

                    if (pwsdCHAR == null || pwsdCHAR.length == 0) {
                        throw new IllegalArgumentException("[ERROR] The password can't be 0 or empty");
                    } else {
                        System.out.println("[INFO] Char will be converted");
                        String convertedPWSD = new String(pwsdCHAR);

                        if (convertedPWSD.trim().isEmpty()) {
                            throw new IllegalArgumentException("[ERROR] The Password can't contain only empty spaces");
                        } else {
                            System.out.println("[OK] Entered password is valid\n");
                            this.enteredPWSD = convertedPWSD;
                            break;
                        }
                    }
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }

    public String getEnteredUserName() {
        return this.enteredUserName;
    }

    public String getEnteredPWSD() {
        return this.enteredPWSD;
    }

}
