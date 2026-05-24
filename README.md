# Patient Management System V5.01

Java console project for a patient management system. The current focus is the technical foundation: bootstrap flow, central controller routing, `.env` validation, MySQL connection setup, default admin account bootstrap, DB-backed registration, DB-backed login, login attempt logging, and first role validation.

## Current Project Status

The project has been restructured from a constructor-heavy flow into a clearer controller, flow, service, and repository structure.

The current entry path is:

- `app.Main`
- `BootConfigService`
- `FrontController`
- `ConfigController`
- `AuthController`
- Authentication flows under `app.Auth.Flow`
- Database access through repository classes

Main changes so far:

- Maven project setup with Java 21 compiler configuration.
- `FrontController` is the central dispatch point for sub-controllers.
- `ConfigController` is the entry point for `.env` and SQL configuration.
- `DBManager` stores runtime JDBC connection settings.
- Default account bootstrap exists for `local_admin` and `admin`.
- Registration was split into `RegistrationFlow`, `RegistrationService`, `PasswordFlow`, `PasswordService`, and `UserAccountRepository`.
- Login was split into `LoginFlow`, `LoginService`, `AuthenticationRepository`, `LoginResult`, and `LoginLogsRepository`.
- Login attempts are persisted in `login_attempts`.
- After successful login, `accounts.user_role` is checked through `RoleRepository`.
- Users without a role are routed into a first role selection flow through `RoleValidationService` and `roleMenu`.
- Menu classes were moved into `app.Menus`.
- Architecture, activity, and UML documentation is stored under `docs/`.

## Current Runtime Flow

1. `app.Main` starts the console application.
2. The welcome message and system loader are printed.
3. `BootConfigService.SystemConfig(scanner)` creates the controllers.
4. `FrontController` routes first to `CONFIG`.
5. `ConfigController` validates the `.env` file.
6. `SQLValidationService` builds the MySQL JDBC URL and tests the connection.
7. `DBManager` stores the runtime user, password, and JDBC URL.
8. `SystemAccountValidationService` checks whether roles `local_admin` and `admin` exist in `accounts.user_role`.
9. `SetDefaultAccounts` creates missing default accounts with BCrypt password hashes.
10. If configuration succeeds, `FrontController` routes to `AUTH`.
11. `AuthController` shows the authentication menu: registration, login, or exit.
12. Registration collects username, email, phone number, and password.
13. `PasswordService` validates and hashes the password.
14. `UserAccountRepository` inserts the new account into `accounts` with status `waiting_for_authorization`.
15. Login validates username and password against the database.
16. Every login attempt is saved through `LoginLogsRepository` into `login_attempts`.
17. After successful login, `RoleRepository` checks whether the account has a role.
18. If `user_role = unassigned`, the application currently displays and validates a role selection only.

## Implemented

- Maven setup in `pom.xml`
- Java 21 Maven compiler source/target
- `exec-maven-plugin` configured with `app.Main`
- Console bootstrap through `BootConfigService`
- Central dispatch structure through `FrontController`
- Configuration flow through `ConfigController`
- `.env` existence and required value validation through `dotenv-java`
- MySQL JDBC connection test
- Runtime DB connection settings through `DBManager`
- Default account check for `local_admin` and `admin`
- Automatic creation of missing default accounts
- BCrypt hashing for default accounts, registration, and login checks
- Authentication menu with registration, login, and exit
- Registration with username, email, phone number, and password validation
- Password rules: at least 10 characters, uppercase letter, lowercase letter, number, and special character
- Database insert for new accounts through `UserAccountRepository`
- Login with username lookup and BCrypt password verification
- `LoginResult` for login success state and failure reason
- Login attempt logging through `LoginLogsRepository`
- First role check through `RoleRepository`
- Role selection menu through `roleMenu`
- Mermaid and Draw.io documentation under `docs/`

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
|           |-- AccountPolicyService.java
|           |-- LoginService
|           |   |-- LoginResult.java
|           |   |-- LoginService.java
|           |   `-- RoleValidationService.java
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
    |   |-- AuthenticationRepository.java
    |   `-- RoleRepository.java
    |-- PasswordPolicyRepository.java
    |-- RegistrationRepository
    |   `-- UserAccountRepository.java
    `-- logsRepository
        `-- LoginLogsRepository.java
```

Additional documentation:

```text
docs
|-- patient-management-architecture.mmd
|-- patient-management-activity.drawio
|-- patient-management-activity.mmd
`-- patient-management-uml.mmd
```

## Requirements

- JDK 21
- Maven 3.9+ recommended
- MySQL Server
- `.env` file in the project root
- Database tables created manually before starting the application

## .env Setup

The application currently expects these values:

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

- The `.env` file must be located in the project root.
- All values listed above are required.
- `DB_PORT` must be numeric.
- `DB_NAME` must exactly match the created MySQL database.
- Default account values are used when `local_admin` or `admin` are missing.
- Roles, jobs, permissions, department, and `requires_password_change` for default accounts are currently hard-coded in `SetDefaultAccounts`.

## Database Setup

The application does not create tables automatically yet. The database and tables must exist before startup.

Create the database:

```mysql
CREATE DATABASE patient_management_v5;
```

Select the database:

```mysql
USE patient_management_v5;
```

Required `roles` table:

```mysql
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

Starter roles matching the current role menu:

```mysql
INSERT INTO roles (role_name, role_description)
VALUES
    ('local_admin', 'Full infrastructure and system access'),
    ('admin', 'Administrative access for user and system management'),
    ('it_specialist', 'IT specialist access for system maintenance'),
    ('it_support', 'Technical support access for troubleshooting'),
    ('doctor', 'Medical access to patient treatment data'),
    ('nurse', 'Limited medical access under doctor permissions'),
    ('finance', 'Access to financial and billing information'),
    ('office_staff', 'Administrative access to patient organization data'),
    ('intern', 'Restricted training role with minimal permissions'),
    ('apprentice', 'Training role with limited operational permissions');
```

`account_status` table:

```mysql
CREATE TABLE account_status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(50) NOT NULL DEFAULT 'disabled',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO account_status (status)
VALUES
('active'),
('disabled'),
('pending'),
('locked'),
('on_quarantine'),
('waiting_for_password_change');
```

`accounts` table:

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

`login_attempts` table:

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

`access_management` table:

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

Optional checks:

```mysql
SHOW TABLES;
SHOW COLUMNS FROM roles;
SHOW COLUMNS FROM account_status;
SHOW COLUMNS FROM accounts;
SHOW COLUMNS FROM login_attempts;
SHOW COLUMNS FROM access_management;
```

## Default Accounts

If `accounts.user_role` has no entries for `local_admin` or `admin`, missing default accounts are created.

Current default values:

- `local_admin`: `user_role = local_admin`, `user_job = system_administrator`, `permission = root_access`, `account_status = enabled`, `department = IT`
- `admin`: `user_role = admin`, `user_job = application_administrator`, `permission = admin_rights`, `account_status = enabled`, `department = IT`
- Both accounts receive `requires_password_change = true`
- Both accounts receive the `BOOTSTRAP_KEY` from `.env`
- Default passwords are hashed with BCrypt salt round 12

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

- There is no session or connected main menu after successful login yet.
- `RoleValidationService` validates the selected role but does not persist it yet.
- The announced department check is not implemented yet.
- Account activation and account management after registration are still open.
- Newly registered accounts start with `account_status = waiting_for_authorization`.
- Failed login attempts are logged, but account locking is not persisted in `accounts.failed_password_attempts` yet.
- `roles` is currently documented for later role management; runtime code still checks `accounts.user_role`.
- `AccountPolicyService`, `PasswordPolicyRepository`, `MenuController`, `ServiceController`, `uiController`, and `CLIText` are still placeholders.
- Patient data, patient workflows, treatment, appointments, billing, and reporting are not implemented yet.
- There are no automated tests yet.

## Known Current Notes

- The application is currently strongly console-based.
- `LoginService.enterPWSD()` has no scanner fallback when `System.console()` is unavailable.
- `PasswordService.RetypePWSD()` currently also expects `System.console()` and can fail in IDE run configurations without a real console.
- `SQLValidationService.DBConnection()` logs failed DB connections but does not immediately stop the boot process at that point.
- `EnvValidationService.CheckFileStatus()` reports a missing `.env` but does not immediately hard-stop the boot process.
- The runtime welcome message in `Main` still shows `Version 5.0`, while the project is documented as `V5.01`.
- Some class and method names still contain typos or inconsistent naming and should be cleaned up later.

## Future Plans

These features are planned for a later stage, after the core console application is as stable as possible:

## Future Plans

### Testing & Development
- Testing and debugging environment
- Unit and integration testing
- Automated testing pipelines

### Backend & Infrastructure
- REST API support
- Redis integration for caching and session management
- Docker and Kubernetes compatibility
- Cloud compatibility with AWS, Azure, and similar platforms

### Monitoring & Security
- Monitoring with Grafana and Prometheus
- Audit logging and security monitoring

### User Interface
- Graphical user interface with JavaFX

### AI & Data Processing
- Machine learning integration into the application workflow using Python and JSON

Developed by Agramm18 (c) 2026
