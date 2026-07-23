package app.Auth.Flow.Services.LoginService;

import app.Auth.Flow.Services.PasswordService.CallPasswordPolicyRules;
import app.Repository.LoginRepository.CheckUserInDB;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import java.sql.SQLException;
import java.util.Scanner;

/*
     This Section Checks the Username and Validate how to proceed after the first Login

     If pending -> first setup
     If active -> display Menu based on Department & Job
     If waiting_for_password_change & user_job = admin or local_admin -> the password must be changed to unlock these accounts

*/

public class SetupCurrentSession {

    private final CheckUserInDB repository = new CheckUserInDB();

    public LogsForDB configurateSessionObject(String username, String password, Scanner scanner) {

        try {
            LogManager.auth(AuthState.INFO, "Starting the first Account Setup");
            boolean userExists = checkAccount(username);

            if (!userExists) {
                LogManager.auth(AuthState.ERROR, "Unknown Username");
                return new LogsForDB(username, false, "USERNAME_NOT_FOUND");
            }

            LogManager.auth(AuthState.INFO, "Continue with the Password Check");
            boolean passwordMatchesWithDB = checkPassword(password, username);

            if (!passwordMatchesWithDB) {
                LogManager.auth(AuthState.ERROR, "The Password does not match with the DB entry");

                CallPasswordPolicyRules call = new CallPasswordPolicyRules();
                return call.passwordPolicies(username);
            }

            LogManager.auth(AuthState.INFO, "Continuing with the Account status check");

            String status = checkAccountStatus(username);

            if (status == null) {
                LogManager.auth(AuthState.INFO, "Unknown Account Status");
                return new LogsForDB(username, false, "UNKNOWN_ACCOUNT-STATUS");
            }

            HandleAccountStatusTasks run = new HandleAccountStatusTasks();
            return run.accountStatusBehaviour(status, username, scanner);

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            LogManager.sql(SqlState.ERROR, error.getMessage());
            return new LogsForDB(username, false, "SQL Exception");
        } catch (IllegalStateException error) {
            System.out.println(error.getMessage());
            LogManager.auth(AuthState.ERROR, error.getMessage());
            return new LogsForDB(username, false, error.getMessage());
        }
    }

    //Check if the User exists in the DB
    boolean checkAccount(String username) throws SQLException {
        CheckUserInDB check = new CheckUserInDB();
        return check.checkUserInDB(username);
    }

    //Check if the entered password matches with the password in the db
    boolean checkPassword(String password, String username) throws SQLException {
        CheckUserInDB check = new CheckUserInDB();
        return check.checkPWSD(password, username);
    }

    //Check the Account status
    String checkAccountStatus(String username) throws SQLException {
        CheckUserInDB check = new CheckUserInDB();
        return check.checkUserStatus(username);
    }
}