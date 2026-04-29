package app.Controller;

//Main Controller classes that navigate the System
public class FrontController {
    private AuthController authController;
    private final ConfigController configController;
    private MenuController menuController;
    private ServiceController serviceController;
    private uiController UIController;

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
    public void NavigateSubController(RequestType request) {

        switch (request) {
            case CONFIG:
                configController.execute();
                break;

            case AUTH:
                authController.VerifyUser();
        }
    }
}
