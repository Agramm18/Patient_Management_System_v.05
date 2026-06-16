package app.CLIText.Menus.ServiceMenus;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

import app.Auth.Flow.CurrentSession;
import app.Auth.Flow.Services.LoginService.CurrentUser;

public class LocalAdminMenu {

    public void localAdminMenu() {
        LogManager.log(LogType.AUTH_SUCCESS, "Routed successfully into the local admin menu");
        CurrentUser user = CurrentSession.getCurrentUser();

        System.out.println("\nWelcome Local Admin: " + user.getUserName());
        System.out.println("Please choose one of the following options\n");
    }

}
