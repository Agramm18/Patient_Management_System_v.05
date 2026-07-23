package app.Auth.Flow;
import app.Auth.Flow.Services.LoginService.LoginBehaviour.SessionAccount;

public final class CurrentSession {
    private static SessionAccount currentAccount;

    private CurrentSession() {

    }

    public static void setCurrentAccount(SessionAccount account) {
        currentAccount = account;
    }

    public static SessionAccount getCurrentAccount() {
        return currentAccount;
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

    public static void clear() {
        currentAccount = null;
    }
}
