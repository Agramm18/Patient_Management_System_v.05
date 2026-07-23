# Project Structure

Last synchronized: 2026-07-23.

Patient Management System V5.01 is a Java 21 Maven console application. The repository currently contains 93 production Java files, two test classes, one Logback resource, Maven Wrapper scripts, and Markdown/Mermaid documentation.

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

- `pom.xml` uses Maven compiler `source` and `target` 21 and declares all runtime and test dependencies.
- Maven Wrapper 3.3.4 is configured to use Maven 3.9.16.
- `.env` is required at runtime, is local to the project root, and is ignored by Git.
- `logs/`, `*.log`, `target/`, and `*.sql` are generated or local paths ignored by Git.
- `src/main/resources/logback.xml` defines console and category-specific file logging.
- The test suite contains 55 passing JUnit 5 tests across two test classes.

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
|           |   |-- CollectLoginValues.java
|           |   |-- CurrentAccountInSessionValues.java
|           |   |-- FirstLoginFlow.java
|           |   |-- HandleAccountStatusTasks.java
|           |   |-- LogsForDB.java
|           |   `-- SetupCurrentSession.java
|           |-- PasswordService/
|           |   |-- CallPasswordPolicyRules.java
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
|                   `-- AdminChilds/
|                       `-- RequestMenu.java
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
|   |-- Enums/
|   |   `-- ServiceAction.java
|   |-- MenuContextStructure.java
|   |-- MenuFlow.java
|   `-- MenuOption.java
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
    |-- AccountRoles.java
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

- `PasswordServiceTest` contains 15 tests for terminal fallback, password rules, password matching, and clearing password arrays.
- `RegistrationServiceTest` contains 40 tests for username, email, phone, confirmation/correction input, and collected-password validation.
- On 2026-07-23, `.\mvnw.cmd test` completed with 55 tests, 0 failures, 0 errors, and 0 skipped tests.
- Repository integration, complete authentication-flow, session, menu, and service tests do not exist yet.

## Package Responsibilities

### `app`

`Main` creates the shared `Scanner`, prints `StartMSG`, displays the loader through `BootConfigService`, and starts system configuration.

### `app.Bootstrap`

`BootConfigService` constructs `AuthController`, `ConfigController`, `MenuControllerParent`, `ServiceController`, `UIController`, and `FrontController`. It routes configuration and authentication, requires a current account with menu access, then performs one `MENU` dispatch followed by one `SERVICE` dispatch. Any `RuntimeException` from these phases is caught as a fatal error, prints the generic message `System Config Failed`, and exits with status 1.

### `app.Controller`

- `FrontController` dispatches `CONFIG`, `AUTH`, `MENU`, and `SERVICE`.
- `UI` and `EXIT` remain declared `RequestType` values without switch cases and therefore return `false`.
- `ConfigController` validates `.env`, tests and stores database configuration, persists the startup recovery-key hash, and ensures both starter roles exist.
- `AuthController` owns the registration/login/recovery/exit loop and returns only for a current account with menu access and active status ID 1.
- `MenuControllerParent` selects a menu by numeric role ID and returns a `MenuContextStructure`.
- `ServiceController` dispatches by `ServiceAction`. Only `ADMIN_USER_REQUESTS` is implemented; it calls `ShowCurrentRequests`.
- `UIController` is an empty injected placeholder.

There is no `MenuController` or `SubMenuController` class in the current source tree.

### `app.Config`

- `EnvValidationService` requires a root `.env` file, loads it with dotenv-java, and constructs an `EnvSetup` record.
- `EnvSetup` validates 13 required settings and a database port from 1 through 65535.
- `SQLValidationService` builds and tests the JDBC URL.
- `DBManager` stores static database runtime values and opens a new JDBC connection for each repository call.
- `HandleRecoveryKey` loads and hashes `RECOVERY_KEY`.

### `app.Logging`

`LogManager` provides typed methods for boot, authentication, configuration, security, SQL, system, recovery, menu, account, and other-input events. Ten enums under `Logging.Enums.ProgrammState` define accepted states. `logback.xml` configures console output and ten non-rolling category files.

### `app.Auth.Flow`

- `RegistrationFlow` coordinates validated registration data and account insertion.
- `LoginFlow` collects one credential pair, delegates session configuration, and persists one `StoreLogs` result to `login_attempts`; the surrounding retry loop is in `AuthController`.
- `PasswordFlow` delegates password creation to `PasswordService`.
- `RecoveryFlow` validates the recovery key and coordinates password reset.
- `CurrentSession` stores one static `SessionAccount` record and exposes `setCurrentAccount`, `getCurrentAccount`, `isLoggedIn`, and `clear`.

### `app.Auth.Flow.Services`

- `RegistrationService` validates profile data, supports repeated confirmation/correction, requires a nonblank generated hash, and uses libphonenumber for international phone validation.
- `PasswordService` validates passwords, creates BCrypt hashes, and clears both character arrays after hashing.
- Login services are split into input collection (`CollectLoginValues`), credential/status orchestration (`SetupCurrentSession`), status-specific work (`HandleAccountStatus`), session data (`SessionAccount`), persistence results (`StoreLogs`), and pending-user setup (`FirstLoginFlow`).
- `CallPasswordPolicyRules` reads stored failed-login counts and calls status-update repositories after an invalid password.
- Recovery services collect, validate, and route recovery input.
- Management services contain department and role input plus the empty `CollectUserJob` placeholder.
- `AuthSecurityService.Audit.CollectLogs` is an older disconnected value object; active login persistence uses `LoginService.LogsForDB` and `Repository.logsRepository.CollectLogs`.

### `app.CLIText`

Contains user-facing console messages and menus. `AdminMenu` exposes five `MenuOption` records: Requests, User, Security, Logs, and Logout. `LocalAdminMenu` displays only a heading. `RequestMenu` is empty.

### `app.Menu`

- `ServiceAction` identifies five admin actions and one local-admin dashboard action.
- `MenuOption` pairs a display label with a `ServiceAction`.
- `MenuContextStructure` carries the numeric role and selected action from `MenuControllerParent` to `ServiceController`.
- `MenuFlow` repeatedly validates a one-based numeric choice against the supplied menu size.

### `app.Repository`

- Configuration repositories manage recovery-key persistence and starter accounts.
- Login repositories perform account lookup, password verification, status lookup, and session-value queries.
- Registration repositories insert new accounts.
- `logsRepository.CollectLogs` inserts one `login_attempts` row for each completed login result.
- Authentication repositories manage access requests, failed-attempt counts, password/status updates, and recovery queries.
- `ShowCurrentRequests` joins access requests with accounts, departments, and roles and is now connected to `ADMIN_USER_REQUESTS`.

Repository methods generally create their own connection through static `DBManager`. Several methods still catch, print, and swallow SQL failures instead of returning a reliable result to the caller.

### `app.Services`

`AccountRoles` declares ten role names but is not referenced by the active numeric-role routing. `RouteService.userChoice` is empty and not part of the runtime path.

## Connected Runtime Flow

### Startup and Configuration

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> ConfigController
-> EnvValidationService / EnvSetup
-> SQLValidationService
-> DBManager.initialize
-> HandleRecoveryKey / SetRecoveryKey
-> CheckForDefaultAccounts / CreateDefaultAccounts when required
-> FrontController(AUTH)
-> AuthController
```

### Login and Session

```text
LoginFlow
-> LoginService.CollectLoginValues
-> SetupCurrentSession
-> CheckUserInDB
-> CallPasswordPolicyRules on an invalid password
   or HandleAccountStatusTasks after valid credentials
-> LoginService.LogsForDB
-> logsRepository.CollectLogs
-> CurrentAccountInSessionValues / CurrentSession only for active status
```

`LoginFlow` handles one attempt and returns. `AuthController` redisplays the authentication menu until an active, menu-enabled session exists.

### Menu and Service

```text
BootConfigService
-> CurrentSession.getCurrentAccount
-> FrontController(MENU)
-> MenuControllerParent
-> role 1: LocalAdminMenu -> LOCAL_ADMIN_DASHBOARD
   role 2: AdminMenu -> MenuFlow -> selected ServiceAction
-> MenuContextStructure(role, action)
-> FrontController(SERVICE)
-> ServiceController
-> ADMIN_USER_REQUESTS -> ShowCurrentRequests
```

All other declared actions currently reach `ServiceController`'s default branch, which throws `IllegalStateException`; `BootConfigService` catches it and exits with status 1. The request-list action runs once and then the application reaches the end of `main`; there is no persistent service/menu loop.

## Structural Notes

- Database configuration and session state are global static values.
- Dependencies are constructed directly; there is no dependency-injection container or connection pool.
- `CurrentSession.clear()` exists, but no logout action calls it.
- The new action-based menu model replaced the former numeric `MenuValues` parent/child context.
- `ShowCurrentRequests` is connected only to the Requests action. The other admin actions and local-admin dashboard are not implemented.
- `AccountRoles`, `RouteService`, `RequestMenu`, `CollectUserJob`, `SetNewStatus`, and `UIController` are unused or empty placeholders.
- Logging migration is partial; production sources still contain extensive direct console output alongside SLF4J/Logback logging.
- Several package, class, method, and variable names do not follow standard Java naming conventions; their current spellings are retained here to match the source.
