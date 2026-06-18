# Environment Setup

Last synchronized: 2026-06-18.

This document describes the `.env` values used by the current Java implementation.

## File Location

Create `.env` in the project root next to `pom.xml`.

```text
Patient_Management_V5.01/
|-- .env
|-- pom.xml
|-- src/
`-- docs/
```

The file is ignored by Git. Do not commit credentials, starter passwords, bootstrap keys, or recovery keys.

## Current Template

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

`EnvValidationService` currently requires:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `LOCAL_ADMIN_NAME`
- `LOCAL_ADMIN_PASSWORD`
- `LOCAL_ADMIN_EMAIL`
- `ADMIN_NAME`
- `ADMIN_PASSWORD_DEFAULT`
- `ADMIN_EMAIL_DEFAULT`
- `BOOTSTRAP_KEY`
- `RECOVERY_KEY`

`DB_PORT` must be numeric. Every other required value must exist and must not be blank.

## Admin Password Key

`ADMIN_PASSWORD_DEFAULT` is the single required starter password value for the default admin account.
`EnvValidationService` validates this value, and `CreateDefaultAccounts` reads the same value when it creates the default admin account.

## Database Values

- `DB_HOST` is the MySQL host name or IP address.
- `DB_PORT` is the MySQL TCP port.
- `DB_NAME` must match the database created in `DB_SETUP.md`.
- `DB_USER` must be able to connect to the configured database.
- `DB_PASSWORD` is the password for the configured MySQL user.

Older documentation used `DB_PWSD`; that name is not read by the current code.

## Starter Account Values

Starter-account values are used only when an account with role `1` or role `2` is missing.

- Local admin uses `LOCAL_ADMIN_NAME`, `LOCAL_ADMIN_PASSWORD`, and `LOCAL_ADMIN_EMAIL`.
- Admin uses `ADMIN_NAME`, `ADMIN_PASSWORD_DEFAULT`, and `ADMIN_EMAIL_DEFAULT` during creation.
- `BOOTSTRAP_KEY` is stored with created starter accounts.
- Starter passwords are hashed with BCrypt before insert.

The starter-account values are still required during every startup because environment validation checks them even when both accounts already exist.

## Recovery Key

`RECOVERY_KEY` is required during startup.

Current behavior:

1. `HandleRecoveryKey` reads the plain value.
2. The value is hashed with BCrypt.
3. `SetRecoveryKey` inserts or updates `recovery_keys.id = 1`.
4. Recovery input is checked against the stored hash.

The recovery hash changes on each startup because BCrypt creates a new salt, even when the plain key remains unchanged.

Do not reuse database credentials, starter passwords, or the bootstrap key as the recovery key.

## Runtime Validation

Startup stops before authentication when:

- `.env` is missing.
- A required value is missing or blank.
- `DB_PORT` is not numeric.
- The test database connection fails.
- `DBManager` receives invalid runtime values.

Hidden password and recovery-key input requires a real terminal-backed `System.console()`. Running through an IDE configuration without a terminal can fail in authentication flows.

## Related Setup

After creating `.env`, follow `DB_SETUP.md` to create and seed the database.
