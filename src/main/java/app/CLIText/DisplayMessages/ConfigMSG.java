package app.CLIText.DisplayMessages;

import app.Config.LogManager;
import app.Config.LogManager.LogType;

public class ConfigMSG {

    public void configMSG() {
        System.out.println("\n==================================================");
        System.out.println("              Configuration & Database");
        System.out.println("--------------------------------------------------");
        System.out.println("   Validating .env configuration");
        System.out.println("   Establishing SQL connection");
        System.out.println("==================================================\n");

        LogManager.log(LogType.MESSAGE, "Load .env files");
    }
}
