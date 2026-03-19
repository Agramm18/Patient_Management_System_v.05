package app.db.Admin.LocalAdmin;
import app.db.Admin.LocalAdmin.ChangeUserStatus;

import java.sql.Connection;

import java.util.Scanner;

public class GrantAdminPriveleges {
    private Connection connection;
    private int IDChoice;

    public GrantAdminPriveleges(Connection connection) {
        this.connection = connection;
    }

    public void SelectUser(Scanner scanner) {

        System.out.println("\nPlease select a user based from the ID wich should have Admin privieleges");
        scanner.nextLine();
        String Choice = scanner.nextLine();

        try {

            if (Choice.isBlank()) {
                throw new IllegalArgumentException("This can't be empty please try again");
            }

            int INTValue = Integer.parseInt(Choice);

            if (INTValue < 1) {
                throw new IllegalArgumentException("Invalid option you are out of range your ID can't be less than 1");
            }

            this.IDChoice = INTValue;
            
            ChangeUserStatus run = new ChangeUserStatus(connection, this.IDChoice);
            run.ChangeStatus();

        } catch (Exception error) {
            System.out.println("Error " + error.getMessage());
        }
    
    }
}
