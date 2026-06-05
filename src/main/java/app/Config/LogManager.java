package app.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {

    private static final Logger AUTH = LoggerFactory.getLogger("AUTH");
    private static final Logger CONFIG = LoggerFactory.getLogger("CONFIG");
    private static final Logger SECURITY = LoggerFactory.getLogger("SECURITY");
    private static final Logger ACCESS = LoggerFactory.getLogger("ACCESS");
    private static final Logger systemLogger = LoggerFactory.getLogger("SYSTEM");
    private static final Logger DATABASE = LoggerFactory.getLogger("DATABASE");
    private static final Logger SYSTEM = LoggerFactory.getLogger("MESSAGE");
    private static final Logger SQL = LoggerFactory.getLogger("SQL");

    public enum LogType {
        SQL_EXCEPTION,
        USERNAME_NOT_FOUND,
        SQL_OK,
        SQL_DEBUG,
        SQL_INFO,

        ACCOUNT_STATUS_PROBLEM,

        RECOVERY_FAILED,
        RECOVERY_SUCCESS,

        BOOT_FAILED,
        BOOT_SUCCESS,

        CONFIG_FAILED,
        CONFIG_SUCCESS,
        CONFIG_INFO,

        AUTH_FAILED,
        AUTH_SUCCESS,

        INVALID_INPUT,
        INVALID_PASSWORD,
        MISSING_INPUT,
        BLANK_INPUT,

        MESSAGE,

        SYSTEM_WARN
    }

    public static void log(LogType type, String logMessage) {

        switch (type) {
            case SQL_EXCEPTION:
                DATABASE.error(logMessage);
                break;

            case SQL_OK:
                DATABASE.info(logMessage);
                break;

            case SQL_DEBUG:
                DATABASE.debug(logMessage);
                break;

            case BOOT_FAILED:
                systemLogger.error(logMessage);
                break;

            case RECOVERY_FAILED:
            case ACCOUNT_STATUS_PROBLEM:
                SECURITY.warn(logMessage);
                break;

            case RECOVERY_SUCCESS:
                SECURITY.info(logMessage);
                break;

            case AUTH_FAILED:
                AUTH.warn(logMessage);

            case MISSING_INPUT:
            case INVALID_INPUT:
            case BLANK_INPUT:
                systemLogger.warn(logMessage);
                break;

            case AUTH_SUCCESS:
            case BOOT_SUCCESS:
            case CONFIG_SUCCESS:
                systemLogger.info(logMessage);
                break;

            case CONFIG_FAILED:
                CONFIG.error(logMessage);
                break;

            case INVALID_PASSWORD:
            case USERNAME_NOT_FOUND:
                AUTH.warn(logMessage);
                break;

            case MESSAGE:
                SYSTEM.info(logMessage);
                break;

            case CONFIG_INFO:
                CONFIG.info(logMessage);
                break;

            case SYSTEM_WARN:
                SYSTEM.warn(logMessage);
                break;

            case SQL_INFO:
                SQL.info(logMessage);
                break;
        }
    }

}
