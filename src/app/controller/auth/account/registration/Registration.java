package app.controller.auth.account.registration;
import java.util.Scanner;

import java.util.Arrays;
import java.io.Console;
import org.mindrot.jbcrypt.BCrypt;

public class Registration {

    private boolean HasAccount;

    private boolean IsAccountValid;

    private String UserName;
    
    private String UserMail;
    private String UserPhone;

    private String UserPwsdPlain;
    private String UserPWSDConfirmPlain;

    private String UserPwsdHashed;

    //Password Credentials
    private boolean FitPWSDLength;
    private boolean ContainsUpperLetters;
    private boolean ContainsLowerLetters;
    private boolean ContainsSpecialLetters;
    private boolean ContainsNumbers;


    public void RegisterUser(Scanner scanner) {

        Console console = System.console();

        //Registration Loop if the User didn't have an Account
        while (true) {
                String TempUserName;
                String TempEmail;
                String TempPhoneNumber;

                //Password credentials
                boolean HasUpper = false;
                boolean HasLower = false;
                boolean HasSpecial = false;
                boolean HasNumbers = false;
                boolean FitLength = false;

                System.out.println("Please set a user name for your account: ");
                TempUserName = scanner.nextLine();

                if (TempUserName.isBlank()) {
                    throw new IllegalArgumentException("Your Username can't be empty!");

                }

                System.out.println("Please set your email adress for your account: ");
                TempEmail = scanner.nextLine();

                if (TempEmail.isBlank()) {
                    throw new IllegalArgumentException("Your email adress can't be empty!");
                }

                System.out.println("Please set a phone number for your account: ");
                TempPhoneNumber = scanner.nextLine();

                if (TempPhoneNumber.isBlank()) {
                    throw new IllegalArgumentException("Your Phone Number can't be empty!");
                }
                
                //Create pwsd with inivisible user input
                char[] PWSD_REG = console.readPassword("Please set a password for your account: ");

                //Check if the password fit to the credentaisl
                if (PWSD_REG.length == 0) {
                    throw new IllegalArgumentException("Your Password can't be empty!");
                } else if (PWSD_REG.length < 10) {
                    throw new IllegalArgumentException("Your password must bee at least 10 letters long");
                } else {
                    FitLength = true;
                }

                //Check if the PWSD fit to the credentials if thats the case the default vars will be sett to true
                for (char c : PWSD_REG) {
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
 
                char[] VerPWSD_REG = console.readPassword("Please retype your password from before: ");

                if (VerPWSD_REG.length == 0 || !Arrays.equals(PWSD_REG, VerPWSD_REG)) {
                    throw new IllegalArgumentException("The verification password can't be empty and must be equal to the password from before");
                } else {
                    System.out.println("The base values for the Database are setted");
                    this.IsAccountValid = true;

                    this.UserName = TempUserName;
                    this.UserMail = TempEmail;
                    this.UserPhone = TempPhoneNumber;

                    System.out.println("Convert Password Values to String");
                    this.UserPwsdPlain = String.valueOf(PWSD_REG);
                    this.UserPWSDConfirmPlain = String.valueOf(VerPWSD_REG);
                    System.out.println("Passwords are converted");

                    System.out.println("Your Password is collected and will be Hashed now");
                    
                    this.UserPwsdHashed = BCrypt.hashpw(UserPwsdPlain, BCrypt.gensalt(15));

                    System.out.println("The Password where Hashed sucsessfully");

                    System.out.println("Your registration where sucsessfull");
                    break;
                }
        }
    }
}
