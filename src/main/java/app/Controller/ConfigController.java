package app.Controller;
import java.util.Scanner;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

import app.Repository.ConfigRepository.CheckForDefaultAccounts;
import app.Repository.ConfigRepository.SetRecoveryKey;
import io.github.cdimascio.dotenv.Dotenv;
import app.Config.*;

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

        LogManager.log(LogType.CONFIG_INFO, "Running Config Env & Build SQL Connection as Entrypoint for the System");

        EnvValidationService configurate = new EnvValidationService();
        boolean isValid = configurate.envStatus();

        if (isValid) {
            SQLValidationService build = new SQLValidationService(configurate);
            boolean connectionIsValid = build.DBConnection();

            if (connectionIsValid) {
                boolean globalConnectionIsValid = DBManager.initialize(
                        build.getSQLUser(),
                        build.getSqlPWSD(),
                        build.getSqlURL()
                );

                if (!globalConnectionIsValid) {
                    return false;
                }

            } else {
                return false;
            }

        } else {
            return false;
        }

        Dotenv dotenv = Dotenv.load();
        HandleRecoveryKey collect = new HandleRecoveryKey(dotenv);
        collect.plainKey();
        collect.hashedKey();

        String recoveryKey = collect.getRecoveryKeyHashed();

        SetRecoveryKey insert = new SetRecoveryKey();
        insert.keyValue(recoveryKey);

        CheckForDefaultAccounts CheckStatus = new CheckForDefaultAccounts();
        return CheckStatus.dbAccounts();

    }
}
