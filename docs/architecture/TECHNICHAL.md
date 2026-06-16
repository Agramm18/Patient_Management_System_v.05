# Technical Overview

Last synchronized: 2026-06-16.

## Technology Stack

| Area | Current technology |
| --- | --- |
| Language and runtime | Java 21 |
| Build | Maven |
| Entry point | `app.Main` through `exec-maven-plugin` 3.1.0 |
| Database | MySQL |
| Database access | JDBC |
| Environment loading | dotenv-java 3.0.0 |
| MySQL driver | mysql-connector-j 9.6.0 |
| Password hashing | jBCrypt 0.4 |
| Logging backend | Logback Classic 1.5.18 |
| Logging API | SLF4J through Logback |
| Documentation | Markdown and Mermaid |

## Runtime Architecture

The active runtime uses a controller-driven console architecture:

```text
Main
-> BootConfigService
-> FrontController
-> ConfigController
-> AuthController
-> MenuController when an active user has menu access
```

`FrontController` currently routes `CONFIG`, `AUTH`, and `MENU`. The service, UI, and exit request types are reserved but not routed through active switch cases.

The project separates code into:

- Controllers for top-level routing
- Flows for authentication use cases
- Services for input collection, validation, session creation, and coordination
- Repositories for JDBC operations
- CLI classes for user-facing menus and messages
- Menu classes for early role-aware menu routing
- Configuration classes for startup, connection values, recovery keys, and logging

## Database Access

`SQLValidationService` builds:

```text
jdbc:mysql://<DB_HOST>:<DB_PORT>/<DB_NAME>
```

It validates the connection once during startup. `DBManager.initialize` then stores the URL, user, and password in static fields. Repository methods call `DBManager.getConnection()` and open a new JDBC connection for each operation.

Current tradeoffs:

- The implementation is simple and easy to follow.
- Database configuration is global mutable state.
- There is no connection pool or dependency injection.
- Repositories return inconsistent success values and often print errors directly.

## Authentication, Sessions, and Passwords

Passwords are collected through `System.console()` and stored as BCrypt hashes.

- User-created and recovery passwords use `BCrypt.gensalt(15)`.
- Starter-account passwords and recovery-key startup hashes use `BCrypt.gensalt(12)`.
- Login verification uses `BCrypt.checkpw`.
- Password rules require at least 10 characters, uppercase, lowercase, number, and special character.
- Password creation allows three invalid policy attempts before throwing an exception.

A real terminal is required. IDE execution without a terminal-backed console can fail during login, registration password creation, starter password change, or recovery.

For active accounts:

1. `LoginVerification` validates username, password, and account status.
2. `CollectLoginValues` loads runtime account values from `accounts`.
3. `CurrentUser` stores username, account ID, status ID, role ID, system-account flag, and menu-access flag.
4. `CurrentSession` stores the active user for menu routing.

Session handling is still a baseline. It is static, not request-scoped, and it is not yet covered by automated tests.

## Menu Routing

Menu routing is partially implemented after authentication:

1. `BootConfigService` reads `CurrentSession.getCurrentUser()`.
2. If `hasAccessToMenu()` is true, it routes `FrontController.RequestType.MENU`.
3. `MenuController` reads the user's role.
4. Role `1` displays `LocalAdminMenu`.
5. Role `2` displays `AdminMenu`.
6. `AdminMenu` provides eight labels for planned administrative actions.
7. `MenuFlow.chooseOption` validates numeric input against the menu size.

Current limitations:

- `MenuFlow.chooseOption` always returns `false`.
- `MenuFlow.admin()` is empty.
- Menu labels do not execute real service workflows yet.
- Non-admin roles are not routed.

## Recovery Design

At startup:

1. `RECOVERY_KEY` is loaded from `.env`.
2. The plain value is hashed with BCrypt.
3. The hash is upserted into `recovery_keys.id = 1`.

During recovery:

1. The user enters the recovery key through `System.console()`.
2. The stored hash is loaded.
3. BCrypt verifies the key.
4. Up to four invalid attempts are allowed.
5. System accounts are displayed.
6. A selected existing account receives a new password hash.

The final lookup is not restricted to system accounts, and the recovery update changes only `password_hash`.

## Login Attempt and Status Policy

Every completed login result is inserted into `login_attempts`.

For invalid passwords, `CountFailedLoginAttempts` counts matching `INVALID_PASSWORD` rows from the previous 24 hours. `ExecutePWSDPolicy` updates:

- Status `4` at five or more stored attempts
- Status `7` at six or more stored attempts
- Status `5` at twenty-five or more stored attempts

The current attempt is inserted after policy evaluation. The policy therefore evaluates the previous stored count.

## Logging Architecture

`LogManager` is a static facade over named SLF4J loggers:

- `AUTH`
- `CONFIG`
- `SECURITY`
- `ACCESS`
- `SYSTEM`
- `DATABASE`
- `SQL`
- `CREDENTIALS`
- `BOOT`

`LogType` values are mapped to logger categories and levels in a switch.

Current state:

- Boot, configuration, recovery, and parts of authentication use the facade.
- Other classes still print diagnostic messages directly.
- No `src/main/resources/logback.xml` exists.
- Logback therefore uses its default console configuration.
- `MESSAGE`, `SYSTEM_WARN`, `SYSTEM_DEBUG`, and some other declared values are not currently handled consistently.
- `AUTH_DEBUG` falls through into `CONFIG_WARN` because it has no `break`.
- The `ACCESS` logger is declared but not used by the switch.

Logging is an active migration, not a completed subsystem.

## Security State

Implemented:

- BCrypt password hashes
- BCrypt recovery-key hashes
- Hidden terminal input
- Starter-account first password change
- Active-user session object
- Database-backed login attempt history
- Persisted locked, suspicious, and quarantine statuses
- Recovery-key retry limit
- First admin and local-admin menu routing after active login

Partially implemented:

- Failed-login threshold semantics and reset policy
- System-account recovery boundary
- Pending-user access requests
- Role and department checks
- Menu routing and service actions
- Diagnostic logging

Not implemented:

- Complete RBAC enforcement
- Access-request approval
- Complete admin workflows
- Automated security tests
- Central exception strategy

## Build and Quality Tooling

Configured:

- Maven
- Java 21 compiler target
- `exec-maven-plugin`

Not configured:

- Maven Wrapper
- JUnit or another test framework
- CI pipeline
- Formatter or linter
- Static analysis
- Docker
- Explicit Logback configuration

## Current Build Verification

The following command succeeds as of 2026-06-16:

```bash
mvn -DskipTests compile
```

Maven compiles 73 Java source files. There are no tests to execute.
