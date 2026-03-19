package app.db.Admin.LocalAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Scanner;

public class ShowUsers {
    private Connection connection;
    
    public ShowUsers(Connection connection) {
        this.connection = connection;
    }

    public void showAvailableUsers(Scanner scanner) {
        System.out.println("\nCurrent Users in the DB\n");
        String show = "SELECT id, account_name, user_role FROM accounts";

        try (
            PreparedStatement stmt = connection.prepareStatement(show);
            ResultSet rs = stmt.executeQuery();
        ) {

        System.out.printf("%-5s %-20s %-15s%n", "ID", "NAME", "ROLE");
        System.out.println("--------------------------------------------------");

        while (rs.next()) {
            System.out.printf(
                "%-5d %-20s %-15s%n",
                rs.getInt("id"),
                rs.getString("account_name"),
                rs.getString("user_role")
            );
        }

        GrantAdminPriveleges action = new GrantAdminPriveleges(connection);
        action.SelectUser(scanner);
        

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
