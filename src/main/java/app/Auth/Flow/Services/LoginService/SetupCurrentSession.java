package app.Auth.Flow.Services.LoginService;

import app.Auth.Flow.Services.LoginService.LoginBehaviour.HandleAccountStatus;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.LoginOutcome;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.StoreLogs;
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

    public StoreLogs configurateSessionObject(String username, String password, Scanner scanner) {

        CheckInput check = new CheckInput();

        try {
            LogManager.auth(AuthState.INFO, "Starting the first Account Setup");
            boolean userExists = check.account(username);

            if (!userExists) {
                LogManager.auth(AuthState.ERROR, "Unknown Username");
                return new StoreLogs(username, LoginOutcome.USERNAME_NOT_FOUNT, "USERNAME_NOT_FOUND");
            }

            LogManager.auth(AuthState.INFO, "Continue with the Password Check");
            boolean passwordMatchesWithDB = check.password(password, username);

            if (!passwordMatchesWithDB) {
                LogManager.auth(AuthState.ERROR, "The Password does not match with the DB entry");

                CallPasswordPolicyRules call = new CallPasswordPolicyRules();
                return call.passwordPolicies(username);
            }

            LogManager.auth(AuthState.INFO, "Continuing with the Account status check");

            String status = check.status(username);

            if (status == null) {
                LogManager.auth(AuthState.INFO, "Unknown Account Status");
                return new StoreLogs(username, LoginOutcome.USERNAME_NOT_FOUNT, "UNKNOWN_ACCOUNT-STATUS");
            }

            HandleAccountStatus run = new HandleAccountStatus();
            return run.accountStatusBehaviour(status, username, scanner);

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            LogManager.sql(SqlState.ERROR, error.getMessage());
            return new StoreLogs(username, LoginOutcome.SQL_EXCEPTION, "SQL Exception");
        } catch (IllegalStateException error) {
            System.out.println(error.getMessage());
            LogManager.auth(AuthState.ERROR, error.getMessage());
            return new StoreLogs(username, LoginOutcome.INPUT_ERROR, error.getMessage());
        }
    }
}