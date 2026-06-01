package app.Config;

import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

public class HandleRecoveryKey {
    private final Dotenv dotenv;

    private String recoveryKeyPlain;
    private String recoveryKeyHashed;

    public HandleRecoveryKey(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    public void plainKey() {
        System.out.println("\n[INFO] Collecting recovery from .env file\n");
        this.recoveryKeyPlain= dotenv.get("RECOVERY_KEY");
    }

    public void hashedKey() {
        System.out.println("[INFO] Hashing Plain key to an unreadable format");
        this.recoveryKeyHashed = BCrypt.hashpw(this.recoveryKeyPlain, BCrypt.gensalt(12));
    }

    public String getRecoveryKeyHashed() {
        return this.recoveryKeyHashed;
    }
}
