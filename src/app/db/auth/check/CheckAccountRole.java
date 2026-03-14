package app.db.auth.check;

//Libary imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import app.menu.MenuController;
import app.menu.MenuUI.LocalAdminUIScreen;

public class CheckAccountRole {
    private String UserName;
    private Connection connection;

    public CheckAccountRole(String UserName, Connection connection) {
        this.UserName = UserName;
        this.connection = connection;
    }

    public void AccountRole(Scanner scanner) {
        
        try {
            String GetRole = "SELECT user_role FROM accounts WHERE account_name = ?";
            PreparedStatement CollectRole = connection.prepareStatement(GetRole);
            CollectRole.setString(1, this.UserName);

            ResultSet UserRole = CollectRole.executeQuery();

            if (UserRole.next()) {

                MenuController show = new MenuController(
                    this.UserName,
                    this.connection,
                    UserRole
                );

                show.RoleBasedUI(scanner);
            }
        } catch (Exception error) {
            System.out.println("Error" + error.getMessage());
        }

    }
}
