package app.Auth.Flow.Services.AuthSecurityService;
import java.util.Scanner;

public class SelectDepartment {
    private int selectedDepartment;

    public void Department(Scanner scanner) {

        while (true) {
             try {
                 String department = scanner.nextLine();

                 if (department.isBlank()) {
                     throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                 } else {
                     this.selectedDepartment = Integer.parseInt(department);

                     if (this.selectedDepartment < 1 || this.selectedDepartment > 11) {
                         throw new IllegalArgumentException("[ERROR] You are out of range please try again");
                     }

                     break;
                 }

             } catch (NumberFormatException error) {
                 System.out.println("[ERROR] Please enter a valid number");
                 System.out.println(error.getMessage());
             } catch (IllegalArgumentException error) {
                 System.out.println(error.getMessage());
             }
        }
    }

    public int getSelectedDepartment() {
        return this.selectedDepartment;
    }
}
