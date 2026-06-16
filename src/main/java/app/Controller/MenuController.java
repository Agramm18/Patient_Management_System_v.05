package app.Controller;

import app.Auth.Flow.Services.LoginService.CurrentUser;
import app.CLIText.Menus.ServiceMenus.AdminMenu;
import app.CLIText.Menus.ServiceMenus.LocalAdminMenu;
import app.Config.LogManager;
import app.Config.LogManager.LogType;
import app.Auth.Flow.CurrentSession;
import app.Menu.MenuFlow;

import java.util.Scanner;

public class MenuController {

    public void routeMenu(Scanner scanner) {
        LogManager.log(LogType.SYSTEM_INFO, "Starting Menu routing");

        CurrentUser user = CurrentSession.getCurrentUser();
        int UserRole = user.getRole();

        MenuFlow route = new MenuFlow();

        switch(UserRole) {
            case 1:
                new LocalAdminMenu().localAdminMenu();
                break;

            case 2:
                AdminMenu adminMenu = new AdminMenu();
                adminMenu.showMenu();
                int ADMIN_MAX_OPTIONS = adminMenu.getMenuSize();

                boolean choiceIsValid = route.chooseOption(scanner, ADMIN_MAX_OPTIONS);



                route.admin();

                break;
        }


    }
}
