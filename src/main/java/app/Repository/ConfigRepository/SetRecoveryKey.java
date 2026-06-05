package app.Repository.ConfigRepository;

import app.Config.DBManager;
import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class SetRecoveryKey {

    public void keyValue(String recoveryKey) {
        LogManager.log(LogType.MESSAGE, "Put the Recovery Key into the DB");
        String sql = "INSERT INTO recovery_keys (id, recovery_key_hash) VALUES (1, ?)" +
                "ON DUPLICATE  KEY UPDATE  recovery_key_hash = VALUES(recovery_key_hash)";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, recoveryKey);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LogManager.log(LogType.CONFIG_SUCCESS, "he recovery key where entered successful");
                LogManager.log(LogType.SQL_INFO, "Rows affected: " + rows);
            }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
        }
    }
}
