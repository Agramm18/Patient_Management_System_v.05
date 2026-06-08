package app.Auth.Flow;

import java.util.Scanner;

import app.Auth.Flow.Services.AuthSecurityService.Recovery.*;
import app.Auth.Flow.Services.AuthSecurityService.Recovery.SelectUserForRecovery;
import app.Auth.Flow.Services.AuthSecurityService.Recovery.ValidateRecoveryKey;
import app.Auth.Flow.Services.PasswordService.PasswordService;
import app.Repository.AuthRepository.Recovery.GetRecoveryKeyHash;
import app.Repository.AuthRepository.Recovery.SelectUserForRecover;
import app.Repository.AuthRepository.Password.UpdateSystemAccountPassword;
import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class RecoveryFlow {
    private int RETRY_COUNT = 0;
    private int MAX_RETRYS = 4;

    public void SystemAccounts(Scanner scanner) {
        while (true) {
            try {
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
                    LogManager.log(LogManager.LogType.SECURITY_INFO, "Total Retry: " + this.RETRY_COUNT);
                    if (this.RETRY_COUNT >= this.MAX_RETRYS) {
                        throw new IllegalStateException("Retry limit reached please try again");
                    }
                    throw new IllegalArgumentException(("The Key is wrong"));
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogManager.LogType.INVALID_INPUT, error.getMessage());
            } catch(IllegalStateException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogManager.LogType.SECURITY_WARN, error.getMessage());
                break;
            }
        }

    }

}
