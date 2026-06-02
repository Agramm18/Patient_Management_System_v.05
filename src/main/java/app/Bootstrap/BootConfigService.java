package app.Bootstrap;

import app.CLIText.DisplayMessages.AuthMSG;
import app.CLIText.DisplayMessages.LoaderMSG;
import app.Controller.*;

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

        System.out.println("[INFO] Booting into the System..........");
        System.out.println("[INFO] Running Controller classes.......");

        try {
            boolean configOK = dispatcher.navigateSubController(FrontController.RequestType.CONFIG, scanner);

            if (!configOK) {
                throw new RuntimeException("\n[FATAL] System config failed");
            }

            System.out.println("\n[OK] System Config where a success");
            System.out.println("[INFO] Starting Authentication phase\n");

            AuthMSG show = new AuthMSG();
            show.msg();

            dispatcher.navigateSubController(FrontController.RequestType.AUTH, scanner);

        } catch (RuntimeException error) {
            System.out.println(error.getMessage());
            System.out.println("[WARNING] System won't load and will be shut down right now");
            System.exit(1);
        }
    }
}
