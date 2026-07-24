package app.Auth.Flow.Services.LoginService.LoginBehaviour;

public enum LoginOutcome {
    PERMITTED,
    REJECTED,
    PENDING_REQUEST,
    PASSWORD_CHANGED,
    WAITING_FOR_PASSWORD_CHANGE,
    INVALID_PASSWORD,
    UNKOWN_ACCOUNT_STATUS,
    USERNAME_NOT_FOUNT,
    SQL_EXCEPTION,
    INPUT_ERROR
}
