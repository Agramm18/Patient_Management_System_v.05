package app.Auth.Flow.Services.PasswordService;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;
/*
    This section handles the Password Validation & Creation

    The Password must be

    - Contain a lowerCase Letter
    - Contain a upperCase Letter
    - Contain a number
    - contain a specialLetter
    - Must be at least 10 letters long

    After that you are forced to type in the same password again

    Then the PWSD Will converted from char to string

    And then the plain password will be hashed with bcrypt

    After that the password is stored in a getter to direct it to the DB querry
*/

public class PasswordService {
    String plainPWSD;
    String hashedPWSD;
    char[] originalPassword;
    char[] retypedPassword;
    char[] verifyPWSD;

    //Password Credentials
    private boolean FitPWSDLength;
    private boolean ContainsUpperLetters;
    private boolean ContainsLowerLetters;
    private boolean ContainsSpecialLetters;
    private boolean ContainsNumbers;
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 3;

    //Parrent Method to call other methods
    public void userPWSD(Scanner scanner) {
        LogManager.security(SecurityState.INFO, "Starting Password creation");
        System.out.println("[INFO] Running through password creation");
        System.out.println("[INFO] Creating plain text PWSD");

        while (true) {
            plainPWSD();
            try {
                System.out.println("[INFO] Running through validation process and validate if the password is valid");
                validatePWSD(this.originalPassword);
                break;
            } catch (IllegalStateException error) {
                System.out.println(error.getMessage());
                this.retryCount++;
                System.out.println("[INFO] Retry Count: " + this.retryCount);
                LogManager.security(SecurityState.WARN, "Retry Count: " + this.retryCount);
                LogManager.other(OtherState.INVALID_INPUT, error.getMessage());

                if (this.retryCount >= MAX_RETRY_COUNT) {
                    LogManager.security(SecurityState.WARN, "The user typed in to many wrong passwords");
                    System.out.println("[ERROR] Max retry attempts reached your account will be disabled");
                    throw new IllegalStateException("[INFO] Please reactivate your account via the basic AUTH Menu");
                }
            } catch (RuntimeException error) {
                LogManager.auth(AuthState.FAILED, "The Program was not run via Terminal");
                System.out.println(error.getMessage());
            }
        }

        retypePassword(this.originalPassword, this.retypedPassword);

        LogManager.security(SecurityState.INFO, "Converting Char back to String");
        convertCHARtoString();
        LogManager.security(SecurityState.INFO, "Hashing entered String value");
        PlainToHash();
        LogManager.security(SecurityState.SUCCESS, "The Password was hashed successfully");
    }

    //Collect the Plain Password with invisible Console input
     void plainPWSD() throws RuntimeException{
        Console console = System.console();

        if (console == null) {
            throw new RuntimeException ("[WARNING] Please run the program only in the Terminal");
        }

        while (true) {
            try {
                //Create pwsd with invisible user input
                this.originalPassword = console.readPassword("[INFO] Please set a password for your account: ");

                if (this.originalPassword == null) {
                    throw new IllegalStateException("The Password is empty and can't be validate");
                }

                break;

            } catch (RuntimeException error) {
                LogManager.security(SecurityState.WARN, "The Program was not run via terminal");
                System.out.println(error.getMessage());
            }
        }
    }

    //Run through the Password credentials and check if they're all valid
     void validatePWSD(char[] password) {

        //Password credentials
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;
        boolean hasNumbers = false;
        boolean fitLength = false;
        int failedAttempts = 0;

        //Check if the password fit to the credential
        if (password.length == 0) {
            throw new IllegalStateException("[ERROR] Your Password can't be empty!");
        } else if (password.length < 10) {
            throw new IllegalStateException("[ERROR] Your password must bee at least 10 letters long");
        } else {
            fitLength = true;
        }

        //Check if the PWSD fit to the credentials if thats the case the default vars will be sett to true
        for (char c : password) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }

            if (Character.isLowerCase(c)) {
                hasLower = true;
            }

            if (Character.isDigit(c)) {
                hasNumbers = true;
            }

            if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        //Throw errors based on the value
        if (!hasUpper) {
            throw new IllegalStateException("[ERROR] Please note that your Password need to contain Uppercase Letters to be valid");
        } else if (!hasLower) {
            throw new IllegalStateException("[ERROR] Please note that your Password need to contain Lowercase Letters to be valid");
        } else if (!hasNumbers) {
            throw new IllegalStateException("[ERROR] Please note that your Password need to contain Numbers to be valid");
        } else if (!hasSpecial) {
            throw new IllegalStateException("[ERROR] Please note that your Password need to contain a Special Letter (e.g. !%$§§%&/) to be valid");
        }
    }

    void retypePassword(char[] original, char[] retyped) {
        Console console = System.console();

        retyped = console.readPassword("[INFO] Please retype your password from before: ");
        validateRetypedPassword(original, retyped);

    }

    //Force the user to retype the password
    void validateRetypedPassword(char[] original, char[] retyped) {

        //Check if the Passwords are match if not the user must retype the password then
        if (retyped.length == 0 || !Arrays.equals(original, retyped)) {
            throw new IllegalArgumentException("[ERROR] The verification password can't be empty and must be equal to the password from before");
        }

        Arrays.fill(original, '\0');
        Arrays.fill(retyped, '\0');

        LogManager.security(SecurityState.SUCCESS, "The password is ok");
        System.out.println("[OK] Your password is correct an fit to all the credentials");
    }

    //Convert the Char back to string to get the Value of it
    private void convertCHARtoString() {
        this.plainPWSD = String.valueOf(this.originalPassword);
        System.out.println("[OK] Passwords are converted");
        LogManager.security(SecurityState.INFO, "The password was converted back to string");
        Arrays.fill(this.originalPassword, '\0');
    }

    //Hash the password with bcrypt and the string value
    private void PlainToHash() {
        this.hashedPWSD = BCrypt.hashpw(plainPWSD, BCrypt.gensalt(15));
    }

    //Store the pwsd in a getter
    public String getHashedPWSD() {
        return this.hashedPWSD;
    }
}