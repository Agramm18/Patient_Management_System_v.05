package app.Config;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

import app.CLIText.DisplayMessages.ConfigMSG;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.FileNotFoundException;

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
    private int dbPortINT;
    private String dbName;
    private String dbUser;
    private String dbPassword;


    public boolean envStatus() {
        try {
            boolean fileExsists = checkFileStatus();

            if (!fileExsists) {
                return false;
            }

            LogManager.log(LogType.CONFIG_SUCCESS, "It seems that the .env file exists");
            LogManager.log(LogType.MESSAGE, "Continue with the value check");

            boolean paramValid = checkENVParam();

            if (!paramValid) {
                return false;
            }

            LogManager.log(LogType.CONFIG_SUCCESS, "The .env values are all valid");
            return true;

        } catch (FileNotFoundException | IllegalStateException error) {
            LogManager.log(LogType.CONFIG_FAILED, error.getMessage());
            return false;
        }
    }

    private boolean checkFileStatus() throws FileNotFoundException {

        ConfigMSG show = new ConfigMSG();
        show.configMSG();

        File envFile = new File(".env");

        //Call CheckENVParam if file exsists

            if (!envFile.exists()) {
                throw new FileNotFoundException("It seems that the .env file do not exist in this project");
            }

            return true;
    }

    private boolean checkENVParam() {
        try {
            int port;

            Dotenv dotenv = Dotenv.load();

            //Set the base values from the .env values
            String host = dotenv.get("DB_HOST");
            String portSTR = dotenv.get("DB_PORT");
            String name = dotenv.get("DB_NAME");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            String localAdminName = dotenv.get("LOCAL_ADMIN_NAME");
            String localAdminPassword = dotenv.get("LOCAL_ADMIN_PASSWORD");
            String localAdminEmail = dotenv.get("LOCAL_ADMIN_EMAIL");

            String adminName = dotenv.get("ADMIN_NAME");
            String adminPassword = dotenv.get("ADMIN_PASSWORD_DEFAULT");
            String adminEmail = dotenv.get("ADMIN_EMAIL_DEFAULT");

            String bootstrapKey = dotenv.get("BOOTSTRAP_KEY");
            String recoveryKey = dotenv.get("RECOVERY_KEY");


            // Check if env exists if not throw error msg
            if (host == null || host.isBlank()) {
                throw new IllegalStateException("It seems that your DB_HOST for the db host seems to be empty");
            }

            if (portSTR == null || portSTR.isBlank()) {
                throw new IllegalStateException("It seems that your DB_PORT in the .env for the DB Port seems to be empty/0");
            }

            try {
                port = Integer.parseInt(portSTR);
            } catch (NumberFormatException error) {
                throw new IllegalStateException("DB_PORT must be a valid number");
            }

            if (name == null || name.isBlank()) {
                throw new IllegalStateException("It seems that your DB_NAME in the .env for the db name seems to be empty");
            }

            if (user == null || user.isBlank()) {
                throw new IllegalStateException("It seems that the DB_USER in your .env seems to be empty");
            }

            if (password == null || password.isBlank()) {
                throw new IllegalStateException("It seems that the DB_PASSWORD in your .env seems to be empty");
            }

            if (localAdminName == null || localAdminName.isBlank()) {
                throw new IllegalStateException("It seems that your LOCAL_ADMIN_NAME for the default local admin account is empty");
            }

            if (localAdminPassword == null || localAdminPassword.isBlank()) {
                throw new IllegalStateException("It seems that your LOCAL_ADMIN_PASSWORD for the default local admin password is empty");
            }

            if (localAdminEmail == null || localAdminEmail.isBlank()) {
                throw new IllegalStateException("It seems that your LOCAL_ADMIN_EMAIL for the default local admin email is empty");
            }

            if (adminName == null || adminName.isBlank()) {
                throw new IllegalStateException("It seems that your ADMIN_NAME for the default admin account is empty");
            }

            if (adminPassword == null || adminPassword.isBlank()) {
                throw new IllegalStateException("It seems that your ADMIN_PASSWORD_DEFAULT for the default admin password is empty");
            }

            if (adminEmail == null || adminEmail.isBlank()) {
                throw new IllegalStateException("It seems that your ADMIN_EMAIL_DEFAULT for the default admin email is empty");
            }

            if (bootstrapKey == null || bootstrapKey.isBlank()) {
                throw new IllegalStateException("It seems that your BOOTSTRAP_KEY for the bootstrap authentication is empty");
            }

            if (recoveryKey == null || recoveryKey.isBlank()) {
                throw new IllegalStateException("It seems that the Recovery Key is missing");
            }

            setEnvValues(host, port, name, user, password);
            return true;

        } catch (IllegalArgumentException error) {
            LogManager.log(LogType.CONFIG_FAILED, error.getMessage());
            return false;
        } catch (IllegalStateException error) {
            LogManager.log(LogType.CONFIG_FAILED, error.getMessage());
            return false;
        }
    }

    //Set the values to the attributes
    public void setEnvValues(String Host, int port, String Name, String User, String PWSD) {
        this.dbHost = Host;
        this.dbPortINT = port;
        this.dbName = Name;
        this.dbUser = User;
        this.dbPassword = PWSD;

        LogManager.log(LogType.CONFIG_SUCCESS, "The .env values are set successfully");
    }

    //Getter Methods to collect values
    public String getHost() {
        return dbHost;
    }

    public int getPort() {
        return dbPortINT;
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
