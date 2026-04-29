package app.Controller;

import app.Config.*;
import app.Controller.FrontController.RequestType;

public class ConfigController {
    private EnvValidationService envValidationService;
    private SQLValidationService sqlValidationService;

    public void execute() {
        System.out.println("[INFO] Running Config Env & Build SQL Connection as Entrypoint for the System");
        System.out.println("[INFO] Please note if anything is invalid in the .env config or SQL config the whole System will crash");

        EnvValidationService check = new EnvValidationService();
        check.CheckFileStatus();

        SQLValidationService configurate = new SQLValidationService(check);
        configurate.DBConnection();
    }
}
