package app.Auth.Flow.Services.LoginService;

import java.sql.SQLException;
import java.util.Scanner;

import app.Auth.Flow.Services.AuthSecurityService.CollectLogs;
import app.Auth.Flow.Services.AuthSecurityService.RoleValidation;
import app.Menus.DepartmentMenu;
import app.Repository.LoginRepository.CheckUserInDB;


/*
     This Section Checks the Username and Validate how to proceed after the first Login

     If pending -> first setup
     If active -> display Menu based on Department & Job
     If waiting_for_password_change & user_job = admin or local_admin -> the password must be changed to unlock these accounts

*/

public class LoginVerification {
    private int RETRYS = 0;
    private int RETRYS_MAX = 5;

    private final CheckUserInDB repository = new CheckUserInDB();

    public enum UserStatus {
        ACTIVE,
        DISABLED,
        PENDING,
        LOCKED,
        ON_QUARANTINE,
        WAITING_FOR_PASSWORD_CHANGE
    };

    private String AccountStatus;


    public CollectLogs loggedUser(String Username, String PWSD, Scanner scanner) {

        try {
            boolean userValid = repository.checkUserInDB(Username);
            boolean passwordOK = repository.checkPWSD(PWSD, Username);
            String userStatus = repository.checkUserStatus(Username);

            if (!userValid) {
                return new CollectLogs(false, "USERNAME_NOT_FOUND");
            }

            System.out.println("\n[INFO] Continue with PWSD check");

            if (!passwordOK) {
                this.RETRYS++;
                System.out.println("[WARNING] Invalid Password detected");
                System.out.println("[INFO] Please Notice if retrys >=5 your account will be locked");
                System.out.println("[INFO] Failed Passwords: " + this.RETRYS);

                if (this.RETRYS >= this.RETRYS_MAX) {
                    return new CollectLogs(false, "TO_MANY_INVALID_PASSWORDS");

                }

                return new CollectLogs(false, "INVALID_PASSWORD");
            }

            if (userStatus == null) {
                return new CollectLogs(false, "Unknown Account Status");
            }

            switch (userStatus) {
                case "active":
                    System.out.println("[OK] The User account is active");
                    return new CollectLogs(true, null);
                case "disabled":
                    System.out.println("[WARNING] This account is Locked an must be activated by an administrator");
                    return new CollectLogs(false, "Account is Locked");
                case "pending":
                    System.out.println("[INFO] This account is not fully activated");

                    FirstLogin run = new FirstLogin();
                    run.firstSetup(Username, scanner);

                    return new CollectLogs(true, "Must be authorized");
                case "locked":
                    System.out.println("[WARNING] This account is locked and must be activated by an administrator");
                    return new CollectLogs(false, "Account is locked");
                case "on_quarantine":
                    System.out.println("[FATAL] This account is on quarantine and must be checked");
                    return new CollectLogs(false, "Account is on quarantine based on malicious activities");
                case "waiting_for_password_change":
                    System.out.println("[INFO] Please change your current password to continue");
                    return new CollectLogs(true, "Account needs a password change");
            }

            this.RETRYS = 0;

            return new CollectLogs(true, null);

        } catch (SQLException error) {
            System.out.println("[ERROR] SQL error during login: " + error.getMessage());
            return new CollectLogs(false, "SQL_EXCEPTION");
        }
    }
}
