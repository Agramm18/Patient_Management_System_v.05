package app.Controller;

import app.CLIText.Menus.ServiceMenus.ParrentMenus.AdminMenu;
import app.CLIText.Menus.ServiceMenus.ParrentMenus.LocalAdminMenu;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;
import app.Auth.Flow.CurrentSession;
import app.Menu.Enums.ServiceAction;
import app.Menu.MenuFlow;

import app.Menu.MenuContextStructure;
import app.Menu.MenuOption;

import java.util.Scanner;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.SessionAccount;
import java.util.List;

public class MenuControllerParent {

    public MenuContextStructure routeRole(Scanner scanner) {
        LogManager.system(SystemState.INFO, "Starting Menu Controller");

        SessionAccount user = CurrentSession.getCurrentAccount();
        int userRole = user.role();
        MenuFlow route = new MenuFlow();

        switch (userRole) {

            case 1:
                LogManager.auth(AuthState.INFO, "The user has granted access to the local admin menu");
                new LocalAdminMenu().localAdminMenu();
                return new MenuContextStructure(userRole, ServiceAction.LOCAL_ADMIN_DASHBOARD);

            case 2:
                LogManager.auth(AuthState.INFO, "The user has granted access to the admin menu");

                //Display Admin Menu Options
                AdminMenu adminMenu = new AdminMenu();
                adminMenu.showMenu();

                List<MenuOption> menuItems = adminMenu.getMenuItems();

                int selectedNumber = route.chooseOption(scanner, menuItems.size());

                int selectedIndex = selectedNumber - 1;

                MenuOption selectedMenuItem = menuItems.get(selectedIndex);

                LogManager.menu(MenuState.INFO, "Selected action: " + selectedMenuItem.action());

                return new MenuContextStructure(userRole, selectedMenuItem.action());

            default:
                throw new IllegalArgumentException("Unknown user role: " + userRole);
        }
    }
}
