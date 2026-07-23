package app.Auth.Flow.Services.LoginService;

import app.Repository.LoginRepository.CheckUserInDB;

import java.sql.SQLException;

public class CheckInput {

    //Check if the User exists in the DB
    boolean account(String username) throws SQLException {
        CheckUserInDB check = new CheckUserInDB();
        return check.checkUserInDB(username);
    }

    //Check if the entered password matches with the password in the db
    boolean password(String password, String username) throws SQLException {
        CheckUserInDB check = new CheckUserInDB();
        return check.checkPWSD(password, username);
    }

    //Check the Account status
    String status(String username) throws SQLException {
        CheckUserInDB check = new CheckUserInDB();
        return check.checkUserStatus(username);
    }
}
