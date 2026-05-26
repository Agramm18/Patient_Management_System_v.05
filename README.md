# Patient Management System V5.01

Java 21 console project for a patient management system. The current implementation focuses on bootstrapping, controller routing, `.env` validation, MySQL connection setup, default administrator account creation, registration, login, account status checks, login attempt logging, and first groundwork for access requests.

The project is not a complete patient management application yet. Patient records, treatment workflows, appointments, billing, reporting, administration workflows, a connected main application menu after login, and a graphical UI are still planned work.

## Current Project Status

The application is structured into bootstrap, controller, configuration, authentication flow, service, menu, and repository layers.

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
- Runtime connection settings through `DBManager`.
- Default account checks through `SystemAccountValidationService`.
- Automatic creation of missing `local_admin` and `admin` accounts through `SetDefaultAccounts`.
- Registration through `RegistrationFlow`, `RegistrationService`, `PasswordFlow`, `PasswordService`, and `CreateAccount`.
- BCrypt hashing for default accounts, registration passwords, and login verification.
- Login through `LoginFlow`, `LoginInputCollector`, `LoginVerification`, and `CheckUserInDB`.
- Account status checks during login.
- Login attempt persistence through `app.Repository.logsRepository.CollectLogs`.
- First-login/access-request groundwork for `pending` accounts through `FirstLogin`, `DepartmentMenu`, `SelectDepartment`, department job menus, and `HandleAccessManagement`.
- SQL schema documentation in `sqlDESCRIPTION.md`.
- Mermaid and Draw.io documentation under `docs/`.

## Current Runtime Flow

1. `app.Main` creates a `Scanner`, prints the welcome text, and starts `BootConfigService`.
2. `BootConfigService.SystemConfig(scanner)` creates controller objects.
3. `FrontController` routes first to `CONFIG`.
4. `ConfigController.execute(scanner)` validates `.env`, tests SQL connection settings, initializes `DBManager`, and checks starter accounts.
5. `EnvValidationService` requires database values and default account values from `.env`.
6. `SQLValidationService` builds `jdbc:mysql://<host>:<port>/<database>` and tests the connection.
7. `DBManager` stores the runtime connection settings.
8. `SystemAccountValidationService` checks whether the starter administrator accounts exist.
9. `SetDefaultAccounts` creates missing default accounts with BCrypt password hashes.
10. If configuration succeeds, `FrontController` routes to `AUTH`.
11. `AuthController` shows the authentication menu: registration, login, or exit.
12. Registration collects username, email, phone number, and password.
13. `PasswordService` validates, confirms, and hashes the password.
14. `CreateAccount` stores the new account as `pending` with role `intern`.
15. Login collects username and password, validates both, checks account status, and logs the attempt.
16. If the account status is `pending`, `FirstLogin` starts the current access-request flow.
17. `FirstLogin` asks for a department, displays the matching job menu stub, and stores an access request through `HandleAccessManagement`.

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
|   |-- DepartmentMenu.java
|   |-- roleMenu.java
|   `-- JobMenus
|       |-- AdministrationJobsMenu.java
|       |-- EmergencyJobsMenu.java
|       |-- FinanceJobsMenu.java
|       |-- LaboratoryJobsMenu.java
|       |-- MedicalJobsMenu.java
|       |-- OfficeJobsMenu.java
|       |-- PharmacyJobsMenu.java
|       |-- SecurityJobsMenu.java
|       |-- SystemJobsMenu.java
|       |-- TrainingJobsMenu.java
|       `-- itJobsMenu.java
`-- Repository
    |-- PasswordPolicyRepository.java
    |-- LoginRepository
    |   |-- CheckRoles.java
    |   |-- CheckUserInDB.java
    |   `-- HandleAccessManagement.java
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
sqlDESCRIPTION.md
pom.xml
README.md
```

## Requirements

- JDK 21
- Maven 3.9+ recommended
- MySQL-compatible database
- `.env` file in the project root
- Database setup from `sqlDESCRIPTION.md`

## Environment Setup

Create a `.env` file in the project root. The complete list of required values is documented in `sqlDESCRIPTION.md`.

## Database Setup

The application does not create the full database schema automatically yet. Database schema, seed data, required environment values, and current ID dependencies are documented in `sqlDESCRIPTION.md`.

## Default Accounts

At startup, `SystemAccountValidationService` checks for the starter administrator accounts. If one or both accounts are missing, `SetDefaultAccounts` creates them from `.env` values. The exact database values are documented in `sqlDESCRIPTION.md`.

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

New accounts currently start as `pending` and `intern`. The underlying database defaults and IDs are documented in `sqlDESCRIPTION.md`.

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
- `FirstLogin` starts the access-request groundwork.
- The user selects a department.
- The matching department job menu is displayed.
- The access request is stored through `HandleAccessManagement`.

locked
- Login fails.

on_quarantine
- Login fails.

waiting_for_password_change
- Login is treated as successful, but the password change flow is not implemented yet.
```

## Pending Account Access Request

For `pending` accounts, the current flow is:

1. `FirstLogin.FirstSetup(username, scanner)` displays `DepartmentMenu`.
2. `SelectDepartment` validates a department number from `1` to `11`.
3. `FirstLogin` displays the matching job menu class from `app.Menus.JobMenus`.
4. `HandleAccessManagement` stores the access request.

Current limitations:

- Job menus only display placeholder text.
- `SelectJob` exists but is empty.
- Job selection is not collected yet.
- Requested job and requested role are not based on user selection yet.
- Account approval, rejection, and activation workflows are not implemented yet.

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
- Password change for `waiting_for_password_change` accounts is not implemented yet.
- Failed login counters are tracked in memory during a login flow, but are not persisted yet.
- Account locking after too many failed login attempts is not persisted yet.
- `RoleValidation` and `roleMenu` still exist, but they are not part of the active pending-login flow.
- `CheckRoles` is present but does not match the current role handling and is not part of the active login flow.
- `AccountPolicy`, `SelectJob`, `PasswordPolicyRepository`, `MenuController`, `ServiceController`, `uiController`, and `CLIText` are placeholders.
- Most department job menu classes are placeholders.
- Patient data, treatment, appointment, billing, reporting, and UI workflows are not implemented yet.
- There are no automated tests yet.

## Known Current Notes

- The application is currently console-based.
- `LoginInputCollector.enterPWSD()` only handles password input when `System.console()` is available. IDE run configurations without a real console can hang at login.
- `PasswordService.RetypePWSD()` expects `System.console()` and can fail in environments without a real console.
- `SQLValidationService.DBConnection()` logs failed DB connections but does not immediately hard-stop the boot process itself.
- `EnvValidationService.CheckFileStatus()` reports a missing `.env` but does not immediately hard-stop the boot process itself.
- The runtime welcome message in `Main` still prints `Version 5.0`, while the project is documented as `V5.01`.
- Some class and method names still contain typos or inconsistent naming and should be cleaned up later.
- The Mermaid and Draw.io documentation under `docs/` may need another pass after the latest class and database changes.

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
