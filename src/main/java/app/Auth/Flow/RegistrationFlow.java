package app.Auth.Flow;
import app.Auth.Flow.Services.RegistrationService.RegistrationService;
import app.Repository.RegistrationRepository.CreateAccount;

import java.util.Scanner;

/*
    This Part regulates the Registration Process

    followed things are handed here

    - Set Username
    - Set Email Address
    - Set Phone Number
    - Set Password
*/

public class RegistrationFlow {

    public void user(Scanner scanner) {
        System.out.println("\n[INFO] You can now Register your User");
        RegistrationService register = new RegistrationService();
        register.userAccunt(scanner);

        //Collect Data from getter
        String username = register.getUserName();
        String email = register.getEmailAddress();
        String phoneNumber = register.getPhoneNumber();
        String pwsd = register.getHashedPWSD();

        CreateAccount userAccountRepository = new CreateAccount();
        userAccountRepository.newAccount(username, email, phoneNumber, pwsd); //Collect User Values

    }
}
