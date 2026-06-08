package app.Repository.AuthRepository.Password;


import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/*
    In this Section the System Accounts Are Checked

    1. It will be checked if the User Password must be changed or not

*/
public class SystemAccountRequiresPasswordChange {

    public boolean checkUserStatus(String Username) {
        System.out.println("[INFO] Checking if the user must change his password");

        String sql = "SELECT * FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean requiresChange = rs.getBoolean("requires_password_change");

                if (requiresChange) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
        return false;
    }
}
