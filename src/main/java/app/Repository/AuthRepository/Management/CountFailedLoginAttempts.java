package app.Repository.AuthRepository.Management;


import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class CountFailedLoginAttempts {

    public int RETRY_COUNT_24_H;
    private int id;

    public int Logs(String Username) {
        CollectID(Username);
        return CountUser();
    }

    private int CountUser() {

        LogManager.log(LogType.SECURITY_INFO, "Checking Failed PWSD Attempts in 24 hours");
        System.out.println("[INFO] Checking for Failed PWSD in 24 Hours");

        String sql = "SELECT COUNT(*) AS failed_count FROM login_attempts WHERE account_id=? AND failure_reason='INVALID_PASSWORD' AND created_at >= NOW() - INTERVAL 24 HOUR";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setInt(1, this.id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("failed_count");
            } else {
                return 0;
            }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            System.out.println(error.getMessage());
            return 0;
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
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            System.out.println(error.getMessage());
        }
    }

    public int getRETRY_COUNT_24_H() {
        return this.RETRY_COUNT_24_H;
    }
}
