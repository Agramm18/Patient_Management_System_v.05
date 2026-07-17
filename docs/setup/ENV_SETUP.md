# Environment Setup

Last synchronized: 2026-07-18.

This document describes the `.env` values required by the current Java implementation.

## Prerequisites

- Java 21 or a newer JDK capable of compiling the Java 21 source target
- MySQL accessible from the application host
- Maven through the included wrapper or a global Maven installation
- A real terminal for hidden password and recovery-key input

## File Location

Create `.env` in the project root next to `pom.xml`.

```text
Patient_Management_System_v.05/
|-- .env
|-- pom.xml
|-- mvnw
|-- mvnw.cmd
|-- src/
`-- docs/
```

`.env` is ignored by Git. Do not commit database credentials, starter passwords, bootstrap keys, or recovery keys.

## Template

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=patient_management_v5
DB_USER=your_mysql_user
DB_PASSWORD=your_mysql_password

LOCAL_ADMIN_NAME=local_admin
LOCAL_ADMIN_PASSWORD=replace_with_a_strong_starter_password
LOCAL_ADMIN_EMAIL=local_admin@example.com

ADMIN_NAME=admin
ADMIN_PASSWORD_DEFAULT=replace_with_a_strong_starter_password
ADMIN_EMAIL_DEFAULT=admin@example.com

BOOTSTRAP_KEY=replace_with_a_bootstrap_key
RECOVERY_KEY=replace_with_a_recovery_key
```

## Required Values

`EnvValidationService` loads all values into the `EnvSetup` record. Every value is required and must not be blank.

| Key | Purpose |
| --- | --- |
| `DB_HOST` | MySQL host name or IP address |
| `DB_PORT` | MySQL TCP port as an integer from 1 through 65535 |
| `DB_NAME` | Database name; must match `DB_SETUP.md` |
| `DB_USER` | MySQL user with access to the configured database |
| `DB_PASSWORD` | Password for `DB_USER` |
| `LOCAL_ADMIN_NAME` | Starter local-admin account name |
| `LOCAL_ADMIN_PASSWORD` | Starter local-admin password before BCrypt hashing |
| `LOCAL_ADMIN_EMAIL` | Starter local-admin email address |
| `ADMIN_NAME` | Starter admin account name |
| `ADMIN_PASSWORD_DEFAULT` | Starter admin password before BCrypt hashing |
| `ADMIN_EMAIL_DEFAULT` | Starter admin email address |
| `BOOTSTRAP_KEY` | Value stored with newly created starter accounts |
| `RECOVERY_KEY` | Secret used to authorize password recovery |

Legacy names such as `DB_PWSD`, `LOCAL_ADMIN_PWSD`, and `ADMIN_PWSD_DEFAULT` are not read by the current code.

## Validation Flow

1. `EnvValidationService` checks for `.env` in the current project root.
2. dotenv-java loads the file.
3. `DB_PORT` is parsed as an integer.
4. `EnvSetup` rejects missing or blank values.
5. `EnvSetup` rejects ports below 1 or above 65535.
6. Validated database values are exposed through `EnvValidationService` getters.
7. `SQLValidationService` builds and tests the JDBC connection.
8. `DBManager` stores the connection settings for repository calls.

Startup stops before authentication when the file is missing, a value is missing or blank, the port is invalid, the database connection fails, or `DBManager` receives invalid values.

## Starter Accounts

Starter-account values are used when no account exists for role ID 1 or role ID 2:

- Local admin uses `LOCAL_ADMIN_NAME`, `LOCAL_ADMIN_PASSWORD`, and `LOCAL_ADMIN_EMAIL`.
- Admin uses `ADMIN_NAME`, `ADMIN_PASSWORD_DEFAULT`, and `ADMIN_EMAIL_DEFAULT`.
- `BOOTSTRAP_KEY` is stored in each newly created starter account.
- Starter passwords are hashed with BCrypt cost 12.

All starter-account keys remain mandatory on every startup because `EnvSetup` validates them even when both accounts already exist.

The current fallback password-hash checks use fixed account IDs 1 and 2. Databases whose starter accounts have different IDs are not validated reliably by that fallback.

## Recovery Key

On every successful configuration run:

1. `HandleRecoveryKey` reads `RECOVERY_KEY`.
2. BCrypt hashes it with cost 12 and a new random salt.
3. `SetRecoveryKey` inserts or updates `recovery_keys.id = 1`.
4. Recovery input is later verified against that stored hash.

The stored hash changes on each startup even when the plain recovery key is unchanged. Do not reuse database credentials, starter passwords, or the bootstrap key as the recovery key.

## Terminal Requirement

`System.console()` is used for login passwords, password creation, password re-entry, and recovery-key input. Many IDE run configurations do not provide a terminal-backed console. Use a real PowerShell, Command Prompt, Bash, or another terminal when running interactive authentication flows.

## Setup and Run Order

1. Create `.env` from the template.
2. Create and seed MySQL by following `DB_SETUP.md`.
3. Run the tests.
4. Start the application from a terminal.

PowerShell with the Maven Wrapper:

```powershell
.\mvnw.cmd test
.\mvnw.cmd exec:java
```

Global Maven alternative:

```powershell
mvn test
mvn exec:java
```

In the current Windows PowerShell validation environment, `mvnw.cmd` failed inside the generated wrapper script while checking a null filesystem-link target. The configured Maven 3.9.16 distribution itself ran the 53-test suite successfully. If the same wrapper error occurs locally, use a global Maven installation until the wrapper script is corrected.
