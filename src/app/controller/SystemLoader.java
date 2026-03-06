package app.controller;
import java.util.Scanner;

import app.controller.auth.RunSqlHandeling;
public class SystemLoader {
        
        public void HeaderLoad() {
            System.out.println("\n==================================================");
            System.out.println("                 SYSTEM LOADER");
            System.out.println("--------------------------------------------------");
            System.out.println("  Loading program modules...");
            System.out.println("==================================================\n");
        }

        public void start(Scanner scanner) {
            //Load SQL Connection and handeling .env file/parameter
            RunSqlHandeling connection = new RunSqlHandeling();
            connection.sqlHeader();
            connection.ValidateENV();
            connection.SQLConnection(scanner);
        }
}
