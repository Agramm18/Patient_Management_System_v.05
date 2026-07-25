package app.Repository.AuthRepository.Management;


import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;
import app.Repository.AuthRepository.Management.PolicieThresholdStructure;

public class CountFailedLoginAttempts {

    public int RETRY_COUNT_24_H;
    private int id;

    public PolicieThresholdStructure failedEntries(String Username) {

        CollectID(Username);

        return new PolicieThresholdStructure(
                countDay(),
                countWeek(),
                countMonth(),
                countYear(),
                countFiveYears(),
                countTenYears()
        );
    }

    private int countDay() {

        LogManager.security(SecurityState.INFO, "Checking failed password attempts in 24 hours");
        System.out.println("[INFO] Checking failed password attempts in 24 hours");

        String sql = "SELECT COUNT(*) AS failed_count FROM login_attempts WHERE account_id=? AND failure_reason='INVALID_PASSWORD' AND created_at >= CURDATE()";

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
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
            return 0;
        }
    }

    private int countWeek() {
        LogManager.security(SecurityState.INFO, "Checking failed password attempts in the last week");
        System.out.println("[INFO] Checking failed password attempts in the last week");

        String sql = "SELECT COUNT(*) AS failed_count FROM login_attempts WHERE account_id=? AND failure_reason='INVALID_PASSWORD' AND created_at >= CURDATE() - INTERVAL 7 DAY";

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
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
            return 0;
        }
    }

    private int countMonth() {
        LogManager.security(SecurityState.INFO, "Checking failed password attempts in the last month");
        System.out.println("[INFO] Checking failed password attempts in the last month");

        String sql = "SELECT COUNT(*) AS failed_count FROM login_attempts WHERE account_id=? AND failure_reason='INVALID_PASSWORD' AND YEAR(created_at) = YEAR(CURDATE()) AND MONTH(created_at) = MONTH((CURDATE()))";

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
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
            return 0;
        }
    }

    private int countYear() {
        LogManager.security(SecurityState.INFO, "Checking for failed passwords from now till one year");
        System.out.println("[INFO] Checking for failed passwords from now till one year");

        String sql = "SELECT COUNT(*) AS failed_count FROM login_attempts WHERE account_id=? AND failure_reason='INVALID_PASSWORD' AND created_at >= NOW() - INTERVAL 365 DAY";

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
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
            return 0;
        }
    }

    private int countFiveYears() {
        LogManager.security(SecurityState.INFO, "Checking failed passwords from now till the last 5 years");
        System.out.println("[INFO] Checking failed passwords from now till the last 5 years");

        String sql = "SELECT COUNT(*) AS failed_count FROM login_attempts WHERE account_id=? AND failure_reason='INVALID_PASSWORD' AND created_at >= CURDATE() - INTERVAL 5 YEAR";

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
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
            return 0;
        }
    }

    private int countTenYears() {
        LogManager.security(SecurityState.INFO, "Checking failed passwords from now till the last 10 years");
        System.out.println("[INFO] Checking failed passwords from now till the last 10 years");

        String sql = "SELECT COUNT(*) AS failed_count FROM login_attempts WHERE account_id=? AND failure_reason='INVALID_PASSWORD' AND created_at >= CURDATE() - INTERVAL 10 YEAR";

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
            LogManager.sql(SqlState.ERROR, error.getMessage());
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
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
        }
    }

    public int getRETRY_COUNT_24_H() {
        return this.RETRY_COUNT_24_H;
    }
}
