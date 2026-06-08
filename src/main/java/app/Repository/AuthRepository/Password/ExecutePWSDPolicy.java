package app.Repository.AuthRepository.Password;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class ExecutePWSDPolicy {


    public void locked(String Username) {
        String sql = "UPDATE accounts SET account_status = ? WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setInt(1, 4);
            stmt.setString(2, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LogManager.log(LogType.AUTH_INFO, "The Account was locked because of to many password attempts in 24 hours");
                LogManager.log(LogType.SQL_INFO, "Rows affected: " + rows);
                System.out.println("[OK] Account is locked");
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            System.out.println(error.getMessage());
        }
    }

    public void quarantine(String Username) {
        String sql = "UPDATE accounts SET account_status = ? WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setInt(1, 5);
            stmt.setString(2, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LogManager.log(LogType.AUTH_INFO, "The Account was set to quarantine because of to many password attempts in 24 hours");
                LogManager.log(LogType.SQL_INFO, "Rows affected: " + rows);

                System.out.println("[OK] Account is locked");
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            System.out.println(error.getMessage());
        }
    }

    public void suspicious(String Username) {
        String sql = "UPDATE accounts SET account_status = ? WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setInt(1, 7);
            stmt.setString(2, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LogManager.log(LogType.AUTH_INFO, "The Account was set to suspicious because of to many password attempts in 24 hours");
                LogManager.log(LogType.SQL_INFO, "Rows affected: " + rows);

                System.out.println("[OK] Account is locked");
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            System.out.println(error.getMessage());
        }
    }
}
