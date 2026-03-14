package app.menu.MenuValidation;

import app.controller.auth.account.admin.create.CreateAdmin;

import java.sql.Connection;
import java.util.Scanner;

//Collect Admin Choice
public class LocalAdminValidation {

    private Connection connection;
    private int AdminChoice;

    public LocalAdminValidation(Connection connection) {
        this.connection = connection;
    }

    public void AdminChoice(Scanner scanner) {
        System.out.println("Please choose one of the following options: ");

        this.AdminChoice = scanner.nextInt();

        if (this.AdminChoice < 1 || this.AdminChoice > 12) {
            throw new IllegalArgumentException("You are out of range only 1-13 are allowed");
        } else {
            switch (this.AdminChoice) {
                case 1:
                    CreateAdmin create = new CreateAdmin();
                    create.LoacalAdmin();
            }
        }
    }
}
