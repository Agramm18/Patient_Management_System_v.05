# Technical Overview

Last synchronized: 2026-07-18.

The filename is retained for compatibility with existing repository links. It should eventually be renamed to `TECHNICAL.md` together with every reference.

## Technology Stack

| Area | Current technology |
| --- | --- |
| Language target | Java 21 |
| Build | Maven with Wrapper 3.3.4, configured for Maven 3.9.16 |
| Entry point | `app.Main` through exec-maven-plugin 3.1.0 |
| Database | MySQL |
| Database access | JDBC with mysql-connector-j 9.6.0 |
| Environment loading | dotenv-java 3.0.0 |
| Password hashing | jBCrypt 0.4 |
| Phone validation | libphonenumber 9.0.31 |
| Logging | SLF4J API through Logback Classic 1.5.18 |
| Unit testing | JUnit Jupiter 5.13.4 and Surefire 3.5.3 |
| Documentation | Markdown and Mermaid |

## Runtime Architecture

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> ConfigController
-> FrontController(AUTH)
-> AuthController
-> FrontController(MENU) after an active session
-> MenuController
-> FrontController(SERVICE)
-> ServiceController
```

`FrontController` handles `CONFIG`, `AUTH`, `MENU`, and `SERVICE`. `UI` and `EXIT` are declared but not dispatched. `SubMenuController` is a constructor dependency without a request type or implementation.

The architecture separates:

- Controllers for top-level routing
- Authentication flows for registration, login, password creation, and recovery
- Services for input collection, validation, status handling, and session creation
- Repositories for JDBC operations
- CLI classes for menus and user-facing messages
- Menu records and validators for routing context
- A typed logging facade plus Logback configuration

Dependencies are constructed directly, database settings and session state are static, and repositories open a new connection per operation. There is no dependency-injection container or connection pool.

## Configuration Model

`EnvValidationService` loads `.env` and constructs `EnvSetup`, a record containing the 13 required values. Its compact constructor rejects blank values and database ports outside 1 through 65535.

Only database settings are copied back into `EnvValidationService` fields. Starter-account and recovery values are loaded again directly through dotenv in their respective classes.

`SQLValidationService` creates:

```text
jdbc:mysql://<DB_HOST>:<DB_PORT>/<DB_NAME>
```

It tests the connection once. `DBManager.initialize` then stores the URL, user, and password in static fields, and repositories call `DBManager.getConnection()` for new JDBC connections.

Current risks:

- Global mutable database configuration
- No connection pooling
- No transaction boundary spanning multi-step workflows
- Inconsistent repository return values
- SQL failures often printed and swallowed
- Recovery-key persistence failure does not fail configuration explicitly

## Authentication and Session Design

Login input uses `Scanner` for usernames and `System.console()` for hidden passwords. `CheckUserInDB` performs username lookup, BCrypt verification, and account-status lookup. `LoginVerification` routes by status and creates `CurrentUser` only for an active account.

`CurrentUser` stores:

- Username
- Account ID
- Account-status ID
- Role ID
- System-account flag
- Menu-access flag

`CurrentSession` stores one static `CurrentUser`. It has no clear or logout method, and no tests currently cover stale-session behavior.

## Password Design

Password policy requires:

- At least 10 characters
- At least one uppercase character
- At least one lowercase character
- At least one digit
- At least one special character

User-created and recovery passwords use `BCrypt.gensalt(15)`. Starter-account passwords and the startup recovery-key hash use cost 12. Login uses `BCrypt.checkpw`.

The current full creation sequence is incorrect:

```text
read original password
-> validate policy
-> read and compare retyped password
-> clear original and retyped arrays
-> convert the already-cleared original array to String
-> hash that value
```

The unit tests exercise policy and comparison methods separately and therefore do not catch this sequence defect. Hash generation needs an end-to-end test that verifies the resulting hash against the original input.

All password and recovery input requires a terminal-backed `System.console()`. IDE execution without a real console cannot reliably complete these flows.

## Registration Validation

`RegistrationService` currently validates:

- Usernames of 6 through 19 characters
- Nonblank email strings of 6 through 253 characters with an `@`, domain, dot, and suffix
- Phone strings beginning with `+`, containing only digits after the plus sign, and accepted by libphonenumber
- `y` or `n` confirmation values
- Correction choices 1 through 3

The correction branch generates a password but does not store the returned hash in `hashedPWSD`. `RegistrationFlow` can consequently call `CreateAccount` with a null hash. Uniqueness checks depend on database constraints.

## Login Status and Failed-Attempt Policy

Active users create a session. Disabled, locked, and quarantined accounts are rejected. Pending accounts create a department request without a session. Waiting starter accounts update their password and activation fields, then return to the authentication loop. Suspicious accounts return a successful login result without creating a session.

`CountFailedLoginAttempts` counts `INVALID_PASSWORD` rows from the previous 24 hours. Policy evaluation uses the previously stored count because the current attempt is inserted afterward:

| Stored count | Update |
| --- | --- |
| 5 or more | status 4, locked |
| 6 or more | status 7, suspicious |
| 25 or more | status 5, on quarantine |

The ordered `if` chain applies the highest matching threshold first, but the thresholds overlap and need a documented state-transition policy. No success or administrator reset clears the failed history.

## Recovery Design

At every successful startup, `RECOVERY_KEY` is hashed with a new BCrypt salt and upserted into `recovery_keys.id = 1`.

During recovery:

1. `ValidateRecoveryKey` reads hidden input.
2. `GetRecoveryKeyHash` loads row ID 1.
3. `CheckKeyStatus` verifies the key with BCrypt.
4. Four invalid attempts end the recovery flow.
5. `FindRecoverableUser` displays accounts marked as system accounts.
6. The user enters an account name.
7. `SelectUserForRecover` accepts any existing account, not only system accounts.
8. `UpdateSystemAccountPassword` updates only `password_hash`.

The stored hash is not checked for null before BCrypt verification. Recovery does not alter account status, password-change flags, menu access, or session state.

## Access-Request Design

A pending user chooses department ID 1 through 11. `FirstLogin` displays the corresponding job menu, but no job is selected. `CreateAccessRequest` inserts the account ID, department, job `unassigned`, and role ID 9. The database default supplies request status ID 3.

`CollectUserJob` is empty. `CollectUserRole` validates a role choice but returns no value and is not connected. Duplicate requests and request reasons are not handled.

`ShowCurrentRequests` joins access requests with accounts, departments, and roles. It prints directly from the repository, does not filter by status, and is not invoked by the current menu or service path.

## Menu and Service Routing

Role 1 displays a local-admin heading without options. Role 2 displays five admin parent options. `MenuFlow` validates an admin choice and `MenuValues` carries the parent context, role, and a child context of 0.

The current gaps are structural:

- `SubMenuController` is empty and not dispatched.
- `RequestMenu` is empty.
- `MenuValues.childKontext` is never populated.
- Both `ServiceController` role handlers only log startup.
- `ShowCurrentRequests` is disconnected.
- Other role IDs are rejected by `MenuControllerParrent`.
- There is no logout or session clearing.

## Logging Architecture

`LogManager` uses typed methods with state enums instead of the former single `LogType` switch. Active SLF4J logger fields are:

- `AUTH`
- `CONFIG`
- `SECURITY`
- `SYSTEM`
- `SQL`
- `CREDENTIALS`
- `BOOT`
- `MENU`

`logback.xml` also defines file appenders and logger entries for `ACCESS` and `DATABASE`, but `LogManager` has no corresponding logger fields. Recovery states are currently written to `SYSTEM`.

All configured category loggers write to both the console and a non-rolling file under `logs/`. The configuration has no root logger, size limit, rotation, or retention policy. Logging migration is partial and many classes still print diagnostics directly.

## Database Model

The current Java code expects these core tables:

- `roles`
- `account_status`
- `departments`
- `recovery_keys`
- `accounts`
- `login_attempts`
- `access_management`

Numeric role, department, and status IDs are embedded in Java code. The full schema and required reference rows are documented in `../setup/DB_SETUP.md`.

## Tests and Quality Tooling

Configured:

- Maven Wrapper files
- Java 21 compiler source and target
- JUnit Jupiter 5.13.4
- Maven Surefire 3.5.3
- Explicit Logback configuration
- 53 passing unit tests

Test coverage:

- 11 `PasswordServiceTest` methods
- 42 `RegistrationServiceTest` methods
- Password rule, retype, terminal fallback, username, email, phone, and choice validation

Not configured or not covered:

- Repository integration tests
- Complete registration, login, recovery, session, or routing tests
- CI
- Formatter or linter
- Static analysis
- Docker
- Test coverage reporting

The Windows `mvnw.cmd` script failed in the current PowerShell environment because the generated wrapper code indexed a null link target. The test suite was verified with the wrapper-managed Maven 3.9.16 distribution directly.

## Verified Command

On 2026-07-18, `mvn test` completed with 53 tests, 0 failures, 0 errors, and 0 skipped tests.

```powershell
.\mvnw.cmd test
```

The equivalent command with a globally installed Maven is `mvn test`.
