package app.Controller;

import app.Auth.AccountPolicyService;
import app.Auth.LoginService;
import app.Auth.RegistrationService;
import app.Repository.AccountRepository;
import app.Repository.AuthenticationService;
import app.Repository.UserAccountRepository;
import app.Auth.LoginResult;

import java.util.Scanner;

public class AuthController {

    public void VerifyAccountStatus(Scanner scanner) {
        String AccountStatusSTR;

        System.out.println("\n[INFO] Verify the user");
        System.out.println("[INFO] Running through the login & registration process\n");

        while (true) {
            try {
                System.out.println("\n[INFO] Please select one of the following Options\n");
                System.out.println("[1] Registration");
                System.out.println("[2] Login");
                System.out.println("[3] Exit the Program\n");

                AccountStatusSTR = scanner.nextLine().trim().toLowerCase();

                if (AccountStatusSTR.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] Please type in something to continue");
                } else {
                    int UserValue = Integer.parseInt(AccountStatusSTR);

                    if (UserValue < 1 || UserValue > 3) {
                        throw new IllegalArgumentException("[ERROR] The value can't be less than 1 or higher than 4");
                    } else if (UserValue == 1) {
                        System.out.println("\n[INFO] You can now Register your User");
                        RegistrationService register = new RegistrationService();
                        register.UserAccount(scanner);

                        //Collect Data from getter
                        String Username = register.getUserName();
                        String Email = register.getEmailAddress();
                        String PhoneNumber = register.getPhoneNumber();
                        String HashedPWSD = register.getHashedPWSD();

                        UserAccountRepository userAccountRepository = new UserAccountRepository();
                        userAccountRepository.newAccount(Username, Email, PhoneNumber, HashedPWSD);

                        return;

                    } else if (UserValue == 2) {
                        System.out.println("\n[INFO] Welcome You can now Login your User\n");

                        AuthenticationService check = new AuthenticationService();
                        AccountRepository store = new AccountRepository();

                        while (true) {
                            LoginService login = new LoginService();
                            login.User(scanner);

                            String Username = login.getEnteredUserName();
                            String PWSD = login.getEnteredPWSD();

                            LoginResult result = check.LoggedUser(Username, PWSD);

                            store.LogginAttempt(
                                    Username,
                                    result.isSuccess(),
                                    result.getFailureReason()
                            );

                            if (result.isSuccess()) {
                                System.out.println("[OK] The Login where a success");

                                return;
                            } else {
                                System.out.println("\n[ERROR] Something went wrong please try again");
                            }
                        }

                    } else {
                        System.out.println("\n[INFO] You have chosen to end this program");
                        System.out.println("[INFO] Have a nice day and Good Bye!!\n");
                        System.exit(0);
                        break;
                    }
                }

            } catch (NumberFormatException numberError) {
                System.out.println("[ERROR] Please type in a valid number");
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }
}
