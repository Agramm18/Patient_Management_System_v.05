package app;
import app.Bootstrap.BootConfigService;
import app.CLIText.DisplayMessages.StartMSG;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        StartMSG load = new StartMSG();
        load.startMSG();

        BootConfigService run = new BootConfigService();
        run.displayLoader();
        run.systemConfig(scanner);

    }
}