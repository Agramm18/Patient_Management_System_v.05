package app.db.auth.check;
import app.db.auth.check.CheckUser;

//Libary imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CheckAccountStatus {
    private String UserName;
    private Connection connection;

    public CheckAccountStatus(String UserName, Connection connection) {
        this.UserName = UserName;
        this.connection = connection;
    }

    public void AccountStatus(Scanner scanner) {

        try {
        //Check if user is enabled or disabeld
        String GetStatus = "SELECT account_status FROM accounts WHERE account_name = ?";

        PreparedStatement CollectStatus = connection.prepareStatement(GetStatus);
        CollectStatus.setString(1, this.UserName);

        ResultSet AccountStatus = CollectStatus.executeQuery();

        if (AccountStatus.next()) {
            String StatusValue = AccountStatus.getString("account_status");

            if (StatusValue.equals("disabled")) {
                throw new IllegalArgumentException("\nYour account is deacitvated...");
            } else if (StatusValue.equals("enabled")) {
                System.out.println("\nWelcome to the system we will now validate wich job do you have and you can conitnue\n");
                
                CheckAccountRole load = new CheckAccountRole(
                    this.UserName,
                    this.connection
                );

                load.AccountRole(scanner);

            } else {
                throw new IllegalArgumentException("It seems the TABLE inside DB does not exsist please try again or create a new one");
            }
        }
        } catch (Exception error) {
            System.out.println("Error" + error.getMessage());
        }
    }
}