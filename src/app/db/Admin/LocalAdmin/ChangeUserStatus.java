package app.db.Admin.LocalAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChangeUserStatus {
    private Connection connection;
    private int IDChoice;

    public ChangeUserStatus(Connection connection, int IDChoice) {
        this.connection = connection;
        this.IDChoice = IDChoice;
    }

    public void ChangeStatus() {
        System.out.println("\nThe User Status will be changed\n");

        String querry = "SELECT account_status FROM accounts WHERE id = ?";

        try (PreparedStatement smt = connection.prepareStatement(querry)) {
            smt.setInt(1, this.IDChoice);

            ResultSet rs = smt.executeQuery();

            if (!rs.next()) {
                throw new IllegalArgumentException("The User does not exsist");
            }

            String status = rs.getString("account_status");

            if (!status.equals("enabled")) {
                throw new IllegalArgumentException("The user is deactivated please enable this user to change the role");
            }

            String UpdateQuerry = "UPDATE accounts SET user_role = ? WHERE id = ?";

            try (PreparedStatement updateRows = connection.prepareStatement(UpdateQuerry)) {
                updateRows.setString(1, "admin");
                updateRows.setInt(2, this.IDChoice);

                int rows = updateRows.executeUpdate();

                if (rows > 0) {
                    System.out.println("User role sucsessfully changed");
                } else {
                    throw new IllegalArgumentException("User not found");
                }
            }

        } catch (Exception error) {
            System.out.println("Error " + error.getMessage());
        }
    }
}
