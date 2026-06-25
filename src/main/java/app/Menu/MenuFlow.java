package app.Menu;
import java.util.Scanner;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

public class MenuFlow {

    public int chooseOption(Scanner scanner, int MAX_OPTIONS) {

        LogManager.menu(MenuState.INFO, "Starting Menu Choice Collector");

        while (true) {
            try {
                String userChoice = scanner.nextLine();

                if (userChoice.isBlank()) {
                    throw new IllegalArgumentException("The Entered value can't be empty");
                } else {
                    int userChoiceInt = Integer.parseInt(userChoice);

                    if (userChoiceInt <= 0 || userChoiceInt > MAX_OPTIONS) {
                        throw new IllegalArgumentException("The number can't be less than 0 or higher than " + MAX_OPTIONS);
                    }

                    LogManager.menu(MenuState.SUCCESS, "The User choose Option: " + userChoiceInt);
                    return userChoiceInt;
                }

            } catch (NumberFormatException error) {
                System.out.println(error.getMessage());
                LogManager.other(OtherState.INVALID_INPUT, error.getMessage());
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.other(OtherState.INVALID_INPUT, error.getMessage());
            }
        }
    }
}
