package app.Auth;
import java.awt.image.renderable.RenderableImage;
import java.util.Scanner;

import app.Auth.PasswordService;

public class RegistrationService {
    protected String UserName;
    protected String EmailAdress;
    protected String PhoneNumber;

    public void UserAccount(Scanner scanner) {
        System.out.println("[INFO] Creating User Account");
        SetUserName(scanner);
        SetEmailAddress(scanner);
        SetPhoneNumber(scanner);
        ShowCurrentInfo(scanner);
    }

    private void SetUserName(Scanner scanner) {
        System.out.println("\n[INFO] Setting UserName");
        String DefaulUserName;

        while (true) {
            try {
                System.out.println("[INFO] Please Enter your UserName");
                DefaulUserName =  scanner.nextLine();

                if (DefaulUserName.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else if (DefaulUserName.length() < 5 || DefaulUserName.length() > 20) {
                    throw new IllegalArgumentException("[ERROR] The Username can't be shorter than 5 or longer than 20 letters please try again");
                } else {
                    System.out.println("[OK] The UserName is now setted");
                    UserName = DefaulUserName;
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }

        }
    }

    //Set The Email Adress and validate if the Email Adress is valid
    private void SetEmailAddress(Scanner scanner) {
        System.out.println("\n[INFO] Setting E-Mail Adress");

        String UserEmail;

        while (true) {
            try {
                System.out.println("\n[INFO] Please enter your E-Mail Adress");
                UserEmail = scanner.nextLine();

                if (UserEmail.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else if (UserEmail.length() >= 254) {
                    throw new IllegalArgumentException("[ERROR] The E-Mail can't be longer than 254 signs");
                } else if (!UserEmail.contains("@")) {
                    throw new IllegalArgumentException("[ERROR] It seems the @ sign is missing please try again");
                } else {
                    System.out.println("[OK] Your Email Adress is set");
                    EmailAdress = UserEmail;
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }

    }


    //Set the PhoneNumber and validate if the Phone Number is valid
    private void SetPhoneNumber(Scanner scanner) {
        System.out.println("\n[INFO] Setting Phone Number");
        System.out.println("[INFO] Number Format: +49123456789");

        String UserPhone;

        while (true) {
            try {
                System.out.println("\n[INFO] Please enter your Phone Number");
                UserPhone = scanner.nextLine();

                if (UserPhone.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else if (UserPhone.length() > 15) {
                    throw new IllegalArgumentException("[ERROR] Your phone number can't be longer than 15 please try again");
                } else if (UserPhone.charAt(0) != '+') {
                    throw new IllegalArgumentException("[ERROR] The Format is invalid please try again");
                } else {
                    System.out.println("[OK] The Number is Valid an will be setted");
                    PhoneNumber = UserPhone;
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }

    //Printing the Results and allowing changes
    private void ShowCurrentInfo(Scanner scanner) {
        System.out.println("\n[INFO] Showing your Info");
        System.out.println("[DEBUG] User Name: " + UserName);
        System.out.println("[DEBUG] Email Adress: " + EmailAdress);
        System.out.println("[DEBUG] Phone Number: " + PhoneNumber + "\n");

        String RegistrationState;
        int ChangeValue;

        while (true) {
            try {
                System.out.println("\nINFO] Is the Data correct? Y/N\n");
                RegistrationState = scanner.nextLine().trim().toLowerCase();

                if (RegistrationState.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else if (!RegistrationState.equals("y") && !RegistrationState.equals("n")) {
                    throw new IllegalArgumentException("[ERROR] Only y for yes or n for n are valid please try again");
                } else if (RegistrationState.equals("y")) {
                    PasswordService create = new PasswordService();
                    create.UserPWSD(scanner);
                } else {
                    System.out.println("[INFO] Please Enter what you want to chang");
                    System.out.println("[INFO] 1 User Name");
                    System.out.println("[INFO] 2 Email Address");
                    System.out.println("[INFO] 3 Phone Number");

                    ChangeValue = scanner.nextInt();
                    scanner.nextLine();

                    if (ChangeValue == 0) {
                        throw new IllegalArgumentException("[ERROR] The Value can't be 0 or empty please try again");
                    } else if (ChangeValue == 1) {
                        SetUserName(scanner);
                    } else if (ChangeValue == 2) {
                        SetEmailAddress(scanner);
                    } else if (ChangeValue == 3) {
                        SetPhoneNumber(scanner);
                    } else if (ChangeValue > 3) {
                        throw new IllegalArgumentException("[ERROR] The Value is out of range please try again");
                    }
                }

                break;
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }
}