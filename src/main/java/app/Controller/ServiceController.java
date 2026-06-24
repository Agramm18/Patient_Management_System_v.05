package app.Controller;

import app.Menu.MenuValues;
import app.Config.LogManager;
import app.Config.LogManager.LogType;
import app.Repository.ServiceRepository.AdminServices.ShowCurrentRequests;

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
        LogManager.log(LogType.SYSTEM_INFO, "Starting LocalAdmin Service");
    }

    private void startAdminService(MenuValues values, Scanner scanner) {
        LogManager.log(LogType.SYSTEM_INFO, "Starting Admin Service");
        int choice = values.userChoice();

        switch (choice) {
            case 1:
                LogManager.log(LogType.MENU_INFO, "The Admin have chosen to start the Request Service");
                

                break;
        }

    }
}
