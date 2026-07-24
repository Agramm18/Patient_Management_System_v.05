package app.Auth.Flow.Services.PasswordService.PolicieBehaviour;

public enum PolicyThreshold {
    DEFAULT_RETRIES(0),

    RETRIES_MAX(5),
    RETRIES_FOR_SUSPICIOUS(6),
    RETRIES_FOR_QUARANTINE(25);

    private final int retryCount;

    PolicyThreshold(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getRetryCount() {
        return retryCount;
    }
}
