package app.Menu;
import java.util.Scanner;

import app.Auth.Flow.CurrentSession;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class MenuFlow {

    public int chooseOption(Scanner scanner, int MAX_OPTIONS) {

        LogManager.log(LogType.MENU_INFO, "Starting Menu Choice Collector");

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

                    LogManager.log(LogType.MENU_SUCCESS, "The User choose Option: " + userChoiceInt);
                    return userChoiceInt;
                }

            } catch (NumberFormatException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
            }
        }
    }
}
