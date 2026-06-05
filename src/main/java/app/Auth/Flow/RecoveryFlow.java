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
    private int RETRY_COUNT = 0;
    private int MAX_RETRYS = 4;

    public void SystemAccounts(Scanner scanner) {
        while (true) {
            try {
                System.out.println("[INFO] You can now Reset your Password");
                ValidateRecoveryKey validate = new ValidateRecoveryKey();
                validate.keyValues(scanner);


                String recoveryKeyUser = validate.getEnteredHashByUser();

                GetRecoveryKeyHash collect = new GetRecoveryKeyHash();
                collect.key();

                String storedHashinDB = collect.getDbValue();

                CheckKeyStatus check = new CheckKeyStatus();
                boolean canChangePassword = check.Value(recoveryKeyUser, storedHashinDB);

                if (canChangePassword) {
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
                    break;
                } else {
                    this.RETRY_COUNT++;
                    System.out.println("[INFO] Total Retrys: " + this.RETRY_COUNT);
                    if (this.RETRY_COUNT >= this.MAX_RETRYS) {
                        throw new IllegalStateException("[ERROR] Retry limit reached please try again");
                    }
                    throw new IllegalArgumentException(("[ERROR] The Key is wrong"));
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            } catch(IllegalStateException error) {
                System.out.println(error.getMessage());
                break;
            }
        }

    }

}
