# Recruiter Overview

Last synchronized: 2026-07-23.

Patient Management System V5.01 is a Java 21 console-based learning and portfolio project. It demonstrates incremental development of a database-backed application with configuration validation, authentication, account security, runtime sessions, typed menu routing, access-management foundations, logging, unit testing, and technical documentation.

It is intentionally presented as an in-progress engineering project, not as a finished hospital product.

## Demonstrated Skills

- Java and Object-Oriented Programming
- Maven project organization and dependency management
- MySQL schema design and JDBC repositories
- Controller, flow, service, and repository separation
- Runtime configuration through `.env` and a validated `EnvSetup` record
- BCrypt password and recovery-key hashing
- Authentication and account-status handling
- Login-attempt auditing and database-backed policy foundations
- Immutable runtime state through `SessionAccount`
- Static session lifecycle methods through `CurrentSession`
- Typed menu modeling through records and enums
- Role-aware controller and service routing
- Typed logging state through SLF4J and Logback
- JUnit 5 unit testing
- Markdown and Mermaid documentation
- Git-based incremental development and refactoring

## Current Technical Baseline

- Fail-fast environment and database validation before authentication
- Automatic creation of missing local-admin and admin starter accounts
- Registration of pending accounts with username, email, international phone, and password validation
- BCrypt login verification and login-attempt persistence
- First-login password replacement for starter accounts
- Recovery-key verification with a four-attempt limit
- Account-status-specific behavior separated into `HandleAccountStatus`
- Active-user session creation with account ID, name, status, role, system-account flag, and menu access
- Pending department access requests
- Five immutable admin `MenuOption` entries mapped to typed `ServiceAction` values
- `MenuContextStructure(userRole, action)` routing through `MenuControllerParent` and `ServiceController`
- A connected `ADMIN_USER_REQUESTS` action that invokes the access-request listing query
- Console and per-category file logging through `logback.xml`

The 2026-07-23 source snapshot contains 93 production Java files and two test classes. A verified Windows Maven Wrapper run completed 55 tests successfully:

- 15 `PasswordServiceTest` tests
- 40 `RegistrationServiceTest` tests
- 0 failures, 0 errors, and 0 skipped tests

## Recent Refactoring

Password creation now converts the original character input before hashing and clears both password arrays afterward. Registration now returns corrected profile data to one confirmation step and stores the hash returned by `PasswordFlow`.

The former numeric menu contexts were replaced by `MenuOption`, `ServiceAction`, and `MenuContextStructure`. `MenuController` and `SubMenuController` were removed from the active design, and the Requests option is connected to `ShowCurrentRequests`.

The login path was separated into focused components:

- `CollectLoginValues` collects credentials.
- `SetupCurrentSession` verifies the account, password, and status.
- `HandleAccountStatus` executes status-specific behavior.
- `SessionAccount` models the active account.
- `CurrentSession` stores, reports, and clears the active account reference.
- `StoreLogs` carries the login-attempt values persisted by the repository.
- `CallPasswordPolicyRules` contains the failed-password policy call path.

## Current Engineering Limitations

- Invalid-password persistence and counting use different reason strings, so the current 24-hour policy thresholds do not advance coherently.
- The current attempt is evaluated before it is written to the login-attempt table.
- Complete password, login, recovery, database, session, and routing workflows are not covered by automated tests.
- Missing-terminal password creation reaches the generic fatal bootstrap handler instead of a controlled authentication result.
- Registration repositories do not return structured outcomes or independently reject null and blank hashes.
- Recovery's final account lookup is not restricted to the system accounts displayed to the user.
- Access requests still use a default job and role and have no connected approval or activation transaction.
- Four displayed admin actions and the local-admin dashboard are not implemented.
- `ServiceController` does not independently authorize role/action combinations.
- Logout and a repeated menu loop are not connected, although `CurrentSession.clear()` exists.
- Repository error handling and logging migration remain incomplete.

## Development Stage

Patient records, appointments, treatment, billing, reporting, complete administrator workflows, JavaFX, REST APIs, continuous integration, and deployment tooling remain future work.

The current engineering focus is to repair failed-login policy accounting, finish authorized service actions and logout, harden recovery and repository outcomes, complete access approval, and add integration-level test coverage.

## Repository Reading Order

1. [README](../../README.md)
2. [Current project status](CURRENT_STATUS.md)
3. [Project backlog](ToDo.md)
4. [Project structure](../architecture/PROJECT_STRUCTURE.md)
5. [Technical overview](../architecture/TECHNICHAL.md)
6. [Environment setup](../setup/ENV_SETUP.md)
7. [Database setup](../setup/DB_SETUP.md)
8. [Program-flow diagram](../architecture/diagramms/patient-management-uml.md)
