package app.Repository.ConfigRepository;

import app.Config.DBManager;
import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

public class SetRecoveryKey {

    public void keyValue(String recoveryKey) {
        System.out.println("[INFO] Put the Recovery Key into the DB");
        String sql = "INSERT INTO recovery_keys (id, recovery_key_hash) VALUES (1, ?)" +
                "ON DUPLICATE  KEY UPDATE  recovery_key_hash = VALUES(recovery_key_hash)";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, recoveryKey);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] The recovery key where entered successful");
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }
}
