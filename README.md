# Patient Management System V5.01

Java 21 console project for a patient management system. The current implementation focuses on the technical foundation: bootstrapping, controller routing, `.env` validation, MySQL connection setup, default administrator account creation, registration, login, account status checks, login attempt logging, and early role request validation.

The project is not a complete patient management application yet. Patient records, treatment workflows, appointments, billing, reporting, a main application menu after login, and a graphical UI are still planned work.

## Current Project Status

The application has been restructured into a controller, flow, service, repository, and configuration layout.

Current runtime entry path:

1. `app.Main`
2. `BootConfigService`
3. `FrontController`
4. `ConfigController`
5. `AuthController`
6. Authentication flows under `app.Auth.Flow`
7. Database access through repository classes under `app.Repository`

Main implemented areas:

- Maven project setup with Java 21 compiler configuration.
- Console bootstrap through `BootConfigService`.
- Central dispatcher structure through `FrontController`.
- Configuration startup through `ConfigController`.
- `.env` file validation through `EnvValidationService`.
- MySQL JDBC URL creation and connection test through `SQLValidationService`.
- Runtime database settings through `DBManager`.
- Default account checks through `SystemAccountValidationService`.
- Automatic creation of missing `local_admin` and `admin` accounts through `SetDefaultAccounts`.
- Registration through `RegistrationFlow`, `RegistrationService`, `PasswordFlow`, `PasswordService`, and `CreateAccount`.
- Login through `LoginFlow`, `LoginInputCollector`, `LoginVerification`, and `CheckUserInDB`.
- BCrypt hashing for default accounts, registration passwords, and login verification.
- Login attempt persistence through `app.Repository.logsRepository.CollectLogs`.
- Account status checks through the `account_status` table.
- Pending-account role selection validation through `RoleValidation` and `roleMenu`.
- Mermaid and Draw.io documentation under `docs/`.

## Current Runtime Flow

1. `app.Main` creates a `Scanner`, prints the welcome text, and starts `BootConfigService`.
2. `BootConfigService.SystemConfig(scanner)` creates the controller objects.
3. `FrontController` routes first to `CONFIG`.
4. `ConfigController.execute(scanner)` validates `.env`, builds the SQL connection values, initializes `DBManager`, and checks starter accounts.
5. `EnvValidationService` requires all database and default account values from `.env`.
6. `SQLValidationService` builds `jdbc:mysql://<host>:<port>/<database>` and tests the connection.
7. `DBManager` stores the runtime database user, password, and JDBC URL.
8. `SystemAccountValidationService` checks whether accounts with `user_role = 1` and `user_role = 2` exist.
9. `SetDefaultAccounts` creates missing default accounts with BCrypt password hashes.
10. If configuration succeeds, `FrontController` routes to `AUTH`.
11. `AuthController` shows the authentication menu: registration, login, or exit.
12. Registration collects username, email, phone number, and password.
13. `PasswordService` validates and hashes the password.
14. `CreateAccount` inserts the new account into `accounts`.
15. Login collects username and password, validates both against the database, checks account status, and logs the attempt.
16. If the account status is `pending`, `RoleValidation` displays the role menu and validates the selected role number.

## Current Project Structure

```text
src/main/java/app
|-- Main.java
|-- Auth
|   `-- Flow
|       |-- LoginFlow.java
|       |-- PasswordFlow.java
|       |-- RegistrationFlow.java
|       `-- Services
|           |-- AuthSecurityService
|           |   |-- AccountPolicy.java
|           |   |-- CollectLogs.java
|           |   `-- RoleValidation.java
|           |-- LoginService
|           |   |-- LoginInputCollector.java
|           |   `-- LoginVerification.java
|           |-- PasswordService
|           |   `-- PasswordService.java
|           `-- RegistrationService
|               `-- RegistrationService.java
|-- Bootstrap
|   `-- BootConfigService.java
|-- Config
|   |-- DBManager.java
|   |-- EnvValidationService.java
|   |-- SQLValidationService.java
|   |-- SetDefaultAccounts.java
|   `-- SystemAccountValidationService.java
|-- Controller
|   |-- AuthController.java
|   |-- ConfigController.java
|   |-- FrontController.java
|   |-- MenuController.java
|   |-- ServiceController.java
|   `-- uiController.java
|-- Menus
|   |-- AuthMenu.java
|   |-- CLIText.java
|   `-- roleMenu.java
`-- Repository
    |-- LoginRepository
    |   |-- CheckRoles.java
    |   `-- CheckUserInDB.java
    |-- PasswordPolicyRepository.java
    |-- RegistrationRepository
    |   `-- CreateAccount.java
    `-- logsRepository
        `-- CollectLogs.java
```

Additional project files:

```text
docs
|-- patient-management-architecture.mmd
|-- patient-management-activity.drawio
|-- patient-management-activity.mmd
`-- patient-management-uml.mmd

Query.sql
pom.xml
README.md
```

## Requirements

- JDK 21
- Maven 3.9+ recommended
- MySQL Server
- `.env` file in the project root
- Database schema created manually before starting the application

## Environment Setup

Create a `.env` file in the project root with these values:

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

Important details:

- All listed values are required by `EnvValidationService`.
- `DB_PORT` must be numeric.
- `DB_NAME` must match the existing MySQL database.
- Default account credentials are only used when the starter accounts are missing.
- The runtime code currently depends on hard-coded role and account status IDs.

## Database Setup

The application does not create the schema automatically yet. Create the database and tables before running the app.

```mysql
CREATE DATABASE IF NOT EXISTS patient_management_v5;
USE patient_management_v5;
```

### Roles

The current code depends on these role IDs:

- `1`: `local_admin`
- `2`: `admin`
- `9`: `intern`
- `10`: `apprentice`

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

### Account Statuses

The current code depends on these status IDs:

- `1`: `active`
- `2`: `disabled`
- `3`: `pending`
- `4`: `locked`
- `5`: `on_quarantine`
- `6`: `waiting_for_password_change`

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

### Accounts

```mysql
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    department VARCHAR(50) NOT NULL DEFAULT 'unassigned',
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
    FOREIGN KEY (account_status) REFERENCES account_status(id)
);
```

### Login Attempts

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

### Access Management

`Query.sql` currently contains the work-in-progress access management table definition. The final table shape represented there is:

```mysql
CREATE TABLE access_management (
    id INT AUTO_INCREMENT PRIMARY KEY,

    requested_by INT NOT NULL,
    requested_role INT NOT NULL,
    requested_job VARCHAR(50) NOT NULL,

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
    FOREIGN KEY (request_status) REFERENCES account_status(id)
);
```

Useful schema checks:

```mysql
SHOW TABLES;
SHOW COLUMNS FROM roles;
SHOW COLUMNS FROM account_status;
SHOW COLUMNS FROM accounts;
SHOW COLUMNS FROM login_attempts;
SHOW COLUMNS FROM access_management;
```

## Default Accounts

At startup, `SystemAccountValidationService` checks for existing accounts with `user_role = 1` and `user_role = 2`.

If one or both are missing, `SetDefaultAccounts` creates them:

```text
local_admin
- role ID: 1
- status ID: 6
- user_job: system_administrator
- permission: root_access
- department: IT
- requires_password_change: true
- password: LOCAL_ADMIN_PWSD from .env, hashed with BCrypt salt round 12

admin
- role ID: 2
- status ID: 6
- user_job: application_administrator
- permission: admin_rights
- department: IT
- requires_password_change: true
- password: ADMIN_PWSD_DEFAULT from .env, hashed with BCrypt salt round 12
```

Both default accounts receive the `BOOTSTRAP_KEY` value from `.env`.

## Registration

Registration is handled by `RegistrationFlow`.

Current validation:

- Username must not be blank.
- Username length must be between 5 and 20 characters.
- Email must not be blank, must contain `@`, and must be shorter than 254 characters.
- Phone number must not be blank, must start with `+`, and must not be longer than 15 characters.
- Password must be at least 10 characters long.
- Password must contain uppercase letters, lowercase letters, numbers, and special characters.
- Password confirmation must match.

New accounts are inserted by `CreateAccount` with:

```text
account_status = 3
user_role = 9
```

That means newly registered accounts currently start as `pending` and `intern`.

## Login

Login is handled by `LoginFlow`.

Current login behavior:

- `LoginInputCollector` collects username and password.
- `CheckUserInDB` checks whether the username exists.
- `CheckUserInDB` verifies the submitted password with BCrypt.
- `CheckUserInDB` resolves the account status through `account_status`.
- `LoginVerification` decides how to continue based on the account status.
- `app.Repository.logsRepository.CollectLogs` writes each login attempt to `login_attempts`.

Current account status handling:

```text
active
- Login succeeds.

disabled
- Login fails.

pending
- The role selection menu is displayed.
- The selected role number is validated, but not persisted yet.

locked
- Login fails.

on_quarantine
- Login fails.

waiting_for_password_change
- Login is treated as successful, but the password change flow is not implemented yet.
```

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

- `dotenv-java` 3.0.0
- `mysql-connector-j` 9.6.0
- `jbcrypt` 0.4
- `exec-maven-plugin` 3.1.0

## In Progress

- No connected main menu exists after successful login yet.
- `RoleValidation` validates a selected role number but does not persist it to the database.
- `access_management` exists only as database groundwork.
- Account approval and administration workflows are not implemented yet.
- Password change for `waiting_for_password_change` accounts is not implemented yet.
- Failed login counters are tracked in memory during a login flow, but are not persisted to `accounts.failed_password_attempts`.
- Account locking after too many failed login attempts is not persisted yet.
- `CheckRoles` is present but does not match the current integer-based `user_role` schema and is not part of the active login flow.
- `AccountPolicy`, `PasswordPolicyRepository`, `MenuController`, `ServiceController`, `uiController`, and `CLIText` are placeholders.
- Patient data, treatment, appointment, billing, reporting, and UI workflows are not implemented yet.
- There are no automated tests yet.

## Known Current Notes

- The application is currently console-based.
- `LoginInputCollector.enterPWSD()` only handles password input when `System.console()` is available. IDE run configurations without a real console can hang at login.
- `SQLValidationService.DBConnection()` logs failed DB connections but does not immediately stop the boot process itself.
- `EnvValidationService.CheckFileStatus()` reports a missing `.env` but does not immediately hard-stop the boot process itself.
- The runtime welcome message in `Main` still prints `Version 5.0`, while the project is documented as `V5.01`.
- Some class and method names still contain typos or inconsistent naming and should be cleaned up later.
- The Mermaid and Draw.io documentation under `docs/` may need another pass after the latest class renames and database changes.

## Future Plans

### Testing and Development

- Unit tests
- Integration tests
- Debugging setup
- Automated test pipeline

### Backend and Infrastructure

- REST API support
- Redis integration for caching and session management
- Docker and Kubernetes compatibility
- Cloud compatibility with AWS, Azure, and similar platforms

### Monitoring and Security

- Audit logging
- Security monitoring
- Grafana and Prometheus integration

### User Interface

- JavaFX graphical user interface

### AI and Data Processing

- Machine learning integration into application workflows using Python and JSON

Developed by Agramm18 (c) 2026
