package app.controller.auth.account.login;

import app.db.auth.check.CheckUser;

import java.util.Scanner;
import java.sql.Connection;
import java.io.Console;
import java.util.Arrays;

public class Login {

    private boolean HasAccount;

    private boolean IsAccountValid;

    private String UserName;
    
    private String UserMail;
    private String UserPhone;

    private String UserPwsdPlain;
    private String UserPWSDConfirmPlain;

    private String UserPwsdHashed;

    private Connection connection;

    public Login(Connection connection) {
        this.connection = connection;
    }

    //Password Credentials
    private boolean FitPWSDLength;
    private boolean ContainsUpperLetters;
    private boolean ContainsLowerLetters;
    private boolean ContainsSpecialLetters;
    private boolean ContainsNumbers;

    public void LoginUser(Scanner scanner) {
            //Input handler to cover passwords
            Console console = System.console();

            //Loop through value of AccountStatus

            //Login Loop if AccountStatus == true
            while (true) {
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
                    //Set base values and convert char to str
                    this.UserName = TempUserName;
                    this.UserPwsdPlain = String.valueOf(PWSD);

                    //Build constructor and load it to db logic
                    CheckUser load = new CheckUser(
                        this.UserName,
                        this.UserPwsdPlain,
                        connection
                    );

                    load.CollectDBValues(scanner);

                    break;
                }
            }
    }
}
