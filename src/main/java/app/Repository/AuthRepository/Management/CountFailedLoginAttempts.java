package app.Repository.AuthRepository.Management;


import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CountFailedLoginAttempts {

    public int RETRY_COUNT_24_H;
    private int id;

    public void Logs(String Username) {
        CollectID(Username);
        CountUser();
    }

    private void CountUser() {

        System.out.println("[INFO] Checking for Failed PWSD in 24 Hours");

        String sql = "SELECT COUNT(*) AS failed_count FROM login_attempts WHERE account_id=? AND failure_reason='INVALID_PASSWORD' AND created_at >= NOW() - INTERVAL 24 HOUR";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setInt(1, this.id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.RETRY_COUNT_24_H = rs.getInt("failed_count");
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }

    }

    private void CollectID (String Username) {

        String sql = "SELECT id FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("id");
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

    public int getRETRY_COUNT_24_H() {
        return this.RETRY_COUNT_24_H;
    }
}
