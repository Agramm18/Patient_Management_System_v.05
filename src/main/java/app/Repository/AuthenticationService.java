package app.Repository;

import app.Auth.LoginResult;
import app.Config.DBManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AuthenticationService {

    private int UserRetrys = 0;
    private int UserRetrysMAX = 5;

    public LoginResult LoggedUser(String Username, String PWSD) {

        try {
            boolean userExists = CheckUserInDB(Username);

            if (!userExists) {
                return new LoginResult(false, "USERNAME_NOT_FOUND");
            }

            System.out.println("\n[INFO] Continue with PWSD check");

            boolean passwordIsCorrect = CheckPWSD(PWSD, Username);

            if (!passwordIsCorrect) {
                this.UserRetrys++;
                System.out.println("[WARNING] Invalid Password detected");
                System.out.println("[INFO] Please Notice if retrys >=5 your account will be locked");
                System.out.println("[INFO] Failed Passwords: " + this.UserRetrys);

                if (this.UserRetrys >= this.UserRetrysMAX) {
                    return new LoginResult(false, "TO_MANY_INVALID_PASSWORDS");

                }

                return new LoginResult(false, "INVALID_PASSWORD");
            }

            this.UserRetrys = 0;

            return new LoginResult(true, null);

        } catch (SQLException error) {
            System.out.println("[ERROR] SQL error during login: " + error.getMessage());
            return new LoginResult(false, "SQL_EXCEPTION");
        }
    }

    private boolean CheckUserInDB(String username) throws SQLException {
        System.out.println("\n[INFO] Validate the User");

        String sql = "SELECT id FROM accounts WHERE account_name = ?";

        try (
                Connection connection = DBManager.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("[OK] Username exists in the DB\n");
                    return true;
                }

                System.out.println("[ERROR] This username does not exist\n");
                return false;
            }
        }
    }

    private boolean CheckPWSD(String pwsd, String username) throws SQLException {
        System.out.println("\n[INFO] Checking User password");

        String sql = "SELECT password_hash FROM accounts WHERE account_name = ?";

        try (
                Connection connection = DBManager.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHASH = rs.getString("password_hash");
                    boolean matchesUserInput = BCrypt.checkpw(pwsd, storedHASH);

                    if (matchesUserInput) {
                        System.out.println("[OK] Password is correct\n");
                        return true;
                    }

                    System.out.println("[ERROR] Password is incorrect\n");
                    return false;
                }
                return false;
            }
        }
    }
}