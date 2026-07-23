package app.Auth.Flow.Services.LoginService;

import app.Auth.Flow.CurrentSession;
import app.Auth.Flow.Services.PasswordService.PasswordService;
import app.Logging.Enums.ProgrammState.AuthState;
import app.Logging.Enums.ProgrammState.SecurityState;
import app.Logging.LogManager;
import app.Repository.AuthRepository.Password.UpdateUserPassword;
import app.Repository.LoginRepository.CollectLoginValues;

import java.util.Scanner;

public class HandleAccountStatusTasks {
    private CurrentSession currentSession;

    //Handle the UserStatus to execute different policies
    public LogsForDB accountStatusBehaviour(String status, String username, Scanner scanner) {
        app.Repository.LoginRepository.CollectLoginValues sessionObject = new CollectLoginValues();

        switch (status) {
            case "active":
                LogManager.auth(AuthState.INFO, "The User Status is active");
                System.out.println("[OK] The User account is active");

                sessionObject.loginValues(username);

                int accountID = sessionObject.getUserID();
                String accountName = sessionObject.getAccount();
                int accountStatus = sessionObject.getUserStatus();
                boolean hasAccessToMenu = sessionObject.gethasAccesToMenu();
                boolean isSystemAccount = sessionObject.isSystemAccount();
                int userRole = sessionObject.getUserRole();

                CurrentAccountInSessionValues sessionValues = new CurrentAccountInSessionValues(accountID, accountName, accountStatus, hasAccessToMenu, isSystemAccount, userRole);

                CurrentSession.setCurrentAccount(sessionValues);

                return new LogsForDB(username, true, "account status is active");

            case "disabled":
                LogManager.auth(AuthState.INFO, "The User Status is disabled");
                System.out.println("[WARNING] This account is Locked an must be activated by an administrator");

                return new LogsForDB(username, false, "account status is disabled");

            case "pending":
                LogManager.auth(AuthState.INFO, "The User Status is pending");
                System.out.println("[INFO] This account is not fully activated");

                FirstLoginFlow run = new FirstLoginFlow();
                run.firstSetup(username, scanner);

                return new LogsForDB(username, true, "account status is pending");

            case "locked":
                LogManager.auth(AuthState.INFO, "The User Status is locked");
                System.out.println("[WARNING] This account is locked and must be activated by an administrator");
                return new LogsForDB(username, false, "account status is locked");

            case "on_quarantine":
                LogManager.auth(AuthState.INFO, "The User Status is on_quarantine");
                System.out.println("[FATAL] This account is on quarantine and must be checked");
                return new LogsForDB(username, false, "account status is on_quarantine");

            case "waiting_for_password_change":
                LogManager.auth(AuthState.INFO, "The User Status is waiting_for_password_change");
                System.out.println("\n[INFO] First Login for a System Account recognized");
                System.out.println("[INFO] Please change your current password to continue\n");

                PasswordService update = new PasswordService();
                update.userPWSD(scanner);

                String hashedPWSD = update.getHashedPWSD();

                System.out.println("[INFO] Updating User PWSD");

                UpdateUserPassword change = new UpdateUserPassword();
                boolean changeSuccess = change.dbValues(username, hashedPWSD);

                if (changeSuccess) {
                    LogManager.security(SecurityState.INFO, "The password was changed successfully");
                };

                return new LogsForDB(username, true, null);

            case "suspicious":
                LogManager.auth(AuthState.INFO, "The User Status is suspicious");
                System.out.println("[INFO] You account is set to suspicious maybe you need to change your password");
                return new LogsForDB(username, false, "account status is suspicious");

            default:
                LogManager.auth(AuthState.ERROR, "Invalid Account status");
                throw new IllegalStateException("[ERROR] Invalid account status detected");
        }
    }
}
