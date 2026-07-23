package app.Auth.Flow.Services.LoginService.LoginBehaviour;

public record SessionAccount(
        int userID,
        String accountName,
        int accountStatus,
        boolean hasAccessToMenu,
        boolean isSystemAccount,
        int role

) {
}
