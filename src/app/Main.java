package app;
import java.util.Scanner;

import app.controller.SystemLoader;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //CLI Hello MSG
        System.out.println("\n==================================================");
        System.out.println("         Welcome to the Patient Management System");
        System.out.println("                     Version 5.0");
        System.out.println("--------------------------------------------------");
        System.out.println("              Developed by Agramm18");
        System.out.println("                     © 2026");
        System.out.println("==================================================\n");
        
        SystemLoader load = new SystemLoader();
        load.HeaderLoad();
        load.start(scanner);
    }
}