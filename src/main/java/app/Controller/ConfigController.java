package app.Controller;
import java.util.Scanner;
import app.Config.*;
import app.Controller.FrontController.RequestType;

/*
    This Section is a controller that routes the user to the Auth Process

    1. The Controller routes to  EnvValidationService.java which checks if the .env is valid
    2. Then the controller routes to SQLValidationService.java which creates the DB URL and validates if the connection is valid
    3. After that, a global DB connection is created so that you can import it into other files instead of passing the connection to called methods.
*/

public class ConfigController {
    private EnvValidationService envValidationService;
    private SQLValidationService sqlValidationService;

    public boolean execute(Scanner scanner) {
        System.out.println("[INFO] Running Config Env & Build SQL Connection as Entrypoint for the System");
        System.out.println("[INFO] Please note if anything is invalid in the .env config or SQL config the whole System will crash");

        EnvValidationService check = new EnvValidationService();
        check.checkFileStatus();

        SQLValidationService configurate = new SQLValidationService(check);
        configurate.DBConnection();

        DBManager.initialize(
                configurate.getSQLUser(),
                configurate.getSqlPWSD(),
                configurate.getSqlURL()
        );

        SystemAccountValidationService CheckStatus = new SystemAccountValidationService();
        return CheckStatus.DBAccounts();
    }
}
