package app.Auth.Flow.Services.LoginService;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

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
        LogManager.log(LogType.AUTH_INFO, "Created successfully Login Session object");

        LogManager.log(LogType.AUTH_SUCCESS, "Username: " + this.userName);
        LogManager.log(LogType.AUTH_SUCCESS, "Username by ID: " + this.userID);
        LogManager.log(LogType.AUTH_SUCCESS, "Account Status: " + this.accountStatus);
        LogManager.log(LogType.AUTH_SUCCESS, "User has access to Menu: " + this.hasAccessToMenu);
        LogManager.log(LogType.AUTH_SUCCESS, "Is System Account " + this.isSystemAccount);
        LogManager.log(LogType.AUTH_SUCCESS, "User Role: " + this.role);
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

