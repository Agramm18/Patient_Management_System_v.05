import java.io.Console;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Basic Programm Running Logic
        System.out.println("\n----------------------System Starting---------------\n");
        System.out.println("\n-------Welcome to my Patient Management System .v 5.0 this time in Java-------");
        System.out.println("-------------Made from Agramm18 2026 All Rights reserved---------------\n");

        //Load Login Logic

        Account account = new Account();
        account.HeaderMSGAccount();

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
                account.SetAccount(scanner);
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