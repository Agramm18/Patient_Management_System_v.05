package app.Auth.Flow.Services.LoginService;

public record LogsForDB(
        String accountName,
        boolean canUseSystem,
        String reason
) {
}
