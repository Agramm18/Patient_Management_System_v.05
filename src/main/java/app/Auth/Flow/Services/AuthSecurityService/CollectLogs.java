package app.Auth.Flow.Services.AuthSecurityService;

public class CollectLogs {
    private final boolean success;
    private final String failureReason;

    public CollectLogs(boolean success, String failureReason) {
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
