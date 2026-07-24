package app.Auth.Flow.Services.LoginService.LoginBehaviour;

import app.Auth.Flow.Services.LoginService.LoginBehaviour.LoginOutcome;

public record StoreLogs(
        String accountName,
        LoginOutcome outcome,
        String reason
) {
}
