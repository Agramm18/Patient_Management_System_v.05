package app;
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

//load bcrypt for Password Hashing
import org.mindrot.jbcrypt.BCrypt;

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