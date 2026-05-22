# Patient Management System V5.01

Java console project for a patient management system rewrite. The current focus is the technical foundation: bootstrap flow, controller routing, `.env` validation, MySQL connection setup, default admin account bootstrap, DB-backed registration, DB-backed login, and login attempt logging.

## Project Direction

This version reflects a restructuring of the project architecture and tooling.

The earlier project state relied heavily on constructor-driven flow. Objects created other objects directly, logic was chained through constructors, and startup behavior became spread across multiple classes. That made the control flow harder to trace and harder to extend.

The project is now moving toward a more central controller structure:

- `BootConfigService` starts the boot process
- `FrontController` dispatches requests into sub-controllers
- `ConfigController` validates configuration and database access
- `AuthController` handles the current registration/login entry point
- Repository classes isolate database access from console input logic

The goal is to keep application flow visible from one central place instead of hiding system behavior inside nested constructors.

The tooling was also changed during this phase:

- IntelliJ IDEA is now the main development environment
- Maven handles dependency management and project execution
- The Maven compiler configuration currently targets Java 21

## Current Status

The application currently starts through a console bootstrap flow and routes into configuration and authentication.

Active flow right now:

1. Start `app.Main`
2. Print the welcome message and system loader
3. Run `BootConfigService.SystemConfig(scanner)`
4. Create `FrontController` with the available sub-controllers
5. Route to `ConfigController`
6. Validate the `.env` file and required values
7. Build and test the MySQL JDBC connection
8. Store runtime DB connection settings in `DBManager`
9. Check whether `local_admin` and `admin` starter accounts exist
10. Create missing default admin accounts with BCrypt password hashes
11. Route to `AuthController`
12. Show the authentication menu: registration, login, or exit
13. Registration collects username, email, phone number, and password, then inserts into `accounts`
14. Login validates username and password through repository classes
15. Each login attempt is written through `AccountRepository`

## What Is Implemented

- Maven project setup via `pom.xml`
- Maven compiler source/target set to Java 21
- Console entry point in `app.Main`
- Bootstrap service in `app.Bootstrap.BootConfigService`
- Central routing through `FrontController`
- Configuration flow via `ConfigController`
- `.env` file validation with `dotenv-java`
- JDBC connection test for MySQL
- Runtime DB connection settings through `DBManager`
- Starter account checks for `local_admin` and `admin`
- Automatic creation of missing default admin accounts
- Default starter account values: `root_access` for `local_admin`, `admin_rights` for `admin`, `IT` department, enabled account status, and required password change
- BCrypt password hashing for default accounts, registration, and login verification
- Authentication menu with registration, login, and exit options
- Registration input flow for username, email, phone number, password creation, and DB insert
- Password creation rules: at least 10 characters, uppercase letter, lowercase letter, number, and special character
- Login input flow with username lookup and BCrypt password check
- `LoginResult` object for returning login success state and failure reason
- Login attempt persistence through `AccountRepository`
- Repository classes for account creation, authentication checks, and login attempt logging
- Placeholder policy classes for future account/password policy logic
- Placeholder controller/view classes for future menu, service, and UI flows

## Current Features

- Boot flow with system loader, config check, and routing into authentication
- Config validation for `.env`, MySQL connection, and runtime DB setup
- Automatic default admin setup for missing `local_admin` and `admin` accounts
- Registration with user input validation, BCrypt password hashing, and DB insert
- Login with username lookup and BCrypt password verification
- Login result tracking with failure reasons such as username not found, invalid password, too many invalid passwords, and SQL exception
- Login attempt logging after each login try

## Current Project Structure

```text
src/main/java/app
|-- Main.java
|-- Auth
|   |-- AccountPolicyService.java
|   |-- LoginResult.java
|   |-- LoginService.java
|   |-- PasswordPolicyService.java
|   |-- PasswordService.java
|   `-- RegistrationService.java
|-- Bootstrap
|   `-- BootConfigService.java
|-- Config
|   |-- DBManager.java
|   |-- EnvValidationService.java
|   |-- SQLValidationService.java
|   |-- SetDefaultAccounts.java
|   `-- SystemAccountValidationService.java
|-- ConsoleView
|   `-- CLIText.java
|-- Controller
|   |-- AuthController.java
|   |-- ConfigController.java
|   |-- FrontController.java
|   |-- MenuController.java
|   |-- ServiceController.java
|   `-- uiController.java
`-- Repository
    |-- AccountRepository.java
    |-- AuthenticationService.java
    |-- PasswordPolicyRepository.java
    `-- UserAccountRepository.java
```

Additional project file:

```text
Query.sql
```

## Requirements

- JDK 21, matching the current Maven compiler plugin configuration
- Maven 3.9+ recommended
- MySQL server
- `.env` file in the project root
- Database tables created manually before running the application

## .env Setup

The application currently expects these keys:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=patient_management_v5
DB_USER=your_mysql_user
DB_PWSD=your_mysql_password

LOCAL_ADMIN_NAME=local_admin
LOCAL_ADMIN_PWSD=change_this_password
LOCAL_ADMIN_EMAIL=local_admin@example.com

ADMIN_NAME=admin
ADMIN_PWSD_DEFAULT=change_this_password
ADMIN_EMAIL_DEFAULT=admin@example.com

BOOTSTRAP_KEY=change_this_bootstrap_key
```

Important:

- The `.env` file must be located in the project root
- All values listed above are required
- `DB_PORT` must be numeric
- The default account values are used when the application needs to create missing `local_admin` or `admin` rows
- Default account permissions and department values are currently hard-coded in `SetDefaultAccounts`

## Database Setup

The current configuration flow depends on a valid MySQL setup before the application starts. The program reads database values from `.env`, tries to build a JDBC connection, and then checks the `accounts` table for required starter accounts.

Create the database:

```sql
CREATE DATABASE patient_management_v5;
```

Verify that the database exists:

```sql
SHOW DATABASES;
```

Switch into the database:

```sql
USE patient_management_v5;
```

Create the `accounts` table used by the current authentication foundation:

```sql
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    department VARCHAR(50) NOT NULL DEFAULT 'unassigned',
    user_job VARCHAR(50) NOT NULL DEFAULT 'unassigned',
    user_role VARCHAR(50) NOT NULL DEFAULT 'unassigned',
    account_status VARCHAR(50) NOT NULL DEFAULT 'disabled',
    permission VARCHAR(50) NOT NULL DEFAULT 'read_only',
    password_hash VARCHAR(255) NOT NULL,
    requires_password_change BOOLEAN NOT NULL DEFAULT FALSE,
    failed_password_attempts INT NOT NULL DEFAULT 0,
    bootstrap_key VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

Create the `login_attempts` table used by the current login attempt repository:

```sql
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

Create the separate `roles` table intended for account roles:

```sql
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

Insert the default account roles:

```sql
INSERT INTO roles (role_name, role_description)
VALUES
    ('local_admin', 'Full infrastructure and system access'),
    ('admin', 'Administrative access for user and system management'),
    ('auditor', 'Read-only access to logs and audit data'),
    ('doctor', 'Medical access to patient treatment data'),
    ('nurse', 'Limited medical access under doctor permissions'),
    ('finance', 'Access to financial and billing information'),
    ('office', 'Administrative access to patient organization data'),
    ('support', 'Technical support access for troubleshooting and assistance'),
    ('intern', 'Restricted training role with minimal permissions'),
    ('apprentice', 'Training role with limited operational permissions');
```

Optional checks:

```sql
SHOW TABLES;
SHOW COLUMNS FROM accounts;
SHOW COLUMNS FROM login_attempts;
SHOW COLUMNS FROM roles;
```

Important for the current config flow:

- `DB_NAME` in `.env` must match the created database exactly
- The MySQL user from `DB_USER` must have access to that database
- `DB_PWSD` must match the password of that MySQL user
- The application does not create tables automatically
- The application can create missing `local_admin` and `admin` rows if the `accounts` table already exists
- Default admin rows are created as enabled accounts in the `IT` department and must change their password later
- The `permission` column is used by the current default account setup
- The login attempt table name must match `AccountRepository`, which currently inserts into `login_attempts`

## Run The Project

Build:

```bash
mvn clean package
```

Start:

```bash
mvn exec:java
```

## Dependencies

- `dotenv-java`
- `mysql-connector-j`
- `jbcrypt`

## In Progress

- Login validates username and password, but no post-login session, menu, or role-based routing is connected yet
- Failed login retry tracking is currently in memory for the active login flow
- Login attempt rows are saved, but account locking is not persisted yet
- Registration creates a basic account row, but activation, role assignment, and account management are still open
- `AccountPolicyService`, `PasswordPolicyService`, and `PasswordPolicyRepository` are placeholders
- `MenuController`, `ServiceController`, `uiController`, and `CLIText` are currently placeholders
- The `roles` table is documented for future role handling, but current account logic still uses string values in `accounts.user_role`
- No patient management workflow is connected yet
- No persistent patient data logic exists yet

## Known Current Notes

- `Query.sql` currently contains a standalone login-attempt table script, but the Java repository code is the source of truth for the table name used at runtime.
- `LoginService` only reads passwords when `System.console()` is available. Some IDE run configurations may not provide a console.
- `PasswordService.RetypePWSD` currently expects `System.console()` and may need a scanner fallback for IDE execution.

Developed by Agramm18 (c) 2026
