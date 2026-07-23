package app.Controller;
import java.util.Scanner;


import app.Auth.Flow.Services.LoginService.CurrentUser;
import app.Menu.MenuContextStructure;

/*
    This Section is the Frontcontroller which basically Manage the other controllers

    The Idea behind this was to have a Parent Controller who calls the child Controller such as Auth, Config etc.
    The main reason behind this is to keep the code reliable and maintainable so I can clearly see which class calls which.

    Currently only Config and Auth are called the normal runtime should should be

    1. Config
    2. Auth
    3. Menu
    4. Service

    So I can call the Menus based on the Department of the User to maintain Security
*/

public class FrontController {
    private final AuthController authController;
    private final ConfigController configController;
    private final MenuControllerParent menuControllerParent;
    private final ServiceController serviceController;
    private final UIController UIController;
    private MenuContextStructure menuChoice;

    private CurrentUser currentUser;

    //Enum to store request types
    public enum RequestType {
        CONFIG,
        AUTH,
        MENU,
        SERVICE,
        UI,
        EXIT
    }

    public FrontController(AuthController authController, ConfigController configController, MenuControllerParent menuControllerParent, ServiceController serviceController, UIController UIController) {
        this.authController = authController;
        this.configController = configController;
        this.menuControllerParent = menuControllerParent;
        this.serviceController = serviceController;
        this.UIController = UIController;
    }

    //Handle UseCase via RequestType
    public boolean callController(RequestType request, Scanner scanner) {

        switch (request) {
            case CONFIG:
                return configController.execute(scanner);

            case AUTH:
                authController.verifyAccountStatus(scanner);
                return true;

            case MENU:
                this.menuChoice = menuControllerParent.routeRole(scanner);
                return true;

            case SERVICE:
                serviceController.routeService(this.menuChoice, scanner);
                return true;
        }

        return false;
    }
}
