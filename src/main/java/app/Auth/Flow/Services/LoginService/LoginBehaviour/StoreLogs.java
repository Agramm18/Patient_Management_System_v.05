package app.Auth.Flow.Services.LoginService.LoginBehaviour;

public record StoreLogs(
        String accountName,
        boolean canUseSystem,
        String reason
) {
}
