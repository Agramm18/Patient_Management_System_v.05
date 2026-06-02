package app.Config;

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
    private String dbPWSD;


    public boolean envStatus() {
        try {
            boolean fileExsists = checkFileStatus();

            if (!fileExsists) {
                return false;
            }

            System.out.println("\n[OK] It seems that the .env file exists\n");
            System.out.println("[INFO] Continue with the value check");

            boolean paramValid = checkENVParam();

            if (!paramValid) {
                return false;
            }

            System.out.println("\n[OK] The .env values are all valid");
            return true;

        } catch (IllegalStateException error) {
            System.out.println(error.getMessage());
            return false;
        }
    }

    private boolean checkFileStatus() {

        ConfigMSG show = new ConfigMSG();
        show.configMSG();

        File envFile = new File(".env");

        //Call CheckENVParam if file exsists
        try {
            if (!envFile.exists()) {
                throw new FileNotFoundException("[ERROR] It seems that the .env file do not exist in this project");
            } else {
                return true;
            }


        } catch (FileNotFoundException error) {
            System.out.println(error.getMessage());
            return false;
        }
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
            String pwsd = dotenv.get("DB_PWSD");

            String localAdminName = dotenv.get("LOCAL_ADMIN_NAME");
            String localAdminPwsd = dotenv.get("LOCAL_ADMIN_PWSD");
            String localAdminEmail = dotenv.get("LOCAL_ADMIN_EMAIL");

            String adminName = dotenv.get("ADMIN_NAME");
            String adminPWSD = dotenv.get("ADMIN_PWSD_DEFAULT");
            String adminEmail = dotenv.get("ADMIN_EMAIL_DEFAULT");

            String bootstrapKey = dotenv.get("BOOTSTRAP_KEY");
            String recoveryKey = dotenv.get("RECOVERY_KEY");


            //Check if env exsist if not throw error msg
            if (host == null || host.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your DB_HOST for the db host seems to be empty");
            }

            if (portSTR == null || portSTR.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your DB_PORT in the .env for the DB Port seems to be empty/0");
            }

            try {
                port = Integer.parseInt(portSTR);
            } catch (NumberFormatException error) {
                throw new IllegalStateException("[ERROR] DB_PORT must be a valid number");
            }

            if (name == null || name.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your DB_NAME in the .env for the db name seems to be empty");
            }

            if (user == null || user.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your the DB_USER in your .env seems to be empty");
            }

            if (pwsd == null || pwsd.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that the DB_PWSD in your .env seems to be empty");
            }

            if (localAdminName == null || localAdminName.isBlank()) {
                throw new IllegalStateException ("[ERROR] It seems that your LOCAL_ADMIN_NAME for the default local admin account is empty" );
            }

            if (localAdminPwsd == null || localAdminPwsd.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your LOCAL_ADMIN_PWSD for the default local admin password is empty" );
            }

            if (localAdminEmail == null || localAdminEmail.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your LOCAL_ADMIN_EMAIL for the default local admin email is empty");
            }

            if (adminName == null || adminName.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your ADMIN_NAME for the default admin account is empty");
            }

            if (adminPWSD == null || adminPWSD.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your ADMIN_PWSD_DEFAULT for the default admin password is empty");
            }

            if (adminEmail == null || adminEmail.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your ADMIN_EMAIL_DEFAULT for the default admin email is empty");
            }

            if (bootstrapKey == null || bootstrapKey.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that your BOOTSTRAP_KEY for the bootstrap authentication is empty");
            }

            if (recoveryKey == null || recoveryKey.isBlank()) {
                throw new IllegalStateException("[ERROR] It seems that the Recovery Key is missing");
            }

            setEnvValues(host, port, name, user, pwsd);
            return true;

        } catch (IllegalArgumentException error) {
            System.out.println("\n[ERROR] Something in your .env file is wrong or corrupted");
            System.out.println(error.getMessage() +  "\n");
            return false;
        } catch (IllegalStateException error) {
            System.out.println(error.getMessage());
            return false;
        }
    }

    //Set the values to the attributes
    public void setEnvValues(String Host, int port, String Name, String User, String PWSD) {
        this.dbHost = Host;
        this.dbPortINT = port;
        this.dbName = Name;
        this.dbUser = User;
        this.dbPWSD = PWSD;

        System.out.println("\n[OK] The .env values are set successfully\n");
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

    public String getPWSD() {
        return dbPWSD;
    }
}
