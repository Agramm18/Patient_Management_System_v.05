package app.Config;
import app.Config.DBManager;

import java.sql.*;

public class SystemAccountValidationService {

    private boolean LocalAdminExsist;
    private boolean adminExists;

    //Method to check if a Local Admin or DB Admin Exsist
    public boolean DBAccounts() {
        System.out.println("\n[INFO] Checking if a Admin or a Local Admin exsists in the DB");
        System.out.println("[INFO] If none of the roles exsists a default LocalAdmin will be added");
        System.out.println("[INFO] If either one of these exsists the other default role will be added");

        CheckLocalAdmin();
        CheckAdmin();

        return ValidAteResults();
    }

    public void CheckLocalAdmin() {
        System.out.println("\n[INFO] Checking for an Local Admin in the DB");

        String sql = "SELECT ID FROM accounts WHERE user_role = ? LIMIT 1";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "local_admin");

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                System.out.println("[OK] Local Admin exsist");
                this.LocalAdminExsist = true;

            } else {
                System.out.println("[WARING] No Local Admin exsist in the DB");
                this.LocalAdminExsist = false;
            }

        } catch (SQLException error) {
            System.out.println("[ERROR] Failed Local Admin check");
            System.out.println(error.getMessage());
        }
    }

    public void CheckAdmin() {
        System.out.println("\n[INFO] Checking for an Admin in the DB");

        String sql = "SELECT ID FROM accounts WHERE user_role = ? LIMIT 1";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "admin");

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                System.out.println("[OK] Admin where found in the DB");
                this.adminExists = true;
            } else {
                System.out.println("[WARNING] No Admin Account exsists in the DB");
                this.adminExists = false;
            }

        } catch (SQLException error) {
            System.out.println("[ERROR] Failed Admin check");
            System.out.println(error.getMessage());
        }
    }

    public boolean ValidAteResults() {

        SetDefaultAccounts create = new SetDefaultAccounts();

        if (this.adminExists && this.LocalAdminExsist) {
            System.out.println("[OK] Both starter Accounts exsists in the DB");
            return true;
        }

        else if (this.adminExists) {
            System.out.println("\n[INFO] The Default Local Admin will be created");
            return create.DefaultLocalAdmin();

        } else if (this.LocalAdminExsist) {
            System.out.println("\n[INFO] The Default Admin will be created");
            return create.DefaultAdmin();
        } else {
            System.out.println("\n[INFO] Both the Local Admin and Admin will be created");

            boolean localCreated = create.DefaultLocalAdmin();
            boolean adminCreated = create.DefaultAdmin();

            return localCreated && adminCreated;
        }
    }
}
