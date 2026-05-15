package app.Config;

import app.Config.DBManager;
import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

public class SetDefaultAccounts {
    private final Dotenv dotenv = Dotenv.load();

    public boolean DefaultLocalAdmin() {

        System.out.println("\n[INFO] Creating Local Admin");

        String LocalAdminName = dotenv.get("LOCAL_ADMIN_NAME");
        String LocalAdminPWSD = dotenv.get("LOCAL_ADMIN_PWSD");
        String LocalAdminEmail = dotenv.get("LOCAL_ADMIN_EMAIL");
        String BootstrapKey = dotenv.get("BOOTSTRAP_KEY");

        String HashedLocalPWSD;
        String role = "local_admin";
        String account_status = "enabled";
        String job = "system_administrator";
        String permission = "root_access";


        System.out.println("[INFO] Hashing Password");
        HashedLocalPWSD = BCrypt.hashpw(LocalAdminPWSD, BCrypt.gensalt(12));
        System.out.println("[OK] Local Admin Password is sucsessfully Hashed");

        String sql = "INSERT INTO accounts (account_name, email, user_role, password_hash, bootstrap_key, account_status, user_job, permission) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, LocalAdminName);
            statement.setString(2, LocalAdminEmail);
            statement.setString(3, role);
            statement.setString(4, HashedLocalPWSD);
            statement.setString(5, BootstrapKey);
            statement.setString(6, account_status);
            statement.setString(7, job);
            statement.setString(8, permission);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Local Admin is created");
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

    public boolean DefaultAdmin() {
        System.out.println("\n[INFO] Creating Admin");
        String AdminName = dotenv.get("ADMIN_NAME");
        String AdminPWSD = dotenv.get("ADMIN_PWSD_DEFAULT");
        String AdminEmail = dotenv.get("ADMIN_EMAIL_DEFAULT");
        String Bootstrap_Key = dotenv.get("BOOTSTRAP_KEY");
        String HashedPWSD;
        String role = "admin";
        String account_status_admin = "enabled";
        String admin_job = "application_administrator";
        String permission = "admin_rights";

        System.out.println("[INFO] Hashing Password");
        HashedPWSD = BCrypt.hashpw(AdminPWSD, BCrypt.gensalt(12));
        System.out.println("[OK] Admin Password is sucsessfully hashed");

        String sql = "INSERT INTO accounts (account_name, email, user_role, password_hash, bootstrap_key, account_status, user_job, permission) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, AdminName);
            statement.setString(2, AdminEmail);
            statement.setString(3, role);
            statement.setString(4, HashedPWSD);
            statement.setString(5, Bootstrap_Key);
            statement.setString(6, account_status_admin);
            statement.setString(7, admin_job);
            statement.setString(8, permission);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Admin is created");
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
