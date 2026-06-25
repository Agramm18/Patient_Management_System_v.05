package app.Repository.LoginRepository;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

public class CollectLoginValues {

    private int userID;
    private int userStatus;
    private boolean hasAccessToMenu;
    private boolean isSystemAccount;
    private int userRole;

    public void loginValues(String Username) {
        String sql = "SELECT * FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, Username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.userID = rs.getInt("id");
                this.hasAccessToMenu = rs.getBoolean("has_access_to_menu");
                this.userStatus = rs.getInt("account_status");
                this.isSystemAccount = rs.getBoolean("is_system_account");
                this.userRole = rs.getInt("user_role");
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            LogManager.sql(SqlState.ERROR, error.getMessage());
        }
    }

    public boolean gethasAccesToMenu() {
        return this.hasAccessToMenu;
    }

    public int getUserStatus() {
        return this.userStatus;
    }

    public int getUserID() {
        return this.userID;
    }

    public boolean isSystemAccount() {
        return isSystemAccount;
    }

    public int getUserRole() {
        return this.userRole;
    }
}
