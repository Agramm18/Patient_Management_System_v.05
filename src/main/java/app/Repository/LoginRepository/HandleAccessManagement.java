package app.Repository.LoginRepository;


import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class HandleAccessManagement {
    private static final String DEFAULT_REQUESTED_JOB = "unassigned";
    private static final int DEFAULT_REQUESTED_ROLE = 9;

    private int RequestedBy;
    private int RequestedDepartment;

    public void AccessManagement(String Username, int Department) {
        this.RequestedDepartment = Department;
        collectUser(Username);

        if (this.RequestedBy > 0) {
            insertData();
        } else {
            System.out.println("[ERROR] No valid account by the Username " + Username + "Where found");
        }
    }

    private void collectUser(String username) {
        System.out.println("[INFO] Collect User Request");
        String sql = "SELECT id FROM accounts WHERE account_name = ?";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    this.RequestedBy = rs.getInt("id");

                    System.out.println("[OK] Username is Sucsessfully Collected");
                    System.out.println("[INFO] Requested by Account id: " + this.RequestedBy);
                } else {
                    System.out.println("[ERROR] No account found with this username");
                }
            }
        catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

    private void insertData() {
        System.out.println("\n[INFO] Request where saved");

        String sql = "INSERT INTO access_management (requested_by, requested_department, requested_job, requested_role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            stmt.setInt(1, this.RequestedBy);
            stmt.setInt(2, this.RequestedDepartment);
            stmt.setString(3, DEFAULT_REQUESTED_JOB);
            stmt.setInt(4, DEFAULT_REQUESTED_ROLE);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("[OK] Table updated succsessfully");
                System.out.println("[INFO] Rows affected: " + rows);
            }
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }
}
