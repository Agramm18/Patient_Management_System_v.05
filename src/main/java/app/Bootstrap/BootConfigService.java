package app.Bootstrap;

import app.CLIText.DisplayMessages.AuthMSG;
import app.CLIText.DisplayMessages.LoaderMSG;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;
import app.Controller.*;
import app.Auth.Flow.Services.LoginService.CurrentAccountInSessionValues;
import app.Auth.Flow.CurrentSession;

import java.util.Scanner;

import app.Controller.MenuControllerParent;

public class BootConfigService {

    public void displayLoader() {
        LoaderMSG show = new LoaderMSG();
        show.message();
    }

    public void systemConfig(Scanner scanner) {
        AuthController auth = new AuthController();
        ConfigController config = new ConfigController();
        MenuControllerParent menu = new MenuControllerParent();
        ServiceController service = new ServiceController();
        UIController ui = new UIController();

        FrontController dispatcher = new FrontController(auth, config, menu, service, ui);

        LogManager.boot(BootState.INFO, "Starting Boot Process");
        LogManager.boot(BootState.INFO, "Running Controller classes");

        try {
            boolean configOK = dispatcher.callController(FrontController.RequestType.CONFIG, scanner);

            if (!configOK) {
                throw new RuntimeException("System config failed");
            }

            System.out.println("[OK] The System Config was a success");
            System.out.println("[INFO] Starting Authentication phase");

            LogManager.config(ConfigState.SUCCESS, "System Config was a success");
            LogManager.config(ConfigState.INFO, "Starting Authentication phase");

            AuthMSG show = new AuthMSG();
            show.msg();

            dispatcher.callController(FrontController.RequestType.AUTH, scanner);

            CurrentAccountInSessionValues user = CurrentSession.getCurrentAccount();

            if (user == null) {
                LogManager.auth(AuthState.INFO, "No User Session where found");
                throw new IllegalStateException("[WARN] No User in this session could be found");
            }

            if (user.hasAccessToMenu()) {
                LogManager.menu(MenuState.SUCCESS, "The User have access to the menu");
                dispatcher.callController(FrontController.RequestType.MENU, scanner);

                LogManager.auth(AuthState.INFO, "Calling submenu controller to route the service");
                dispatcher.callController(FrontController.RequestType.SERVICE, scanner);

            } else {
                throw new IllegalStateException("The User does not have enough rights to use the menu");
            }

        } catch (RuntimeException error) {
            System.out.println("[ERROR] System Config Failed");
            LogManager.system(SystemState.WARN,  error.getMessage());
            System.exit(1);
        }
    }
}
