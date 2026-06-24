package app.Controller;

import app.Auth.Flow.Services.LoginService.CurrentUser;
import app.CLIText.Menus.ServiceMenus.ParrentMenus.AdminMenu;
import app.CLIText.Menus.ServiceMenus.ParrentMenus.LocalAdminMenu;
import app.Config.LogManager;
import app.Config.LogManager.LogType;
import app.Auth.Flow.CurrentSession;
import app.Menu.MenuFlow;

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
                return new MenuValues(0, user.getRole(), 0);

            case 2:
                LogManager.log(LogType.AUTH_INFO, "The user has granted access to the admin menu");

                AdminMenu adminMenu = new AdminMenu();
                adminMenu.showMenu();

                this.MAX_OPTIONS = adminMenu.getMenuSize();

                //The sub menu should be called here like choice = 1 -> request sub menu and this valude should be routed to the service controller

                int childKontext = route.chooseOption(scanner, this.MAX_OPTIONS);
                MenuValues menuChoice = new MenuValues(2, user.getRole(), childKontext);

                return menuChoice;
        }

        throw new IllegalStateException("Unknown user role: " + UserRole);
    }
}
