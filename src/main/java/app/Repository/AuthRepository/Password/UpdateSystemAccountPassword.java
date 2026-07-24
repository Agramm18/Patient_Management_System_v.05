package app.Repository.AuthRepository.Password;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

public class UpdateSystemAccountPassword {

    public void sqlQuerry(String Username, String password) {

        int changedStatus = 1;

        String sql = "UPDATE accounts SET password_hash = ?, account_status = ? WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, password);
            stmt.setInt(2, changedStatus);
            stmt.setString(3, Username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LogManager.sql(SqlState.SUCCESS, "The Password from the System Account " + Username + " is Updated");
                System.out.println("\n[OK] The Password from the System Account " + Username + " is updated");
                LogManager.sql(SqlState.SUCCESS, "Rows Affected: " + rows);
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
        }
    }

}
