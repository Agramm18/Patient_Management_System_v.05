package app.Auth.Flow.Services.RegistrationService;
import java.util.Scanner;

import app.Auth.Flow.PasswordFlow;

import app.Auth.Flow.Services.PasswordService.PasswordService;
import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

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
    private Phonenumber.PhoneNumber libNumber;

    public void userAccunt(Scanner scanner) {
        setUserName(scanner);
        setEmailAddress(scanner);
        setPhoneNumber(scanner);
        showCurrentInfo(scanner);
    }

     void setUserName(Scanner scanner) {
        LogManager.auth(AuthState.INFO, "Starting Username Setup");
        String defaultUserName;

         System.out.println("[INFO] Please Enter your UserName");

        while (true) {
            try {

                defaultUserName =  scanner.nextLine();

                validateUsername(defaultUserName);

                LogManager.auth(AuthState.SUCCESS, "The UserName is now set");
                this.userName = defaultUserName;
                break;

            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.other(OtherState.INVALID_INPUT, error.getMessage());
            }

        }
    }

    //Validation Method so Testing is possible
    void validateUsername(String defaultUsername) throws IllegalArgumentException {
        if (defaultUsername.isBlank()) {
            throw new IllegalArgumentException("This field can't be empty please try again");
        }

        if (defaultUsername.length() <= 5 || defaultUsername.length() >= 20) {
            throw new IllegalArgumentException("The Username can't be shorter than 5 or longer than 20 letters please try again");
        }
    }

    //Set The Email Address and validate if the Email Address is valid
    private void setEmailAddress(Scanner scanner) {
        LogManager.auth(AuthState.INFO, "Starting E-Mail Address Setup");
        String userEmail;

        while (true) {
            try {
                System.out.println("Please enter your E-Mail Address");
                userEmail = scanner.nextLine();

                validateEmailAddress(userEmail);

                LogManager.auth(AuthState.SUCCESS, "Your Email Address is set");
                this.emailAddress = userEmail;
                break;

            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.other(OtherState.INVALID_INPUT, error.getMessage());
            }
        }
    }

    //Validation Method so testing is possible
    void validateEmailAddress(String userEmail) throws IllegalArgumentException {

        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("This field can't be empty please try again");
        }

        if (userEmail.length() >= 254) {
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
        LogManager.auth(AuthState.INFO, "Starting Phone Number Setup");
        LogManager.auth(AuthState.INFO, "Number Format: +49123456789");

        String userPhone;

        while (true) {
            try {
                System.out.println("\n[INFO] Please enter your Phone Number");
                userPhone = scanner.nextLine();

                validatePhoneNumber(userPhone);

                LogManager.auth(AuthState.SUCCESS, "The Number is Valid an will be set");
                this.phoneNumber = userPhone;
                break;

            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.other(OtherState.INVALID_INPUT, "Something went wrong: " + error.getMessage());
            }
        }
    }

    void validatePhoneNumber(String userPhone) throws IllegalArgumentException {
        if (userPhone == null || userPhone.isBlank()) {
            throw new IllegalArgumentException("This field can't be empty please try again");
        }

        if (userPhone.length() > 16) {
            throw new IllegalArgumentException("Your Phone Number can't be longer or be equal than 15 please try again");
        }

        if (userPhone.length() <=5) {
            throw new IllegalArgumentException("Your Phone Number is to short your Phone Number can't be shorter than or be equal to 5");
        }

        if (userPhone.charAt(0) != '+') {
            throw new IllegalArgumentException("Invalid Format the Phone Number needs one + at the start");
        }

        if (userPhone.matches(".*\\p{L}.*")) {
            throw new IllegalArgumentException("Invalid Format Your Phone Number can't contain Letters");
        }

        if (!userPhone.matches("^\\+\\d+$")) {
            throw new IllegalArgumentException("Invalid Format Your Phone Number can't contain other Special letters and more than 1 +");
        }

        //Load Library to validate if the Country Code for the phone number is valid
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();

        try {
            this.libNumber = util.parse(userPhone, null);
        } catch (NumberParseException error) {
            throw new IllegalArgumentException("The Country Code in your Phone Number is Invalid");
        }

        if (!util.isValidNumber(this.libNumber)) {
            throw new IllegalArgumentException("The Format for the Phone Number is Invalid");
        }
    }

    private void currentData() {
        System.out.println("\n[INFO] Current Data");
        System.out.println("[DEBUG] User Name: " + this.userName);
        System.out.println("[DEBUG] Email Address: " + this.emailAddress);
        System.out.println("[DEBUG] Phone Number: " + this.phoneNumber + "\n");
    }

    //Printing the Results and allowing changes
     void showCurrentInfo(Scanner scanner) {
        currentData();

        String registrationState;

        while (true) {
            try {
                System.out.println("\nINFO] Is the Data correct? Y/N]");
                registrationState = scanner.nextLine().trim().toLowerCase();

                if (registrationState.isBlank()) {
                    throw new IllegalArgumentException("[ERROR] This field can't be empty");
                }

                if (!registrationState.equals("y") && !registrationState.equals("n")) {
                    throw new IllegalArgumentException("[ERROR] Only y or n are permitted");
                }

                if (registrationState.equals("y")) {

                    LogManager.auth(AuthState.SUCCESS, "Basic Registration Credentials Successfully collected");

                    PasswordFlow execute = new PasswordFlow();
                    this.hashedPWSD = execute.policy(scanner);

                    break;

                } else {

                    //If anything is incorrect you can change the value
                    System.out.println("[INFO] Please Enter what you want to change");
                    System.out.println("[INFO] 1 User Name");
                    System.out.println("[INFO] 2 Email Address");
                    System.out.println("[INFO] 3 Phone Number");

                    String changeValueString = scanner.nextLine().trim();

                    if (changeValueString.trim().isBlank()) {
                        throw new IllegalArgumentException("[ERROR] This field can't be empty");
                    }

                    int changeValueINT;

                    try {
                        changeValueINT= Integer.parseInt(changeValueString);
                    } catch (NumberFormatException error) {
                        throw new IllegalArgumentException("[ERROR] Please enter a Number between 1 and 3");
                    }

                    try {
                        if (changeValueINT == 1) {
                            changeUsername(scanner);

                        } else if (changeValueINT == 2) {
                            changeEmailAddress(scanner);

                        } else if (changeValueINT == 3) {
                            changePhoneNumber(scanner);

                        } else {
                            throw new IllegalArgumentException("Only 1-3 are permitted");
                        }

                        currentData();

                    } catch (IllegalArgumentException error) {
                        System.out.println(error.getMessage());
                        LogManager.auth(AuthState.INFO, error.getMessage());
                    }

                }

            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
                LogManager.auth(AuthState.INFO, error.getMessage());
            }
        }

    }

    void changeUsername(Scanner scanner) {
        setUserName(scanner);
    }

    void changeEmailAddress(Scanner scanner) {
        setEmailAddress(scanner);
    }

    void changePhoneNumber(Scanner scanner) {
        setPhoneNumber(scanner);
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