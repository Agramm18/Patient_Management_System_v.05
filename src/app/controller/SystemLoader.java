package app.controller;
import java.util.Scanner;

import app.db.auth.config.CheckENV;
public class SystemLoader {
        
        public void HeaderLoad() {
            System.out.println("\n==================================================");
            System.out.println("                 SYSTEM LOADER");
            System.out.println("--------------------------------------------------");
            System.out.println("  Loading program modules...");
            System.out.println("==================================================\n");
        }

        //Load .env check
        public void start(Scanner scanner) {
            CheckENV  run = new CheckENV();
            run.sqlHeader();
            run.ValidateENV(scanner);
        }
}
