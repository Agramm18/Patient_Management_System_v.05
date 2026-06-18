package app.Controller;

import app.Auth.Flow.Services.LoginService.CurrentUser;
import app.CLIText.Menus.ServiceMenus.AdminMenu;
import app.CLIText.Menus.ServiceMenus.LocalAdminMenu;
import app.Config.LogManager;
import app.Config.LogManager.LogType;
import app.Auth.Flow.CurrentSession;
import app.Menu.MenuFlow;

import app.Config.LogManager;
import app.Config.LogManager.LogType;
import app.Menu.MenuValues;

import java.util.Scanner;

public class MenuController {
    private MenuValues menuValues;

    private int MAX_OPTIONS;

    public MenuValues routeMenu(Scanner scanner) {
        LogManager.log(LogType.SYSTEM_INFO, "Starting Menu Controller");

        CurrentUser user = CurrentSession.getCurrentUser();
        int UserRole = user.getRole();
        MenuFlow route = new MenuFlow();

        switch(UserRole) {
            case 1:
                LogManager.log(LogType.AUTH_INFO, "The user has granted access to the local admin menu");
                new LocalAdminMenu().localAdminMenu();
                return new MenuValues(0, user.getRole());

            case 2:
                LogManager.log(LogType.AUTH_INFO, "The user has granted access to the admin menu");

                AdminMenu adminMenu = new AdminMenu();
                adminMenu.showMenu();

                this.MAX_OPTIONS = adminMenu.getMenuSize();

                int Choice = route.chooseOption(scanner, this.MAX_OPTIONS);
                MenuValues menuChoice = new MenuValues(Choice, user.getRole());

                return menuChoice;
        }

        throw new IllegalStateException("Unknown user role: " + UserRole);
    }
}
