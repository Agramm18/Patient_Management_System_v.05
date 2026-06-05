package app.Repository.AuthRepository.Recovery;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class SelectUserForRecover {

    public boolean inDB(String Username) {

        String sql = "SELECT * FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        )  {
                stmt.setString(1, Username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        LogManager.log(LogType.RECOVERY_SUCCESS, "The Username exist and can be changed");
                        return true;
                    } else {
                        LogManager.log(LogType.RECOVERY_FAILED, "The Username does not exist in the DB");
                        return false;
                    }
                }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
            return false;
        }
    }

}
