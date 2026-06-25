package app.Config;

// Record to store .env values separate from the class
public record EnvSetup(
        String dbHost,
        int dbPort,
        String dbName,
        String dbUser,
        String dbPassword,
        String localAdminName,
        String localAdminPassword,
        String localAdminEmail,
        String adminName,
        String adminPassword,
        String adminEmail,
        String bootstrapKey,
        String recoveryKey
) {
    //Check every .env value if they exists and they are not empty or null
    public EnvSetup {
        requireNotBlank(dbHost, "DB_HOST");
        requireNotBlank(dbName, "DB_NAME");
        requireNotBlank(dbUser, "DB_USER");
        requireNotBlank(dbPassword, "DB_PASSWORD");

        requireNotBlank(localAdminName, "LOCAL_ADMIN_NAME");
        requireNotBlank(localAdminPassword, "LOCAL_ADMIN_PASSWORD");
        requireNotBlank(localAdminEmail, "LOCAL_ADMIN_EMAIL");

        requireNotBlank(adminName, "ADMIN_NAME");
        requireNotBlank(adminPassword, "ADMIN_PASSWORD_DEFAULT");
        requireNotBlank(adminEmail, "ADMIN_EMAIL_DEFAULT");

        requireNotBlank(bootstrapKey, "BOOTSTRAP_KEY");
        requireNotBlank(recoveryKey, "RECOVERY_KEY");

        if (dbPort <= 0 || dbPort > 65535) {
            throw new IllegalStateException("DB_PORT must be between 1 and 65535");
        }
    }

    //Method for the .env check
    private static void requireNotBlank(String value, String key) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(key + " is missing or empty");
        }
    }
}
