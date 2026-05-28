package app.Repository.logsRepository;

import app.Config.DBManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;


/*
    This Section handles the DB Action for the Login logs

    following things are saved right now

    1. Who tried to log in (Username)
    2. Was the Login a Success
    3. If not failure reason
*/

public class CollectLogs {

    public void loginAttempts(String Username, boolean isSuccess, String failureReason) {

        String username = Username;

        boolean IsSuccess = isSuccess;
        String FailureReason = failureReason;

        System.out.println("[INFO] Entered data will be saved in the LOGS");

        String sqlGetUserID = "SELECT id FROM accounts WHERE account_name = ?";
        String sqlInserData = "INSERT INTO login_attempts(account_id, entered_username, failure_reason, is_success) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmtGetUserID = connection.prepareStatement(sqlGetUserID);
            PreparedStatement stmtInsertData = connection.prepareStatement(sqlInserData);
        ) {
            stmtGetUserID.setString(1, Username);
            Integer accountID = null;

            try (ResultSet rs = stmtGetUserID.executeQuery()) {
                if (rs.next()) {
                    accountID = rs.getInt("id");
                }
            }

            stmtInsertData.setObject(1, accountID);
            stmtInsertData.setString(2, username);
            stmtInsertData.setString(3, failureReason);
            stmtInsertData.setBoolean(4, isSuccess);

            int rows = stmtInsertData.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Login attempt was saved");
            } else {
                System.out.println("[WARNING] Login attempt wsa not saved");
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

}
