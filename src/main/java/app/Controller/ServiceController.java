package app.Controller;

import app.Menu.MenuValues;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import java.util.Scanner;

public class ServiceController {

    public void routeService(MenuValues values, Scanner scanner) {
        int role = values.userRole();

        switch (role) {
            case 1:
                startLocalAdminService(values, scanner);
                break;

            case 2:
                startAdminService(values, scanner);
                break;
        }
    }


    private void startLocalAdminService(MenuValues values, Scanner scanner) {
        LogManager.system(SystemState.INFO, "Starting LocalAdmin Service");
    }

    private void startAdminService(MenuValues values, Scanner scanner) {
        LogManager.system(SystemState.INFO, "Starting Admin Service");

    }
}
