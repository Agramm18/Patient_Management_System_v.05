package app.Auth;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class PasswordService {
    String PlainPWSD;
    String HashedPWSD;
    char[] PWSDChar;
    char[] VerifyPWSD;

    //Password Credentials
    private boolean FitPWSDLength;
    private boolean ContainsUpperLetters;
    private boolean ContainsLowerLetters;
    private boolean ContainsSpecialLetters;
    private boolean ContainsNumbers;


    public void UserPWSD(Scanner scanner) {
        System.out.println("[INFO] Running through password creation");
        System.out.println("[INFO] Creating plain text PWSD");
        PlainPWSD(scanner);
        System.out.println("[INFO] Running through validation process and validate if the password is valid");
        ValidatePWSD();
        System.out.println("[INFO] Please Retype your Password from before");
        RetypePWSD();
        System.out.println("[INFO] Converting Char back to string");
        ConvertCharToString();
        System.out.println("[INFO] Hashing String values to an unreadable format");
        PlainToHash();
        System.out.println("[OK] Password where hashed successfully");
    }

    public void PlainPWSD(Scanner scanner) {

        while (true) {
            try {
                Console console = System.console();

                if (console != null) {

                    //Create pwsd with inivisible user input
                    PWSDChar = console.readPassword("[INFO] Please set a password for your account: ");
                    break;
                } else {
                    System.out.println("[INFO] Please set a password for your account");
                    PWSDChar = scanner.nextLine().toCharArray();
                    break;
                }
            } catch (Exception error) {
                System.out.println(error.getMessage());
            }
        }
    }

    public void ValidatePWSD() {
        //Password credentials
        boolean HasUpper = false;
        boolean HasLower = false;
        boolean HasSpecial = false;
        boolean HasNumbers = false;
        boolean FitLength = false;

        //Check if the password fit to the credentaisl
        if (this.PWSDChar.length == 0) {
            throw new IllegalArgumentException("[ERROR] Your Password can't be empty!");
        } else if (this.PWSDChar.length < 10) {
            throw new IllegalArgumentException("[ERROR] Your password must bee at least 10 letters long");
        } else {
            FitLength = true;
        }

        //Check if the PWSD fit to the credentials if thats the case the default vars will be sett to true
        for (char c : PWSDChar) {
            if (Character.isUpperCase(c)) {
                HasUpper = true;
            }

            if (Character.isLowerCase(c)) {
                HasLower = true;
            }

            if (Character.isDigit(c)) {
                HasNumbers = true;
            }

            if (!Character.isLetterOrDigit(c)) {
                HasSpecial = true;
            }
        }

        //Throw errors based on the value
        if (!HasUpper) {
            throw new IllegalArgumentException("[ERROR] Please note that your Password need to contain Uppercase Letters to be valid");
        } else if (!HasLower) {
            throw new IllegalArgumentException("[ERROR] Please note that your Password need to contain Lowercase Letters to be valid");
        } else if (!HasNumbers) {
            throw new IllegalArgumentException("[ERROR] Please note that your Password need to contain Numbers to be valid");
        } else if (!HasSpecial) {
            throw new IllegalArgumentException("[ERROR] Please note that your Password need to contain a Special Letter (e.g. !%$§§%&/) to be valid");
        }

    }

    public void RetypePWSD() {
        Console console = System.console();

        this.VerifyPWSD = console.readPassword("[INFO] Please retype your password from before: ");

        if (this.VerifyPWSD.length == 0 || !Arrays.equals(this.PWSDChar, this.VerifyPWSD)) {
            throw new IllegalArgumentException("[ERROR] The verification password can't be empty and must be equal to the password from before");
        } else {
            System.out.println("[OK] Your password is correct an fit to all the credentials");
            ConvertCharToString();
            PlainToHash();
        }
    }

    public void ConvertCharToString() {
        System.out.println("[INFO] Convert Password Values to String...");
        this.PlainPWSD = String.valueOf(PWSDChar);
        System.out.println("[OK] Passwords are converted");
    }

    public void PlainToHash() {
        System.out.println("[INFO] Your Password is collected and will be Hashed now");

        this.HashedPWSD = BCrypt.hashpw(PlainPWSD, BCrypt.gensalt(15));

        System.out.println("[OK] The Password where Hashed sucsessfully");
    }

    public String getHashedPWSD() {
        return this.HashedPWSD;
    }
}
