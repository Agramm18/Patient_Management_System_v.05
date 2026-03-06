import java.util.Arrays;
import java.util.Scanner;
import java.io.Console;
import org.mindrot.jbcrypt.BCrypt;

public class Account {

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


    public static void HeaderMSGAccount() {
        System.out.println("\n==================================================");
        System.out.println("                 Account System");
        System.out.println("--------------------------------------------------");
        System.out.println("   Login or create a new account to continue");
        System.out.println("==================================================\n");
    }


    public void SetBaseValues(Scanner scanner) {
        while (true) {
            String StartValue;
            System.out.println("Do you have an Account or are you allready registerd/logged in (Y/N)?: ");
            StartValue = scanner.nextLine().trim().toLowerCase();

            //Check if the user have an account
            if (StartValue.isBlank()) {
                throw new IllegalArgumentException("This field can't be empty!");
            } else if (!StartValue.equals("y") && !StartValue.equals("n")) {
                throw new IllegalArgumentException("Only y and n are allowed as values please try again!");
            } else if (StartValue.equals("y")) {
                System.out.println("Please follow the login");
                this.HasAccount = true;
                break;
            } else if (StartValue.equals("n")) {
                System.out.println("Please follow the registration");
                this.HasAccount = false;
                break;
            } else {
                throw new IllegalStateException("It seems that the base value is corrupted");
            }
            
        }
    }

    public void AccountValidation(Scanner scanner) {

            //Input handler to cover passwords
            Console console = System.console();

            //Loop through value of AccountStatus

            //Login Loop if AccountStatus == true
            while (this.HasAccount) {
                String TempUserName;

                System.out.println("Please type in your Username: ");
                TempUserName = scanner.nextLine();

                if (TempUserName.isBlank()) {
                    throw new IllegalArgumentException("Your Username can't be empty!");
                }

                if (console == null) {
                    throw new IllegalStateException("Please run the programm via a terminal");
                }

                char[] PWSD = console.readPassword("Please type in your Password: ");

                if (PWSD.length == 0) {
                    throw new IllegalArgumentException("Your Password can't be empty!");
                }

                char[] VerPWSD = console.readPassword("Please retype your Password for verification: ");

                if (VerPWSD.length == 0 || !Arrays.equals(PWSD, VerPWSD)) {
                    throw new IllegalArgumentException("Your Verification Password is empty or does not equal your Password");
                } else {
                    System.out.println("\nThe Login where Sucsessfull");
                    this.IsAccountValid = true;
                    break;
                }
            }
        
        //Registration Loop if the User didn't have an Account
        while (!this.HasAccount) {
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
                    System.out.println("\nHashed Password " + this.UserPwsdHashed + "\n");

                    System.out.println("Your registration where sucsessfull");
                    break;
                }


        }   
    }
}
