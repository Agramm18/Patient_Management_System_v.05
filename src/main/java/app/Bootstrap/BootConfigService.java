package app.Bootstrap;

import app.CLIText.DisplayMessages.AuthMSG;
import app.CLIText.DisplayMessages.LoaderMSG;

import app.Config.LogManager.LogType;
import app.Config.LogManager;
import app.Controller.*;

import java.sql.SQLException;
import java.util.Scanner;

public class BootConfigService {

    public void displayLoader() {
        LoaderMSG show = new LoaderMSG();
        show.message();
    }

    public void SystemConfig(Scanner scanner) {
        AuthController auth = new AuthController();
        ConfigController config = new ConfigController();
        MenuController menu = new MenuController();
        ServiceController service = new ServiceController();
        uiController ui = new uiController();

        FrontController dispatcher = new FrontController(auth, config, menu, service, ui);

        LogManager.log(LogType.BOOT_INFO, "Starting Boot Process");
        LogManager.log(LogType.BOOT_INFO, "Running Controller classes");

        try {
            boolean configOK = dispatcher.navigateSubController(FrontController.RequestType.CONFIG, scanner);

            if (!configOK) {
                throw new RuntimeException("System config failed");
            }

            System.out.println("[OK] The System Config was a success");
            System.out.println("[INFO] Starting Authentication phase");

            LogManager.log(LogType.CONFIG_SUCCESS, "System Config was a success");
            LogManager.log(LogType.SYSTEM_INFO, "Starting Authentication phase");

            AuthMSG show = new AuthMSG();
            show.msg();

            dispatcher.navigateSubController(FrontController.RequestType.AUTH, scanner);

        } catch (RuntimeException error) {
            System.out.println("[ERROR] System Config Failed");
            LogManager.log(LogType.CONFIG_FAILED, error.getMessage());
            System.exit(1);
        }
    }
}
