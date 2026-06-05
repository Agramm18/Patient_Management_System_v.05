package app.Repository.AuthRepository.Recovery;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class SelectUserForRecover {

    public boolean inDB(String Username) {

        String sql = "SELECT * FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        )  {
                stmt.setString(1, Username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\n[OK] The Username exist and can be changed\n");
                        return true;
                    } else {
                        System.out.println("[ERROR] The Username does not exsit in the DB");
                        return false;
                    }
                }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            return false;
        }
    }

}
