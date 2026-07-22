package app.Controller;

import app.Menu.MenuContextStructure;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import java.util.Scanner;

public class ServiceController {

    public void routeService(MenuContextStructure values, Scanner scanner) {
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


    private void startLocalAdminService(MenuContextStructure values, Scanner scanner) {
        LogManager.system(SystemState.INFO, "Starting LocalAdmin Service");
    }

    private void startAdminService(MenuContextStructure values, Scanner scanner) {
        LogManager.system(SystemState.INFO, "Starting Admin Service");

    }
}
