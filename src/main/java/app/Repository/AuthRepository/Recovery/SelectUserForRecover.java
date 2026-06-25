package app.Repository.AuthRepository.Recovery;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

public class SelectUserForRecover {

    public boolean inDB(String Username) {

        String sql = "SELECT * FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        )  {
                stmt.setString(1, Username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        LogManager.recovery(RecoveryState.SUCCESS, "The Username exist and can be changed");
                        return true;
                    } else {
                        LogManager.recovery(RecoveryState.FAILED, "The Username does not exist in the DB");
                        return false;
                    }
                }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            LogManager.sql(SqlState.ERROR, error.getMessage());
            return false;
        }
    }

}
