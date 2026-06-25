package app.Repository.ConfigRepository;

import app.Config.DBManager;
import java.sql.*;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

public class SetRecoveryKey {

    public void keyValue(String recoveryKey) {
        LogManager.config(ConfigState.INFO, "Put the Recovery Key into the DB");
        String sql = "INSERT INTO recovery_keys (id, recovery_key_hash) VALUES (1, ?)" +
                "ON DUPLICATE  KEY UPDATE  recovery_key_hash = VALUES(recovery_key_hash)";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, recoveryKey);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LogManager.recovery(RecoveryState.SUCCESS, "The recovery key where entered successful");
                LogManager.sql(SqlState.INFO, "Rows affected: " + rows);
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            LogManager.sql(SqlState.ERROR, error.getMessage());
        }
    }
}
