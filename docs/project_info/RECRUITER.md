# Patient Management System V5.01

Java 21 console application for the early-stage Patient Management System. The current implementation focuses on application bootstrapping, controller routing, environment validation, database connection initialization, starter account creation, registration, login, login attempt logging, password hashing, password-change handling for starter accounts, and first-login access requests for pending users.

This is not a complete patient management application yet. Patient records, treatment workflows, appointments, billing, reporting, administration workflows, a connected main menu after login, and a graphical UI are still planned work.

## Current Status

The application is structured into these layers:

- Bootstrap and startup entry point
- Front controller and subcontrollers
- Configuration services
- Authentication flows
- Authentication and registration services
- CLI display text and menu classes
- Repository classes for persistence operations

Implemented runtime areas:

- Maven project setup with Java 21.
- Console startup through `app.Main` and `BootConfigService`.
- Central routing through `FrontController`.
- Configuration startup through `ConfigController`.
- Environment validation through `EnvValidationService`.
- Runtime connection setup through `SQLValidationService` and `DBManager`.
- Starter account checks through `CheckForDefaultAccounts`.
- Automatic creation of missing `local_admin` and `admin` accounts through `CreateDefaultAccounts`.
- Registration through `RegistrationFlow`, `RegistrationService`, `PasswordFlow`, `PasswordService`, and `CreateAccount`.
- BCrypt password hashing for registration, starter accounts, and login verification.
- Login through `LoginFlow`, `LoginInputCollector`, `LoginVerification`, and `CheckUserInDB`.
- Account status handling for active, disabled, pending, locked, quarantined, and password-change accounts.
- Password update flow for starter accounts with `waiting_for_password_change` status.
- Login attempt logging through `app.Repository.logsRepository.CollectLogs`.
- First-login access request groundwork through `FirstLogin`, `DepartmentMenu`, `SelectDepartment`, department job menus, and `HandleAccessManagement`.
- Mermaid documentation under `docs/`.

All database schema, seed data, environment keys, default account database values, and setup SQL are documented in `sqlDESCRIPTION.md`.

## Runtime Flow

1. `app.Main` creates a shared `Scanner`, prints the startup message, displays the loader, and starts `BootConfigService`.
2. `BootConfigService.SystemConfig(scanner)` creates the controller objects.
3. `FrontController` routes to `CONFIG`.
4. `ConfigController.execute(scanner)` validates the environment, builds the database connection configuration, initializes `DBManager`, and checks starter accounts.
5. `CheckForDefaultAccounts` checks whether `local_admin` and `admin` starter accounts exist.
6. `CreateDefaultAccounts` creates missing starter accounts from environment values.
7. If configuration succeeds, `FrontController` routes to `AUTH`.
8. `AuthController` displays the authentication menu: registration, login, or exit.
9. Registration collects username, email, phone number, and password.
10. Login collects username and password, verifies credentials, resolves account status, and logs the attempt.
11. Pending users enter the first-login access request flow.
12. Starter accounts with password-change status must create a new password before they are activated.

## Project Structure

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
    |   |-- HandleAccessManagement.java
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

Additional project files:

```text
docs
|-- patient-management-activity.drawio
|-- patient-management-activity.mmd
|-- patient-management-architecture.mmd
`-- patient-management-uml.mmd

Query.sql
sqlDESCRIPTION.md
pom.xml
README.md
```

## Requirements

- JDK 21
- Maven 3.9+ recommended
- MySQL-compatible database
- A valid `.env` file in the project root
- Database setup completed from `sqlDESCRIPTION.md`

## Configuration And Database Documentation

Keep database-related setup out of this README. Use `sqlDESCRIPTION.md` for:

- Required environment keys
- Database creation
- Table definitions
- Required seed data and IDs
- Starter account database values
- Runtime persistence notes

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

After validation, the account is persisted through `CreateAccount` with the initial database state documented in `sqlDESCRIPTION.md`.

## Login

Login is handled by `LoginFlow`.

Current login behavior:

- `LoginInputCollector` collects username and password.
- `CheckUserInDB` checks whether the username exists.
- `CheckUserInDB` verifies the submitted password with BCrypt.
- `CheckUserInDB` resolves the account status.
- `LoginVerification` decides how to continue based on the account status.
- `app.Repository.logsRepository.CollectLogs` writes each login attempt.

Current account status handling:

```text
active
- Login succeeds.

disabled
- Login fails.

pending
- Login password check succeeds.
- FirstLogin starts the access-request flow.
- The user selects a department.
- The matching department job menu is displayed.
- The access request is stored through HandleAccessManagement.

locked
- Login fails.

on_quarantine
- Login fails.

waiting_for_password_change
- The user must create a new password.
- UpdateUserPWSD updates the stored password hash.
- The account status is changed to active.
- The password-change flag is cleared.
```

## Pending Account Access Request

For `pending` accounts, the current flow is:

1. `FirstLogin.firstSetup(username, scanner)` displays `DepartmentMenu`.
2. `SelectDepartment` validates a department number from `1` to `11`.
3. `FirstLogin` displays the matching job menu class from `app.CLIText.Menus.DepartmentJobs`.
4. `HandleAccessManagement.accessManagement(username, department)` stores the access request.

Current limitations:

- Job menus only display placeholder text.
- `SelectJob` exists but is not implemented yet.
- Job selection is not collected yet.
- Requested job uses the current default documented in `sqlDESCRIPTION.md`.
- Requested role uses the current default documented in `sqlDESCRIPTION.md`.
- Account approval, rejection, and activation workflows are not implemented yet.

## Run The Project

Build:

```bash
mvn clean package
```

Compile without packaging:

```bash
mvn -DskipTests compile
```

Start:

```bash
mvn exec:java
```

## Dependencies

Dependencies are defined in `pom.xml`:

- `dotenv-java` 3.0.0
- `mysql-connector-j` 9.6.0
- `jbcrypt` 0.4
- `exec-maven-plugin` 3.1.0

## In Progress

- No connected main menu exists after successful login yet.
- First-login access request storage exists only as groundwork.
- Department selection is stored, but job and role selection are not implemented yet.
- Account approval and administration workflows are not implemented yet.
- Failed login counters are tracked in memory during a login flow, but are not persisted yet.
- Account locking after too many failed login attempts is not persisted yet.
- `RoleValidation`, `roleMenu`, and `CheckRoles` still exist, but they are not part of the active pending-login flow.
- `CheckSystemAccounts` exists, but the current password-change login path uses account status and `UpdateUserPWSD` directly.
- `AccountPolicy`, `SelectJob`, `MenuController`, `ServiceController`, and `uiController` are placeholders.
- Department job menu classes are placeholders.
- Patient data, treatment, appointment, billing, reporting, and UI workflows are not implemented yet.
- There are no automated tests yet.

## Known Current Notes

- The application is currently console-based.
- `LoginInputCollector.enterPWSD()` only handles password input when `System.console()` is available. IDE run configurations without a real console can block the login flow.
- `PasswordService.plainPWSD()` has a scanner fallback, but `PasswordService.retypePWSD()` expects `System.console()` and can fail in environments without a real console.
- Environment validation reports missing or invalid values, but the current flow can still continue into connection setup afterward.
- Connection validation reports failed connections, but the current flow relies on later startup checks to return failure.
- Some class and method names still contain typos or inconsistent casing and should be cleaned up later.
- `docs/patient-management-uml.mmd` documents the current class-level structure. SQL schema details are kept in `sqlDESCRIPTION.md`.

## Future Plans

### Testing And Development

- Unit tests
- Integration tests
- Debugging setup
- Automated test pipeline

### Backend And Infrastructure

- REST API support
- Redis integration for caching and session management
- Docker and Kubernetes compatibility
- Cloud compatibility with AWS, Azure, and similar platforms

### Monitoring And Security

- Audit logging
- Security monitoring
- Grafana and Prometheus integration

### User Interface

- JavaFX graphical user interface

### AI And Data Processing

- Machine learning integration into application workflows using Python and JSON

Developed by Agramm18 (c) 2026
