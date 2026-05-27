package app.Auth.Flow;
import app.Auth.Flow.Services.PasswordService.PasswordService;

import java.util.Scanner;


/*
 Sub Controller to call the Password configuration
*/

public class PasswordFlow {

    public String policy(Scanner scanner) {
        PasswordService create = new PasswordService();
        create.userPWSD(scanner);

        return create.getHashedPWSD();
    }
}
