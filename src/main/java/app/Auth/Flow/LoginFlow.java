package app.Auth.Flow;

import java.util.Scanner;

import app.Auth.Flow.Services.LoginService.SetupCurrentSession;
import app.Auth.Flow.Services.LoginService.CollectLoginValues;
import app.Repository.logsRepository.CollectLogs;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.StoreLogs;

import app.Auth.Flow.Services.LoginService.LoginBehaviour.LoginOutcome;
/*
    Just sub controller to rout to the Login flow and to collect the logs
*/

public class LoginFlow {

    //Current Runtime flow to coordinate Login flow
    public void user(Scanner scanner) {

        //Build Obj to call the Login flow
        SetupCurrentSession run = new SetupCurrentSession();
        CollectLogs store = new CollectLogs();

        while (true) {
            CollectLoginValues login = new CollectLoginValues();
            login.user(scanner);

            //Check if Username & Password is Valid
            String username = login.getEnteredUserName();
            String password = login.getEnteredPWSD();

            StoreLogs result = run.configurateSessionObject(username, password, scanner);

            LoginOutcome outcome = result.outcome();

            boolean authenticated = outcome == LoginOutcome.PERMITTED;

            store.loginAttempts(result.accountName(), authenticated, result.reason());

            switch (outcome) {
                case PERMITTED -> {
                    LogManager.auth(AuthState.SUCCESS, "The login was successful");
                    return;
                }

                case PASSWORD_CHANGED, PENDING_REQUEST -> {
                    return;
                }

                default -> {
                    continue;
                }
            }
        }
    }

}
