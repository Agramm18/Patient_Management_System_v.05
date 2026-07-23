package app.Controller;

import app.Auth.Flow.CurrentSession;
import app.Menu.MenuContextStructure;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import java.util.Scanner;

import app.Repository.ServiceRepository.AdminServices.ShowCurrentRequests;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.SessionAccount;
public class ServiceController {

    public ServiceController() {

    }

    public void routeService(MenuContextStructure values, Scanner scanner) {

        SessionAccount user = CurrentSession.getCurrentAccount();

        switch (values.action()) {
            case ADMIN_USER_REQUESTS -> {
                System.out.println("[INFO] Welcome admin: " + CurrentSession.getCurrentAccount().accountName());
                LogManager.menu(MenuState.INFO, "The admin has started to display the current requests");
                new ShowCurrentRequests().CurrentRequests();
            }

            default -> throw new IllegalStateException("[ERROR] The User Action is unknown");
        }
    }
}
