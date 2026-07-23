package app.CLIText.Menus.ServiceMenus.ParrentMenus;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.SessionAccount;
import app.Auth.Flow.CurrentSession;

public class LocalAdminMenu {

    public void localAdminMenu() {
        LogManager.auth(AuthState.SUCCESS, "Routed successfully into the local admin menu");
        SessionAccount user = CurrentSession.getCurrentAccount();

        System.out.println("\nWelcome Local Admin: " + user.accountName());
        System.out.println("Please choose one of the following options\n");
    }

}
