package app.Repository.AuthRepository.Password;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

public class UpdateUserPassword {

    public boolean dbValues (String Username, String hashedPWSD) {
        String sqlUpdateData = "UPDATE accounts SET password_hash = ? WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sqlUpdateData);
        ) {
            stmt.setString(1, hashedPWSD);
            stmt.setString(2, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LogManager.security(SecurityState.SUCCESS, "The password where changed successfully");
                LogManager.auth(AuthState.INFO, "The DB Value for requrires_password_change will be set to false");
                LogManager.sql(SqlState.INFO, "Rows affected: " + rows);

                System.out.println("\n[OK Password is changed successfully]");
                System.out.println("[INFO] The DB Value for requrires_password_change will be set to false");
                changePWSDStatus(Username);
                System.out.println("[INFO] Rows affected: " + rows + "\n");
                return true;
            } else {
                throw new IllegalStateException("[ERROR] The Username where not found in the DB");
            }

        } catch (SQLException error) {
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
            return false;
        } catch (IllegalStateException error) {
            LogManager.account(AccountState.USERNAME_NOT_FOUND, error.getMessage());
            System.out.println(error.getMessage());
        }
        return false;
    }

    private void changePWSDStatus(String Username) {

        String sql = "UPDATE accounts SET account_status = 1, requires_password_change = FALSE, has_access_to_menu=TRUE WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK DB Values are changed]");
            }

        } catch (SQLException error) {
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
        }
    }
}
