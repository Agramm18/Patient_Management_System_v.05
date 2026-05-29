# Project Structure

This file describes the current folder structure and runtime flow of Patient Management System V5.01. The project is still a console-based Java application, so the structure is focused on bootstrapping, configuration, authentication, database access, and the first access-management groundwork.

## Root Structure

```text
Patient_Management_System_v.05
|-- docs
|   |-- architecture
|   |-- project_info
|   `-- setup
|-- src
|   `-- main
|       `-- java
|           `-- app
|-- target
|-- .env
|-- .gitignore
|-- pom.xml
|-- Query.sql
`-- README.md
```

Main root files:

- `pom.xml` contains the Maven setup, Java 21 configuration, dependencies, and `exec-maven-plugin` entry point.
- `.env` contains local database and starter account values.
- `README.md` gives the general project overview.
- `docs/` contains setup, architecture, and project status documentation.
- `src/main/java/app` contains the application source code.

## Source Package Structure

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
|           |-- LoginService
|           |-- PasswordService
|           `-- RegistrationService
|-- Bootstrap
|-- CLIText
|   |-- DisplayMessages
|   `-- Menus
|       |-- DepartmentJobs
|       |-- Departments
|       `-- Program
|-- Config
|-- Controller
`-- Repository
    |-- AuthRepository
    |-- ConfigRepository
    |-- LoginRepository
    |-- logsRepository
    `-- RegistrationRepository
```

## Package Responsibilities

### `app`

Contains `Main.java`, the application entry point. It creates the shared `Scanner`, displays the startup message, starts the loader, and hands control to `BootConfigService`.

### `app.Bootstrap`

Contains the boot process. `BootConfigService` creates the controller objects, builds the `FrontController`, starts the configuration phase, and only moves into authentication if configuration succeeds.

### `app.Controller`

Contains the central controller layer.

- `FrontController` routes requests to subcontrollers.
- `ConfigController` runs environment and database startup logic.
- `AuthController` shows the authentication menu and routes to registration, login, or exit.
- `MenuController`, `ServiceController`, and `uiController` currently exist as placeholders for later runtime phases.

The intended high-level controller order is:

```text
Config -> Auth -> Menu -> Service -> UI
```

Currently only `Config` and `Auth` are active.

### `app.Config`

Contains configuration and database startup services.

- `EnvValidationService` checks the `.env` file and required values.
- `SQLValidationService` builds the JDBC URL and tests the database connection.
- `DBManager` stores the runtime database connection configuration and provides new JDBC connections.
- `CheckForDefaultAccounts` checks whether the starter admin accounts exist.

### `app.Auth.Flow`

Contains the main authentication flow classes.

- `RegistrationFlow` coordinates account registration.
- `LoginFlow` coordinates login and login attempt logging.
- `PasswordFlow` routes into password creation and hashing.

### `app.Auth.Flow.Services`

Contains smaller services used by the authentication flows.

- `RegistrationService` collects and validates username, email, and phone number.
- `PasswordService` validates password rules and creates BCrypt hashes.
- `LoginInputCollector` collects login credentials.
- `LoginVerification` checks the login result and routes based on account status.
- `FirstLogin` handles the first-login access request flow for pending users.
- `SelectDepartment` validates department selection.
- `SelectJob`, `AccountPolicy`, and parts of the role flow are still placeholders or not fully connected.

### `app.CLIText`

Contains console text output and menu classes.

- `DisplayMessages` contains startup, config, auth, loader, and default-account messages.
- `Menus.Program` contains the authentication and role menus.
- `Menus.Departments` contains the department selection menu.
- `Menus.DepartmentJobs` contains department-specific job menu classes. These are currently placeholder menus.

### `app.Repository`

Contains database access classes.

- `ConfigRepository` creates missing starter accounts.
- `RegistrationRepository` creates new registered user accounts.
- `LoginRepository` checks users, passwords, and account status.
- `logsRepository` stores login attempt logs.
- `AuthRepository` contains access-management, password update, role, and system-account database operations.

## Program Runtime Flow

### 1. Startup Phase

1. `app.Main` creates a shared `Scanner`.
2. `StartMSG` prints the startup message.
3. `BootConfigService.displayLoader()` displays the loader message.
4. `BootConfigService.SystemConfig(scanner)` creates the controllers.
5. `FrontController` is created with `AuthController`, `ConfigController`, `MenuController`, `ServiceController`, and `uiController`.

### 2. Config Phase

The config phase prepares the environment and database connection before the user can authenticate.

1. `FrontController` routes to `CONFIG`.
2. `ConfigController.execute(scanner)` starts the configuration process.
3. `EnvValidationService` checks whether `.env` exists and validates required values.
4. `SQLValidationService` builds the MySQL JDBC URL from the `.env` values.
5. `SQLValidationService` tries to connect to the database.
6. `DBManager.initialize(...)` stores the runtime database connection values.
7. `CheckForDefaultAccounts` checks for the `local_admin` and `admin` starter accounts.
8. `CreateDefaultAccounts` creates missing starter accounts with BCrypt-hashed passwords.
9. If the starter account check succeeds, the boot process continues into authentication.

### 3. Auth Phase

The auth phase is the first interactive user phase.

1. `FrontController` routes to `AUTH`.
2. `AuthController` displays `AuthMenu`.
3. The user chooses registration, login, or exit.
4. Registration routes to `RegistrationFlow`.
5. Login routes to `LoginFlow`.
6. Exit shuts down the console application.

## Registration Flow

Registration creates a new pending account.

1. `RegistrationFlow` starts `RegistrationService`.
2. `RegistrationService` collects username, email, and phone number.
3. The user confirms or changes the entered data.
4. `PasswordFlow` starts `PasswordService`.
5. `PasswordService` validates password rules and hashes the password with BCrypt.
6. `CreateAccount` inserts the new account into the database.

Current registration defaults:

- Account status: `pending`
- Role: `intern`
- Department: `unassigned`
- Permission: database default `read_only`

## Login Flow

Login verifies credentials and decides what happens based on account status.

1. `LoginFlow` starts `LoginInputCollector`.
2. `LoginInputCollector` collects username and password.
3. `LoginVerification` uses `CheckUserInDB` to check the username.
4. `CheckUserInDB` verifies the password with BCrypt.
5. `CheckUserInDB` loads the account status.
6. `LoginVerification` decides the next action.
7. `app.Repository.logsRepository.CollectLogs` stores the login attempt.

Current account status behavior:

```text
active
- Login succeeds.

disabled
- Login fails.

pending
- Password check succeeds.
- The first-login access request flow starts.

locked
- Login fails.

on_quarantine
- Login fails.

waiting_for_password_change
- The user must create a new password.
- The account is changed to active after the password update.
```

## Pending User Access Flow

Pending users can currently request access during first login.

1. `FirstLogin.firstSetup(username, scanner)` displays the department menu.
2. `SelectDepartment` collects and validates a department number from `1` to `11`.
3. `FirstLogin` displays the matching department job menu.
4. `HandleAccessManagement` stores an access request in the database.

Current limitations:

- Department selection is stored.
- Job selection is not implemented yet.
- Role selection is not fully connected yet.
- Requested job currently uses the default value.
- Requested role currently uses the default intern role.
- Approval, rejection, and activation workflows are not implemented yet.

## Starter Account Flow

During startup, the application checks whether `local_admin` and `admin` exist.

If one or both are missing:

1. `.env` starter account values are loaded.
2. Passwords are hashed with BCrypt.
3. Accounts are inserted into the `accounts` table.
4. Both starter accounts receive `waiting_for_password_change`.

On first login, starter accounts must change their password. `UpdateUserPWSD` updates the password hash, changes the account status to `active`, and clears the password-change flag.

## Documentation Structure

```text
docs
|-- architecture
|   |-- PROJECT_STRUCTURE.md
|   |-- TECHNICHAL.md
|   `-- diagramms
|       `-- patient-management-uml.mmd
|-- project_info
|   |-- CURRENT_STATUS.md
|   |-- FUTURE_PLANS.md
|   |-- MISSING_NOW.md
|   `-- RECRUITER.md
`-- setup
    |-- DB_SETUP.md
    `-- ENV_SETUP.md
```

Documentation purpose:

- `PROJECT_STRUCTURE.md` explains package structure and runtime flow.
- `TECHNICHAL.md` explains tools, dependencies, and technical concepts.
- `CURRENT_STATUS.md` describes what is currently implemented.
- `MISSING_NOW.md` lists missing short-term and core production work.
- `FUTURE_PLANS.md` contains later project roadmap ideas.
- `ENV_SETUP.md` documents required `.env` values.
- `DB_SETUP.md` documents database schema, seed data, and verification queries.

## Current Structure Notes

- The application is still console-based.
- The active runtime currently stops after authentication-related flows.
- `MenuController`, `ServiceController`, and `uiController` are part of the intended architecture but are not implemented yet.
- Repository classes access the database through `DBManager`.
- CLI text is separated from flow and repository logic to keep console output easier to maintain.
- Some class names and folder names still contain inconsistent casing or spelling and should be cleaned up later.
