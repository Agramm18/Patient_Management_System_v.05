package app.Repository;

import java.sql.SQLException;

import app.Config.DBManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AuthenticationService {

    public boolean LoggedUser(String Username, String PWSD) {

        try {
            boolean UserExists = CheckUserInDB(Username);

            if (UserExists) {
                System.out.println("\n[INFO] Continue with PWSD check");
                boolean PasswordIsCorrect = CheckPWSD(PWSD, Username);

                if (PasswordIsCorrect) {
                    return true;
                }
            }

            return false;

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            return false;
        }
    }

    private boolean CheckUserInDB(String username) throws SQLException {
        System.out.println("\n[INFO] Validate the User");

        String sql = "SELECT * FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("[OK] Username exists in the DB\n");
                return true;

            } else {
                throw new IllegalArgumentException("[ERROR] This username does not exist\n");
            }
        } catch (IllegalArgumentException error) {
            System.out.println(error.getMessage());
            return false;
        }
    }

    private boolean CheckPWSD(String pwsd, String username) throws SQLException {
        System.out.println("\n[INFO] Checking User password");

        String sql = "SELECT password_hash FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHASH = rs.getString("password_hash");
                boolean MatchesUserInput = BCrypt.checkpw(pwsd, storedHASH);

                if (MatchesUserInput) {
                    System.out.println("[OK] Password is correct\n");
                    return true;

                } else {
                    System.out.println("[ERROR] Password is not correct please try again\n");
                    return false;
                }
            }
            return false;
        }
    }
}
