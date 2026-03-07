package app.controller.auth.account.Password;
import app.controller.auth.account.registration.Registration;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class SetPassword {
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

    public void PlainPWSD(Scanner scanner) {
        Console console = System.console();

        //Create pwsd with inivisible user input
        PWSDChar = console.readPassword("Please set a password for your account: ");
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
            throw new IllegalArgumentException("Your Password can't be empty!");
        } else if (this.PWSDChar.length < 10) {
            throw new IllegalArgumentException("Your password must bee at least 10 letters long");
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
            throw new IllegalArgumentException("Please note that your Password need to contain Uppercase Letters to be valid");
        } else if (!HasLower) {
            throw new IllegalArgumentException("Please note that your Password need to contain Lowercase Letters to be valid");
        } else if (!HasNumbers) {
            throw new IllegalArgumentException("Please note that your Password need to contain Numbers to be valid");
        } else if (!HasSpecial) {
            throw new IllegalArgumentException("Please note that your Password need to contain a Special Letter (e.g. !%$§§%&/) to be valid");
        }

    }

    public void VerifyPWSD() {
        Console console = System.console();

        this.VerifyPWSD = console.readPassword("Please retype your password from before: ");

        if (this.VerifyPWSD.length == 0 || !Arrays.equals(this.PWSDChar, this.VerifyPWSD)) {
            throw new IllegalArgumentException("The verification password can't be empty and must be equal to the password from before");
        } else {
            ConvertCharToString();
            PlainToHash();
        }
    }

    public void ConvertCharToString() {
        System.out.println("Convert Password Values to String");
        this.PlainPWSD = String.valueOf(PWSDChar);
        System.out.println("Passwords are converted");
    }

    public void PlainToHash() {
        System.out.println("Your Password is collected and will be Hashed now");
                    
        this.HashedPWSD = BCrypt.hashpw(PlainPWSD, BCrypt.gensalt(15));

        System.out.println("The Password where Hashed sucsessfully");
    }
}
