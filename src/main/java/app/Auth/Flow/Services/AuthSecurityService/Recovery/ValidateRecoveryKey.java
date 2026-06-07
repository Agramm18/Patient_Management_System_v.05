package app.Auth.Flow.Services.AuthSecurityService.Recovery;


import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class ValidateRecoveryKey {
    private char[] invisibleInput;
    private String passwordStringValue;
    private String enteredHashByUser;

    public void keyValues(Scanner scanner) {
        Console console = System.console();

        if (console == null) {
            throw new IllegalStateException("Please run the program only in the Terminal");
        }

        while (true) {
            try {

                this.invisibleInput = console.readPassword("Please type in your recovery key: ");

                if (this.invisibleInput == null || this.invisibleInput.length == 0) {
                    throw new IllegalArgumentException("The password can't be empty please try again");
                } else {
                    LogManager.log(LogType.MESSAGE, "Convert your Password as String");
                    String convertedPassword = String.valueOf(this.invisibleInput);
                    LogManager.log(LogType.MESSAGE, "Giving the Password to your Database");
                    this.passwordStringValue = convertedPassword;
                }

                this.enteredHashByUser = this.passwordStringValue;
                Arrays.fill(invisibleInput, '\0');
                break;

            } catch (IllegalArgumentException error) {
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
            } catch (IllegalStateException error) {
                LogManager.log(LogType.SYSTEM_WARN, error.getMessage());
            }
        }
    }

    public String getEnteredHashByUser() {
        return this.enteredHashByUser;
    }

}
