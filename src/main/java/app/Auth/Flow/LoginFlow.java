package app.Auth.Flow;

import java.util.Scanner;

import app.Auth.Flow.Services.LoginService.LoginResult;
import app.Auth.Flow.Services.LoginService.LoginService;
import app.Auth.Flow.Services.LoginService.RoleValidationService;
import app.Repository.logsRepository.LoginLogsRepository;
import app.Repository.LoginRepository.AuthenticationRepository;
import app.Repository.LoginRepository.RoleRepository;

public class LoginFlow {

    public void User(Scanner scanner) {
        System.out.println("\n[INFO] Welcome You can now Login your User\n");

        AuthenticationRepository check = new AuthenticationRepository();
        LoginLogsRepository store = new LoginLogsRepository();

        while (true) {
            LoginService login = new LoginService();
            login.User(scanner);

            //Check if Username & Password is Valid
            String Username = login.getEnteredUserName();
            String PWSD = login.getEnteredPWSD();

            LoginResult result = check.LoggedUser(Username, PWSD);

            //Collect values for logs
            store.LogginAttempt(
                    Username,
                    result.isSuccess(),
                    result.getFailureReason()
            );

            //If all values are valid the role will be checked
            if (result.isSuccess()) {
                System.out.println("[OK] The Login where a success");
                System.out.println("\n[INFO] You will now redirected to the Verification Menu");

                RoleRepository verify = new RoleRepository();

                boolean hasRole = verify.UserRole(Username);

                //If the role is valid the department will be checked
                if (hasRole) {
                    System.out.println("\n[OK] The User have a valid Role");
                    System.out.println("[INFO] Continue with Department Check");
                }
                //If the role is unassigned the user must aks for a role
                else {
                    RoleValidationService get = new RoleValidationService();
                    get.RequestedRole(scanner);
                }

                break;
            } else {
                System.out.println("\n[ERROR] Something went wrong please try again");
            }
        }
    }

}
