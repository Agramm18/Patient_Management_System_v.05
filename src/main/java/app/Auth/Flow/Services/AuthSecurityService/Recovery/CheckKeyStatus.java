package app.Auth.Flow.Services.AuthSecurityService.Recovery;

import app.Repository.AuthRepository.Recovery.FindRecoverableUser;
import org.mindrot.jbcrypt.BCrypt;


import app.Config.LogManager;
import app.Config.LogManager.LogType;


public class CheckKeyStatus {

    public boolean Value(String recoveryKeyUser, String storedHashinDB) {
        LogManager.log(LogType.MESSAGE, "Check if the value matches the key");

        if (BCrypt.checkpw(recoveryKeyUser, storedHashinDB)) {
            LogManager.log(LogType.CONFIG_SUCCESS, "The user now has access to change the password");
            FindRecoverableUser display = new FindRecoverableUser();
            display.systemAccounts();
            return true;
        } else {
            LogManager.log(LogType.CONFIG_FAILED, "The key is wrong, please try again");
            return false;
        }
    }
}
