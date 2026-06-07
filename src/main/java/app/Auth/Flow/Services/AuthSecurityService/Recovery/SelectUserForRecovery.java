package app.Auth.Flow.Services.AuthSecurityService.Recovery;
import java.util.Scanner;


import app.Config.LogManager;
import app.Config.LogManager.LogType;


public class SelectUserForRecovery {
    private   String recoverUsername;

    public String username(Scanner scanner) {

        while (true) {
            try {
                System.out.println("\n[INFO] Please enter the user you want to recover\n");

                String username = scanner.nextLine();

                if (username.isBlank()) {
                    throw new IllegalArgumentException("The username can't be empty");
                }

                this.recoverUsername = username;
                return this.recoverUsername;

            } catch(IllegalArgumentException error) {
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
            }
        }
    }

    public String getRecoverUsername() {
        return recoverUsername;
    }
}
