package app.Repository.AuthRepository;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UpdateUserPWSD {

    public boolean dbValues (String Username, String hashedPWSD) {
        String sqlUpdateData = "UPDATE accounts SET password_hash = ? WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sqlUpdateData);
        ) {
            stmt.setString(1, hashedPWSD);
            stmt.setString(2, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("\n[OK Password is changed successfully]");
                System.out.println("[INFO] Other the DB Value for requires_password_change will be set to false");
                changePWSDStatus(Username);
                System.out.println("[INFO] Rows affected: " + rows + "\n");
                return true;
            } else {
                System.out.println("[ERROR] Something went wrong please try again");
                return false;
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
        return false;
    }

    private void changePWSDStatus(String Username) {

        String sql = "UPDATE accounts SET account_status = 1, requires_password_change = FALSE WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK DB Values are changed]");
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }
}
