# Database Setup

Last synchronized: 2026-06-03.

This file contains the database-related documentation for the Patient Management System V5.01 project. Keep SQL setup, schema details, seed data, required IDs, default account database values, and database checks in this file.

## Environment Configuration

Create the project `.env` file from [ENV_SETUP.md](./ENV_SETUP.md) before running the application. The `DB_NAME` value must match the database created below.

## Database Creation

```mysql
CREATE DATABASE IF NOT EXISTS patient_management_v5;
USE patient_management_v5;
```

Create and seed the tables in the order shown below because of foreign key dependencies.

## Required IDs Used By Java Code

The current Java code depends on these IDs:

- Role `1`: `local_admin`
- Role `2`: `admin`
- Role `9`: `intern`
- Role `10`: `apprentice`
- Department `5`: `IT`
- Department `11`: `System`
- Department `12`: `unassigned`
- Status `1`: `active`
- Status `2`: `disabled`
- Status `3`: `pending`
- Status `4`: `locked`
- Status `5`: `on_quarantine`
- Status `6`: `waiting_for_password_change`

Use explicit IDs when seeding reference data so the Java defaults match the database.

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
    (4, 'it_support', 'Technical support access for troubleshooting'),
    (5, 'doctor', 'Medical access to patient treatment data'),
    (6, 'nurse', 'Limited medical access under doctor permissions'),
    (7, 'finance', 'Access to financial and billing information'),
    (8, 'office_staff', 'Administrative access to patient organization data'),
    (9, 'intern', 'Restricted training role with minimal permissions'),
    (10, 'apprentice', 'Training role with limited operational permissions');
```

## Account Status

```mysql
CREATE TABLE account_status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(50) NOT NULL DEFAULT 'disabled',
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
    (6, 'waiting_for_password_change');
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
    (3, 'Laboratory', 'Lab tests, diagnostics and analysis'),
    (4, 'Pharmacy', 'Medication, prescriptions and stock'),
    (5, 'IT', 'Software, systems and technical support'),
    (6, 'Security', 'Security, access control and monitoring'),
    (7, 'Finance', 'Billing, accounting and financial tasks'),
    (8, 'Office', 'Office work and administration support'),
    (9, 'Administration', 'Management and organizational tasks'),
    (10, 'Training', 'Training, education and onboarding'),
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
    has_access_to_menu BOOLEAN NOT NULL DEFAULT false,
    password_hash VARCHAR(255) NOT NULL,
    requires_password_change BOOLEAN NOT NULL DEFAULT FALSE,
    failed_password_attempts INT NOT NULL DEFAULT 0,
    bootstrap_key VARCHAR(255) DEFAULT NULL,
    recovery_key_id INT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_role) REFERENCES roles(id),
    FOREIGN KEY (account_status) REFERENCES account_status(id),
    FOREIGN KEY (department) REFERENCES departments(id),
    CONSTRAINT fk_accounts_recovery_key
        FOREIGN KEY (recovery_key_id) REFERENCES recovery_keys(id)
);
```

## Login Attempts

```mysql
CREATE TABLE login_attempts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NULL,
    entered_username VARCHAR(50) NOT NULL,
    failure_reason VARCHAR(50) NULL,
    is_success BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_login_attempts_account
        FOREIGN KEY (account_id) REFERENCES accounts(id)
        ON DELETE SET NULL
);
```

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
    FOREIGN KEY (requested_role) REFERENCES roles(id),
    FOREIGN KEY (approved_by) REFERENCES accounts(id),
    FOREIGN KEY (request_status) REFERENCES account_status(id),
    FOREIGN KEY (requested_department) REFERENCES departments(id)
);
```

## Current Java Database Defaults

`ConfigController` stores the recovery key during startup after DB initialization:

- `HandleRecoveryKey` reads `RECOVERY_KEY` from `.env`.
- The plain recovery key is hashed with BCrypt.
- `SetRecoveryKey` inserts or updates `recovery_keys.id = 1`.
- The recovery key scope uses the database default `resetting system accounts`.

`CreateAccount` creates registered user accounts with:

- `account_status`: `3` (`pending`)
- `user_role`: `9` (`intern`)
- `department`: `12` (`unassigned`)
- `user_job`: database default `unassigned`
- `permission`: database default `read_only`
- `is_system_account`: `false`
- `has_access_to_menu`: `false`

`CreateDefaultAccounts` creates missing starter accounts with:

- `local_admin`: role `1`, status `6`, department `11`, job `system_administrator`, permission `root_access`, `requires_password_change = true`, `has_access_to_menu = false`
- `admin`: role `2`, status `6`, department `5`, job `application_administrator`, permission `admin_rights`, `requires_password_change = true`, `has_access_to_menu = false`

Both starter accounts use password hashes generated from `.env` password values and store the `.env` `BOOTSTRAP_KEY`.

`HandleAccessManagement` creates pending access requests with:

- `requested_by`: account ID resolved from the username
- `requested_department`: selected department ID from `1` to `11`
- `requested_job`: `unassigned`
- `requested_role`: `9` (`intern`)
- `request_status`: database default `3` (`pending`)

`UpdateUserPWSD` changes a starter account after first password change:

- Updates `password_hash`
- Sets `account_status = 1`
- Sets `requires_password_change = FALSE`
- Sets `has_access_to_menu = TRUE`

`RecoveryFlow` resets a selected existing account password after recovery-key validation:

- Loads `recovery_keys.id = 1` through `CollectRecoveryKey`
- Checks the entered recovery key with BCrypt through `CheckKeyStatus`
- Selects a target account by account name
- Verifies that the account exists through `SelectUserForRecover`
- Creates a new password hash through `PasswordService`
- Updates `accounts.password_hash` through `UpdateSystemAccount`

The current recovery flow updates only the password hash. It does not currently change `account_status`, `requires_password_change`, `has_access_to_menu`, or `recovery_key_id`.

`app.Repository.logsRepository.CollectLogs` writes each login attempt to `login_attempts`. If the username is unknown, `account_id` remains `NULL`.

## Existing Database Migration

Use this migration when the database already exists and the new recovery key structure must be added.

```mysql
USE patient_management_v5;

ALTER TABLE accounts
    MODIFY COLUMN has_access_to_menu BOOLEAN NOT NULL DEFAULT FALSE
    AFTER permission;

ALTER TABLE accounts
    ADD COLUMN is_system_account BOOLEAN NOT NULL DEFAULT FALSE
    AFTER permission;

UPDATE accounts
SET accounts.is_system_account = TRUE
WHERE account_name IN ('local_admin', 'admin');

CREATE TABLE IF NOT EXISTS recovery_keys (
    id INT AUTO_INCREMENT PRIMARY KEY,
    recovery_key_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    key_scope VARCHAR(255) NOT NULL DEFAULT 'resetting system accounts',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE accounts
    ADD COLUMN recovery_key_id INT DEFAULT NULL
    AFTER bootstrap_key;

ALTER TABLE accounts
    ADD CONSTRAINT fk_accounts_recovery_key
    FOREIGN KEY (recovery_key_id) REFERENCES recovery_keys(id);
```

Use `has_access_to_menu` with underscores. `has-access_to_menu` is not valid as an unquoted MySQL column name.

For a local development reset, `accounts` cannot be truncated while referenced by `login_attempts` unless foreign key checks are disabled for the reset.

```mysql
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE login_attempts;
TRUNCATE TABLE accounts;
SET FOREIGN_KEY_CHECKS = 1;
```

## Query.sql

`Query.sql` currently contains only the department table creation and department seed data. Use the complete setup in this file when creating a full development database.

## Verification Queries

```mysql
SHOW TABLES;
SHOW COLUMNS FROM roles;
SHOW COLUMNS FROM account_status;
SHOW COLUMNS FROM departments;
SHOW COLUMNS FROM recovery_keys;
SHOW COLUMNS FROM accounts;
SHOW COLUMNS FROM login_attempts;
SHOW COLUMNS FROM access_management;

SELECT id, role_name FROM roles ORDER BY id;
SELECT id, status FROM account_status ORDER BY id;
SELECT id, department_name FROM departments ORDER BY id;
SELECT id, is_active, key_scope, created_at, updated_at
FROM recovery_keys
ORDER BY id;
SELECT id, LEFT(recovery_key_hash, 7) AS hash_prefix, is_active, key_scope
FROM recovery_keys
WHERE id = 1;
SELECT id, account_name, user_role, account_status, department, user_job, permission,
       is_system_account, has_access_to_menu, requires_password_change, bootstrap_key, recovery_key_id
FROM accounts
ORDER BY id;
```
