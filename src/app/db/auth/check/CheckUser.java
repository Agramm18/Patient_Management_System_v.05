package app.db.auth.check;

import app.menu.MenuScreen;
import app.menu.MenuValidation;

//Libary imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Scanner;

public class CheckUser {
    private String UserName;
    private String UserPwsdPlain;
    private Connection connection;

    //Load Login values
    public CheckUser(String UserName, String UserPwsdPlain, Connection connection) {
        this.UserName = UserName;
        this.UserPwsdPlain = UserPwsdPlain;
        this.connection = connection;
    }

    public void CollectDBValues(Scanner scanner) {
        System.out.println("\nThe values from the database are loaded and compared with the entered values\n");
        try {

            String GetData = "Select password_hash FROM accounts WHERE account_name = ?";

            //Collect User from DB
            PreparedStatement CollectData = connection.prepareStatement(GetData);
            CollectData.setString(1, this.UserName);

            //Execute Querry
            ResultSet rs = CollectData.executeQuery();

            //Check if User exsist in DB
            if (!rs.next()) {
                throw new IllegalArgumentException("The entered User does not exsist in the DB!!");
            }

            //Collect hashed pwsd from db
            String StoredHash = rs.getString("password_hash");
            
            if(BCrypt.checkpw(this.UserPwsdPlain, StoredHash)) {
                System.out.println("\nThe Login where a sucsess the username and the pwsd fits to the db\n");
                
                MenuScreen collect = new MenuScreen();
                collect.show();

                MenuValidation start = new MenuValidation();
                start.CollectMenuChoice(scanner);

            } else {
                throw new IllegalArgumentException("The entered Password is not correct please try again...");
            }
        } catch(Exception error) {
            System.out.println("\nThere seems to be something wrong with the user input and the DB synchronisation");
            System.out.println("Error: " + error + "\n");
        }
    }
    
}
