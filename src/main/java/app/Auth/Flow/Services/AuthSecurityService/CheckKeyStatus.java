package app.Auth.Flow.Services.AuthSecurityService;

import app.Repository.AuthRepository.ShowSystemAccounts;
import org.mindrot.jbcrypt.BCrypt;

public class CheckKeyStatus {

    public boolean Value(String recoveryKeyUser, String storedHashinDB) {
        System.out.println("[INFO] Check if the value matches the key");

        if (BCrypt.checkpw(recoveryKeyUser, storedHashinDB)) {
            System.out.println("[OK] The user now has access to change the password");
            ShowSystemAccounts display = new ShowSystemAccounts();
            display.systemAccounts();



            return true;
        } else {
            System.out.println("[ERROR] The key is wrong, please try again");
            return false;
        }
    }
}
