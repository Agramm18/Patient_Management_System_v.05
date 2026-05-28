# SQL Description

This file contains all database-related documentation for the Patient Management System V5.01 project. Keep SQL setup, schema details, seed data, required IDs, default account database values, and database checks in this file.

## Environment Variables

Create a `.env` file in the project root with the following keys:

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
```

`DB_PORT` must be numeric. `DB_NAME` must match the database created below. The default account values are used only when one or both starter accounts are missing.

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
    password_hash VARCHAR(255) NOT NULL,
    requires_password_change BOOLEAN NOT NULL DEFAULT FALSE,
    failed_password_attempts INT NOT NULL DEFAULT 0,
    bootstrap_key VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_role) REFERENCES roles(id),
    FOREIGN KEY (account_status) REFERENCES account_status(id),
    FOREIGN KEY (department) REFERENCES departments(id)
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

`CreateAccount` creates registered user accounts with:

- `account_status`: `3` (`pending`)
- `user_role`: `9` (`intern`)
- `department`: `12` (`unassigned`)
- `user_job`: database default `unassigned`
- `permission`: database default `read_only`

`CreateDefaultAccounts` creates missing starter accounts with:

- `local_admin`: role `1`, status `6`, department `11`, job `system_administrator`, permission `root_access`, `requires_password_change = true`
- `admin`: role `2`, status `6`, department `5`, job `application_administrator`, permission `admin_rights`, `requires_password_change = true`

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

`app.Repository.logsRepository.CollectLogs` writes each login attempt to `login_attempts`. If the username is unknown, `account_id` remains `NULL`.

## Query.sql

`Query.sql` currently contains only the department table creation and department seed data. Use the complete setup in this file when creating a full development database.

## Verification Queries

```mysql
SHOW TABLES;
SHOW COLUMNS FROM roles;
SHOW COLUMNS FROM account_status;
SHOW COLUMNS FROM departments;
SHOW COLUMNS FROM accounts;
SHOW COLUMNS FROM login_attempts;
SHOW COLUMNS FROM access_management;

SELECT id, role_name FROM roles ORDER BY id;
SELECT id, status FROM account_status ORDER BY id;
SELECT id, department_name FROM departments ORDER BY id;
SELECT id, account_name, user_role, account_status, department, user_job, permission, requires_password_change
FROM accounts
ORDER BY id;
```
