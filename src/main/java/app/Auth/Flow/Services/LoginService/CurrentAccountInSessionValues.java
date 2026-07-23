package app.Auth.Flow.Services.LoginService;

public record CurrentAccountInSessionValues(
        int userID,
        String accountName,
        int accountStatus,
        boolean hasAccessToMenu,
        boolean isSystemAccount,
        int role

) {
}
