package app.Repository.AuthRepository.Password;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                System.out.println("\n[OK] The Password from the System Account " + Username + " is updated");
                System.out.println("[INFO] Rows affected: " + rows);
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

}
