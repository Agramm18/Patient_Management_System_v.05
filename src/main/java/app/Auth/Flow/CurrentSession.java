package app.Auth.Flow;
import app.Auth.Flow.Services.LoginService.CurrentAccountInSessionValues;

public final class CurrentSession {
    private static CurrentAccountInSessionValues currentAccount;

    private CurrentSession() {

    }

    public static void setCurrentAccount(CurrentAccountInSessionValues account) {
        currentAccount = account;
    }

    public static CurrentAccountInSessionValues getCurrentAccount() {
        return currentAccount;
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

    public static void clear() {
        currentAccount = null;
    }
}
