package app.Auth.Flow.Services.AuthSecurityService.Recovery;

import app.Repository.AuthRepository.Recovery.FindRecoverableUser;
import org.mindrot.jbcrypt.BCrypt;


import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;


public class CheckKeyStatus {

    public boolean Value(String recoveryKeyUser, String storedHashinDB) {
        System.out.println("[INFO] Check if the entered value matches the Recovery Key");
        LogManager.security(SecurityState.INFO, "Check if the value matches the key");

        if (BCrypt.checkpw(recoveryKeyUser, storedHashinDB)) {
            System.out.println("[OK] The Entered value Matches the Hash in the DB");
            LogManager.recovery(RecoveryState.SUCCESS, "Access to the System Recovery granted");
            FindRecoverableUser display = new FindRecoverableUser();
            display.systemAccounts();
            return true;
        } else {
            System.out.println("[ERROR] The Key is wrong please try again");
            LogManager.security(SecurityState.WARN, "The key is wrong, please try again");
            return false;
        }
    }
}
