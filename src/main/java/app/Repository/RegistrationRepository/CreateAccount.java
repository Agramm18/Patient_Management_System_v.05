package app.Repository.RegistrationRepository;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateAccount {

    public void newAccount(String Username, String Email, String PhoneNumber, String HashedPWSD) {
        System.out.println("\n[INFO] Creating new User Account inside the DB");

        try {
            createUser(Username, Email, PhoneNumber, HashedPWSD);
        } catch (SQLException error) {
            System.out.println("Something went wrong: " + error.getMessage());
        }

    }

    private void createUser(String Username, String Email, String PhoneNumber, String HashedPWSD ) throws SQLException {

        int UserStatus = 3;
        int UserRole = 9;

        String sql = "INSERT INTO accounts (account_name, email, phone_number, password_hash, account_status, user_role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, Username);
            statement.setString(2, Email);
            statement.setString(3, PhoneNumber);
            statement.setString(4, HashedPWSD);
            statement.setInt(5, UserStatus);
            statement.setInt(6, UserRole);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("\n[OK] User Is Created");
                System.out.println("[INFO] Rows effected: " + rows);
            }
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }
}
