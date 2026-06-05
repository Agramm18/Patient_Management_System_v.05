package app.Repository.AuthRepository.Recovery;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class FindRecoverableUser {
    private int id;
    private String username;

    public void systemAccounts() {
        String sql = "SELECT * FROM accounts WHERE is_system_account IS true";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            System.out.println("\n========== Available Accounts ==========\n");
            System.out.println("[id] " + "[username]");

            while (rs.next()) {
                this.id = rs.getInt("id");
                this.username = rs.getString("account_name");
                System.out.println(this.id + " - " + this.username);
            }

            System.out.println("\n");

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

}
