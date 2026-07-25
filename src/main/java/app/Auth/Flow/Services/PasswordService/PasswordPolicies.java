package app.Auth.Flow.Services.PasswordService;

import app.Auth.Flow.Services.LoginService.LoginBehaviour.LoginOutcome;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.StoreLogs;
import app.Auth.Flow.Services.PasswordService.PolicieBehaviour.PolicyThreshold;
import app.Auth.Flow.Services.PasswordService.PolicieBehaviour.TimePeriod;
import app.Logging.Enums.ProgrammState.SecurityState;
import app.Logging.LogManager;
import app.Repository.AuthRepository.Management.CountFailedLoginAttempts;
import app.Repository.AuthRepository.Management.PolicieThresholdStructure;
import app.Repository.AuthRepository.Password.ExecutePWSDPolicy;

import java.time.LocalDateTime;

public class PasswordPolicies {

    private int RETRY_COUNT = 0;

    public StoreLogs passwordPolicies(String username) {

        LogManager.security(SecurityState.INFO, "Running Password Police Logic");

        LogManager.security(SecurityState.WARN, "Invalid Password Detected");
        System.out.println("[ERROR] Invalid Password Detected");

        this.RETRY_COUNT++;

        LogManager.security(SecurityState.WARN, "Failed logins in this session: " + this.RETRY_COUNT);
        System.out.println("[ERROR] Failed logins in this session: " + this.RETRY_COUNT);

        //Collect the record values
        CountFailedLoginAttempts counter = new CountFailedLoginAttempts();
        PolicieThresholdStructure failureCount = counter.failedEntries(username).includingAttempt();

        ExecutePWSDPolicy statusChanger = new ExecutePWSDPolicy();

        if (isReached(failureCount, PolicyThreshold.RETRIES_FOR_QUARANTINE)) {
            System.out.println("[WARNING] Account will be quarantined");
            LogManager.security(SecurityState.WARN, "The account was set on quarantine due to the police rules");
            statusChanger.quarantine(username);
        } else if (isReached(failureCount, PolicyThreshold.RETRIES_FOR_SUSPICIOUS)) {
            System.out.println("[WARNING] Account will be set on suspicious");
            LogManager.security(SecurityState.WARN, "The account was set on suspicious du to the police rules");
            statusChanger.suspicious(username);
        } else if (isReached(failureCount, PolicyThreshold.RETRIES_MAX)) {
            System.out.println("[WARNING] Account will be locked");
            LogManager.security(SecurityState.WARN, "The account was locked due to the police rules");
            statusChanger.locked(username);
        }

        return new StoreLogs(username, LoginOutcome.INVALID_PASSWORD, "INVALID_PASSWORD");
    }

    private boolean isReached(PolicieThresholdStructure counts, PolicyThreshold threshold) {
        for (TimePeriod period : TimePeriod.values()) {
            int actualCount = counts.countFor(period);
            int thresholdValue = period.calculateThreshold(threshold);

            if (actualCount >= thresholdValue) {
                return true;
            }
        }

        return false;
    }
}
