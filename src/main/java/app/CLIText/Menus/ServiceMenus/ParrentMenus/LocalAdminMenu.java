package app.CLIText.Menus.ServiceMenus.ParrentMenus;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import app.Auth.Flow.CurrentSession;
import app.Auth.Flow.Services.LoginService.CurrentUser;

public class LocalAdminMenu {

    public void localAdminMenu() {
        LogManager.auth(AuthState.SUCCESS, "Routed successfully into the local admin menu");
        CurrentUser user = CurrentSession.getCurrentUser();

        System.out.println("\nWelcome Local Admin: " + user.getUserName());
        System.out.println("Please choose one of the following options\n");
    }

}
