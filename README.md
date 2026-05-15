# Patient Management System V5.01

Java console project for a patient management system rewrite. The current focus is the technical foundation: bootstrap flow, controller structure, `.env` validation, MySQL connection setup, starter account checks, and the authentication foundation.

## Project Direction

This version reflects a restructuring of the project architecture and tooling.

The earlier project state relied heavily on constructor-driven flow. Objects created other objects directly, logic was chained through constructors, and more and more startup behavior was spread across different classes. That made the code harder to follow over time because it became unclear:

- which class created which dependency
- where a method call originally came from
- which part of the system was responsible for the next step in the flow

Because of that, the project was remodeled toward a more central control structure with `BootConfigService`, `FrontController`, and dedicated sub-controllers such as `ConfigController` and `AuthController`.

The reason for this change is simple: the application flow should be visible and understandable from one central place. A dispatcher/controller approach makes the control flow easier to trace, easier to extend, and easier to debug than pushing system behavior through nested constructors.

The tooling was also changed during this phase:

- The project was moved from VS Code to IntelliJ IDEA because the Java workflow there is more stable and transparent for this project setup
- Maven replaced the old ad-hoc build handling because dependency management and project builds repeatedly caused problems before
- This was especially relevant together with VS Code, where build/setup issues became a recurring blocker during development

So the current project state is not only a feature update, but also a cleanup of architecture, project structure, and build workflow.

## Current Status

The project currently starts through a simple bootstrap flow and routes requests into dedicated controllers.

Active flow right now:

1. Start `app.Main`
2. Show boot and loader messages
3. Run `BootConfigService`
4. Route to `ConfigController`
5. Validate the `.env` file
6. Read database and starter account values from `.env`
7. Try to establish the MySQL connection
8. Store the runtime DB config in `DBManager`
9. Check whether `local_admin` and `admin` accounts exist
10. Create missing default admin accounts with BCrypt password hashes and default permissions
11. Route to `AuthController`
12. Show the authentication menu: login, registration, or exit
13. Start the selected authentication path

## What Is Implemented

- Maven project setup via `pom.xml`
- Maven compiler plugin currently targets Java 25
- Console entry point in `app.Main`
- Bootstrap service in `app.Bootstrap.BootConfigService`
- Central routing through `FrontController`
- Configuration flow via `ConfigController`
- `.env` file validation with `dotenv-java`
- JDBC connection test for MySQL
- Runtime DB connection settings through `DBManager`
- Starter account checks for `local_admin` and `admin`
- Automatic creation of missing default admin accounts
- Default permissions for starter accounts: `root_access` for `local_admin`, `admin_rights` for `admin`
- BCrypt password hashing for default accounts and registration password input
- Authentication menu with login, registration, and exit options
- Registration input flow for username, email, phone number, and password creation
- Activity documentation under `docs/`
- Base controller and service structure for future expansion

## What Changed Compared To The Old README

- Removed outdated descriptions about finished login, registration, role checks, and admin menu logic
- Removed old manual run instructions with `javac`, `lib/*`, and `run.sh`
- Removed references to files that are not present right now, such as `.env.example`
- Adjusted the documentation to the actual package structure under `src/main/java`
- Added the current default account bootstrap flow
- Reduced the README to the current real project scope

## Current Project Structure

```text
src/main/java/app
|-- Main.java
|-- Auth
|   |-- LoginService.java
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
`-- Controller
    |-- AuthController.java
    |-- ConfigController.java
    |-- FrontController.java
    |-- MenuController.java
    |-- ServiceController.java
    `-- uiController.java
```

Additional documentation:

```text
docs
|-- patient-management-activity.drawio
`-- patient-management-activity.mmd
```

## Requirements

- JDK 25, matching the current Maven compiler plugin configuration
- Maven 3.9+ recommended
- MySQL server
- `.env` file in the project root

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
- All values are required
- `DB_PORT` must be numeric
- The default account values are used when the application needs to create missing `local_admin` or `admin` rows
- Default account permissions are currently hard-coded in `SetDefaultAccounts`

## Database Setup

The current configuration flow depends on a valid MySQL setup before the application starts. The program reads the database values from `.env`, tries to build a JDBC connection with them, and then checks the `accounts` table for required starter accounts.

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
    user_job VARCHAR(50) DEFAULT 'intern',
    user_role VARCHAR(50) DEFAULT 'unassigned',
    account_status VARCHAR(50) DEFAULT 'disabled',
    permission VARCHAR(50) NOT NULL DEFAULT 'read_only',
    password_hash VARCHAR(255) NOT NULL,
    bootstrap_key VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

Optional checks:

```sql
SHOW TABLES;
SHOW COLUMNS FROM accounts;
```

Important for the current config flow:

- `DB_NAME` in `.env` must match the created database exactly
- The MySQL user from `DB_USER` must have access to that database
- `DB_PWSD` must match the password of that MySQL user
- The application does not create tables automatically
- The application can create missing `local_admin` and `admin` rows if the `accounts` table already exists
- The `permission` column is used by the current default account setup

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

- `LoginService` is still empty, so the login option is only a placeholder
- Registration currently collects user data and creates a password hash, but it does not persist a new user account yet
- `MenuController`, `ServiceController`, and `uiController` are currently placeholders
- No patient management workflow is connected yet
- No persistent patient data logic exists yet

Developed by Agramm18 (c) 2026
