# SQL Description

This file contains the database setup required before running the application.

## Environment Variables

Add these database values to the project root `.env` file:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=patient_management_v5
DB_USER=your_mysql_user
DB_PWSD=your_mysql_password
```

`DB_PORT` must be numeric. `DB_NAME` must match the database created below.

## Database

```mysql
CREATE DATABASE IF NOT EXISTS patient_management_v5;
USE patient_management_v5;
```

Create the tables in this order because of foreign key dependencies.

## Required IDs

The current Java code depends on these IDs:

- Role `1`: `local_admin`
- Role `2`: `admin`
- Role `9`: `intern`
- Role `10`: `apprentice`
- Department `5`: `IT`
- Department `12`: `unassigned`
- Status `1`: `active`
- Status `2`: `disabled`
- Status `3`: `pending`
- Status `4`: `locked`
- Status `5`: `on_quarantine`
- Status `6`: `waiting_for_password_change`

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

INSERT INTO departments (department_name, department_description)
VALUES
    ('Medical', 'Medical treatment and patient care'),
    ('Emergency', 'Emergency response and urgent care'),
    ('Laboratory', 'Lab tests, diagnostics and analysis'),
    ('Pharmacy', 'Medication, prescriptions and stock'),
    ('IT', 'Software, systems and technical support'),
    ('Security', 'Security, access control and monitoring'),
    ('Finance', 'Billing, accounting and financial tasks'),
    ('Office', 'Office work and administration support'),
    ('Administration', 'Management and organizational tasks'),
    ('Training', 'Training, education and onboarding'),
    ('System', 'System administration and internal system roles'),
    ('unassigned', 'Default department for new accounts');
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

## Default Accounts

`SetDefaultAccounts` creates missing starter accounts with these database values:

- `local_admin`: role `1`, status `6`, department `5`, job `system_administrator`, permission `root_access`
- `admin`: role `2`, status `6`, department `5`, job `application_administrator`, permission `admin_rights`

Both accounts require a password change and use the `BOOTSTRAP_KEY` from `.env`.

## Checks

```mysql
SHOW TABLES;
SHOW COLUMNS FROM roles;
SHOW COLUMNS FROM account_status;
SHOW COLUMNS FROM departments;
SHOW COLUMNS FROM accounts;
SHOW COLUMNS FROM login_attempts;
SHOW COLUMNS FROM access_management;
```
