package app.Repository.RegistrationRepository;

import app.Config.DBManager;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



/*
    In this Section the Account Creation is Handled

    Following things are current implemented

    1. Username
    2. Email
    2. Phone Number
    3. Hashed PWSD
    4. User Status
    5. User Role
    6. Department

    A Default Account is created with the Following Default States

    1. Role -> Intern
    2. Department -> Unassigned
    3. User Status -> Pending
    5. User Permission -> Read Only

*/

public class CreateAccount {

    public void newAccount(String Username, String Email, String PhoneNumber, String HashedPWSD) {
        System.out.println("\n[INFO] Creating new User Account inside the DB");

        try {
            createUser(Username, Email, PhoneNumber, HashedPWSD);
        } catch (SQLException error) {
            System.out.println("Something went wrong: " + error.getMessage());
            LogManager.sql(SqlState.ERROR, error.getMessage());
        }

    }

    private void createUser(String Username, String Email, String PhoneNumber, String HashedPWSD ) throws SQLException {

        int UserStatus = 3;
        int UserRole = 9;
        int Department = 12;
        boolean has_access_to_menu = false;
        boolean is_system_account = false;

        String sql = "INSERT INTO accounts (account_name, email, phone_number, password_hash, account_status, user_role, department, has_access_to_menu, is_system_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, Username);
            statement.setString(2, Email);
            statement.setString(3, PhoneNumber);
            statement.setString(4, HashedPWSD);
            statement.setInt(5, UserStatus);
            statement.setInt(6, UserRole);
            statement.setInt(7, Department);
            statement.setBoolean(8, has_access_to_menu);
            statement.setBoolean(9, is_system_account);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("\n[OK] User Is Created");
                LogManager.auth(AuthState.SUCCESS, "User is created");
                System.out.println("[INFO] Rows effected: " + rows);
                LogManager.sql(SqlState.INFO, "rows effected: " + rows);
            }
        } catch (SQLException error) {
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
        }
    }
}
