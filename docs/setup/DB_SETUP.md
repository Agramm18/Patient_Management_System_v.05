# Database Setup

Last synchronized: 2026-07-23.

This document defines the MySQL schema and reference IDs expected by the current Java implementation.

## Intended Setup Order

Follow this order for a fresh setup. The application validates `.env` before it can use the database, and the SQL tables below depend on each other through foreign keys.

1. Create `.env` in the project root by following `ENV_SETUP.md`.
2. Set `DB_NAME=patient_management_v5` in `.env`, or change the database name below to match your `.env` value.
3. Make sure `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD` are set before starting the application.
4. Make sure `DB_PORT` is numeric and between 1 and 65535, for example `3306`.
5. Connect to MySQL with a user that can create and use the configured database.
6. Run the database creation statement below.
7. Run the table creation and seed statements in the exact order shown in this document.
8. Start the application only after the schema and reference data exist.

`EnvValidationService` looks for `.env` in the project root and builds an `EnvSetup` record from all 13 required values. `EnvSetup` rejects missing or blank values and ports outside 1 through 65535. Valid database settings are then exposed to `SQLValidationService` and `DBManager`. If `.env` is missing or invalid, startup stops before the database can be used.

The application does not create this schema or seed reference data. There are no versioned migrations. SQL repository errors are often logged or printed and then swallowed, so successful startup is not proof that every table and column below is usable.

## Database Creation

```mysql
CREATE DATABASE IF NOT EXISTS patient_management_v5;
USE patient_management_v5;
```

The database name used here must match `DB_NAME` in `.env`.

Create and seed tables in the order shown because later tables use foreign keys.

## Required Reference IDs

The Java code currently uses numeric IDs directly. The seeded names also matter because login status routing compares the `account_status.status` text with exact string values.

### Roles

| ID | Value |
| --- | --- |
| 1 | `local_admin` |
| 2 | `admin` |
| 9 | `intern` |
| 10 | `apprentice` |

Role IDs 1 and 2 control starter-account detection and the only active parent-menu routes. Role ID 9 is written for registered accounts and access requests. Role ID 10 is the database default. The other seeded roles are displayed in the role menu and represented by `AccountRoles`, but the current request flow does not persist a selected role.

### Departments

| ID | Value |
| --- | --- |
| 5 | `IT` |
| 11 | `System` |
| 12 | `unassigned` |

Pending users may request department IDs 1 through 11. Registered accounts are inserted with department ID 12. Starter accounts use IDs 11 and 5.

### Account Statuses

| ID | Value |
| --- | --- |
| 1 | `active` |
| 2 | `disabled` |
| 3 | `pending` |
| 4 | `locked` |
| 5 | `on_quarantine` |
| 6 | `waiting_for_password_change` |
| 7 | `suspicious` |

Status ID `7` is required by `ExecutePWSDPolicy.suspicious`.

## Roles

```mysql
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO roles (id, role_name, role_description)
VALUES
    (1, 'local_admin', 'Full infrastructure and system access'),
    (2, 'admin', 'Administrative access for user and system management'),
    (3, 'it_specialist', 'IT specialist access for system maintenance'),
    (4, 'it_support', 'Technical support access'),
    (5, 'doctor', 'Medical access to patient treatment data'),
    (6, 'nurse', 'Medical care access'),
    (7, 'finance', 'Financial and billing access'),
    (8, 'office_staff', 'Administrative office access'),
    (9, 'intern', 'Restricted training access'),
    (10, 'apprentice', 'Limited operational training access');
```

## Account Statuses

```mysql
CREATE TABLE account_status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO account_status (id, status)
VALUES
    (1, 'active'),
    (2, 'disabled'),
    (3, 'pending'),
    (4, 'locked'),
    (5, 'on_quarantine'),
    (6, 'waiting_for_password_change'),
    (7, 'suspicious');
```

## Departments

```mysql
CREATE TABLE departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL UNIQUE,
    department_description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO departments (id, department_name, department_description)
VALUES
    (1, 'Medical', 'Medical treatment and patient care'),
    (2, 'Emergency', 'Emergency response and urgent care'),
    (3, 'Laboratory', 'Lab tests, diagnostics, and analysis'),
    (4, 'Pharmacy', 'Medication, prescriptions, and stock'),
    (5, 'IT', 'Software, systems, and technical support'),
    (6, 'Security', 'Security, access control, and monitoring'),
    (7, 'Finance', 'Billing, accounting, and financial tasks'),
    (8, 'Office', 'Office work and administration support'),
    (9, 'Administration', 'Management and organizational tasks'),
    (10, 'Training', 'Training, education, and onboarding'),
    (11, 'System', 'System administration and internal system roles'),
    (12, 'unassigned', 'Default department for new accounts');
```

## Recovery Keys

```mysql
CREATE TABLE recovery_keys (
    id INT AUTO_INCREMENT PRIMARY KEY,
    recovery_key_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    key_scope VARCHAR(255) NOT NULL DEFAULT 'resetting system accounts',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

During configuration, `SetRecoveryKey` attempts to insert or update row ID `1`. It writes only `recovery_key_hash`; the defaults for `is_active` and `key_scope` are therefore required when the row is first inserted. Current recovery code does not read or enforce either of those two fields.

`SetRecoveryKey` catches SQL exceptions without returning failure to `ConfigController`, so an unsuccessful upsert can be followed by the remaining bootstrap logic.

After a valid key check, `FindRecoverableUser` displays only rows with `is_system_account = true`, but `SelectUserForRecover` looks up only `account_name`. The final recovery target can therefore be any existing account. A missing row or null hash is not validated before `BCrypt.checkpw`.

## Accounts

```mysql
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    department INT NOT NULL DEFAULT 12,
    user_job VARCHAR(50) NOT NULL DEFAULT 'unassigned',
    user_role INT NOT NULL DEFAULT 10,
    account_status INT NOT NULL DEFAULT 2,
    permission VARCHAR(50) NOT NULL DEFAULT 'read_only',
    is_system_account BOOLEAN NOT NULL DEFAULT FALSE,
    has_access_to_menu BOOLEAN NOT NULL DEFAULT FALSE,
    password_hash VARCHAR(255) NOT NULL,
    requires_password_change BOOLEAN NOT NULL DEFAULT FALSE,
    failed_password_attempts INT NOT NULL DEFAULT 0,
    bootstrap_key VARCHAR(255) DEFAULT NULL,
    recovery_key_id INT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (department) REFERENCES departments(id),
    FOREIGN KEY (user_role) REFERENCES roles(id),
    FOREIGN KEY (account_status) REFERENCES account_status(id),
    FOREIGN KEY (recovery_key_id) REFERENCES recovery_keys(id)
);
```

The defaults for `user_job`, `permission`, and `requires_password_change` are required by the current registration insert because `CreateAccount` omits those columns. The `failed_password_attempts` column is not read or updated; failed-password policy reads `login_attempts` instead.

Current validation and schema limits are not fully aligned:

- Registration accepts email strings up to 253 characters, while this schema stores at most 100. A value that passes Java validation can therefore fail at insert time.
- Login does not enforce the 50-character `account_name` limit before repository calls.
- `BOOTSTRAP_KEY` is not length-validated and is stored unchanged in `bootstrap_key`, whose limit is 255 characters.
- Username and email uniqueness are enforced only by the database.

## Login Attempts

```mysql
CREATE TABLE login_attempts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NULL,
    entered_username VARCHAR(50) NOT NULL,
    failure_reason VARCHAR(50) NULL,
    is_success BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (account_id) REFERENCES accounts(id)
        ON DELETE SET NULL
);
```

Unknown usernames are stored with `account_id = NULL`.

`LoginFlow` inserts the `StoreLogs` result after each completed login attempt. The current values have several important consequences:

- An unknown username is stored with reason `USERNAME_NOT_FOUND`.
- A wrong password is stored with reason `to many false attempts`.
- Active and pending status results are stored with `is_success = true` and a non-null status description.
- The starter-account password-change branch returns `is_success = true` even if the repository update reports failure. It does not create a session, so the caller must log in again.
- A pending account is also stored as successful after creating an access request, although no session is created.

`CountFailedLoginAttempts` counts only rows where `failure_reason = 'INVALID_PASSWORD'` from the previous 24 hours. `CallPasswordPolicyRules` now returns `to many false attempts`, so newly written wrong-password rows do not match the count query. As a result, current wrong-password attempts do not advance the locked, suspicious, or quarantine thresholds unless matching legacy rows already exist. Policy evaluation also occurs before the current attempt is inserted.

When matching historical rows do exist, the ordered checks set status ID 5 at 25 or more rows, status ID 7 at 6 through 24 rows, and status ID 4 at exactly 5 rows.

`failure_reason` is only 50 characters in this schema, but one path can forward a longer `IllegalStateException` message. In strict SQL mode that login-attempt insert can fail. Repository code logs or prints the SQL exception and continues.

## Access Management

```mysql
CREATE TABLE access_management (
    id INT AUTO_INCREMENT PRIMARY KEY,
    requested_by INT NOT NULL,
    requested_department INT NOT NULL,
    requested_job VARCHAR(50) NOT NULL,
    requested_role INT NOT NULL,
    request_status INT NOT NULL DEFAULT 3,
    approved_by INT NULL,
    approved_at TIMESTAMP NULL,
    request_reason TEXT NULL,
    reject_reason TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (requested_by) REFERENCES accounts(id),
    FOREIGN KEY (requested_department) REFERENCES departments(id),
    FOREIGN KEY (requested_role) REFERENCES roles(id),
    FOREIGN KEY (request_status) REFERENCES account_status(id),
    FOREIGN KEY (approved_by) REFERENCES accounts(id)
);
```

## Current Java Defaults

### Registered Accounts

`CreateAccount` explicitly inserts:

- Status `3`: pending
- Role `9`: intern
- Department `12`: unassigned
- `has_access_to_menu = false`
- `is_system_account = false`

The database defaults provide `user_job = 'unassigned'` and `permission = 'read_only'`.

### Starter Accounts

`CreateDefaultAccounts` creates:

| Account | Role | Status | Department | Job | Permission |
| --- | --- | --- | --- | --- | --- |
| Local admin | 1 | 6 | 11 | `system_administrator` | `root_access` |
| Admin | 2 | 6 | 5 | `application_administrator` | `admin_rights` |

Both accounts:

- Are system accounts
- Require a password change
- Start without menu access
- Reference recovery key ID `1`
- Store the bootstrap key

### Access Requests

`CreateAccessRequest` currently stores:

- Requesting account ID
- Selected department ID
- Job `unassigned`
- Role `9`
- Default request status `3`

`ShowCurrentRequests` contains a query that joins `access_management`, `accounts`, `departments`, and `roles`, then prints:

- Requesting account name
- Requested department name
- Requested job
- Requested role name

The admin menu's Requests option maps to `ServiceAction.ADMIN_USER_REQUESTS`, and `ServiceController` now calls `ShowCurrentRequests` for that action. The query does not filter by `request_status`, so it can display more than pending requests when older approved or rejected rows exist. It prints rows directly instead of returning structured data. `ServiceController` does not independently verify that the current session is authorized for the supplied role/action context.

### Password and Status Updates

- `UpdateUserPassword` changes the password hash, sets status ID `1`, clears the password-change flag, and enables menu access.
- `UpdateSystemAccountPassword` changes only the password hash.
- `ExecutePWSDPolicy` sets status ID `4`, `5`, or `7`.

The previously documented end-to-end password and registration-correction defects are fixed:

- `PasswordService` converts the validated password before clearing the character arrays and hashes user-created passwords with BCrypt cost 15.
- `RegistrationService` stores the hash returned by `PasswordFlow`, returns corrected data to the full confirmation step, and rejects a null or blank collected hash before calling `CreateAccount`.

Remaining defects:

- `CreateAccount.newAccount` has no repository-level null or blank hash guard, and repository failures are swallowed instead of returned.
- `UpdateUserPassword` changes the hash and activation fields in two separate connections without a transaction. Its first update can report success even if the second update fails.
- `UpdateSystemAccountPassword` returns no result and recovery does not update status, `requires_password_change`, menu access, or session state.
- Password re-entry has no retry loop; a mismatch exits that password-service call.

## Migration for Existing Databases

An existing database must contain the suspicious status used by the current policy repository:

```mysql
USE patient_management_v5;

INSERT INTO account_status (id, status)
VALUES (7, 'suspicious')
ON DUPLICATE KEY UPDATE status = VALUES(status);
```

Verify that system-account and recovery columns exist:

```mysql
SHOW COLUMNS FROM accounts LIKE 'is_system_account';
SHOW COLUMNS FROM accounts LIKE 'recovery_key_id';
SHOW COLUMNS FROM accounts LIKE 'has_access_to_menu';
SHOW TABLES LIKE 'recovery_keys';
```

## Development Reset

Foreign-key checks must be disabled when truncating referenced tables:

```mysql
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE access_management;
TRUNCATE TABLE login_attempts;
TRUNCATE TABLE accounts;
TRUNCATE TABLE recovery_keys;
SET FOREIGN_KEY_CHECKS = 1;
```

The next application startup recreates the recovery-key row and missing starter accounts.

## Local SQL Files

The repository ignores `*.sql`. Any root-level SQL scratch file is local, is not tracked, and must not be treated as the canonical schema.

Use this document as the current database setup source until versioned migrations are introduced.

## Verified Test State

The Maven Wrapper was verified in Windows PowerShell on 2026-07-23:

```powershell
.\mvnw.cmd test
```

The build passed 55 tests with no failures, errors, or skipped tests: 15 `PasswordServiceTest` tests and 40 `RegistrationServiceTest` tests. These tests do not connect to MySQL and therefore do not validate this schema, foreign keys, bootstrap behavior, login policy, recovery, sessions, or repository error handling.

## Verification Queries

```mysql
SHOW TABLES;

SELECT id, role_name FROM roles ORDER BY id;
SELECT id, status FROM account_status ORDER BY id;
SELECT id, department_name FROM departments ORDER BY id;

SELECT id, is_active, key_scope, created_at, updated_at
FROM recovery_keys
ORDER BY id;

SELECT id, account_name, user_role, account_status, department, user_job,
       permission, is_system_account, has_access_to_menu,
       requires_password_change, recovery_key_id
FROM accounts
ORDER BY id;

SELECT id, account_id, entered_username, failure_reason, is_success, created_at
FROM login_attempts
ORDER BY id DESC;

SELECT *
FROM access_management
ORDER BY id DESC;
```
