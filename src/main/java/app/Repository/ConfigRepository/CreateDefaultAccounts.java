package app.Repository.ConfigRepository;

import app.Config.DBManager;
import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

/*
    In this Section the default Accounts for the Local Admin and Admin are created

    1. Local Admin Values

    1. Department: System
    2. Status: waiting_for_password_change
    3. role: local_admin
    4. permission: root_access

    2. Admin Values

    1. Department: IT
    2. Status: waiting_for_password_change
    3. role: admin
    4. permission: admin_rights

    This Accounts where created as default Accounts with the values form the .env
*/


public class CreateDefaultAccounts {
    private final Dotenv dotenv = Dotenv.load();

    public boolean defaultAccounts(boolean CreateDefaultLocalAdmin, boolean CreateDefaultAdmin) {
        boolean localAdminCreated = true;
        boolean adminCreated = true;

        if (CreateDefaultLocalAdmin) {
            localAdminCreated = createLocalDefaultAdmin();
        }

        if (CreateDefaultAdmin) {
            adminCreated = createDefaultAdmin();
        }

        return adminCreated && localAdminCreated;
    }

    private boolean createLocalDefaultAdmin() {

        System.out.println("\n[INFO] Creating Local Admin");

        String LocalAdminName = dotenv.get("LOCAL_ADMIN_NAME");
        String LocalAdminPWSD = dotenv.get("LOCAL_ADMIN_PWSD");
        String LocalAdminEmail = dotenv.get("LOCAL_ADMIN_EMAIL");
        String BootstrapKey = dotenv.get("BOOTSTRAP_KEY");

        String HashedLocalPWSD;
        int role = 1;
        int status = 6;
        String job = "system_administrator";
        String permission = "root_access";
        boolean needs_change = true;
        int department = 11;
        boolean has_access_to_menu = false;
        int recovery_id = 1;
        boolean isSystemAccount = true;


        System.out.println("[INFO] Hashing Password");
        HashedLocalPWSD = BCrypt.hashpw(LocalAdminPWSD, BCrypt.gensalt(12));
        System.out.println("[OK] Local Admin Password is sucsessfully Hashed");

        String sql = "INSERT INTO accounts (account_name, email, user_role, password_hash, bootstrap_key, account_status, user_job, permission, requires_password_change, department, has_access_to_menu, recovery_key_id, is_system_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, LocalAdminName);
            statement.setString(2, LocalAdminEmail);
            statement.setInt(3, role);
            statement.setString(4, HashedLocalPWSD);
            statement.setString(5, BootstrapKey);
            statement.setInt(6, status);
            statement.setString(7, job);
            statement.setString(8, permission);
            statement.setBoolean(9, needs_change);
            statement.setInt(10, department);
            statement.setBoolean(11, has_access_to_menu);
            statement.setInt(12, recovery_id);
            statement.setBoolean(13, isSystemAccount);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Local Admin is created");
                System.out.println("[WARNING] A default Local Admin exists in the DB");
                System.out.println("\n[INFO] Rows effected " + rows);

                return true;
            }
            return false;

        } catch (SQLException error) {
            System.out.println("\n[ERROR] Failed to create a default Local Admin");
            System.out.println(error.getMessage());

            return false;
        }
    }

    private boolean createDefaultAdmin() {
        System.out.println("\n[INFO] Creating Admin");
        String AdminName = dotenv.get("ADMIN_NAME");
        String AdminPWSD = dotenv.get("ADMIN_PWSD_DEFAULT");
        String AdminEmail = dotenv.get("ADMIN_EMAIL_DEFAULT");
        String Bootstrap_Key = dotenv.get("BOOTSTRAP_KEY");
        String HashedPWSD;
        int role = 2;
        int status = 6;
        String admin_job = "application_administrator";
        String permission = "admin_rights";
        boolean needs_change = true;
        int department = 5;
        boolean has_access_to_menu = false;
        int recovery_id = 1;
        boolean isSystemAccount = true;

        System.out.println("[INFO] Hashing Password");
        HashedPWSD = BCrypt.hashpw(AdminPWSD, BCrypt.gensalt(12));
        System.out.println("[OK] Admin Password is sucsessfully hashed");


        String sql = "INSERT INTO accounts (account_name, email, user_role, password_hash, bootstrap_key, account_status, user_job, permission, requires_password_change, department, has_access_to_menu, recovery_key_id, is_system_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, AdminName);
            statement.setString(2, AdminEmail);
            statement.setInt(3, role);
            statement.setString(4, HashedPWSD);
            statement.setString(5, Bootstrap_Key);
            statement.setInt(6, status);
            statement.setString(7, admin_job);
            statement.setString(8, permission);
            statement.setBoolean(9, needs_change);
            statement.setInt(10, department);
            statement.setBoolean(11, has_access_to_menu);
            statement.setInt(12, recovery_id);
            statement.setBoolean(13, isSystemAccount);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Admin is created");
                System.out.println("[WARNING] A default Admin account exists in the DB");
                System.out.println("\n[INFO] Rows effected " + rows);
                return true;
            }

            return false;
        } catch (SQLException error) {
            System.out.println("\n[ERROR] Failed to create a default Admin");
            System.out.println(error.getMessage());
            return false;
        }
    }

}
