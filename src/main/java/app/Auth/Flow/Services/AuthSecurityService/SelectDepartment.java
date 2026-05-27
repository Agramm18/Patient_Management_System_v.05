package app.Auth.Flow.Services.AuthSecurityService;
import java.util.Scanner;


/*
    In this Section the User can request his Department

    The Department will be selected via the User Input
    If the Department not Empty the Str. will be converted to an int
    After that the int value will be checked if it is in a valid range
    And then will be stored on the getter and with the int value redirected to the DB Querry

*/

public class SelectDepartment {
    private int selectedDepartment;

    public void department(Scanner scanner) {

        while (true) {
             try {
                 //Collect User Input as str
                 String department = scanner.nextLine();

                 if (department.isBlank()) {
                     throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                 } else {
                     this.selectedDepartment = Integer.parseInt(department); //convert str to int

                     //Check if the Input is in va valid range
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
