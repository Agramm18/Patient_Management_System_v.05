package app.Menu;
import java.util.Scanner;

import app.Auth.Flow.CurrentSession;

public class MenuFlow {

    public boolean chooseOption(Scanner scanner, int ADMIN_MAX_OPTIONS) {

        try {
            String userChoice = scanner.nextLine();

            if (userChoice.isBlank()) {
                throw new IllegalArgumentException("The Entered value can't be empty");
            } else {
                int userChoiceInt = Integer.parseInt(userChoice);

                if (userChoiceInt < 0 || userChoiceInt > ADMIN_MAX_OPTIONS) {
                    throw new IllegalArgumentException("The number can't be less than 0 or higher than " + ADMIN_MAX_OPTIONS);
                }
            }
        } catch (IllegalArgumentException error) {
            System.out.println(error.getMessage());
            return false;
        }

        return false;

    }

    public void admin() {

    }

}
