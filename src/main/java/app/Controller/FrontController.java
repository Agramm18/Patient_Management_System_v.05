package app.Controller;
import java.util.Scanner;
//Main Controller classes that navigate the System
public class FrontController {
    private final AuthController authController;
    private final ConfigController configController;
    private final MenuController menuController;
    private final ServiceController serviceController;
    private final uiController UIController;

    //Enum to store request types
    public enum RequestType {
        CONFIG,
        AUTH,
        MENU,
        SERVICE,
        UI,
        EXIT
    }

    public FrontController(AuthController authController, ConfigController configController, MenuController menuController, ServiceController serviceController, uiController UIController) {
        this.authController = authController;
        this.configController = configController;
        this.menuController = menuController;
        this.serviceController = serviceController;
        this.UIController = UIController;
    }

    //Handle UseCase via RequestType
    public boolean NavigateSubController(RequestType request, Scanner scanner) {

        switch (request) {
            case CONFIG:
                return configController.execute(scanner);

            case AUTH:
                authController.VerifyAccountStatus(scanner);
                return true;
        }

        return false;
    }
}
