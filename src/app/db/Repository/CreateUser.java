package app.db.Repository;

import app.controller.auth.account.login.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class CreateUser {
    private String DBUser;
    private String DBEmail;
    private String DBPhone;
    private String PWSDHashed; 
    private Connection connection;

    public CreateUser(String DBUser, String DBEmail, String DBPhone, String PWSDHashed, Connection connection) {
        this.DBUser = DBUser;
        this.DBEmail = DBEmail;
        this.DBPhone = DBPhone;
        this.PWSDHashed = PWSDHashed;
        this.connection = connection;
    }

    public void InsertData() {
        System.out.println("\nThe follwoing Data will be insert in the DB");

        System.out.println("Username: " + this.DBUser);
        System.out.println("Email: " + this.DBEmail);
        System.out.println("Phone: " + this.DBPhone);
        System.out.println("PWSD: "  + this.PWSDHashed + "\n");
    }

    public void InsertUser(Scanner scanner) {
        try {
            System.out.println("\nThe User will now be laid in into the Database\n");

            //SQL Parameter to paste the user into the db
            String SQLParam = "INSERT INTO accounts (account_name, email, phone_number, password_hash) VALUES (?, ?, ?, ?)";

            
            PreparedStatement stmt = connection.prepareStatement(SQLParam);

            stmt.setString(1, this.DBUser);
            stmt.setString(2, this.DBEmail);
            stmt.setString(3, this.DBPhone);
            stmt.setString(4, this.PWSDHashed);

            stmt.executeUpdate();

            System.out.println("\nThe registration was a sucsess");
            System.out.println("Welcome to the programm");
            System.out.println("You will now be redirected to the Login\n");
            Login load = new Login(connection);
            load.LoginUser(scanner);

        } catch (Exception error) {
            System.out.println("Something didnt't work...");
        }
    }
}
