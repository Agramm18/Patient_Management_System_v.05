package app.Auth.Flow.Services.PasswordService;

import app.Auth.Flow.Services.LoginService.LoginBehaviour.LoginOutcome;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.StoreLogs;
import app.Logging.Enums.ProgrammState.SecurityState;
import app.Logging.LogManager;
import app.Repository.AuthRepository.Management.CountFailedLoginAttempts;
import app.Repository.AuthRepository.Password.ExecutePWSDPolicy;

public class CallPasswordPolicyRules {

    private int RETRYS = 0;
    private int RETRYS_MAX = 5;
    private int RETRYS_FOR_SUSPICOUS = 6;
    private int RETRYS_FOR_QUARANTINE = 25;

    public StoreLogs passwordPolicies(String username) {
        System.out.println("[INFO] Please Notice if retry >=5 your account will be locked");
        System.out.println("[INFO] If you have 25 Failed Passwords the Accounts will be set to quarantine");

        LogManager.security(SecurityState.WARN, "The User entered a wrong Password");

        System.out.println("\n[WARNING] Invalid Password detected");

        this.RETRYS++;

        LogManager.security(SecurityState.WARN, "Failed Passwords: " + this.RETRYS);
        System.out.println("[INFO] Failed Passwords: " + this.RETRYS + "\n");

        CountFailedLoginAttempts count = new CountFailedLoginAttempts();
        int failedAttempts = count.Logs(username);

        LogManager.security(SecurityState.WARN, "Failed Passwords in 24 Hours: " + failedAttempts);
        System.out.println("\n[INFO] FAILED PWSD Im 24 Hours: " + failedAttempts + "\n");

        ExecutePWSDPolicy changeStatusTo = new ExecutePWSDPolicy();

        if (failedAttempts >= this.RETRYS_FOR_QUARANTINE) {
            System.out.println("\n[WARNING] Malicious Activities Recognized you Account will be set to quarantine\n");
            changeStatusTo.quarantine(username);
            return new StoreLogs(username, LoginOutcome.INVALID_PASSWORD, "INVALID_PASSWORD");

        } else if (failedAttempts >= this.RETRYS_FOR_SUSPICOUS) {
            System.out.println("\n[INFO] Due to your current activities your account will be set to suspicious\n");
            changeStatusTo.suspicious(username);

            return new StoreLogs(username, LoginOutcome.INVALID_PASSWORD, "INVALID_PASSWORD");

        } else if (failedAttempts >= this.RETRYS_MAX) {
            changeStatusTo.locked(username);
            System.out.println("\n[WARNING] To many requests you account will be locked\n");

            return new StoreLogs(username, LoginOutcome.INVALID_PASSWORD, "INVALID_PASSWORD");
        }

        return new StoreLogs(username, LoginOutcome.INVALID_PASSWORD, "INVALID_PASSWORD");
    }
}
