package app.Auth.Flow;
import app.Auth.Flow.Services.RegistrationService.RegistrationService;
import app.Repository.RegistrationRepository.UserAccountRepository;

import java.util.Scanner;

public class RegistrationFlow {

    public void User(Scanner scanner) {
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

    }
}
