package app.Repository.AuthRepository.Password;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ExecutePWSDPolicy {


    public void locked(String Username) {
        String sql = "UPDATE accounts SET account_name = ? WHERE account_status = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);
            stmt.setInt(2, 4);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Account is locked");
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

    public void quarantine(String Username) {
        String sql = "UPDATE accounts SET account_name = ? WHERE account_status = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);
            stmt.setInt(2, 5);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Account is locked");
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

    public void suspicious(String Username) {
        String sql = "UPDATE accounts SET account_name = ? WHERE account_status = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);
            stmt.setInt(2, 7);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Account is locked");
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }
}
