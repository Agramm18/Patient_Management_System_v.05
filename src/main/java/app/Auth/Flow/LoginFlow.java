package app.Auth.Flow;

import java.util.Scanner;

import app.Auth.Flow.Services.LoginService.LoginVerification;
import app.Auth.Flow.Services.LoginService.LoginInputCollector;
import app.Repository.logsRepository.CollectLogs;

public class LoginFlow {

    public void User(Scanner scanner) {
        System.out.println("\n[INFO] Welcome You can now Login your User\n");

        LoginVerification check = new LoginVerification();
        CollectLogs store = new CollectLogs();

        while (true) {
            LoginInputCollector login = new LoginInputCollector();
            login.User(scanner);

            //Check if Username & Password is Valid
            String Username = login.getEnteredUserName();
            String PWSD = login.getEnteredPWSD();

            app.Auth.Flow.Services.AuthSecurityService.CollectLogs result = check.LoggedUser(Username, PWSD, scanner);

            //Collect values for logs
            store.LogginAttempt(
                    Username,
                    result.isSuccess(),
                    result.getFailureReason()
            );
        }
    }

}
