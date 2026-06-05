package app.Repository.AuthRepository.Management;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class HasAssignedDepartment {

    public boolean status(String Username) {

        String sql = "SELECT * FROM accounts WHERE account_name = ?";

        try(Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int account_job = rs.getInt("department");

                if (account_job == 11 || account_job == 5) {
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
