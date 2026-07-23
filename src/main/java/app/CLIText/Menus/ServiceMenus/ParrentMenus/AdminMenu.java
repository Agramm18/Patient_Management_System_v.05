package app.CLIText.Menus.ServiceMenus.ParrentMenus;

import app.Auth.Flow.CurrentSession;
import app.Auth.Flow.Services.LoginService.CurrentUser;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;
import app.Menu.Enums.ServiceAction;
import app.Menu.MenuOption;

import java.util.List;

public class AdminMenu {
    private int menuSize;

    private static final List<MenuOption> MENU_ITEMS = List.of(
            new MenuOption("Requests", ServiceAction.ADMIN_USER_REQUESTS),
            new MenuOption("User", ServiceAction.ADMIN_DISPLAY_ACCOUNTS),
            new MenuOption("Security", ServiceAction.ADMIN_SECURITY_OPTIONS),
            new MenuOption("Logs", ServiceAction.ADMIN_VIEW_LOGS),
            new MenuOption("Logout", ServiceAction.ADMIN_LOGOUT)
    );

    public void showMenu() {
        LogManager.auth(AuthState.SUCCESS, "Routed successfully into the admin menu");
        CurrentUser user = CurrentSession.getCurrentUser();

        System.out.println("\nWelcome Admin: " + user.getUserName());
        System.out.println("Please choose one of the following options\n");

        for (int i = 0; i < MENU_ITEMS.size(); i++) {
            MenuOption menuOption = MENU_ITEMS.get(i);

            System.out.println("[" + (i + 1) + "] " + menuOption.label());
        }
    }

    public List<MenuOption> getMenuItems() {
        return MENU_ITEMS;
    }
}
