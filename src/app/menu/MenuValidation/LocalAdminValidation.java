package app.menu.MenuValidation;

import app.db.Admin.LocalAdmin.ShowUsers;

import java.sql.Connection;
import java.sql.Connection;
import java.util.Scanner;

//Collect Admin Choice
public class LocalAdminValidation {

    private Connection connection;
    private String UserName;
    private int AdminChoice;

    public LocalAdminValidation(Connection connection, String UserName) {
        this.connection = connection;
        this.UserName = UserName;
    }

    public void AdminChoice(Scanner scanner) {
        System.out.println("Please choose one of the following options: ");

        this.AdminChoice = scanner.nextInt();

        if (this.AdminChoice < 1 || this.AdminChoice > 5) {
            throw new IllegalArgumentException("You are out of range only 1-13 are allowed");
        } else {
            switch (this.AdminChoice) {
                case 1:
                    ShowUsers action = new ShowUsers(
                        connection
                    );
                    action.showAvailableUsers(scanner);
            }
        }
    }
}
