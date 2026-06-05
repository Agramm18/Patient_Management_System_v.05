package app.Repository.AuthRepository.Recovery;

import app.Config.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class GetRecoveryKeyHash {

    private String dbValue;

    public void key() {
        String sql = "SELECT recovery_key_hash FROM recovery_keys WHERE id = 1";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ) {
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    this.dbValue = rs.getString("recovery_key_hash");
                }
            } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

    public String getDbValue() {
        return this.dbValue;
    }
}
