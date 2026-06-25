package app.Bootstrap;

import app.CLIText.DisplayMessages.AuthMSG;
import app.CLIText.DisplayMessages.LoaderMSG;
import app.Auth.Flow.Services.LoginService.CurrentUser;

import app.Config.LogManager.LogType;
import app.Config.LogManager;
import app.Controller.*;

import app.Auth.Flow.CurrentSession;

import java.util.Scanner;

public class BootConfigService {

    public void displayLoader() {
        LoaderMSG show = new LoaderMSG();
        show.message();
    }

    public void systemConfig(Scanner scanner) {
        AuthController auth = new AuthController();
        ConfigController config = new ConfigController();
        MenuController menu = new MenuController();
        SubMenuController subMenu = new SubMenuController();
        ServiceController service = new ServiceController();
        UIController ui = new UIController();

        FrontController dispatcher = new FrontController(auth, config, menu, subMenu, service, ui);

        LogManager.log(LogType.BOOT_INFO, "Starting Boot Process");
        LogManager.log(LogType.BOOT_INFO, "Running Controller classes");

        try {
            boolean configOK = dispatcher.callController(FrontController.RequestType.CONFIG, scanner);

            if (!configOK) {
                throw new RuntimeException("System config failed");
            }

            System.out.println("[OK] The System Config was a success");
            System.out.println("[INFO] Starting Authentication phase");

            LogManager.log(LogType.CONFIG_SUCCESS, "System Config was a success");
            LogManager.log(LogType.SYSTEM_INFO, "Starting Authentication phase");

            AuthMSG show = new AuthMSG();
            show.msg();

            dispatcher.callController(FrontController.RequestType.AUTH, scanner);

            CurrentUser user = CurrentSession.getCurrentUser();

            if (user == null) {
                LogManager.log(LogType.AUTH_FAILED, "No User Session where found");
                throw new IllegalStateException("[WARN] No User in this session could be found");
            }

            if (user.hasAccessToMenu()) {
                LogManager.log(LogType.AUTH_SUCCESS, "The User have access to the menu");
                dispatcher.callController(FrontController.RequestType.MENU, scanner);

                dispatcher.callController(FrontController.RequestType.SERVICE, scanner);
                LogManager.log(LogType.SYSTEM_INFO, "Started Service Flow");

            } else {
                throw new IllegalStateException("The User does not have enough rights to use the menu");
            }

        } catch (RuntimeException error) {
            System.out.println("[ERROR] System Config Failed");
            LogManager.log(LogType.CONFIG_FAILED, error.getMessage());
            System.exit(1);
        }
    }
}
