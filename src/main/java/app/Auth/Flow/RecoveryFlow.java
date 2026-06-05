package app.Auth.Flow;

import java.util.Scanner;

import app.Auth.Flow.Services.AuthSecurityService.Recovery.*;
import app.Auth.Flow.Services.AuthSecurityService.Recovery.SelectUserForRecovery;
import app.Auth.Flow.Services.AuthSecurityService.Recovery.ValidateRecoveryKey;
import app.Auth.Flow.Services.PasswordService.PasswordService;
import app.Repository.AuthRepository.Recovery.GetRecoveryKeyHash;
import app.Repository.AuthRepository.Recovery.SelectUserForRecover;
import app.Repository.AuthRepository.Password.UpdateSystemAccountPassword;

public class RecoveryFlow {

    public void SystemAccounts(Scanner scanner) {
        System.out.println("[INFO] You can now Reset your Password");
        ValidateRecoveryKey validate = new ValidateRecoveryKey();
        validate.keyValues(scanner);


        String recoveryKeyUser = validate.getEnteredHashByUser();

        GetRecoveryKeyHash collect = new GetRecoveryKeyHash();
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

            UpdateSystemAccountPassword run = new UpdateSystemAccountPassword();
            run.sqlQuerry(Username, password);

        }
    }

}
