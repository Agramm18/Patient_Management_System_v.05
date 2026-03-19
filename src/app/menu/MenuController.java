package app.menu;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.ResultSet;

import app.menu.MenuUI.LocalAdminUIScreen;
import app.menu.MenuValidation.LocalAdminValidation;

public class MenuController {
    private String UserName;
    private Connection connection;
    private ResultSet UserRole;

    public MenuController(String UserName, Connection connection, ResultSet UserRole) {
        this.UserName = UserName;
        this.connection = connection;
        this.UserRole = UserRole;
    }

    public void RoleBasedUI(Scanner scanner) {

        try {
            String RoleValue = UserRole.getString("user_role");
            
            if (RoleValue.equals("local_admin")) {
                System.out.println("Welcome Local Admin");

                LocalAdminUIScreen display = new LocalAdminUIScreen();
                display.LocalAdminPanel();

                LocalAdminValidation collect = new LocalAdminValidation(connection, this.UserName);
                collect.AdminChoice(scanner);
            }
        } catch (Exception error) {
            System.out.println("Error" + error.getMessage());
        }
    }
}
