package app.Auth.Flow.Services.RegistrationService;
import java.util.Scanner;

import app.Auth.Flow.PasswordFlow;

import app.Auth.Flow.Services.PasswordService.PasswordService;
import app.Config.LogManager;
import app.Config.LogManager.LogType;
import app.Controller.*;

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
    private int atINdexEmail;
    private String domain;
    private int tldIndex;
    private String tld;
    private String domainName;

    public void userAccunt(Scanner scanner) {
        setUserName(scanner);
        setEmailAddress(scanner);
        setPhoneNumber(scanner);
        showCurrentInfo(scanner);
    }

     void setUserName(Scanner scanner) {
        LogManager.log(LogManager.LogType.AUTH_INFO, "Starting Username Setup");
        String defaultUserName;

         System.out.println("[INFO] Please Enter your UserName");

        while (true) {
            try {

                defaultUserName =  scanner.nextLine();

                validateUsername(defaultUserName);

                LogManager.log(LogType.AUTH_SUCCESS, "The UserName is now set");
                this.userName = defaultUserName;
                break;

            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
            }

        }
    }

    //Validation Method so Testing is possible
    void validateUsername(String defaultUsername) throws IllegalArgumentException {
        if (defaultUsername.isBlank()) {
            throw new IllegalArgumentException("This field can't be empty please try again");
        }

        if (defaultUsername.length() < 5 || defaultUsername.length() > 20) {
            throw new IllegalArgumentException("The Username can't be shorter than 5 or longer than 20 letters please try again");
        }
    }

    //Set The Email Address and validate if the Email Address is valid
    private void setEmailAddress(Scanner scanner) {
        LogManager.log(LogType.AUTH_INFO, "Starting E-Mail Address Setup");
        String userEmail;

        while (true) {
            try {
                System.out.println("Please enter your E-Mail Address");
                userEmail = scanner.nextLine();

                validateEmailAddress(userEmail);

                LogManager.log(LogType.AUTH_SUCCESS, "Your Email Address is set");
                this.emailAddress = userEmail;
                break;

            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
            }
        }
    }

    //Validation Method so testing is possible
    void validateEmailAddress(String userEmail) throws IllegalArgumentException {

        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("This field can't be empty please try again");
        }

        if (userEmail.length() > 254) {
            throw new IllegalArgumentException("The E-Mail can't be longer than 254 signs");
        }

        if (userEmail.length() <= 5) {
            throw new IllegalArgumentException("It seems your Email is too short");
        }

        this.atINdexEmail = userEmail.indexOf('@');

        if (this.atINdexEmail == -1) {
            throw new IllegalArgumentException("Invalid Email Format It seems the @ sign is missing");
        }

        if (this.atINdexEmail == 0) {
            throw new IllegalArgumentException("Invalid Email Format The Content before the @ can't be empty");
        }

        this.domain = userEmail.substring(this.atINdexEmail + 1);

        if (this.domain.isBlank()) {
            throw new IllegalArgumentException("Invalid Email Format The Domain can't be Empty");
        }

        this.tldIndex = this.domain.lastIndexOf('.');

        if (this.tldIndex == -1) {
            throw new IllegalArgumentException("Invalid Email Format the Domain must contain at least one .");
        }

        if (this.tldIndex == this.domain.length() - 1) {
            throw new IllegalArgumentException("Invalid Email Format the E-Mail does not contain a TLD-Domain like .com, .org etc.");
        }

        this.domainName = this.domain.substring(0, this.tldIndex);

        if (this.domainName.isBlank()) {
            throw new IllegalArgumentException("Invalid Email Format The Domain Name can't be empty");
        }
    }

    //Set the Phonenumber and validate if the Phone Number is valid
    private void setPhoneNumber(Scanner scanner) {
        LogManager.log(LogType.AUTH_INFO, "Starting Phone Number Setup");
        LogManager.log(LogType.AUTH_INFO, "Number Format: +49123456789");

        String userPhone;

        while (true) {
            try {
                System.out.println("\n[INFO] Please enter your Phone Number");
                userPhone = scanner.nextLine();

                if (userPhone.isBlank()) {
                    throw new IllegalArgumentException("This field can't be empty please try again");
                } else if (userPhone.length() > 15) {
                    throw new IllegalArgumentException("Your phone number can't be longer than 15 please try again");
                } else if (userPhone.charAt(0) != '+') {
                    throw new IllegalArgumentException("The Format is invalid please try again");
                } else {
                    LogManager.log(LogType.AUTH_SUCCESS, "The Number is Valid an will be set");
                    this.phoneNumber = userPhone;
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogType.INVALID_INPUT, "Something went wrong: " + error.getMessage());
            }
        }
    }

    //Printing the Results and allowing changes
    private void showCurrentInfo(Scanner scanner) {
        System.out.println("\n[INFO] Current Data");
        System.out.println("[DEBUG] User Name: " + this.userName);
        System.out.println("[DEBUG] Email Address: " + this.emailAddress);
        System.out.println("[DEBUG] Phone Number: " + this.phoneNumber + "\n");

        String registrationState;
        int changeValue;

        while (true) {
            try {
                System.out.println("\nINFO] Is the Data correct? Y/N]\n");
                registrationState = scanner.nextLine().trim().toLowerCase();

                if (registrationState.isBlank()) {
                    throw new IllegalArgumentException("This field can't be empty please try again");
                } else if (!registrationState.equals("y") && !registrationState.equals("n")) {
                    throw new IllegalArgumentException("Only y for yes or n for n are valid please try again");
                } else if (registrationState.equals("y")) {

                    LogManager.log(LogType.AUTH_SUCCESS, "Basic Registration Credentials Successfully collected");

                    //If everything is valid the password flow runs through the registration
                    PasswordFlow execute = new PasswordFlow();
                    this.hashedPWSD = execute.policy(scanner);

                } else {

                    //If anything is incorrect you can change the value
                    System.out.println("[INFO] Please Enter what you want to change");
                    System.out.println("[INFO] 1 User Name");
                    System.out.println("[INFO] 2 Email Address");
                    System.out.println("[INFO] 3 Phone Number");

                    changeValue = scanner.nextInt();
                    scanner.nextLine();

                while (true) {

                         String valueIsRight;

                        if (changeValue == 0) {
                            throw new IllegalArgumentException("The Value can't be 0 or empty please try again");
                        } else if (changeValue == 1) {
                            setUserName(scanner);
                            System.out.println("Is the Username Right?´ Y/N: " + userName);

                            valueIsRight = scanner.nextLine().toLowerCase();

                            if (valueIsRight.equals("y")) {

                                System.out.println("\n[INFO] Current Data");
                                System.out.println("[DEBUG] User Name: " + this.userName);
                                System.out.println("[DEBUG] Email Address: " + this.emailAddress);
                                System.out.println("[DEBUG] Phone Number: " + this.phoneNumber + "\n");

                                PasswordService service = new PasswordService();
                                service.userPWSD(scanner);

                                return;
                            } else {
                                setUserName(scanner);
                            }

                        } else if (changeValue == 2) {
                            setEmailAddress(scanner);

                            System.out.println("Is the Email Address Right? Y/N: " + emailAddress);

                            valueIsRight = scanner.nextLine().toLowerCase();

                            if (valueIsRight.equals("y")) {

                                System.out.println("\n[INFO] Current Data");
                                System.out.println("[DEBUG] User Name: " + this.userName);
                                System.out.println("[DEBUG] Email Address: " + this.emailAddress);
                                System.out.println("[DEBUG] Phone Number: " + this.phoneNumber + "\n");

                                PasswordService service = new PasswordService();
                                service.userPWSD(scanner);

                                return;
                            } else {
                                setEmailAddress(scanner);
                            }


                        } else if (changeValue == 3) {
                            setPhoneNumber(scanner);

                            System.out.println("Is the Phone Number Right?: Y/N: " + phoneNumber);

                            valueIsRight = scanner.nextLine().toLowerCase();

                            if (valueIsRight.equals("y")) {
                                System.out.println("\n[INFO] Current Data");
                                System.out.println("[DEBUG] User Name: " + this.userName);
                                System.out.println("[DEBUG] Email Address: " + this.emailAddress);
                                System.out.println("[DEBUG] Phone Number: " + this.phoneNumber + "\n");

                                PasswordService service = new PasswordService();
                                service.userPWSD(scanner);
                                return;
                            } else {
                                setEmailAddress(scanner);
                            }

                        } else if (changeValue > 3) {
                            throw new IllegalArgumentException("The Value is out of range please try again");
                        }
                    }
                }
                break;
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.log(LogType.INVALID_INPUT, error.getMessage());
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