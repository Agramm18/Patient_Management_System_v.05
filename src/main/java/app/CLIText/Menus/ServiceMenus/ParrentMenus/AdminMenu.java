package app.CLIText.Menus.ServiceMenus.ParrentMenus;

import app.Auth.Flow.CurrentSession;
import app.Auth.Flow.Services.LoginService.CurrentUser;
import app.Config.LogManager;

import app.Config.LogManager.LogType;

import java.util.List;

public class AdminMenu {
    private int menuSize;

    public void showMenu() {
        LogManager.log(LogType.AUTH_SUCCESS, "Routed successfully into the admin menu");
        CurrentUser user = CurrentSession.getCurrentUser();

        System.out.println("\nWelcome Admin: " + user.getUserName());
        System.out.println("Please choose one of the following options\n");

        List<String> menuItems = List.of(
            "Requests",
             "User",
            "Security",
            "Logs",
            "Logout"
        );

        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + menuItems.get(i));
        }

        this.menuSize = menuItems.size();
    }

    public int getMenuSize() {
        return this.menuSize;
    }
}
