package app.Repository.ServiceRepository.AdminServices;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import app.Config.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ShowCurrentRequests {

    public void CurrentRequests() {
        String sql = """
            SELECT
                a.account_name,
                d.department_name,
                am.requested_job,
                r.role_name
            FROM access_management am
            JOIN accounts a
                ON am.requested_by = a.id
            JOIN departments d
                ON am.requested_department = d.id
            JOIN roles r
                ON am.requested_role = r.id
            """;

        try (Connection connection = DBManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
        ) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n");

            while (rs.next()) {
                System.out.println(
                  "User: " + rs.getString("account_name") +
                  " | Department: " + rs.getNString("department_name") +
                  " | Job: " + rs.getString("requested_job") +
                  " | Role: " + rs.getString("role_name")
                );
            }

        } catch (SQLException error) {
            LogManager.sql(SqlState.ERROR, error.getMessage());
            System.out.println(error.getMessage());
        }
    }
}
