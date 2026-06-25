package app.Config;

import app.CLIText.DisplayMessages.ConfigMSG;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.FileNotFoundException;
import app.Config.LogManager.LogType;

/*
    In this Section happens the .env Check

    Following things are Checked

    1. Does the file even exist
    2. If the .env Values have Parameters and if there are not Blank or empty
    3. If the DB Port is an int

    After that the .env values will be set
    At the end they are saved and will be collected via getter

*/

public class EnvValidationService {

    private String dbHost;
    private int dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    //Parent Method to call private child methods
    public boolean envStatus() {
        try {
            boolean fileExists = checkFileStatus();

            if (!fileExists) {
                return false;
            }
            System.out.println("[OK] The .env file exists in the Project root");

            LogManager.log(LogType.CONFIG_SUCCESS, "It seems that the .env file exists");
            LogManager.log(LogType.CONFIG_INFO, "Continue with the value check");

            boolean paramValid = validateEnvParameters();

            if (!paramValid) {
                return false;
            }

            System.out.println("[OK] The .env values are all valid");
            LogManager.log(LogType.CONFIG_SUCCESS, "The .env values are all valid");
            return true;

        } catch (FileNotFoundException | IllegalStateException error) {
            System.out.println("[ERROR] It seems that the .env File is Missing in the Root");
            System.out.println("[ERROR] Please add a .env file in the Project root" + error.getMessage());
            LogManager.log(LogType.CONFIG_FAILED, error.getMessage());
            return false;
        }
    }

    //Check if the .env file exists in the project root
    private boolean checkFileStatus() throws FileNotFoundException {

        ConfigMSG show = new ConfigMSG();
        show.configMSG();

        File envFile = new File(".env");

            if (!envFile.exists()) {
                throw new FileNotFoundException("It seems that the .env file do not exist in this project root");
            }

            return true;
    }

    private boolean validateEnvParameters() {
        try {

            Dotenv dotenv = Dotenv.load();

            // Create the application configuration from the .env values.
            EnvSetup setup = new EnvSetup(
                    dotenv.get("DB_HOST"),
                    Integer.parseInt(dotenv.get("DB_PORT")),
                    dotenv.get("DB_NAME"),
                    dotenv.get("DB_USER"),
                    dotenv.get("DB_PASSWORD"),
                    dotenv.get("LOCAL_ADMIN_NAME"),
                    dotenv.get("LOCAL_ADMIN_PASSWORD"),
                    dotenv.get("LOCAL_ADMIN_EMAIL"),
                    dotenv.get("ADMIN_NAME"),
                    dotenv.get("ADMIN_PASSWORD_DEFAULT"),
                    dotenv.get("ADMIN_EMAIL_DEFAULT"),
                    dotenv.get("BOOTSTRAP_KEY"),
                    dotenv.get("RECOVERY_KEY")
            );

            setEnvValues(setup.dbHost(), setup.dbPort(), setup.dbName(), setup.dbUser(), setup.dbPassword());
            return true;

        } catch (NumberFormatException error) {
            System.out.println("[ERROR] It seems that you do not typed in a number");
            LogManager.log(LogType.CONFIG_FAILED, error.getMessage());
            return false;
        } catch (IllegalStateException error) {
            System.out.println("[ERROR] It seems that something is wrong with your .env value \n " + error.getMessage());
            LogManager.log(LogType.CONFIG_FAILED, error.getMessage());
            return false;
        }
    }

    //Set the values to the attributes
    private void setEnvValues(String host, int port, String name, String user, String dbPassword) {
        this.dbHost = host;
        this.dbPort = port;
        this.dbName = name;
        this.dbUser = user;
        this.dbPassword = dbPassword;
        System.out.println("[OK] All .env values are set successfully");
        LogManager.log(LogType.CONFIG_SUCCESS, "The .env values are set successfully");
    }

    //Getter Methods to collect values
    public String getHost() {
        return dbHost;
    }

    public int getPort() {
        return dbPort;
    }

    public String getDBName() {
        return dbName;
    }

    public String getUser() {
        return dbUser;
    }

    public String getPassword() {
        return dbPassword;
    }
}
