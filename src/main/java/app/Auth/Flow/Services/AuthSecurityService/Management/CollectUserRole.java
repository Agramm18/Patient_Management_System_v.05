package app.Auth.Flow.Services.AuthSecurityService.Management;

import java.util.Scanner;
import app.CLIText.Menus.Program.roleMenu;


import app.Config.LogManager;
import app.Config.LogManager.LogType;

/*
    This part of the Code is for the Role handling
    later this will check if the input was valid.
    based on the input the role will select and redirected to the DB

 */


public class CollectUserRole {

    public void requestRoles(Scanner scanner) {
        boolean selectedRoleIsValid = false;

        while (!selectedRoleIsValid) {

            try {
                System.out.println("\n[ERROR] The User have a default role\n");

                String selectedRoleSTR;
                int selectedRole;

                //Build Object to show Available roles
                roleMenu show = new roleMenu();
                show.roles();

                //Collect roles via user input
                System.out.println("\nPlease choose a role");
                selectedRoleSTR = scanner.nextLine();

                //Bais Error handling if role < 1 or > 10 or empty ERROR
                if (selectedRoleSTR.trim().isEmpty()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else {
                    selectedRole = Integer.parseInt(selectedRoleSTR); //Convert Str value to int

                    //Check if the role is on an valid range
                    if (selectedRole < 1 || selectedRole > 10) {
                        throw new IllegalArgumentException("[ERROR] The chosen value is out of range the value can't be less than 1 or higher than 10");
                    } else {
                        LogManager.log(LogType.AUTH_SUCCESS, "The User have choose the role: " + selectedRole);
                    }
                }

                selectedRoleIsValid = true;


            } catch (NumberFormatException error) {
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
                System.out.println("[ERROR] Please type in a valid number");
            }

            catch (IllegalArgumentException error) {
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
                System.out.println(error.getMessage());
            }
        }
    }
}
