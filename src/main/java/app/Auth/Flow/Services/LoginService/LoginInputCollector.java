package app.Auth.Flow.Services.LoginService;

import java.util.Scanner;
import java.io.Console;
import java.util.Arrays;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

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
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
                System.out.println(error.getMessage());
            }
        }

    }

    private void enterPWSD() {
        Console console = System.console();

        if (console == null) {
            throw new IllegalStateException("[WARNING] Please run the program only in the Terminal");
        }

        while (true) {
            try {
                char[] pwsdCHAR = console.readPassword("\n[INFO] Please enter your Password: ");

                if (pwsdCHAR == null || pwsdCHAR.length == 0) {
                    throw new IllegalArgumentException("[ERROR] The password can't be 0 or empty");
                } else {
                    String convertedPWSD = new String(pwsdCHAR);
                    Arrays.fill(pwsdCHAR, '\0');

                    if (convertedPWSD.trim().isEmpty()) {
                        throw new IllegalArgumentException("[ERROR] The Password can't contain only empty spaces");
                    } else {
                        System.out.println("[OK] Entered password is valid\n");
                        this.enteredPWSD = convertedPWSD;
                        LogManager.log(LogType.AUTH_INFO, "The Login Password is valid");
                        break;
                    }
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
            } catch(IllegalStateException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogType.SYSTEM_WARN, error.getMessage());
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
