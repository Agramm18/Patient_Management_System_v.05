package app.Controller;

//Log Import
import app.Config.LogManager;
import app.Config.LogManager.LogType;

//File Imports
import app.Repository.ConfigRepository.CheckForDefaultAccounts;
import app.Repository.ConfigRepository.SetRecoveryKey;
import app.Config.*;

//Libary Imports
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Scanner;

/*
    This Section is a controller that routes the user to the Auth Process

    1. The Controller routes to  EnvValidationService.java which checks if the .env is valid
    2. Then the controller routes to SQLValidationService.java which creates the DB URL and validates if the connection is valid
    3. After that, a global DB connection is created so that you can import it into other files instead of passing the connection to called methods.
*/

public class ConfigController {

    public boolean execute(Scanner scanner) {

        LogManager.log(LogType.CONFIG_INFO, "Running Config Env & Build SQL Connection as Entrypoint for the System");

        //Check if the .env values are all valid and if the .env file even exists
        EnvValidationService envValidationService = new EnvValidationService();
        boolean isValid = envValidationService.envStatus();

        if (isValid) {
            //Build a SQL connection to the Database
            SQLValidationService sqlValidationService = new SQLValidationService(envValidationService);
            boolean connectionIsValid = sqlValidationService.DBConnection();

            //If the connection is valid a global connection will be created so it's callable
            if (connectionIsValid) {
                boolean globalConnectionIsValid = DBManager.initialize(
                        sqlValidationService.getSQLUser(),
                        sqlValidationService.getSqlPWSD(),
                        sqlValidationService.getSqlURL()
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

        //Load setup to recover system accounts
        Dotenv dotenv = Dotenv.load();
        HandleRecoveryKey handleRecoveryKey = new HandleRecoveryKey(dotenv);
        handleRecoveryKey.plainKey();
        handleRecoveryKey.hashedKey();

        String recoveryKey = handleRecoveryKey.getRecoveryKeyHashed();

        //Check if The required system accounts even exist if not they will automatically create
        SetRecoveryKey setRecoveryKey = new SetRecoveryKey();
        setRecoveryKey.keyValue(recoveryKey);

        CheckForDefaultAccounts checkForDefaultAccounts = new CheckForDefaultAccounts();
        return checkForDefaultAccounts.dbAccounts();

    }
}
