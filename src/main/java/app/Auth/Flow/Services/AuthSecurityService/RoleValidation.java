package app.Auth.Flow.Services.AuthSecurityService;

import java.util.Scanner;
import app.Menus.roleMenu;

public class RoleValidationService {

    public void RequestedRole(Scanner scanner) {
        boolean selectedRoleIsValid = false;

        while (!selectedRoleIsValid) {

            try {
                System.out.println("\n[ERROR] The User have a default role\n");

                String selectedRoleSTR;
                int selectedRole;

                //Show all valid roles
                roleMenu show = new roleMenu();

                //Collect Roles via User Input
                show.Roles();

                System.out.println("\nPlease choose a role");
                selectedRoleSTR = scanner.nextLine();

                //Bais Error handling if role < 1 or > 10 or empty ERROR
                if (selectedRoleSTR.trim().isEmpty()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else {
                    selectedRole = Integer.parseInt(selectedRoleSTR);

                    if (selectedRole < 1 || selectedRole > 10) {
                        throw new IllegalArgumentException("[ERROR] The chosen value is out of range the value can't be less than 1 or higher than 10");
                    } else {
                    }
                }

                selectedRoleIsValid = true;

            } catch (NumberFormatException error) {
                System.out.println("[ERROR] Please type in a valid number");
            }

            catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }
}
