package app.Auth.Flow;
import app.Auth.Flow.Services.PasswordService.PasswordService;

import java.util.Scanner;

public class PasswordFlow {

    public String Policy(Scanner scanner) {
        PasswordService create = new PasswordService();
        create.UserPWSD(scanner);

        return create.getHashedPWSD();
    }
}
