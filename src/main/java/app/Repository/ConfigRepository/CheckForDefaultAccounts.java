package app.Repository.ConfigRepository;
import app.CLIText.DisplayMessages.DefaultAccountsMSG;
import app.Config.DBManager;

import java.sql.*;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;


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

    //Method to check if a Local Admin or DB Admin Exist
    public boolean dbAccounts() {

        DefaultAccountsMSG display = new DefaultAccountsMSG();
        display.msg();

        LogManager.config(ConfigState.INFO, "Checking if a Admin or a Local Admin exists in the DB");
        LogManager.config(ConfigState.INFO, "If none of the roles exists a default LocalAdmin will be added");
        LogManager.config(ConfigState.INFO, "If either one of these exists the other default role will be added");

        checkLocalAdmin();
        checkAdmin();

        return validateResults();
    }

    public void checkLocalAdmin() {

        LogManager.sql(SqlState.INFO, "Checking for an Local Admin in the DB");

        int role = 1;

        String sql = "SELECT ID FROM accounts WHERE user_role = ? LIMIT 1";

        try (
                Connection connection = DBManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, role);

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                LogManager.config(ConfigState.SUCCESS, "Local Admin exist");
                this.localAdminExist = true;

            } else {
                LogManager.account(AccountState.USERNAME_NOT_FOUND, "No Local Admin exist in the DB");
                this.localAdminExist = false;
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            LogManager.sql(SqlState.ERROR, error.getMessage());
        }
    }

    public void checkAdmin() {
        LogManager.config(ConfigState.INFO, "Checking for an Admin in the DB");
        int role = 2;
        String sql = "SELECT ID FROM accounts WHERE user_role = ? LIMIT 1";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, role);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                LogManager.config(ConfigState.SUCCESS, "Admin exists in the DB");
                this.adminExists = true;
            } else {
                LogManager.account(AccountState.USERNAME_NOT_FOUND, "No Admin Account exists in the DB");
                this.adminExists = false;
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            LogManager.sql(SqlState.ERROR, error.getMessage());
        }
    }

    public boolean validateResults() {

        CreateDefaultAccounts create = new CreateDefaultAccounts();

        if (this.adminExists && this.localAdminExist) {
            LogManager.config(ConfigState.SUCCESS, "Both starter Accounts exists in the DB");
            return true;
        }

        else if (this.adminExists) {
            LogManager.config(ConfigState.INFO, "The Default Local Admin will be created");
            return create.defaultAccounts(true, false);

        } else if (this.localAdminExist) {
            LogManager.config(ConfigState.INFO, "The Default Admin will be created");
            return create.defaultAccounts(false, true);
        } else {
            LogManager.config(ConfigState.INFO, "Both the Local Admin and Admin will be created");
            return create.defaultAccounts(true, true);
        }
    }
}
