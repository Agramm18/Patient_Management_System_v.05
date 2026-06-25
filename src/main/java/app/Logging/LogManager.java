package app.Logging;
import app.Logging.Enums.ProgrammState.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {

    private static final Logger AUTH = LoggerFactory.getLogger("AUTH");
    private static final Logger CONFIG = LoggerFactory.getLogger("CONFIG");
    private static final Logger SECURITY = LoggerFactory.getLogger("SECURITY");
    private static final Logger SYSTEM = LoggerFactory.getLogger("SYSTEM");
    private static final Logger SQL = LoggerFactory.getLogger("SQL");
    private static final Logger CREDENTIALS = LoggerFactory.getLogger("CREDENTIALS");
    private static final Logger BOOT = LoggerFactory.getLogger("BOOT");
    private static final Logger MENU = LoggerFactory.getLogger("MENU");


    //Building Methods for every Log Case
    public static void boot(BootState state, String logMessage) {
        switch (state) {
            case INFO, SUCCESS -> BOOT.info(logMessage);
            case FAILED -> BOOT.error(logMessage);
        }
    }

    public static void auth(AuthState state, String logMessage) {
        switch (state) {
            case INFO, SUCCESS -> AUTH.info(logMessage);
            case FAILED, WARN -> AUTH.warn(logMessage);
            case ERROR -> AUTH.error(logMessage);
            case DEBUG -> AUTH.debug(logMessage);
        }
    }

    public static void config(ConfigState state, String logMessage) {
        switch(state) {
            case SUCCESS, INFO -> CONFIG.info(logMessage);
            case FAILED, WARN -> CONFIG.warn(logMessage);
            case ERROR -> CONFIG.error(logMessage);
            case DEBUG -> CONFIG.debug(logMessage);
        }
    }

    public static void security(SecurityState state, String logMessage) {
        switch (state) {
            case SUCCESS, INFO -> SECURITY.info(logMessage);
            case FAILED, WARN -> SECURITY.warn(logMessage);
            case ERROR -> SECURITY.error(logMessage);
            case DEBUG -> SECURITY.debug(logMessage);
        }
    }

    public static void sql(SqlState state, String logMessage) {
        switch (state) {
            case SUCCESS, INFO -> SQL.info(logMessage);
            case FAILED, WARN -> SQL.warn(logMessage);
            case ERROR -> SQL.error(logMessage);
            case DEBUG -> SQL.debug(logMessage);
        }
    }

    public static void system(SystemState state, String logMessage) {
        switch (state) {
            case SUCCESS, INFO -> SYSTEM.info(logMessage);
            case FAILED, WARN -> SYSTEM.warn(logMessage);
            case ERROR -> SYSTEM.error(logMessage);
            case DEBUG -> SYSTEM.debug(logMessage);
        }
    }

    public static void recovery(RecoveryState state, String logMessage) {
        switch (state) {
            case SUCCESS, INFO -> SYSTEM.info(logMessage);
            case FAILED, WARN -> SYSTEM.warn(logMessage);
            case ERROR -> SYSTEM.error(logMessage);
            case DEBUG -> SYSTEM.debug(logMessage);
        }
    }

    public static void menu(MenuState state, String logMessage) {
        switch (state) {
            case SUCCESS, INFO, ROUTE_SUCCESS -> MENU.info(logMessage);
            case FAILED, WARN -> MENU.warn(logMessage);
            case ERROR -> MENU.error(logMessage);
            case DEBUG -> MENU.debug(logMessage);
        }
    }

    public static void account(AccountState state, String logMessage) {
        switch (state) {
            case STATUS_PROBLEM -> CREDENTIALS.error(logMessage);
            case USERNAME_NOT_FOUND -> CREDENTIALS.warn(logMessage);
        }
    }

    public static void other(OtherState state, String logMessage) {
        switch (state) {
            case INVALID_PASSWORD -> SECURITY.error(logMessage);
            case INVALID_INPUT, MISSING_INPUT, BLANK_INPUT -> CREDENTIALS.error(logMessage);
        }
    }

}
