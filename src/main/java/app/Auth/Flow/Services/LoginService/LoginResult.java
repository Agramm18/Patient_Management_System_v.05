package app.Auth.Flow.Services.LoginService;

public class LoginResult {
    private final boolean success;
    private final String failureReason;

    public LoginResult(boolean success, String failureReason) {
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
