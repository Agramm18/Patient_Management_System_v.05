import java.io.Console;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

//Load SQL Connection libaries
import java.sql.Connection;
import java.sql.DriverManager;


//load dotenv libaries
import io.github.cdimascio.dotenv.Dotenv;

//load filehandler

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Basic Programm Running Logic
        System.out.println("\n----------------------System Starting---------------\n");
        System.out.println("\n-------Welcome to my Patient Management System .v 5.0 this time in Java-------");
        System.out.println("-------------Made from Agramm18 2026 All Rights reserved---------------\n");

        //Load SQL Connection and handeling .env file/parameter
        RunSqlHandeling connection = new RunSqlHandeling();
        connection.sqlHeader();
        connection.ValidateENV();

        //Load Login Logic

        Account account = new Account();
        account.HeaderMSGAccount();

        /*
                Connection conn = DriverManager.getConnection() {
            "jdbc:mysql://localhost:3306/patient_management_v5",
            Dbuser,
            "password"
        }        
        */

        //Error Handeling with try and catch
        while (true) {
            try {
                account.SetBaseValues(scanner);;
                break;

            } catch (IllegalArgumentException invalidInput) {
                System.out.println("\nThere is an error of the Login/Registration logic");
                System.out.println("The error is: " + invalidInput.getMessage() + "\n");
            }
        }

        while (true) {
            try {
                account.AccountValidation(scanner);
                break;

            } catch (IllegalArgumentException invalidInput) {
                System.out.println("\nThere is an error of the Login/Registration logic");
                System.out.println("The error is: " + invalidInput.getMessage() + "\n");
            }

        }

        /*
        //Display MainMenu
        MenuScreen window = new MenuScreen();
        window.show();

        menu m = new menu(null);
        m.CollectMenuChoice(scanner);
        */

    }
}