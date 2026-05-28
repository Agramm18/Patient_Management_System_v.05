package app.Config;
import app.Repository.ConfigRepository.CreateDefaultAccounts;

import java.sql.*;


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
        System.out.println("\n[INFO] Checking if a Admin or a Local Admin exists in the DB");
        System.out.println("[INFO] If none of the roles exists a default LocalAdmin will be added");
        System.out.println("[INFO] If either one of these exists the other default role will be added");

        checkLocalAdmin();
        checkAdmin();

        return validateResults();
    }

    public void checkLocalAdmin() {
        System.out.println("\n[INFO] Checking for an Local Admin in the DB");
        int role = 1;

        String sql = "SELECT ID FROM accounts WHERE user_role = ? LIMIT 1";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, role);

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                System.out.println("[OK] Local Admin exist");
                this.localAdminExist = true;

            } else {
                System.out.println("[WARING] No Local Admin exist in the DB");
                this.localAdminExist = false;
            }

        } catch (SQLException error) {
            System.out.println("[ERROR] Failed Local Admin check");
            System.out.println(error.getMessage());
        }
    }

    public void checkAdmin() {
        System.out.println("\n[INFO] Checking for an Admin in the DB");
        int role = 2;
        String sql = "SELECT ID FROM accounts WHERE user_role = ? LIMIT 1";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, role);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                System.out.println("[OK] Admin exists in the DB\n");
                this.adminExists = true;
            } else {
                System.out.println("[WARNING] No Admin Account exists in the DB\n");
                this.adminExists = false;
            }

        } catch (SQLException error) {
            System.out.println("[ERROR] Failed Admin check");
            System.out.println(error.getMessage());
        }
    }

    public boolean validateResults() {

        CreateDefaultAccounts create = new CreateDefaultAccounts();

        if (this.adminExists && this.localAdminExist) {
            System.out.println("[OK] Both starter Accounts exists in the DB");
            return true;
        }

        else if (this.adminExists) {
            System.out.println("\n[INFO] The Default Local Admin will be created");
            return create.defaultAccounts(true, false);

        } else if (this.localAdminExist) {
            System.out.println("\n[INFO] The Default Admin will be created");
            return create.defaultAccounts(false, true);
        } else {
            System.out.println("\n[INFO] Both the Local Admin and Admin will be created");
            return create.defaultAccounts(true, true);
        }
    }
}
