package app.Auth.Flow.Services.RegistrationService;
import java.util.Scanner;

import app.Auth.Flow.PasswordFlow;


/*
    This is the Basic Registration for a User

     Following things will be collected

     - Username
     - Hashed PWSD
     - Email Address
     - Phone Number

       If everything is valid it will be stored via a getter and redirected to the DB query

*/

public class RegistrationService {
    private String userName;
    private String emailAddress;
    private String phoneNumber;
    private String hashedPWSD;

    public void userAccunt(Scanner scanner) {
        System.out.println("[INFO] Creating User Account");
        setUserName(scanner);
        setEmailAddress(scanner);
        setPhoneNumber(scanner);
        showCurrentInfo(scanner);
    }

    private void setUserName(Scanner scanner) {
        System.out.println("\n[INFO] Setting UserName");
        String defaultUserName;

        while (true) {
            try {
                System.out.println("[INFO] Please Enter your UserName");
                defaultUserName =  scanner.nextLine();

                if (defaultUserName.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else if (defaultUserName.length() < 5 || defaultUserName.length() > 20) {
                    throw new IllegalArgumentException("[ERROR] The Username can't be shorter than 5 or longer than 20 letters please try again");
                } else {
                    System.out.println("[OK] The UserName is now setted");
                    this.userName = defaultUserName;
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }

        }
    }

    //Set The Email Address and validate if the Email Address is valid
    private void setEmailAddress(Scanner scanner) {
        System.out.println("\n[INFO] Setting E-Mail Adress");
        String userEmail;

        while (true) {
            try {
                System.out.println("\n[INFO] Please enter your E-Mail Adress");
                userEmail = scanner.nextLine();

                if (userEmail.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else if (userEmail.length() >= 254) {
                    throw new IllegalArgumentException("[ERROR] The E-Mail can't be longer than 254 signs");
                } else if (!userEmail.contains("@")) {
                    throw new IllegalArgumentException("[ERROR] It seems the @ sign is missing please try again");
                } else {
                    System.out.println("[OK] Your Email Adress is set");
                    this.emailAddress = userEmail;
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }

    }

    //Set the Phonenumber and validate if the Phone Number is valid
    private void setPhoneNumber(Scanner scanner) {
        System.out.println("\n[INFO] Setting Phone Number");
        System.out.println("[INFO] Number Format: +49123456789");

        String userPhone;

        while (true) {
            try {
                System.out.println("\n[INFO] Please enter your Phone Number");
                userPhone = scanner.nextLine();

                if (userPhone.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else if (userPhone.length() > 15) {
                    throw new IllegalArgumentException("[ERROR] Your phone number can't be longer than 15 please try again");
                } else if (userPhone.charAt(0) != '+') {
                    throw new IllegalArgumentException("[ERROR] The Format is invalid please try again");
                } else {
                    System.out.println("[OK] The Number is Valid an will be setted");
                    this.phoneNumber = userPhone;
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }

    //Printing the Results and allowing changes
    private void showCurrentInfo(Scanner scanner) {
        System.out.println("\n[INFO] Showing your Info");
        System.out.println("[DEBUG] User Name: " + this.userName);
        System.out.println("[DEBUG] Email Adress: " + this.emailAddress);
        System.out.println("[DEBUG] Phone Number: " + this.phoneNumber + "\n");

        String registrationState;
        int changeValue;

        while (true) {
            try {
                System.out.println("\nINFO] Is the Data correct? Y/N]\n");
                registrationState = scanner.nextLine().trim().toLowerCase();

                if (registrationState.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty please try again");
                } else if (!registrationState.equals("y") && !registrationState.equals("n")) {
                    throw new IllegalArgumentException("[ERROR] Only y for yes or n for n are valid please try again");
                } else if (registrationState.equals("y")) {

                    //If everything is valid the password flow runs through the registration
                    PasswordFlow execute = new PasswordFlow();
                    this.hashedPWSD = execute.policy(scanner);

                } else {

                    //If anything is incorrect you can change the value
                    System.out.println("[INFO] Please Enter what you want to chang");
                    System.out.println("[INFO] 1 User Name");
                    System.out.println("[INFO] 2 Email Address");
                    System.out.println("[INFO] 3 Phone Number");

                    changeValue = scanner.nextInt();
                    scanner.nextLine();

                    if (changeValue == 0) {
                        throw new IllegalArgumentException("[ERROR] The Value can't be 0 or empty please try again");
                    } else if (changeValue == 1) {
                        setUserName(scanner);
                    } else if (changeValue == 2) {
                        setEmailAddress(scanner);
                    } else if (changeValue == 3) {
                        setPhoneNumber(scanner);
                    } else if (changeValue > 3) {
                        throw new IllegalArgumentException("[ERROR] The Value is out of range please try again");
                    }
                }
                break;
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getHashedPWSD() {
        return this.hashedPWSD;
    }
}