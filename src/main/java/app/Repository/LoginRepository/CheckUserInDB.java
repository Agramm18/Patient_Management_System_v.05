package app.Repository.LoginRepository;

import app.Config.DBManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


/*
    In this Section the User is Checked in the DB

    Following things are current be checked

    1. If the User even exist in the DB
    2. If the Hashed Password matches with the Password in the DB
    3. What the Status is in from the User

*/

public class CheckUserInDB {

    public boolean checkUserInDB(String username) throws SQLException {
        System.out.println("\n[INFO] Validate the User");

        String sql = "SELECT id FROM accounts WHERE account_name = ?";

        try (
                Connection connection = DBManager.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("[OK] Username exists in the DB\n");
                    return true;
                }

                System.out.println("[ERROR] This username does not exist\n");
                return false;
            }
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }

        return false;
    }

    public boolean checkPWSD(String pwsd, String username) throws SQLException {
        System.out.println("\n[INFO] Checking User password");

        String sql = "SELECT password_hash FROM accounts WHERE account_name = ?";

        try (
                Connection connection = DBManager.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHASH = rs.getString("password_hash");
                    boolean matchesUserInput = BCrypt.checkpw(pwsd, storedHASH);

                    if (matchesUserInput) {
                        System.out.println("[OK] Password is correct\n");
                        return true;
                    }

                    System.out.println("[ERROR] Password is incorrect\n");
                    return false;
                }
                return false;
            }
        }
    }

    public String checkUserStatus(String username) throws SQLException {
        System.out.println("[INFO] Checking account status");

        String accountStatus;

        String sql = "SELECT s.status FROM accounts a JOIN account_status s ON a.account_status = s.id WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                accountStatus = rs.getString("status");
                System.out.println("[INFO] Current Account Status: " + accountStatus);
                return accountStatus;
            }

        } catch (SQLException error) {
            System.out.println("[ERROR] Failed to check sql call");
        }

        return null;
    }
}