package app.Auth.Flow.Services.LoginService;

import java.util.Scanner;
import java.io.Console;

public class LoginInputCollector {
    private String EnteredUserName;
    private String EnteredPWSD;


    public void User(Scanner scanner) {
            enterUsername(scanner);
            enterPWSD();
    }

    private void enterUsername(Scanner scanner) {
        System.out.println("\n[INFO] Please enter your username");

        while (true) {
            try {
                String UserName = scanner.nextLine();

                if (UserName.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be emtpy please try again");
                } else {
                    this.EnteredUserName = UserName;
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
                    char[] PWSDChar = console.readPassword("\n[INFO] Please enter your Password: ");

                    if (PWSDChar == null || PWSDChar.length == 0) {
                        throw new IllegalArgumentException("[ERROR] The password can't be 0 or empty");
                    } else {
                        System.out.println("[INFO] Char will be converted");
                        String convertedPWSD = new String(PWSDChar);

                        if (convertedPWSD.trim().isEmpty()) {
                            throw new IllegalArgumentException("[ERROR] The Password can't contain only empty spaces");
                        } else {
                            System.out.println("[OK] Entered password is valid\n");
                            this.EnteredPWSD = convertedPWSD;
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
        return this.EnteredUserName;
    }

    public String getEnteredPWSD() {
        return this.EnteredPWSD;
    }

}
