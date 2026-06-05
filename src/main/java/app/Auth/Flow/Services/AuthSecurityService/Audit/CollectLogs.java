package app.Auth.Flow.Services.AuthSecurityService.Audit;

/*
    This part is to collect the logs for the DB Table login_logs

    It collects if the login was successful and the reason why it was not
*/

public class CollectLogs {
    private final boolean success;
    private final String failureReason;

    public CollectLogs (boolean success, String failureReason) {
        this.success = success;
        this.failureReason = failureReason;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
