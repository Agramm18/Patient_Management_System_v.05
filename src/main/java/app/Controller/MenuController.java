package app.Controller;

import app.Auth.Flow.Services.LoginService.CurrentUser;
import app.CLIText.Menus.ServiceMenus.ParrentMenus.AdminMenu;
import app.CLIText.Menus.ServiceMenus.ParrentMenus.LocalAdminMenu;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;
import app.Auth.Flow.CurrentSession;
import app.Menu.MenuFlow;

import app.Menu.MenuContextStructure;

import java.util.Scanner;

public class MenuController {
    private MenuContextStructure menuContextStructure;

    private int MAX_OPTIONS;

    public MenuContextStructure routeMenu(Scanner scanner) {
        LogManager.system(SystemState.INFO, "Starting Menu Controller");

        CurrentUser user = CurrentSession.getCurrentUser();
        int UserRole = user.getRole();
        MenuFlow route = new MenuFlow();

        switch(UserRole) {
            case 1:
                LogManager.auth(AuthState.INFO, "The user has granted access to the local admin menu");
                new LocalAdminMenu().localAdminMenu();
                return new MenuContextStructure(0, user.getRole(), 0);

            case 2:
                LogManager.auth(AuthState.INFO, "The user has granted access to the admin menu");

                AdminMenu adminMenu = new AdminMenu();
                adminMenu.showMenu();

                this.MAX_OPTIONS = adminMenu.getMenuSize();

                //The sub menu should be called here like choice = 1 -> request sub menu and this valude should be routed to the service controller

                int parrentKontext = route.chooseOption(scanner, this.MAX_OPTIONS);

                if (parrentKontext > 0) {
                    LogManager.menu(MenuState.INFO, "The admin called the child admin menus");

                } else {
                    throw new IllegalArgumentException("The value is invalid " + parrentKontext);
                }

                MenuContextStructure menuChoice = new MenuContextStructure(parrentKontext, user.getRole(), 0);

                return menuChoice;
        }

        throw new IllegalStateException("Unknown user role: " + UserRole);
    }
}
