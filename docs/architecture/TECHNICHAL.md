# Technical Overview

Last synchronized: 2026-07-23.

The filename is retained for compatibility with existing repository links. It should eventually be renamed to `TECHNICAL.md` together with every reference.

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
| Logging | SLF4J API through Logback Classic 1.5.18 |
| Unit testing | JUnit Jupiter 5.13.4 and Surefire 3.5.3 |
| Documentation | Markdown and Mermaid |

The project does not declare a framework, dependency-injection container, ORM, connection pool, GUI toolkit, or web stack.

## Verified Build State

On 2026-07-23, the Windows wrapper ran successfully:

```text
Apache Maven 3.9.16
Java runtime 26.0.1 (Oracle)
Windows 11
```

The configured compilation target remains Java 21; the Java 26 value above describes only the local verification runtime. `.\mvnw.cmd test` completed with 55 tests, 0 failures, 0 errors, and 0 skipped tests.

## Runtime Architecture

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> ConfigController
-> FrontController(AUTH)
-> AuthController
-> FrontController(MENU) after an active, menu-enabled session
-> MenuControllerParent
-> FrontController(SERVICE)
-> ServiceController
```

`FrontController` actively dispatches `CONFIG`, `AUTH`, `MENU`, and `SERVICE`. `UI` and `EXIT` are declared request types without switch cases. `UIController` is constructed and injected but empty. There is no current `SubMenuController`.

The implementation separates:

- Controllers for top-level routing
- Authentication flows for registration, login, password creation, and recovery
- Login services for credential collection, session setup, status-specific behavior, and database-log result data
- Repositories for JDBC operations
- CLI classes for menus and user-facing messages
- Records and enums for session and action-routing context
- A typed logging facade plus Logback configuration

Dependencies are constructed directly. Database settings and the current session are static global state, and repositories normally open a new connection per operation. There is no application-wide transaction boundary across multi-step workflows.

## Bootstrap and Configuration

`EnvValidationService` requires `.env` in the process working directory and constructs `EnvSetup`, a record containing 13 values:

- Five database values
- Three local-admin values
- Three admin values
- `BOOTSTRAP_KEY`
- `RECOVERY_KEY`

The record rejects null or blank string values and database ports outside 1 through 65535. Only the five database settings are copied into `EnvValidationService` fields. `CreateDefaultAccounts` and `HandleRecoveryKey` load their values again directly through dotenv.

`SQLValidationService` builds and tests:

```text
jdbc:mysql://<DB_HOST>:<DB_PORT>/<DB_NAME>
```

`DBManager.initialize` stores the URL, username, and password in static fields. `DBManager.getConnection()` then opens a new JDBC connection for repository operations.

After database initialization:

1. `HandleRecoveryKey` hashes `RECOVERY_KEY` with BCrypt cost 12.
2. `SetRecoveryKey` upserts that new hash into `recovery_keys.id = 1`.
3. `CheckForDefaultAccounts` searches for role IDs 1 and 2.
4. `CreateDefaultAccounts` creates either missing starter account.

Important behavior and risks:

- The recovery key receives a new salt and replaces its database hash on every successful startup.
- `SetRecoveryKey` catches SQL errors and returns no status, so recovery-key persistence failure does not fail configuration.
- Several repositories print and swallow SQL failures.
- Starter-account post-insert fallback checks still use fixed account IDs 1 and 2.
- Any later `RuntimeException` in authentication, menu, or service routing is caught by `BootConfigService` under the misleading console message `System Config Failed` before exit status 1.

## Authentication and Session Design

`AuthController` repeatedly offers:

1. Registration
2. Login
3. System-account recovery
4. Exit

`LoginFlow` handles one credential pair per call. Retry behavior comes from the surrounding `AuthController` loop, not from the effectively single-iteration `while` in `LoginFlow`.

The active login service path is:

```text
LoginService.CollectLoginValues
-> SetupCurrentSession
-> CheckUserInDB
-> CallPasswordPolicyRules on an invalid password
   or HandleAccountStatusTasks after valid credentials
-> LogsForDB
-> logsRepository.CollectLogs
```

Only an `active` account creates `CurrentAccountInSessionValues`. The record stores:

- Account ID
- Account name
- Account-status ID
- Menu-access flag
- System-account flag
- Role ID

`CurrentSession` holds one static record and exposes `setCurrentAccount`, `getCurrentAccount`, `isLoggedIn`, and `clear`. The `clear` method is not called anywhere in production code, so there is still no connected logout or session-reset action.

### Status Outcomes

| Account status | Current behavior |
| --- | --- |
| `active` | Loads account fields, stores `CurrentAccountInSessionValues`, and returns `canUseSystem = true`. |
| `disabled` | Rejects access and returns `false`. |
| `pending` | Runs department/request setup without a session but returns `canUseSystem = true`. |
| `locked` | Rejects access and returns `false`. |
| `on_quarantine` | Rejects access and returns `false`. |
| `waiting_for_password_change` | Runs password creation/update without a session and returns `true`, even if the repository update reports failure. |
| `suspicious` | Warns the user, creates no session, and returns `false`. |
| Unknown or unsupported | Returns a failed result; an unsupported status is thrown inside the status handler and converted by `SetupCurrentSession`. |

`LoginFlow` persists `LogsForDB.canUseSystem` as `login_attempts.is_success`. Consequently, pending setup and first password-change attempts are recorded as successful even though they do not create a usable session. `AuthController` still returns to its menu unless the static session is non-null, has menu access, and has status ID 1.

## Failed-Login Policy

`CallPasswordPolicyRules` asks `CountFailedLoginAttempts` for the previously stored count and contains these ordered thresholds:

| Previously counted rows | Intended update |
| --- | --- |
| 25 or more | `on_quarantine`, status ID 5 |
| 6 through 24 | `suspicious`, status ID 7 |
| Exactly 5 | `locked`, status ID 4 |

The current implementation has a critical persistence mismatch:

- `CountFailedLoginAttempts` counts only rows whose `failure_reason` is exactly `INVALID_PASSWORD`.
- `CallPasswordPolicyRules` now returns the reason `to many false attempts`.
- `LoginFlow` stores that returned reason after policy evaluation.

New invalid-password rows therefore do not increase the count queried by the policy. Unless matching legacy rows already exist, repeated wrong passwords remain at a stored count of zero and do not reach the lock, suspicious, or quarantine thresholds. The in-memory `RETRYS` field is also ineffective because a new `CallPasswordPolicyRules` instance is created for each failed attempt.

There is no successful-login or administrator reset for historical failed attempts.

## Password Design

Password policy requires:

- At least 10 characters
- At least one uppercase character
- At least one lowercase character
- At least one digit
- At least one special character

User-created and recovery passwords use `BCrypt.gensalt(15)`. Starter-account passwords and the startup recovery-key hash use cost 12. Login verification uses `BCrypt.checkpw`.

The current creation sequence is:

```text
read original password
-> validate policy
-> read and compare retyped password
-> convert the original character array to a String
-> hash the String
-> clear both character arrays in a finally block
```

The former defect that cleared the original array before conversion is no longer present. The tests now cover array clearing, but there is still no end-to-end test that creates a password through console input and verifies the produced BCrypt hash against the original input.

All hidden input relies on `System.console()`:

- Login without a terminal throws `IllegalStateException`; it reaches `BootConfigService`, which exits with status 1.
- Recovery without a terminal ends the recovery branch and returns to the authentication menu.
- `PasswordService.userPWSD` propagates its no-console `RuntimeException` to `BootConfigService`, which exits with status 1 instead of returning a controlled authentication result.

## Registration Validation

`RegistrationService` validates:

- Usernames of 6 through 19 characters
- Nonblank email strings of 6 through 253 characters with an `@`, nonblank domain name, dot, and suffix
- International phone strings beginning with `+`, containing digits only after the plus sign, and accepted by libphonenumber
- `y` or `n` confirmation values
- Correction choices 1 through 3
- A non-null, nonblank password hash before registration data is returned

If the user selects correction, only username, email, or phone can be changed. The loop then displays the updated data and asks for full confirmation again. Password creation occurs only after a final `y`, and its returned hash is assigned to `hashedPWSD`.

Remaining limitations:

- Username and email uniqueness depend on database constraints.
- `CreateAccount` itself does not guard against a null or blank hash.
- Repository insertion failures are caught and printed instead of being returned to `RegistrationFlow`.
- Validation is custom and does not fully implement email-address standards.

## Recovery Design

During recovery:

1. `ValidateRecoveryKey` reads hidden input.
2. `GetRecoveryKeyHash` loads row ID 1.
3. `CheckKeyStatus` verifies the key with BCrypt.
4. Four invalid keys end the recovery flow.
5. `FindRecoverableUser` displays accounts with `is_system_account = true`.
6. The user enters an account name.
7. `SelectUserForRecover` accepts any existing account, not only the displayed system accounts.
8. `PasswordService` creates a new hash.
9. `UpdateSystemAccountPassword` updates only `password_hash`.

The stored recovery hash is not checked for null or malformed data before `BCrypt.checkpw`. Recovery does not change account status, `requires_password_change`, menu access, or session state.

## Pending Access Requests

A pending user chooses department ID 1 through 11. `FirstLoginFlow` displays a department-specific job menu, but no job value is collected. System jobs are displayed only if the account's existing department is ID 11 or 5.

`CreateAccessRequest` stores:

- The requesting account ID
- The selected department ID
- Job `unassigned`
- Role ID 9
- The database default request status

`CollectUserJob` is empty. Role selection, job selection, duplicate-request handling, reasons, approval, rejection, account activation, and authorization checks are not connected.

## Menu and Service Routing

The former numeric parent/child `MenuValues` model has been replaced by action-based records:

- `ServiceAction` declares five admin actions and `LOCAL_ADMIN_DASHBOARD`.
- `MenuOption` pairs a label with an action.
- `MenuContextStructure` carries `(userRole, action)`.

`MenuControllerParent` routes:

- Role ID 1 to `LocalAdminMenu`, then directly to `LOCAL_ADMIN_DASHBOARD`.
- Role ID 2 to `AdminMenu`, which displays Requests, User, Security, Logs, and Logout and returns the selected action.
- Any other role to `IllegalArgumentException`.

`ServiceController` currently implements only:

```text
ADMIN_USER_REQUESTS
-> ShowCurrentRequests.CurrentRequests
```

`ShowCurrentRequests` joins `access_management`, `accounts`, `departments`, and `roles`, prints all returned rows, and does not filter by request status.

The other four admin actions and `LOCAL_ADMIN_DASHBOARD` reach the default branch, throw `IllegalStateException`, and cause `BootConfigService` to exit with status 1. There is no persistent menu loop: the Requests action runs once, returns through `main`, and the application ends naturally.

`RequestMenu`, `RouteService`, and `UIController` remain empty. `AccountRoles` is declared but unused; active routing still depends on numeric role IDs.

## Logging Architecture

`LogManager` exposes typed methods backed by ten program-state enums. Its eight active SLF4J logger fields are:

- `AUTH`
- `CONFIG`
- `SECURITY`
- `SYSTEM`
- `SQL`
- `CREDENTIALS`
- `BOOT`
- `MENU`

`src/main/resources/logback.xml` defines a console appender plus non-rolling files under `logs/` for:

- `AUTH`
- `CONFIG`
- `SECURITY`
- `ACCESS`
- `DATABASE`
- `SYSTEM`
- `SQL`
- `CREDENTIALS`
- `BOOT`
- `MENU`

`ACCESS` and `DATABASE` have configured loggers and appenders but no matching `LogManager` fields. Recovery events are routed to `SYSTEM`. The configuration has no root logger, rotation, size limit, or retention policy.

Logging migration remains partial. User-facing messages and diagnostics are mixed, and production classes still contain extensive `System.out` output. Some logs include account names, account status, JDBC URLs, and other operational details.

## Database Model

The Java code expects these core tables:

- `roles`
- `account_status`
- `departments`
- `recovery_keys`
- `accounts`
- `login_attempts`
- `access_management`

Numeric role, status, and department IDs are embedded in Java code. The expected schema and reference values are documented in `../setup/DB_SETUP.md`.

## Tests and Quality Tooling

Configured and verified:

- Maven Wrapper 3.3.4 / Maven 3.9.16
- Java 21 compiler source and target
- JUnit Jupiter 5.13.4
- Maven Surefire 3.5.3
- 15 passing `PasswordServiceTest` methods
- 40 passing `RegistrationServiceTest` methods
- 55 tests total
- Explicit Logback configuration

Not covered:

- Complete console password creation and BCrypt verification
- Complete registration and correction flows
- Database repositories and schema compatibility
- Login status routing and failed-attempt transitions
- Recovery limits and target restrictions
- Session creation, stale-session behavior, and clearing
- Menu/service action routing
- Logging configuration behavior

Not configured:

- CI
- Formatter or linter
- Static analysis
- Code coverage reporting
- Docker or deployment automation

## Commands

```powershell
.\mvnw.cmd test
.\mvnw.cmd exec:java
```

If Maven is installed globally, the equivalent commands are `mvn test` and `mvn exec:java`. Complete `../setup/ENV_SETUP.md` and `../setup/DB_SETUP.md` before starting the application. Run the interactive application in a real terminal because hidden input depends on `System.console()`.
