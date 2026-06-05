package app.Repository.ConfigRepository;
import app.CLIText.DisplayMessages.DefaultAccountsMSG;
import app.Config.DBManager;

import java.sql.*;

import app.Config.LogManager;
import app.Config.LogManager.LogType;


/*
    This Section Checks if an Admin and an Local Admin Account Exist in the DB

    it checks for the name and role and collects a boolean

    If the Local Admin Account exists and The Admin does not -> A Admin account will be created (false, true)
    If the Admin Account exist but the Local Admin does not -> The Local Admin will be created (true, false)
    If both Accounts does not exist -> Both Accounts will be created (false, false)


*/

public class CheckForDefaultAccounts {

    private boolean localAdminExist;
    private boolean adminExists;

    //Method to check if a Local Admin or DB Admin Exsist
    public boolean dbAccounts() {

        DefaultAccountsMSG display = new DefaultAccountsMSG();
        display.msg();

        LogManager.log(LogType.MESSAGE, "Checking if a Admin or a Local Admin exists in the DB");
        LogManager.log(LogType.MESSAGE, "If none of the roles exists a default LocalAdmin will be added");
        LogManager.log(LogType.MESSAGE, "If either one of these exists the other default role will be added");

        checkLocalAdmin();
        checkAdmin();

        return validateResults();
    }

    public void checkLocalAdmin() {

        LogManager.log(LogType.MESSAGE, "Checking for an Local Admin in the DB");

        int role = 1;

        String sql = "SELECT ID FROM accounts WHERE user_role = ? LIMIT 1";

        try (
                Connection connection = DBManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, role);

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                LogManager.log(LogType.CONFIG_SUCCESS, "Local Admin exist");
                this.localAdminExist = true;

            } else {
                LogManager.log(LogType.CONFIG_FAILED, "No Local Admin exist in the DB");
                this.localAdminExist = false;
            }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
        }
    }

    public void checkAdmin() {
        LogManager.log(LogType.MESSAGE, "Checking for an Admin in the DB");
        int role = 2;
        String sql = "SELECT ID FROM accounts WHERE user_role = ? LIMIT 1";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, role);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                LogManager.log(LogType.CONFIG_SUCCESS, "Admin exists in the DB");
                this.adminExists = true;
            } else {
                LogManager.log(LogType.CONFIG_FAILED, "No Admin Account exists in the DB");
                this.adminExists = false;
            }

        } catch (SQLException error) {
            LogManager.log(LogType.SQL_EXCEPTION, error.getMessage());
        }
    }

    public boolean validateResults() {

        CreateDefaultAccounts create = new CreateDefaultAccounts();

        if (this.adminExists && this.localAdminExist) {
            LogManager.log(LogType.CONFIG_SUCCESS, "Both starter Accounts exists in the DB");
            return true;
        }

        else if (this.adminExists) {
            LogManager.log(LogType.CONFIG_SUCCESS, "The Default Local Admin will be created");
            return create.defaultAccounts(true, false);

        } else if (this.localAdminExist) {
            LogManager.log(LogType.CONFIG_INFO, "The Default Admin will be created");
            return create.defaultAccounts(false, true);
        } else {
            LogManager.log(LogType.CONFIG_INFO, "Both the Local Admin and Admin will be created");
            return create.defaultAccounts(true, true);
        }
    }
}
