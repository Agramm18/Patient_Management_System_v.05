# Technical Overview

Last synchronized: 2026-07-31.

The filename is retained for compatibility with existing links. A future cleanup should rename it to `TECHNICAL.md` and update all references.

## Technology Stack

| Area | Current technology |
| --- | --- |
| Language target | Java 21 through Maven `source` and `target` properties |
| Build | Maven Wrapper 3.3.4, configured for Maven 3.9.16 |
| Entry point | `app.Main` through exec-maven-plugin 3.1.0 |
| Database | MySQL |
| Database access | JDBC with mysql-connector-j 9.6.0 |
| Environment loading | dotenv-java 3.0.0 |
| Password hashing | jBCrypt 0.4 |
| Phone validation | libphonenumber 9.0.31 |
| Logging | Logback Classic 1.5.18 and its SLF4J API |
| Unit testing | JUnit Jupiter 5.13.4 and Surefire 3.5.3 |
| Documentation | Markdown and Mermaid |

The project has no application framework, dependency-injection container, ORM, connection pool, GUI toolkit, or web stack.

## Verified Build State

On 2026-07-31, the Windows wrapper compiled 98 production sources and 2 test sources, then completed 55 tests with 0 failures, 0 errors, and 0 skipped tests.

The build emits a compiler warning because `source` and `target` 21 are configured without `release` 21. The tests cover service-level password and registration helpers only.

## Runtime Architecture

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> ConfigController
-> FrontController(AUTH)
-> AuthController
-> FrontController(MENU) after authentication
-> MenuControllerParent
-> FrontController(SERVICE)
-> ServiceController
```

Controllers coordinate flows, repositories execute JDBC, CLI classes render text, and records/enums carry outcomes and routing context. Dependencies are constructed directly. Database settings and the current session are static global state.

`FrontController` does not implement its declared `UI` and `EXIT` requests. `BootConfigService` performs only one menu/service pass and reports any runtime exception as `System Config Failed` before exiting with status 1.

## Bootstrap and Configuration

`EnvValidationService` requires `.env` in the working directory and constructs an `EnvSetup` record with five database values, three local-admin values, three admin values, `BOOTSTRAP_KEY`, and `RECOVERY_KEY`. All string values must be nonblank; the port must be 1 through 65535.

`SQLValidationService` tests `jdbc:mysql://<DB_HOST>:<DB_PORT>/<DB_NAME>`. `DBManager.initialize` then stores URL and credentials in static fields. Repositories open a new connection for each operation.

After database initialization:

1. `HandleRecoveryKey` hashes `RECOVERY_KEY` with BCrypt cost 12.
2. `SetRecoveryKey` upserts row ID 1.
3. `CheckForDefaultAccounts` searches for role IDs 1 and 2.
4. `CreateDefaultAccounts` inserts either missing starter account.

The recovery key is re-salted on every startup that reaches this stage. `SetRecoveryKey` catches SQL errors without returning failure. Starter-account fallback checks use fixed account IDs, and the bootstrap key is stored unchanged.

## Authentication Result Model

The active login path is:

```text
LoginService.CollectLoginValues
-> CheckInput
-> SetupCurrentSession
   -> PasswordPolicies for an invalid password
   -> HandleAccountStatus for valid credentials
-> StoreLogs(accountName, LoginOutcome, reason)
-> logsRepository.CollectLogs
```

`LoginOutcome` includes permitted, rejected, pending-request, password-changed, waiting-for-password-change, invalid-password, unknown-status, username-not-found, SQL, and input outcomes. Some identifiers are currently misspelled.

`LoginFlow` persists `is_success = true` only for `PERMITTED`. It returns to `AuthController` after `PASSWORD_CHANGED` or `PENDING_REQUEST`; every other non-permitted result repeats credential collection inside `LoginFlow`.

### Status Behavior

| Status | Result |
| --- | --- |
| `active` | Loads account data, stores `SessionAccount`, returns `PERMITTED`. |
| `disabled` | Returns `REJECTED`. |
| `pending` | Creates a default access request, returns `PENDING_REQUEST`, no session. |
| `locked` | Returns `REJECTED`. |
| `on_quarantine` | Returns `REJECTED`. |
| `waiting_for_password_change` | Runs password update; returns `PASSWORD_CHANGED` only when the first repository update reports success. |
| `suspicious` | Returns `REJECTED`. |

The active branch does not check menu access before setting the session. `AuthController` does check it before returning, which can leave an active but unusable session set while authentication continues. There is no connected logout.

## Failed-Login Policy

Wrong passwords use canonical reason `INVALID_PASSWORD`. `CountFailedLoginAttempts` loads the account ID and independently counts six windows. `includingAttempt()` adds the current attempt in memory before evaluation.

| `TimePeriod` | SQL window | Factor |
| --- | --- | ---: |
| `DAY` | Current calendar day | 1 |
| `WEEK` | Since midnight seven days ago | 2 |
| `MONTH` | Current calendar month | 4 |
| `YEAR` | Rolling 365 days | 8 |
| `FIVE_YEARS` | Rolling five years from the current date | 25 |
| `TEN_YEARS` | Rolling ten years from the current date | 50 |

Base thresholds are 5 for locked, 6 for suspicious, and 25 for quarantine. Each window multiplies the base by its factor. Policy evaluation checks quarantine first, then suspicious, then locked, and triggers when any window reaches its threshold.

Technical limitations:

- Six counts, the status update, and the later attempt insert are separate database operations.
- The status can change even when the attempt insert later fails.
- SQL failures in the counter return zero and enforcement fails open.
- `TimePeriod.getPeriod()` is not used to construct the SQL windows.
- The day and month queries are calendar-based while other queries are rolling.
- `PasswordPolicies.RETRY_COUNT` is ineffective because a new policy instance is created per invalid password.
- There is no success/reset policy and no automated policy coverage.

## Password and Registration Design

Passwords require at least 10 characters and uppercase, lowercase, numeric, and special-character content. User-created and recovery passwords use BCrypt cost 15. Starter passwords and the startup recovery-key hash use cost 12.

The creation sequence validates original input, reads a retype, converts the original array, hashes it, and clears both arrays in `finally`. Hidden input requires `System.console()`. Missing console input during login occurs before setup error handling and reaches bootstrap's fatal handler. Password re-entry has no retry loop and does not safely handle a null array.

Registration validates usernames of 6 through 19 characters, custom email structure up to 253 characters, and international phone numbers through libphonenumber. Corrected profile data returns to full confirmation, and the confirmed password hash is retained.

`CreateAccount` inserts status 3, role 9, department 12, no menu access, and a non-system flag. It has no repository-level blank-hash guard and no result contract. Database constraints provide uniqueness.

## Recovery Design

Recovery reads hidden input, loads `recovery_keys.id = 1`, verifies BCrypt, and stops after four invalid keys. It displays system accounts, but the final lookup accepts any existing username.

`UpdateSystemAccountPassword` updates `password_hash` and sets account status ID 1 in one statement. It returns no result and leaves `requires_password_change`, menu access, and session state unchanged. The stored recovery hash is not checked for missing or malformed data before BCrypt verification.

## Pending Access Requests

Pending users choose department ID 1 through 11. A job menu is displayed, but no job value is collected. `CreateAccessRequest` stores the selected department, job `unassigned`, role ID 9, and the database's default request status.

Job/role collection, duplicate handling, structured results, approval, rejection, activation, and authorization are not connected.

## Menu and Service Routing

`ServiceAction` declares five admin actions and `LOCAL_ADMIN_DASHBOARD`. `MenuOption` binds labels to actions, and `MenuContextStructure` carries `(userRole, action)`.

Role ID 1 returns `LOCAL_ADMIN_DASHBOARD`. Role ID 2 displays five admin options. Only `ADMIN_USER_REQUESTS` is implemented; it joins and prints all access requests without status filtering. All other actions throw and terminate through the generic bootstrap handler.

The service layer does not independently authorize the context against the active session. `RequestMenu`, `RouteService`, and `UIController` remain empty.

## Logging Architecture

`LogManager` exposes eight active logger names: `AUTH`, `CONFIG`, `SECURITY`, `SYSTEM`, `SQL`, `CREDENTIALS`, `BOOT`, and `MENU`. Logback defines those plus unused `ACCESS` and `DATABASE` categories. Recovery writes through `SYSTEM`.

All ten file appenders are non-rolling and have no retention limit. Diagnostic logging and user-facing console output remain mixed, and some messages contain account names or detailed state.

## Database Model

The Java code expects:

- `roles`
- `account_status`
- `departments`
- `recovery_keys`
- `accounts`
- `login_attempts`
- `access_management`

Numeric reference IDs are embedded in Java code. The application does not create or migrate the schema. See [DB_SETUP.md](../setup/DB_SETUP.md).

## Test and Quality State

Configured and verified:

- Java 21 compilation target
- Maven Wrapper 3.3.4 / Maven 3.9.16
- JUnit Jupiter 5.13.4 and Surefire 3.5.3
- 15 password tests and 40 registration tests
- Explicit Logback configuration

Not configured or covered:

- Complete console and BCrypt flows
- JDBC repositories and schema compatibility
- Login outcomes, sessions, and failed-attempt policies
- Recovery and target restrictions
- Menu/service authorization and logout
- Formatting, static analysis, coverage, CI, Docker, or deployment

## Commands

```powershell
.\mvnw.cmd test
.\mvnw.cmd exec:java
```

Complete environment and database setup first, and run interactive flows in a terminal-backed console.
