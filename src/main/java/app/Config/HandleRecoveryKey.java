package app.Config;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

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
        LogManager.log(LogType.MESSAGE, "Collecting recovery from .env file");
        this.recoveryKeyPlain= dotenv.get("RECOVERY_KEY");
    }

    public void hashedKey() {
        LogManager.log(LogType.MESSAGE, "Hashing Plain key to an unreadable format");
        this.recoveryKeyHashed = BCrypt.hashpw(this.recoveryKeyPlain, BCrypt.gensalt(12));
    }

    public String getRecoveryKeyHashed() {
        return this.recoveryKeyHashed;
    }
}
