package app.Config;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.FileNotFoundException;

//Check if .env file exsist and if the parameters are all valid

public class EnvValidationService {

    private String DbHost;
    private int DBPortINT;
    private String DBName;
    private String DBUser;
    private String DbPWSD;

    public void sqlHeader() {
        System.out.println("\n==================================================");
        System.out.println("              Configuration & Database");
        System.out.println("--------------------------------------------------");
        System.out.println("   Validating .env configuration");
        System.out.println("   Establishing SQL connection");
        System.out.println("==================================================\n");
    }

    //Check if file even exsist
    public void CheckFileStatus() {

        //Check if .env file exsist
        System.out.println("\n[INFO] Load .env files...........\n");

        File envFile = new File(".env");

        //Call CheckENVParam if file exsists
        try {
            if (!envFile.exists()) {
                throw new FileNotFoundException("[ERROR] It seems that the .env file do not exsist in this project");
            }

            System.out.println("\n[OK] It seems that the .env file exsists\n");
            CheckENVParam();

        } catch (FileNotFoundException error) {
            System.out.println(error.getMessage());
        }
    }

    public void CheckENVParam() {
        try {
            int port;

            Dotenv dotenv = Dotenv.load();

            //Set the base values from the .env values
            String Host = dotenv.get("DB_HOST");
            String PortSTR = dotenv.get("DB_PORT");
            String Name = dotenv.get("DB_NAME");
            String User = dotenv.get("DB_USER");
            String PWSD = dotenv.get("DB_PWSD");

            String LocalAdminName = dotenv.get("LOCAL_ADMIN_NAME");
            String Local_AdminPWSD = dotenv.get("LOCAL_ADMIN_PWSD");
            String Local_Admin_Email = dotenv.get("LOCAL_ADMIN_EMAIL");

            String admin_name = dotenv.get("ADMIN_NAME");
            String admin_pwsd = dotenv.get("ADMIN_PWSD_DEFAULT");
            String admin_email = dotenv.get("ADMIN_EMAIL_DEFAULT");
            String bootstrap_key = dotenv.get("BOOTSTRAP_KEY");


            //Check if env exsist if not throw error msg
            if (Host == null || Host.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your DB_HOST for the db host seems to be empty");
            }

            if (PortSTR == null || PortSTR.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your DB_PORT in the .env for the DB Port seems to be empty/0");
            }

            try {
                port = Integer.parseInt(PortSTR);
            } catch (NumberFormatException error) {
                throw new IllegalArgumentException("[ERROR] DB_PORT must be a valid number");
            }

            if (Name == null || Name.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your DB_NAME in the .env for the db name seems to be empty");
            }

            if (User == null || User.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your the DB_USER in your .env seems to be empty");
            }

            if (PWSD == null || PWSD.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that the DB_PWSD in your .env seems to be empty");
            }

            if (LocalAdminName == null || LocalAdminName.isBlank()) {
                throw new IllegalArgumentException ("[ERROR] It seems that your LOCAL_ADMIN_NAME for the default local admin account is empty" );
            }

            if (Local_AdminPWSD == null || Local_AdminPWSD.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your LOCAL_ADMIN_PWSD for the default local admin password is empty" );
            }

            if (Local_Admin_Email == null || Local_Admin_Email.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your LOCAL_ADMIN_EMAIL for the default local admin email is empty");
            }

            if (admin_name == null || admin_name.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your ADMIN_NAME for the default admin account is empty");
            }

            if (admin_pwsd == null || admin_pwsd.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your ADMIN_PWSD_DEFAULT for the default admin password is empty");
            }

            if (admin_email == null || admin_email.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your ADMIN_EMAIL_DEFAULT for the default admin email is empty");
            }

            if (bootstrap_key == null || bootstrap_key.isBlank()) {
                throw new IllegalArgumentException("[ERROR] It seems that your BOOTSTRAP_KEY for the bootstrap authentication is empty");
            }

            System.out.println("\n[OK] The .env values are all valid");
            System.out.println("[INFO] The values will be setted now\n");

            setEnvValues(Host, port, Name, User, PWSD);

        } catch (IllegalArgumentException error) {
            System.out.println("\n[ERROR] Something in your .env file is wrong or corrupted");
            System.out.println(error.getMessage() +  "\n");
        }
    }

    //Set the values to the attributes
    public void setEnvValues(String Host, int port, String Name, String User, String PWSD) {
        this.DbHost = Host;
        this.DBPortINT = port;
        this.DBName = Name;
        this.DBUser = User;
        this.DbPWSD = PWSD;

        System.out.println("\n[OK] The .env values are setted sucsessfully\n");
    }

    //Getter Methods to collect values
    public String getHost() {
        return DbHost;
    }

    public int getPort() {
        return DBPortINT;
    }

    public String getDBName() {
        return DBName;
    }

    public String getUser() {
        return DBUser;
    }

    public String getPWSD() {
        return DbPWSD;
    }
}
