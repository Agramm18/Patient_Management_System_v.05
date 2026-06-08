package app.Repository.AuthRepository.Password;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class UpdateSystemAccountPassword {

    public void sqlQuerry(String Username, String password) {

        String sql = "UPDATE accounts SET password_hash = ? WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, password);
            stmt.setString(2, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LogManager.log(LogType.SQL_OK, "The Password from the System Account " + Username + " is Updated");
                System.out.println("\n[OK] The Password from the System Account " + Username + " is updated");
                LogManager.log(LogType.SQL_OK, "Rows Affected: " + rows);
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            System.out.println(error.getMessage());
        }
    }

}
