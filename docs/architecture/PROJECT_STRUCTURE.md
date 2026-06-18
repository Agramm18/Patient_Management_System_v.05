# Project Structure

Last synchronized: 2026-06-18.

Patient Management System V5.01 is a Java 21 Maven console application. The source tree currently contains 76 Java files organized around bootstrap, controllers, authentication flows, runtime session state, configuration, CLI output, menu routing, first service routing, and JDBC repositories.

## Root Structure

```text
Patient_Management_V5.01/
|-- docs/
|   |-- architecture/
|   |   |-- diagramms/
|   |   |   |-- patient-management-uml.md
|   |   |   `-- patient-management-uml.mmd
|   |   |-- PROJECT_STRUCTURE.md
|   |   `-- TECHNICHAL.md
|   |-- archive/
|   |   `-- ARCHIVE.md
|   |-- project_info/
|   |   |-- ABOUT.md
|   |   |-- CURRENT_STATUS.md
|   |   |-- FUTURE_PLANS.md
|   |   |-- RECRUITER.md
|   |   `-- ToDo.md
|   `-- setup/
|       |-- DB_SETUP.md
|       `-- ENV_SETUP.md
|-- src/
|   `-- main/
|       `-- java/
|           `-- app/
|-- target/
|-- .env
|-- .gitignore
|-- pom.xml
|-- Query.sql
`-- README.md
```

Important root notes:

- `pom.xml` targets Java 21 and defines runtime dependencies.
- `.env` is local runtime configuration and is ignored by Git.
- `Query.sql` is an ignored local scratch file, not the canonical database schema.
- `target/` contains generated Maven output.
- There is currently no `src/test` or `src/main/resources` directory.

## Source Tree

```text
src/main/java/app/
|-- Main.java
|-- Auth/
|   `-- Flow/
|       |-- CurrentSession.java
|       |-- LoginFlow.java
|       |-- PasswordFlow.java
|       |-- RecoveryFlow.java
|       |-- RegistrationFlow.java
|       `-- Services/
|           |-- AuthSecurityService/
|           |   |-- AccountPolicy.java
|           |   |-- Audit/
|           |   |   `-- CollectLogs.java
|           |   |-- Management/
|           |   |   |-- CollectUserDepartment.java
|           |   |   |-- CollectUserJob.java
|           |   |   `-- CollectUserRole.java
|           |   `-- Recovery/
|           |       |-- CheckKeyStatus.java
|           |       |-- SelectUserForRecovery.java
|           |       `-- ValidateRecoveryKey.java
|           |-- LoginService/
|           |   |-- CurrentUser.java
|           |   |-- FirstLogin.java
|           |   |-- LoginInputCollector.java
|           |   `-- LoginVerification.java
|           |-- PasswordService/
|           |   `-- PasswordService.java
|           `-- RegistrationService/
|               `-- RegistrationService.java
|-- Bootstrap/
|   `-- BootConfigService.java
|-- CLIText/
|   |-- DisplayMessages/
|   |   |-- AuthMSG.java
|   |   |-- ConfigMSG.java
|   |   |-- DefaultAccountsMSG.java
|   |   |-- LoaderMSG.java
|   |   `-- StartMSG.java
|   `-- Menus/
|       |-- DepartmentJobs/
|       |   |-- AdministrationJobsMenu.java
|       |   |-- EmergencyJobsMenu.java
|       |   |-- FinanceJobsMenu.java
|       |   |-- LaboratoryJobsMenu.java
|       |   |-- MedicalJobsMenu.java
|       |   |-- OfficeJobsMenu.java
|       |   |-- PharmacyJobsMenu.java
|       |   |-- SecurityJobsMenu.java
|       |   |-- SystemJobsMenu.java
|       |   |-- TrainingJobsMenu.java
|       |   `-- itJobsMenu.java
|       |-- Departments/
|       |   `-- DepartmentMenu.java
|       |-- Program/
|       |   |-- AuthMenu.java
|       |   `-- roleMenu.java
|       `-- ServiceMenus/
|           |-- AdminMenu.java
|           `-- LocalAdminMenu.java
|-- Config/
|   |-- DBManager.java
|   |-- EnvValidationService.java
|   |-- HandleRecoveryKey.java
|   |-- LogManager.java
|   `-- SQLValidationService.java
|-- Controller/
|   |-- AuthController.java
|   |-- ConfigController.java
|   |-- FrontController.java
|   |-- MenuController.java
|   |-- ServiceController.java
|   `-- uiController.java
|-- Menu/
|   |-- MenuFlow.java
|   `-- MenuValues.java
|-- Repository/
|   |-- AuthRepository/
|   |   |-- Management/
|   |   |   |-- CountFailedLoginAttempts.java
|   |   |   |-- CreateAccessRequest.java
|   |   |   |-- HasAssignedDepartment.java
|   |   |   `-- HasAssignedRole.java
|   |   |-- Password/
|   |   |   |-- ExecutePWSDPolicy.java
|   |   |   |-- SystemAccountRequiresPasswordChange.java
|   |   |   |-- UpdateSystemAccountPassword.java
|   |   |   `-- UpdateUserPassword.java
|   |   |-- Recovery/
|   |   |   |-- FindRecoverableUser.java
|   |   |   |-- GetRecoveryKeyHash.java
|   |   |   `-- SelectUserForRecover.java
|   |   `-- SetNewStatus.java
|   |-- ConfigRepository/
|   |   |-- CheckForDefaultAccounts.java
|   |   |-- CreateDefaultAccounts.java
|   |   `-- SetRecoveryKey.java
|   |-- LoginRepository/
|   |   |-- CheckUserInDB.java
|   |   `-- CollectLoginValues.java
|   |-- RegistrationRepository/
|   |   `-- CreateAccount.java
|   |-- ServiceRepository/
|   |   `-- AdminServices/
|   |       `-- ShowCurrentRequests.java
|   `-- logsRepository/
|       `-- CollectLogs.java
`-- Services/
    `-- RouteService.java
```

Empty package directories currently present:

- `src/main/java/app/Repository/AuthRepository/Config`
- `src/main/java/app/Services/Admin`
- `src/main/java/app/Services/LocalAdmin`

## Package Responsibilities

### `app`

`Main` creates the shared `Scanner`, prints the startup message, displays the loader through `BootConfigService`, and starts system configuration.

### `app.Bootstrap`

`BootConfigService` creates the controller graph, routes to configuration, stops the process when configuration fails, routes to authentication when configuration succeeds, then routes to `MENU` and `SERVICE` when `CurrentSession` contains a user with menu access.

### `app.Controller`

- `FrontController` routes `CONFIG`, `AUTH`, `MENU`, and `SERVICE`.
- `ConfigController` validates configuration, initializes database access, stores the recovery-key hash, and checks starter accounts.
- `AuthController` owns the authentication menu loop.
- `MenuController` routes local admin and admin users to the first menu display classes and returns `MenuValues`.
- `ServiceController` routes by user role and selected menu option. Admin option `1` calls the current access-request listing repository.
- `uiController` is a placeholder.

Reserved request types still not routed by active cases are `UI` and `EXIT`.

### `app.Config`

- `EnvValidationService` validates `.env` and stores database values.
- `SQLValidationService` builds and tests the JDBC URL.
- `DBManager` stores global runtime database values and opens connections.
- `HandleRecoveryKey` reads and hashes `RECOVERY_KEY`.
- `LogManager` maps application log types to named SLF4J loggers.

### `app.Auth.Flow`

- `RegistrationFlow` coordinates registration and persistence.
- `LoginFlow` repeats credential collection, verifies login, and stores login attempts.
- `PasswordFlow` delegates password creation to `PasswordService`.
- `RecoveryFlow` validates the recovery key and coordinates password reset.
- `CurrentSession` stores the current active `CurrentUser` for downstream routing.

### `app.Auth.Flow.Services`

- `RegistrationService` collects and validates profile data.
- `PasswordService` validates passwords and creates BCrypt hashes.
- `LoginInputCollector` collects username and hidden password input.
- `LoginVerification` verifies credentials and routes by account status.
- `CurrentUser` stores active account ID, username, status, role, system-account flag, and menu-access flag.
- `FirstLogin` creates pending-user access-request groundwork.
- Recovery services collect, validate, and route recovery input.
- Management services contain department and role input plus the job placeholder.
- Audit `CollectLogs` is a small login-result value object, not the database log repository.

### `app.CLIText`

Contains user-facing console messages and menus. Several department job menus remain placeholders. `AdminMenu` and `LocalAdminMenu` display the first service-menu screens. The admin menu is now connected to the first service workflow for option `1`; the remaining options are labels only.

### `app.Menu`

`MenuFlow` validates numeric menu input and returns a valid selected option. `MenuValues` carries the selected option and the current user's role from `MenuController` to `ServiceController`.

### `app.Repository`

- `ConfigRepository` manages recovery-key storage and starter accounts.
- `LoginRepository` reads account credentials, status, and current-login values.
- `RegistrationRepository` inserts registered accounts.
- `logsRepository` inserts login attempts.
- `AuthRepository.Management` handles failed-attempt counts, access requests, and assignment checks.
- `AuthRepository.Password` handles status policy and password updates.
- `AuthRepository.Recovery` handles recovery-key and target-account queries.
- `ServiceRepository.AdminServices` contains the first admin service query for listing access-management requests.

### `app.Services`

Contains the first top-level service placeholder. `RouteService` currently has an empty `userChoice` method and is not part of the active runtime path.

## Runtime Flow

### Startup

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> ConfigController
-> FrontController(AUTH)
-> AuthController
-> FrontController(MENU) when active current user has menu access
-> MenuController
-> FrontController(SERVICE)
-> ServiceController
```

### Configuration

```text
EnvValidationService
-> SQLValidationService
-> DBManager.initialize
-> HandleRecoveryKey
-> SetRecoveryKey
-> CheckForDefaultAccounts
-> CreateDefaultAccounts when required
```

### Active Login

```text
LoginFlow
-> LoginInputCollector
-> LoginVerification
-> CheckUserInDB
-> CollectLoginValues
-> CurrentUser
-> CurrentSession
-> logsRepository.CollectLogs
```

### Menu Routing

```text
BootConfigService
-> CurrentSession.getCurrentUser
-> FrontController(MENU)
-> MenuController
-> LocalAdminMenu or AdminMenu
-> MenuFlow for option validation
-> MenuValues
-> FrontController(SERVICE)
-> ServiceController
-> ShowCurrentRequests for admin option 1
```

### Recovery

```text
RecoveryFlow
-> ValidateRecoveryKey
-> GetRecoveryKeyHash
-> CheckKeyStatus
-> FindRecoverableUser
-> SelectUserForRecovery
-> SelectUserForRecover
-> PasswordService
-> UpdateSystemAccountPassword
```

### Pending Access Request

```text
LoginVerification
-> FirstLogin
-> CollectUserDepartment
-> department job menu
-> CreateAccessRequest
```

## Structural Notes

- Database access is performed directly by repository classes through static `DBManager`.
- Runtime session state is currently stored through static `CurrentSession`.
- Logging migration is partial; direct console diagnostics still exist throughout the source tree.
- Several names do not follow Java naming conventions.
- `AccountPolicy`, `CollectUserJob`, `SetNewStatus`, `RouteService`, service directories, and `uiController` are placeholders.
- `SystemAccountRequiresPasswordChange` and `HasAssignedRole` exist but are not part of the active primary flow.
- `MenuFlow`, `MenuValues`, and `ServiceController` are connected as a first menu-to-service routing baseline.
