# Project Structure

Last synchronized: 2026-05-29.

This file describes the current folder structure and runtime organization of Patient Management System V5.01. The project is currently a console-based Java application focused on bootstrapping, configuration, authentication, database-backed account handling, login attempt logging, and first-login access request groundwork.

Environment setup details are documented only in `docs/setup/ENV_SETUP.md`. Database setup details are documented only in `docs/setup/DB_SETUP.md`.

## Root Structure

```text
Patient_Management_System_v.05
|-- docs
|   |-- architecture
|   |   |-- diagramms
|   |   |   `-- patient-management-uml.mmd
|   |   |-- PROJECT_STRUCTURE.md
|   |   `-- TECHNICHAL.md
|   |-- archive
|   |   `-- ARCHIVE.md
|   |-- project_info
|   |   |-- ABOUT.md
|   |   |-- CURRENT_STATUS.md
|   |   |-- FUTURE_PLANS.md
|   |   |-- MISSING_NOW.md
|   |   `-- RECRUITER.md
|   `-- setup
|       |-- DB_SETUP.md
|       `-- ENV_SETUP.md
|-- src
|   `-- main
|       `-- java
|           `-- app
|-- target
|-- .env
|-- .gitignore
|-- pom.xml
|-- Query.sql
|-- Query_1.sql
`-- README.md
```

Main root files and folders:

- `pom.xml` contains the Maven setup, Java 21 target, dependencies, and `exec-maven-plugin` entry point.
- `README.md` contains the general project overview.
- `docs/` contains project information, setup documentation, architecture notes, and diagrams.
- `src/main/java/app` contains the Java application source code.
- `target/` contains generated Maven build output.
- `Query.sql` and `Query_1.sql` are root-level SQL scratch files; maintained setup instructions belong in `docs/setup/DB_SETUP.md`.
- `.env` is the local runtime configuration file; maintained setup instructions belong in `docs/setup/ENV_SETUP.md`.

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
|           |   |-- AccountPolicy.java
|           |   |-- CollectLogs.java
|           |   |-- RoleValidation.java
|           |   |-- SelectDepartment.java
|           |   `-- SelectJob.java
|           |-- LoginService
|           |   |-- FirstLogin.java
|           |   |-- LoginInputCollector.java
|           |   `-- LoginVerification.java
|           |-- PasswordService
|           |   `-- PasswordService.java
|           `-- RegistrationService
|               `-- RegistrationService.java
|-- Bootstrap
|   `-- BootConfigService.java
|-- CLIText
|   |-- DisplayMessages
|   |   |-- AuthMSG.java
|   |   |-- ConfigMSG.java
|   |   |-- DefaultAccountsMSG.java
|   |   |-- LoaderMSG.java
|   |   `-- StartMSG.java
|   `-- Menus
|       |-- DepartmentJobs
|       |   |-- AdministrationJobsMenu.java
|       |   |-- EmergencyJobsMenu.java
|       |   |-- FinanceJobsMenu.java
|       |   |-- itJobsMenu.java
|       |   |-- LaboratoryJobsMenu.java
|       |   |-- MedicalJobsMenu.java
|       |   |-- OfficeJobsMenu.java
|       |   |-- PharmacyJobsMenu.java
|       |   |-- SecurityJobsMenu.java
|       |   |-- SystemJobsMenu.java
|       |   `-- TrainingJobsMenu.java
|       |-- Departments
|       |   `-- DepartmentMenu.java
|       `-- Program
|           |-- AuthMenu.java
|           `-- roleMenu.java
|-- Config
|   |-- CheckForDefaultAccounts.java
|   |-- DBManager.java
|   |-- EnvValidationService.java
|   `-- SQLValidationService.java
|-- Controller
|   |-- AuthController.java
|   |-- ConfigController.java
|   |-- FrontController.java
|   |-- MenuController.java
|   |-- ServiceController.java
|   `-- uiController.java
`-- Repository
    |-- AuthRepository
    |   |-- CheckRoles.java
    |   |-- CheckSystemAccounts.java
    |   |-- CountFailedLoginAttempts.java
    |   |-- ExecutePWSDPolicy.java
    |   |-- HandleAccessManagement.java
    |   |-- SetNewStatus.java
    |   `-- UpdateUserPWSD.java
    |-- ConfigRepository
    |   `-- CreateDefaultAccounts.java
    |-- LoginRepository
    |   `-- CheckUserInDB.java
    |-- logsRepository
    |   `-- CollectLogs.java
    `-- RegistrationRepository
        `-- CreateAccount.java
```

## Package Responsibilities

### `app`

Contains `Main.java`, the application entry point. It creates the shared `Scanner`, prints the startup message, displays the loader, and hands control to `BootConfigService`.

### `app.Bootstrap`

Contains the boot process. `BootConfigService` creates all controller objects, builds the `FrontController`, runs the configuration phase, and starts the authentication phase only when configuration returns success.

### `app.Controller`

Contains the controller layer.

- `FrontController` routes requests to subcontrollers.
- `ConfigController` runs local configuration and database startup logic.
- `AuthController` shows the authentication menu and routes to registration, login, or exit.
- `MenuController`, `ServiceController`, and `uiController` currently exist as placeholders for later runtime phases.

The intended controller order is:

```text
Config -> Auth -> Menu -> Service -> UI
```

Currently only `Config` and `Auth` are active.

### `app.Config`

Contains startup services for local configuration and database connectivity.

- `EnvValidationService` checks the local runtime configuration file and required values.
- `SQLValidationService` builds the JDBC connection values and tests the database connection.
- `DBManager` stores the runtime database connection configuration and creates JDBC connections.
- `CheckForDefaultAccounts` checks whether starter admin accounts exist and triggers creation when needed.

### `app.Auth.Flow`

Contains the top-level authentication flow classes.

- `RegistrationFlow` coordinates account registration.
- `LoginFlow` coordinates credential collection, login verification, and login attempt logging.
- `PasswordFlow` delegates password creation to `PasswordService`.

### `app.Auth.Flow.Services`

Contains smaller services used by authentication and access request flows.

- `RegistrationService` collects username, email, and phone number.
- `PasswordService` validates password rules and creates BCrypt hashes.
- `LoginInputCollector` collects login credentials.
- `LoginVerification` checks credentials and routes behavior based on account status.
- `FirstLogin` handles first-login access request groundwork for pending accounts.
- `SelectDepartment` validates department selection.
- `RoleValidation`, `SelectJob`, and `AccountPolicy` are draft or placeholder classes.

### `app.CLIText`

Contains console output text and menu classes.

- `DisplayMessages` contains startup, configuration, authentication, loader, and starter-account messages.
- `Menus.Program` contains the authentication and role menus.
- `Menus.Departments` contains the department selection menu.
- `Menus.DepartmentJobs` contains department-specific job menu placeholders.

### `app.Repository`

Contains database access classes.

- `ConfigRepository` creates missing starter accounts.
- `RegistrationRepository` creates registered user accounts.
- `LoginRepository` checks usernames, password hashes, and account statuses.
- `logsRepository` writes login attempt records.
- `AuthRepository` contains access request, password update, failed login counting, role check, and status policy repository classes.

## Program Runtime Flow

### 1. Startup Phase

1. `app.Main` creates a shared `Scanner`.
2. `StartMSG` prints the startup message.
3. `BootConfigService.displayLoader()` displays the loader message.
4. `BootConfigService.SystemConfig(scanner)` creates the controllers.
5. `FrontController` is created with `AuthController`, `ConfigController`, `MenuController`, `ServiceController`, and `uiController`.

### 2. Configuration Phase

The configuration phase prepares local runtime values and database access before users can authenticate.

1. `FrontController` routes to `CONFIG`.
2. `ConfigController.execute(scanner)` starts the configuration process.
3. `EnvValidationService` validates required runtime values.
4. `SQLValidationService` builds and tests the JDBC connection values.
5. `DBManager.initialize(...)` stores the runtime database connection values.
6. `CheckForDefaultAccounts` checks for starter admin accounts.
7. `CreateDefaultAccounts` creates missing starter accounts.
8. If the starter account check succeeds, boot continues into authentication.

### 3. Authentication Phase

The authentication phase is the first interactive user phase.

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
6. `CreateAccount` inserts the new account through the repository layer.

## Login Flow

Login verifies credentials and decides the next action based on account status.

1. `LoginFlow` starts `LoginInputCollector`.
2. `LoginInputCollector` collects username and password.
3. `LoginVerification` uses `CheckUserInDB` to check the username.
4. `CheckUserInDB` verifies the password with BCrypt.
5. `CheckUserInDB` loads the account status.
6. `LoginVerification` routes based on status.
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
- The account is changed to active after the password update succeeds.
```

## Pending User Access Flow

Pending users can currently create an access request during first login.

1. `FirstLogin.firstSetup(username, scanner)` displays the department menu.
2. `SelectDepartment` collects and validates a department number from `1` to `11`.
3. `FirstLogin` displays the matching department job menu placeholder.
4. `HandleAccessManagement` stores an access request.

Current limitations:

- Department selection is stored.
- Job selection is not implemented.
- Role selection is not connected.
- Requested job uses a default value.
- Requested role uses the default intern role.
- Approval, rejection, and activation workflows are not implemented.

## Starter Account Flow

During startup, the application checks whether starter admin accounts exist.

If one or both are missing:

1. Starter account values are loaded by the repository.
2. Passwords are hashed with BCrypt.
3. Missing accounts are inserted through the repository layer.
4. Starter accounts receive `waiting_for_password_change`.

On first login, starter accounts must change their password. `UpdateUserPWSD` updates the password hash, changes account status to `active`, and clears the password-change flag.

## Current Structure Notes

- The application is still console-based.
- The active runtime currently stops after authentication-related flows.
- `MenuController`, `ServiceController`, and `uiController` are placeholders.
- `AccountPolicy`, `SelectJob`, `SetNewStatus`, and parts of the role flow are placeholders or drafts.
- Repository classes access the database through `DBManager`.
- CLI text is separated from flow and repository logic.
- Generated `target/` files are not source files.
- Some class and folder names still contain inconsistent casing or spelling and should be cleaned up later.
