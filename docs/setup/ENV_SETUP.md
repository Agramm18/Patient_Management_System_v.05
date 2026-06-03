# Environment Setup

Last synchronized: 2026-06-03.

This file documents the required `.env` configuration for the Patient Management System V5.01 project.

## File Location

Create a file named `.env` in the project root, next to `pom.xml`.

```text
Patient_Management_System_v.05/
|-- .env
|-- pom.xml
|-- src/
`-- docs/
```

Do not commit real local credentials, production credentials, or bootstrap secrets.

## Required Variables

Use the following template:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=patient_management_v5
DB_USER=your_mysql_user
DB_PWSD=your_mysql_password

LOCAL_ADMIN_NAME=local_admin
LOCAL_ADMIN_PWSD=change_this_default_password
LOCAL_ADMIN_EMAIL=local_admin@example.com

ADMIN_NAME=admin
ADMIN_PWSD_DEFAULT=change_this_default_password
ADMIN_EMAIL_DEFAULT=admin@example.com

BOOTSTRAP_KEY=change_this_bootstrap_key
RECOVERY_KEY=change_this_recovery_key
```

## Database Values

- `DB_HOST` is the hostname or IP address of the MySQL server.
- `DB_PORT` must be numeric.
- `DB_NAME` must match the database created in [DB_SETUP.md](./DB_SETUP.md).
- `DB_USER` must have permission to connect to the configured database.
- `DB_PWSD` is the password for `DB_USER`.

## Starter Account Values

The default account values are used only when one or both starter accounts are missing from the database.

- `LOCAL_ADMIN_NAME`, `LOCAL_ADMIN_PWSD`, and `LOCAL_ADMIN_EMAIL` are used for the `local_admin` starter account.
- `ADMIN_NAME`, `ADMIN_PWSD_DEFAULT`, and `ADMIN_EMAIL_DEFAULT` are used for the `admin` starter account.
- `BOOTSTRAP_KEY` is stored with starter accounts so bootstrap-created accounts can be identified later.
- `RECOVERY_KEY` is used by the recovery flow for system-account password reset.

Both starter account passwords are hashed before they are stored in the database.

## Recovery Key Value

`RECOVERY_KEY` is required during startup.

Current runtime behavior:

- `EnvValidationService` fails startup when `RECOVERY_KEY` is missing or blank.
- `HandleRecoveryKey` reads the plain key from `.env`.
- `HandleRecoveryKey` hashes the key with BCrypt.
- `SetRecoveryKey` stores the hash in `recovery_keys` with `id = 1`.
- Later startup runs update the same recovery-key row.
- `RecoveryFlow` checks user input against the stored BCrypt hash before password reset.

Do not reuse the database password, starter account passwords, or bootstrap key as the recovery key.

## Runtime Validation

During startup, `EnvValidationService` checks that the `.env` file exists and that required values are present. `SQLValidationService` then builds the database connection configuration from the loaded values.

If environment validation, database connection validation, or global DB runtime initialization fails, startup stops before authentication begins.

## Related Setup

After creating `.env`, follow [DB_SETUP.md](./DB_SETUP.md) to create the development database, tables, and seed data.
