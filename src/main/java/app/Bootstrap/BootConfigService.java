package app.Bootstrap;

import app.Controller.*;

public class BootConfigService {
    public void DisplayHelloMSG() {
        System.out.println("\n==================================================");
        System.out.println("                 SYSTEM LOADER");
        System.out.println("--------------------------------------------------");
        System.out.println("  Loading program modules...");
        System.out.println("==================================================\n");
    }

    public void SystemConfig() {
        AuthController auth = new AuthController();
        ConfigController config = new ConfigController();
        MenuController menu = new MenuController();
        ServiceController service = new ServiceController();
        uiController ui = new uiController();

        FrontController dispatcher = new FrontController(auth, config, menu, service, ui);

        System.out.println("[INFO] Booting into the System..........");
        System.out.println("[INFO] Running Controller classes.......");

        try {
            dispatcher.NavigateSubController(FrontController.RequestType.CONFIG);
            dispatcher.NavigateSubController(FrontController.RequestType.AUTH);
        } catch (Exception error) {
            System.out.println("[FATAL] Boot failed " + error.getMessage());
            System.out.println("[INFO] System won't load and will be shutted down right now");
            System.exit(1);
        }
    }
}
