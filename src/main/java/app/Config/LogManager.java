package app.Config;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {

    private static final Logger AUTH = LoggerFactory.getLogger("AUTH");
    private static final Logger CONFIG = LoggerFactory.getLogger("CONFIG");
    private static final Logger SECURITY = LoggerFactory.getLogger("SECURITY");
    private static final Logger ACCESS = LoggerFactory.getLogger("ACCESS");
    private static final Logger systemLogger = LoggerFactory.getLogger("SYSTEM");
    private static final Logger DATABASE = LoggerFactory.getLogger("DATABASE");
    private static final Logger SYSTEM = LoggerFactory.getLogger("SYSTEM");
    private static final Logger SQL = LoggerFactory.getLogger("SQL");
    private static final Logger CREDENTIALS = LoggerFactory.getLogger("CREDENTIALS");
    private static final Logger BOOT = LoggerFactory.getLogger("BOOT");

    public enum LogType {
        SQL_EXCEPTION,
        USERNAME_NOT_FOUND,
        SQL_OK,
        SQL_DEBUG,
        SQL_INFO,

        RECOVERY_FAILED,
        RECOVERY_SUCCESS,
        RECOVERY_INFO,

        BOOT_FAILED,
        BOOT_SUCCESS,
        BOOT_INFO,

        CONFIG_FAILED,
        CONFIG_SUCCESS,
        CONFIG_INFO,

        AUTH_FAILED,
        AUTH_SUCCESS,
        AUTH_INFO,

        INVALID_INPUT,
        INVALID_PASSWORD,
        MISSING_INPUT,
        BLANK_INPUT,

        ACCOUNT_STATUS_PROBLEM,
        ACCOUNT_USERNAME_NOT_FOUND,

        MESSAGE,

        SYSTEM_WARN,
        SYSTEM_INFO
    }

    public static void log(LogType type, String logMessage) {

        switch (type) {

            //Info Logs
            case SQL_INFO:
                SQL.info(logMessage);
                break;

            case RECOVERY_INFO:
                AUTH.info(logMessage);
                break;

            case SYSTEM_INFO:
                SYSTEM.info(logMessage);
                break;

            case BOOT_INFO:
                BOOT.info(logMessage);
                break;

            case CONFIG_INFO:
                CONFIG.info(logMessage);
                break;

            //Success Logs
            case SQL_OK:
                SQL.info(logMessage);
                break;

            case RECOVERY_SUCCESS:
                SECURITY.info(logMessage);
                break;

            case BOOT_SUCCESS:
                BOOT.info(logMessage);
                break;

            case AUTH_SUCCESS:
                AUTH.info(logMessage);
                break;

            //Error Logs
            case SQL_EXCEPTION:
                SQL.error(logMessage);
                break;

            case RECOVERY_FAILED:
                SECURITY.error(logMessage);
                break;

            case BOOT_FAILED:
                BOOT.warn(logMessage);
                break;

            case AUTH_FAILED:
                AUTH.error(logMessage);
                break;


            //Other Errors
            case ACCOUNT_STATUS_PROBLEM:
                DATABASE.error(logMessage);
                break;

            case ACCOUNT_USERNAME_NOT_FOUND:
                DATABASE.error(logMessage);
                break;

            case INVALID_INPUT:
            case INVALID_PASSWORD:
            case MISSING_INPUT:
            case BLANK_INPUT:
                CREDENTIALS.error(logMessage);
                break;
        }
    }

}
