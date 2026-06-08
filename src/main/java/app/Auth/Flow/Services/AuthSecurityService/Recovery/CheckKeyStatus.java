package app.Auth.Flow.Services.AuthSecurityService.Recovery;

import app.Repository.AuthRepository.Recovery.FindRecoverableUser;
import org.mindrot.jbcrypt.BCrypt;


import app.Config.LogManager;
import app.Config.LogManager.LogType;


public class CheckKeyStatus {

    public boolean Value(String recoveryKeyUser, String storedHashinDB) {
        System.out.println("[INFO] Check if the entered value matches the Recovery Key");
        LogManager.log(LogType.SECURITY_INFO, "Check if the value matches the key");

        if (BCrypt.checkpw(recoveryKeyUser, storedHashinDB)) {
            System.out.println("[OK] The Entered value Matches the Hash in the DB");
            LogManager.log(LogType.RECOVERY_SUCCESS, "Access to the System Recovery granted");
            FindRecoverableUser display = new FindRecoverableUser();
            display.systemAccounts();
            return true;
        } else {
            System.out.println("[ERROR] The Key is wrong please try again");
            LogManager.log(LogType.SECURITY_WARN, "The key is wrong, please try again");
            return false;
        }
    }
}
