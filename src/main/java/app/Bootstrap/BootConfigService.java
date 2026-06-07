package app.Bootstrap;

import app.CLIText.DisplayMessages.AuthMSG;
import app.CLIText.DisplayMessages.LoaderMSG;
import app.Config.LogManager;
import app.Controller.*;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

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

        LogManager.log(LogType.BOOT_INFO, "Booting into the System");
        LogManager.log(LogType.BOOT_INFO, "Running Controller classes");

        try {
            boolean configOK = dispatcher.navigateSubController(FrontController.RequestType.CONFIG, scanner);

            if (!configOK) {
                throw new RuntimeException("System config failed");
            }

            LogManager.log(LogType.BOOT_INFO, "System Config where a success");
            LogManager.log(LogType.BOOT_INFO, "Starting Authentication phase");

            AuthMSG show = new AuthMSG();
            show.msg();

            dispatcher.navigateSubController(FrontController.RequestType.AUTH, scanner);

        } catch (RuntimeException error) {
            LogManager.log(LogType.BOOT_FAILED, error.getMessage());
            System.exit(1);
        }
    }
}
