package app.Auth.Flow.Services.AuthSecurityService.Recovery;
import java.util.Scanner;

public class SelectUserForRecovery {
    private   String recoverUsername;

    public String username(Scanner scanner) {

        while (true) {
            try {
                System.out.println("\n[INFO] Please enter the user you want to recover\n");

                String username = scanner.nextLine();

                if (username.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] The username can't be empty");
                }

                this.recoverUsername = username;
                return this.recoverUsername;

            } catch(IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }

    public String getRecoverUsername() {
        return recoverUsername;
    }
}
