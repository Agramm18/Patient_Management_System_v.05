package app.Auth.Flow.Services.LoginService;

import java.sql.SQLException;
import java.util.Scanner;

import app.Auth.Flow.Services.AuthSecurityService.Audit.CollectLogs;
import app.Auth.Flow.Services.PasswordService.PasswordService;
import app.Repository.AuthRepository.Management.CountFailedLoginAttempts;
import app.Repository.AuthRepository.Password.ExecutePWSDPolicy;
import app.Repository.AuthRepository.Password.UpdateUserPassword;
import app.Repository.LoginRepository.CheckUserInDB;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

/*
     This Section Checks the Username and Validate how to proceed after the first Login

     If pending -> first setup
     If active -> display Menu based on Department & Job
     If waiting_for_password_change & user_job = admin or local_admin -> the password must be changed to unlock these accounts

*/

public class LoginVerification {
    private int RETRYS = 0;
    private int RETRYS_MAX = 5;
    private int RETRYS_FOR_SUSPICOUS = 6;
    private int RETRYS_FOR_QUARANTINE = 25;


    private final CheckUserInDB repository = new CheckUserInDB();

    public enum UserStatus {
        ACTIVE,
        DISABLED,
        PENDING,
        LOCKED,
        ON_QUARANTINE,
        WAITING_FOR_PASSWORD_CHANGE,
        SUSPICIOUS
    };

    private String AccountStatus;


    public CollectLogs loggedUser(String Username, String PWSD, Scanner scanner) {

        try {
            boolean userValid = repository.checkUserInDB(Username);

            if (!userValid) {
                LogManager.log(LogType.USERNAME_NOT_FOUND, "The Username " + Username + " Where not found");
                return new CollectLogs(false, "USERNAME_NOT_FOUND");
            }

            System.out.println("[OK] The User exists continue with PWSD Check");
            boolean passwordOK = repository.checkPWSD(PWSD, Username);

            if (!passwordOK) {

                System.out.println("[INFO] Please Notice if retry >=5 your account will be locked");
                System.out.println("[INFO] If you have 25 Failed Passwords the Accounts will be set to quarantine");

                LogManager.log(LogType.SECURITY_WARN, "The User entered a wrong Password");

                System.out.println("\n[WARNING] Invalid Password detected");

                this.RETRYS++;

                LogManager.log(LogType.SECURITY_WARN, "Failed Passwords: " + this.RETRYS);
                System.out.println("[INFO] Failed Passwords: " + this.RETRYS + "\n");

                CountFailedLoginAttempts count = new CountFailedLoginAttempts();
                int failedAttempts = count.Logs(Username);

                LogManager.log(LogType.SECURITY_WARN, "Failed Passwords in 24 Hours: " + failedAttempts);
                System.out.println("\n[INFO] FAILED PWSD Im 24 Hours: " + failedAttempts + "\n");

                ExecutePWSDPolicy changeStatusTo = new ExecutePWSDPolicy();

                if (failedAttempts >= this.RETRYS_FOR_QUARANTINE) {
                    System.out.println("\n[WARNING] Malicious Activities Recognized you Account will be set to quarantine\n");
                    changeStatusTo.quarantine(Username);
                    return new CollectLogs(false, "Account is on Quarantine");
                } else if (failedAttempts >= this.RETRYS_FOR_SUSPICOUS) {
                    System.out.println("\n[INFO] Due to your current activities your account will be set to suspicious\n");
                    changeStatusTo.suspicious(Username);
                    return new CollectLogs(false, "To many Login Attempts");
                } else if (failedAttempts >= this.RETRYS_MAX) {
                    changeStatusTo.locked(Username);
                    System.out.println("\n[WARNING] To many requests you account will be locked\n");
                    return new CollectLogs(false, "To Many Login Attempts");
                }

                return new CollectLogs(false, "INVALID_PASSWORD");
            }

            String userStatus = repository.checkUserStatus(Username);

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
                    System.out.println("\n[INFO] First Login for a System Account recognized");
                    System.out.println("[INFO] Please change your current password to continue\n");

                    PasswordService update = new PasswordService();
                    update.userPWSD(scanner);

                    String hashedPWSD = update.getHashedPWSD();

                    System.out.println("[INFO] Updating User PWSD");

                    UpdateUserPassword change = new UpdateUserPassword();
                    boolean changeSuccess = change.dbValues(Username, hashedPWSD);

                    if (changeSuccess) {
                        return new CollectLogs(true, "password is changed successfully");
                    } else {
                        return new CollectLogs(false, "something went wrong with the password change");
                    }

                case "suspicious":
                    System.out.println("[INFO] You account is set to suspicious maybe you need to change your password");
                    return new CollectLogs(true, "You account is set to suspicious maybe you need to change your password");
            }

            this.RETRYS = 0;

            return new CollectLogs(true, null);

        } catch (SQLException error) {
            System.out.println("[ERROR] SQL error during login: " + error.getMessage());
            return new CollectLogs(false, "SQL_EXCEPTION");
        }
    }
}
