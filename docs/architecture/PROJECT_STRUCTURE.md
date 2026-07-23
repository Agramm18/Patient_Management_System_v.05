# Project Structure

Last synchronized: 2026-07-18.

Patient Management System V5.01 is a Java 21 Maven console application. The repository currently contains 89 production Java files, two test classes, a Logback resource, Maven Wrapper scripts, and Markdown and Mermaid documentation.

## Root Structure

```text
Patient_Management_System_v.05/
|-- .mvn/
|   `-- wrapper/
|       `-- maven-wrapper.properties
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
|   |-- main/
|   |   |-- java/app/
|   |   `-- resources/logback.xml
|   `-- test/
|       `-- java/app/Auth/Flow/Services/
|-- .env
|-- .gitignore
|-- mvnw
|-- mvnw.cmd
|-- pom.xml
`-- README.md
```

Important root notes:

- `pom.xml` targets Java 21 and defines runtime and test dependencies.
- Maven Wrapper 3.3.4 is configured to use Maven 3.9.16.
- `.env` is local runtime configuration and is ignored by Git.
- `logs/`, `*.log`, `target/`, and `*.sql` are generated or local paths ignored by Git.
- `src/main/resources/logback.xml` defines console and file logging.
- `src/test/java` contains 53 passing JUnit 5 tests across two test classes.

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
|           `-- ParrentMenus/
|               |-- AdminMenu.java
|               |-- LocalAdminMenu.java
|               `-- ChildMenus/
|                   `-- AdminChilds/RequestMenu.java
|-- Config/
|   |-- DBManager.java
|   |-- EnvSetup.java
|   |-- EnvValidationService.java
|   |-- HandleRecoveryKey.java
|   `-- SQLValidationService.java
|-- Controller/
|   |-- AuthController.java
|   |-- ConfigController.java
|   |-- FrontController.java
|   |-- MenuController.java
|   |-- ServiceController.java
|   |-- SubMenuController.java
|   `-- UIController.java
|-- Logging/
|   |-- LogManager.java
|   `-- Enums/
|       `-- ProgrammState/
|           |-- AccountState.java
|           |-- AuthState.java
|           |-- BootState.java
|           |-- ConfigState.java
|           |-- MenuState.java
|           |-- OtherState.java
|           |-- RecoveryState.java
|           |-- SecurityState.java
|           |-- SqlState.java
|           `-- SystemState.java
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

## Test Tree

```text
src/test/java/app/Auth/Flow/Services/
|-- PasswordService/
|   `-- PasswordServiceTest.java
`-- RegistrationService/
    `-- RegistrationServiceTest.java
```

- `PasswordServiceTest` contains 11 tests for terminal fallback, password rules, and retype validation.
- `RegistrationServiceTest` contains 42 tests for username, email, phone, and confirmation/correction validation.
- Repository integration and complete authentication-flow tests do not exist yet.

## Package Responsibilities

### `app`

`Main` creates the shared `Scanner`, prints the startup message, displays the loader through `BootConfigService`, and starts system configuration.

### `app.Bootstrap`

`BootConfigService` creates the controller graph, routes to configuration, stops the process when configuration fails, routes to authentication when configuration succeeds, verifies `CurrentSession`, and then routes to `MENU` and `SERVICE` for an active user with menu access.

### `app.Controller`

- `FrontController` routes `CONFIG`, `AUTH`, `MENU`, and `SERVICE` through `callController`.
- `ConfigController` validates configuration, initializes database access, stores the recovery-key hash, and checks starter accounts.
- `AuthController` owns the authentication menu loop.
- `MenuControllerParrent` displays the local-admin or admin parent menu and returns `MenuValues`.
- `ServiceController` dispatches by user role, but both current role handlers only log startup.
- `SubMenuController` is injected into `FrontController` but is empty and has no request route.
- `UIController` is an empty placeholder.

Reserved request types still not routed by active cases are `UI` and `EXIT`.

### `app.Config`

- `EnvSetup` stores and validates all 13 required environment values, including a database port range of 1 through 65535.
- `EnvValidationService` validates `.env`, creates `EnvSetup`, and exposes database values.
- `SQLValidationService` builds and tests the JDBC URL.
- `DBManager` stores global runtime database values and opens connections.
- `HandleRecoveryKey` reads and hashes `RECOVERY_KEY`.

### `app.Logging`

`LogManager` exposes typed logging methods for boot, authentication, configuration, security, SQL, system, recovery, menu, account, and other input events. Ten enums under `Logging.Enums.ProgrammState` define the accepted states. `src/main/resources/logback.xml` configures console and per-category file appenders.

### `app.Auth.Flow`

- `RegistrationFlow` coordinates registration and persistence.
- `LoginFlow` repeats credential collection, verifies login, and stores login attempts.
- `PasswordFlow` delegates password creation to `PasswordService`.
- `RecoveryFlow` validates the recovery key and coordinates password reset.
- `CurrentSession` stores the current active `Unknown` for downstream routing.

### `app.Auth.Flow.Services`

- `RegistrationService` collects and validates profile data, including libphonenumber-based international phone validation.
- `PasswordService` validates passwords and creates BCrypt hashes.
- `CollectLoginValues` collects username and hidden password input.
- `SetupCurrentSession` verifies credentials and routes by account status.
- `Unknown` stores active account ID, username, status, role, system-account flag, and menu-access flag.
- `FirstLoginFlow` creates pending-user access-request groundwork.
- Recovery services collect, validate, and route recovery input.
- Management services contain department and role input plus the job placeholder.
- Audit `CollectLogs` is a small login-result value object, not the database log repository.

### `app.CLIText`

Contains user-facing console messages and menus. `AdminMenu` displays five parent options: Requests, User, Security, Logs, and Logout. `LocalAdminMenu` currently displays only its heading. The nested `RequestMenu` is empty, and no menu option currently executes a business service.

### `app.Menu`

`MenuFlow` validates numeric menu input and returns a valid selected option. `MenuValues` carries `parentKonext`, `userRole`, and `childKontext` from `MenuControllerParrent` to `ServiceController`; the child context is currently always 0.

### `app.Repository`

- `ConfigRepository` manages recovery-key storage and starter accounts.
- `LoginRepository` reads account credentials, status, and current-login values.
- `RegistrationRepository` inserts registered accounts.
- `logsRepository` inserts login attempts.
- `AuthRepository.Management` handles failed-attempt counts, access requests, and assignment checks.
- `AuthRepository.Password` handles status policy and password updates.
- `AuthRepository.Recovery` handles recovery-key and target-account queries.
- `ServiceRepository.AdminServices` contains an access-request listing query. `ShowCurrentRequests` is currently disconnected from controller and service routing.

### `app.Services`

Contains the top-level `RouteService` placeholder. Its `userChoice` method is empty and not part of the active runtime path.

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
-> EnvSetup
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
-> MenuFlow for admin parent-option validation
-> MenuValues(parent, role, child=0)
-> FrontController(SERVICE)
-> ServiceController
-> role-specific handler that currently only logs startup
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
- Logging migration is partial; direct console diagnostics still exist throughout the source tree, while `logback.xml` writes console and category files.
- Several names do not follow Java naming conventions.
- `AccountPolicy`, `CollectUserJob`, `SetNewStatus`, `RouteService`, `RequestMenu`, `SubMenuController`, and `UIController` are placeholders.
- `SystemAccountRequiresPasswordChange` and `HasAssignedRole` exist but are not part of the active primary flow.
- `ShowCurrentRequests` is implemented but no longer connected after the current menu-routing refactor.
- The full password creation path and registration correction path have known hash-handling defects described in `../project_info/CURRENT_STATUS.md`.
