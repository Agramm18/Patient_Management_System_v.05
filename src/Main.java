import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Basic Programm Running Logic
        System.out.println("\n----------------------System Starting---------------\n");
        System.out.println("\n-------Welcome to my Patient Management System .v 5.0 this time in Java-------");
        System.out.println("-------------Made from Agramm18 2026 All Rights reserved---------------\n");

        //Display MainMenu
        MenuScreen window = new MenuScreen();
        window.show();

        menu m = new menu(null);
        m.CollectMenuChoice(scanner);

    }
}