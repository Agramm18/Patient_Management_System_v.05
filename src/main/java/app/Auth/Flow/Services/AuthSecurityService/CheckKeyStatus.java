package app.Auth.Flow.Services.AuthSecurityService;

public class CheckKeyStatus {

    public boolean Value(String recoveryKeyUser, String storedHashinDB) {
        System.out.println("[INFO] Check if the value matches the key");

        if (recoveryKeyUser.equals(storedHashinDB)) {
            System.out.println("[OK] The User have now the Access to change the password");
            return true;
        } else {
            System.out.println("[ERROR] The Password is wrong please try again");
            return false;
        }]
    }
}
