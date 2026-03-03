import java.util.Arrays;
import java.util.Scanner;
import java.io.Console;

public class Account {

    private boolean baseValue;

    private String UserName;
    
    private String UserMail;
    private String UserPhone;

    private String UserPWSD;
    private String UserPWSDConfirm;

    public Account() {

    }

    public static void HeaderMSGAccount() {
        System.out.println("\n==================================================");
        System.out.println("=== Please Login or Register to your Account ===");
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
                this.baseValue = true;
                break;
            } else if (StartValue.equals("n")) {
                this.baseValue = false;
                break;
            }
            
        }
    }


    public void SetAccount(Scanner scanner) {

        //Input handler to cover passwords
        Console console = System.console();

        //Loop through value of baseValue

        while (this.baseValue == true) {
            String UserName;

            System.out.println("Please type in your Username: ");
            UserName = scanner.nextLine();

            if (UserName.isBlank()) {
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
                break;
            }
        }
    }


}
