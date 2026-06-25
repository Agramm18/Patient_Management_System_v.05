package app.Auth.Flow.Services.LoginService;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

public class CurrentUser {

    private final String userName;
    private final int userID;
    private final int accountStatus;
    private final boolean hasAccessToMenu;
    private final boolean isSystemAccount;

    private String department;
    private String job;
    private int role;
    private String permission;

    public CurrentUser(String userName, int userID, boolean hasAccessToMenu, int accountStatus, boolean isSystemAccount, int role) {
        this.userName = userName;
        this.userID = userID;
        this.hasAccessToMenu = hasAccessToMenu;
        this.accountStatus = accountStatus;
        this.isSystemAccount = isSystemAccount;
        this.role = role;

    }

    public void loginResult() {
        LogManager.auth(AuthState.INFO, "Created successfully Login Session object");

        LogManager.auth(AuthState.SUCCESS, "Username: " + this.userName);
        LogManager.auth(AuthState.SUCCESS, "Username by ID: " + this.userID);
        LogManager.auth(AuthState.SUCCESS, "Account Status: " + this.accountStatus);
        LogManager.auth(AuthState.SUCCESS, "User has access to Menu: " + this.hasAccessToMenu);
        LogManager.auth(AuthState.SUCCESS, "Is System Account " + this.isSystemAccount);
        LogManager.auth(AuthState.SUCCESS, "User Role: " + this.role);
    }

    public int getUserID() {
        return this.userID;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean isSystemAccount() {
        return isSystemAccount;
    }

     public int getAccountStatus() {
        return this.accountStatus;
     }

     public boolean hasAccessToMenu() {
        return this.hasAccessToMenu;
     }

     public int getRole() {
        return this.role;
     }
}

