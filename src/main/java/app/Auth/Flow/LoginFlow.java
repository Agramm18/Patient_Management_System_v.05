package app.Auth.Flow;

import java.util.Scanner;

import app.Auth.Flow.Services.LoginService.LoginVerification;
import app.Auth.Flow.Services.LoginService.LoginInputCollector;
import app.Repository.logsRepository.CollectLogs;


/*
    Just sub controller to rout to the Login flow and to collect the logs
*/

public class LoginFlow {

    //Current Runtime flow to coordinate Login flow
    public void user(Scanner scanner) {
        System.out.println("\n[INFO] Welcome You can now Login your User\n");

        //Build Obj to call the Login flow
        LoginVerification check = new LoginVerification();
        CollectLogs store = new CollectLogs();

        while (true) {
            LoginInputCollector login = new LoginInputCollector();
            login.user(scanner);

            //Check if Username & Password is Valid
            String Username = login.getEnteredUserName();
            String PWSD = login.getEnteredPWSD();

            app.Auth.Flow.Services.AuthSecurityService.Audit.CollectLogs result = check.loggedUser(Username, PWSD, scanner);

            //Collect values for logs
            store.loginAttempts(
                    Username,
                    result.isSuccess(),
                    result.getFailureReason()
            );
            
            if (result.isSuccess()) {
                return;
            }
        }
    }

}
