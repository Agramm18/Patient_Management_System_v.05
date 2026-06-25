package app.CLIText.DisplayMessages;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

public class ConfigMSG {

    public void configMSG() {
        System.out.println("\n==================================================");
        System.out.println("              Configuration & Database");
        System.out.println("--------------------------------------------------");
        System.out.println("   Validating .env configuration");
        System.out.println("   Establishing SQL connection");
        System.out.println("==================================================\n");

        LogManager.config(ConfigState.INFO, "Load .env files");
    }
}
