# Project Structure

Last synchronized: 2026-07-31.

Patient Management System V5.01 is a Java 21 Maven console application. The repository contains 98 production Java files, 2 test files, 1 Logback resource, Maven Wrapper scripts, and Markdown/Mermaid documentation.

## Root Structure

```text
Patient_Management_System_v.05/
|-- .mvn/wrapper/maven-wrapper.properties
|-- docs/
|   |-- architecture/
|   |   |-- diagramms/
|   |   |   |-- patient-management-uml.md
|   |   |   `-- patient-management-uml.mmd
|   |   |-- PROJECT_STRUCTURE.md
|   |   `-- TECHNICHAL.md
|   |-- archive/ARCHIVE.md
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
|   `-- test/java/app/Auth/Flow/Services/
|-- .env
|-- .gitignore
|-- mvnw
|-- mvnw.cmd
|-- pom.xml
`-- README.md
```

Root notes:

- `pom.xml` targets Java 21 and declares runtime and test dependencies.
- Maven Wrapper 3.3.4 is configured for Maven 3.9.16.
- `.env` is required at runtime and ignored by Git.
- `target/`, `logs/`, `*.log`, and `*.sql` are generated or local paths ignored by Git.
- `logback.xml` defines console and category-specific file logging.

## Production Source Tree

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
|           |   |-- Audit/CollectLogs.java
|           |   |-- Management/
|           |   |   |-- CollectUserDepartment.java
|           |   |   |-- CollectUserJob.java
|           |   |   `-- CollectUserRole.java
|           |   `-- Recovery/
|           |       |-- CheckKeyStatus.java
|           |       |-- SelectUserForRecovery.java
|           |       `-- ValidateRecoveryKey.java
|           |-- LoginService/
|           |   |-- CheckInput.java
|           |   |-- CollectLoginValues.java
|           |   |-- FirstLoginFlow.java
|           |   |-- SetupCurrentSession.java
|           |   `-- LoginBehaviour/
|           |       |-- HandleAccountStatus.java
|           |       |-- LoginOutcome.java
|           |       |-- SessionAccount.java
|           |       `-- StoreLogs.java
|           |-- PasswordService/
|           |   |-- PasswordPolicies.java
|           |   |-- PasswordService.java
|           |   `-- PolicieBehaviour/
|           |       |-- PolicyThreshold.java
|           |       `-- TimePeriod.java
|           `-- RegistrationService/RegistrationService.java
|-- Bootstrap/BootConfigService.java
|-- CLIText/
|   |-- DisplayMessages/
|   `-- Menus/
|       |-- DepartmentJobs/
|       |-- Departments/
|       |-- Program/
|       `-- ServiceMenus/ParrentMenus/
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
|   |-- MenuControllerParent.java
|   |-- ServiceController.java
|   `-- UIController.java
|-- Logging/
|   |-- LogManager.java
|   `-- Enums/ProgrammState/
|-- Menu/
|   |-- Enums/ServiceAction.java
|   |-- MenuContextStructure.java
|   |-- MenuFlow.java
|   `-- MenuOption.java
|-- Repository/
|   |-- AuthRepository/
|   |   |-- Management/
|   |   |   |-- CountFailedLoginAttempts.java
|   |   |   |-- CreateAccessRequest.java
|   |   |   |-- HasAssignedDepartment.java
|   |   |   |-- HasAssignedRole.java
|   |   |   `-- PolicieThresholdStructure.java
|   |   |-- Password/
|   |   |-- Recovery/
|   |   `-- SetNewStatus.java
|   |-- ConfigRepository/
|   |-- LoginRepository/
|   |-- RegistrationRepository/CreateAccount.java
|   |-- ServiceRepository/AdminServices/ShowCurrentRequests.java
|   `-- logsRepository/CollectLogs.java
`-- Services/
    |-- AccountRoles.java
    `-- RouteService.java
```

The CLI and repository branches are abbreviated where every filename is a menu, state enum, or direct JDBC operation. `rg --files src/main/java` is the authoritative complete file list.

## Test Tree

```text
src/test/java/app/Auth/Flow/Services/
|-- PasswordService/PasswordServiceTest.java
`-- RegistrationService/RegistrationServiceTest.java
```

- `PasswordServiceTest` contains 15 tests.
- `RegistrationServiceTest` contains 40 tests.
- On 2026-07-31, `.\mvnw.cmd test` passed all 55 tests.
- Repository, complete authentication, policy-window, session, menu, and service integration tests do not exist yet.

## Package Responsibilities

### `app` and `app.Bootstrap`

`Main` creates the shared `Scanner`, displays startup text, and delegates to `BootConfigService`. Bootstrap constructs the controllers, routes configuration and authentication, then performs one menu and service pass. It catches every runtime exception as a generic fatal configuration error.

### `app.Controller`

- `FrontController` dispatches `CONFIG`, `AUTH`, `MENU`, and `SERVICE`.
- `AuthController` owns the register/login/recovery/exit menu.
- `ConfigController` validates configuration, initializes JDBC settings, persists the recovery-key hash, and ensures starter accounts exist.
- `MenuControllerParent` maps role IDs 1 and 2 to typed menu actions.
- `ServiceController` implements only `ADMIN_USER_REQUESTS`.
- `UIController` is empty.

### `app.Config`

Configuration classes load and validate the 13 required environment values, test the JDBC URL, retain static database settings, and hash the startup recovery key.

### `app.Auth.Flow`

- `RegistrationFlow` coordinates validated registration data and account insertion.
- `LoginFlow` repeats credential collection until a permitted result or until pending/password-change work returns control to authentication.
- `PasswordFlow` delegates password creation.
- `RecoveryFlow` verifies the recovery key and updates a selected account.
- `CurrentSession` stores one static `SessionAccount` and exposes set, get, status, and clear operations.

### `app.Auth.Flow.Services`

- Login input is collected by `CollectLoginValues` and checked through `CheckInput`.
- `SetupCurrentSession` coordinates credential, policy, and status behavior.
- `HandleAccountStatus` returns typed `LoginOutcome` values.
- `StoreLogs` transports account name, outcome, and persistence reason.
- `PasswordPolicies` combines counts from `PolicieThresholdStructure` with `PolicyThreshold` and `TimePeriod`.
- Registration and password services implement validation and BCrypt hash creation.
- Recovery services validate the key and collect a recovery target.
- Management services support pending department requests; job collection remains empty.

### `app.Menu` and `app.CLIText`

CLI classes render messages and menus. `MenuOption` pairs a label with a `ServiceAction`; `MenuContextStructure` carries role and action to the service controller; `MenuFlow` validates a one-based choice.

### `app.Repository`

Repositories perform direct JDBC operations for configuration, login lookup, attempt logging, registration, recovery, policy counting and status changes, access requests, and request listing. Most methods open their own connection. Several still print and swallow SQL failures instead of returning a reliable result.

### `app.Logging`

`LogManager` maps ten program-state enums to eight active SLF4J logger names. `logback.xml` writes console output and ten non-rolling category files.

### `app.Services`

`AccountRoles` is not used by active numeric role routing. `RouteService` is empty.

## Connected Runtime Flow

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> EnvValidationService / SQLValidationService / DBManager
-> recovery-key persistence and starter-account checks
-> FrontController(AUTH)
-> AuthController
-> LoginFlow
-> CollectLoginValues
-> SetupCurrentSession
   -> PasswordPolicies for invalid passwords
   -> HandleAccountStatus for valid credentials
-> StoreLogs persisted by logsRepository.CollectLogs
-> SessionAccount / CurrentSession only for active status
-> FrontController(MENU)
-> MenuControllerParent
-> MenuContextStructure
-> FrontController(SERVICE)
-> ServiceController
-> ADMIN_USER_REQUESTS -> ShowCurrentRequests
```

Only `ADMIN_USER_REQUESTS` completes normally. Other admin actions and `LOCAL_ADMIN_DASHBOARD` throw. There is no persistent service loop or connected logout.

## Structural Notes

- Static database configuration and session state are global.
- Dependencies are constructed directly; there is no container or connection pool.
- Failed-login counting uses six database queries followed by a separate status update and later attempt insert.
- `CurrentSession.clear()` has no production caller.
- `RequestMenu`, `CollectUserJob`, `RouteService`, and `UIController` are empty; `SetNewStatus` and the older audit value object are disconnected.
- Naming inconsistencies are retained in this document where they match source paths.
