package app.Auth.Flow;
import app.Auth.Flow.Services.LoginService.CurrentUser;

public class CurrentSession {
    private  static CurrentUser currentUser;

    public static void setCurrentUser(CurrentUser user) {
        currentUser = user;
    }

    public static CurrentUser getCurrentUser() {
        return currentUser;
    }
}
