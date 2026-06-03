package app.Auth.Flow;

import java.util.Scanner;

import app.Auth.Flow.Services.AuthSecurityService.CheckKeyStatus;
import app.Auth.Flow.Services.AuthSecurityService.RecoveryCheck;
import app.Auth.Flow.Services.AuthSecurityService.SelectUserForRecovery;
import app.Auth.Flow.Services.PasswordService.PasswordService;
import app.Repository.AuthRepository.CollectRecoveryKey;
import app.Repository.AuthRepository.SelectUserForRecover;
import app.Repository.AuthRepository.UpdateSystemAccount;

public class RecoveryFlow {

    public void SystemAccounts(Scanner scanner) {
        System.out.println("[INFO] You can now Reset your Password");
        RecoveryCheck validate = new RecoveryCheck();
        validate.keyValues(scanner);

        String recoveryKeyUser = validate.getEnteredHashByUser();

        CollectRecoveryKey collect = new CollectRecoveryKey();
        collect.key();

        String storedHashinDB = collect.getDbValue();

        CheckKeyStatus check = new CheckKeyStatus();
        boolean canChangePassword = check.Value(recoveryKeyUser, storedHashinDB);

        SelectUserForRecovery get = new SelectUserForRecovery();
        get.username(scanner);

        String Username = get.getRecoverUsername();

        SelectUserForRecover checkUser = new SelectUserForRecover();

        boolean userExsist = checkUser.inDB(Username);

        if (userExsist) {
            PasswordService update = new PasswordService();
            update.userPWSD(scanner);

            String password = update.getHashedPWSD();

            UpdateSystemAccount run = new UpdateSystemAccount();
            run.sqlQuerry(Username, password);

        }
    }

}
