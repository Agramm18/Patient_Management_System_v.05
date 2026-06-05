package app.Repository.AuthRepository.Management;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


/*
    In this Section the Code from the User will be checked

    Basend on

    1. Username + ID
*/

public class HasAssignedRole {

    public boolean userRole(String Username) {
        String sqlCheckUser = "SELECT id, user_role FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sqlCheckUser);
        ) {
            stmt.setString(1, Username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userRole = rs.getString("user_role");

                if (userRole.equals("unassigned")) {
                    System.out.println("[INFO] You do not have a role assigned");
                    System.out.println("\n[INFO] Please choose one of the following roles");
                    return false;

                } else {
                    System.out.println("[OK] Your account have a role now we will set the permissions");
                    return true;
                }
            }

            throw new IllegalArgumentException("[ERROR] User was not found");

        } catch (SQLException error ){
            System.out.println(error.getMessage());
            return false;
        } catch (IllegalArgumentException error) {
            System.out.println(error.getMessage());
            return false;
        }
    }
}
