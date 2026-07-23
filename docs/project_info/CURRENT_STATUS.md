# Current Project Status

Last synchronized: 2026-07-23.

Patient Management System V5.01 is a Java 21 Maven console application. The current implementation concentrates on configuration, authentication, account recovery, session state, access requests, logging, and the first typed menu-to-service route. It is a learning and portfolio project, not a production-ready hospital system, and must not be used with real patient data.

## Verified Snapshot

| Item | Verified state |
| --- | --- |
| Production sources | 93 Java files under `src/main/java` |
| Test sources | 2 Java files under `src/test/java` |
| Automated tests | 55 passed, 0 failed, 0 errored, 0 skipped |
| Test split | 15 `PasswordServiceTest` tests and 40 `RegistrationServiceTest` tests |
| Application resource | `src/main/resources/logback.xml` |
| Java target | Java 21 through Maven `source` and `target` properties |
| Maven Wrapper | Wrapper 3.3.4 configured for Maven 3.9.16 |

`.\mvnw.cmd test` completed successfully in Windows PowerShell on 2026-07-23. The suite is unit-level only and does not connect to MySQL or exercise complete console workflows.

## Active Runtime

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> ConfigController
-> FrontController(AUTH)
-> AuthController authentication loop
-> CurrentAccountInSessionValues stored in CurrentSession
-> FrontController(MENU)
-> MenuControllerParent
-> MenuContextStructure(userRole, ServiceAction)
-> FrontController(SERVICE)
-> ServiceController
```

`FrontController` actively handles `CONFIG`, `AUTH`, `MENU`, and `SERVICE`. `UI` and `EXIT` remain declared request types without switch cases, and `UIController` is empty.

The application performs only one menu and service pass after authentication. A successful request listing then reaches the end of `Main`. Runtime exceptions from configuration, authentication, menu, or service code are all caught by `BootConfigService`, which prints the generic message `System Config Failed` and exits with status 1 even when configuration itself succeeded.

## Configuration and Bootstrap

- `EnvValidationService` requires a project-root `.env` file.
- `EnvSetup` validates all 13 required values and accepts `DB_PORT` only from 1 through 65535.
- `SQLValidationService` builds and tests `jdbc:mysql://<host>:<port>/<database>`.
- `DBManager.initialize` stores the JDBC URL and credentials in static fields. Each repository operation opens its own connection.
- `HandleRecoveryKey` hashes `RECOVERY_KEY` with BCrypt cost 12.
- `SetRecoveryKey` attempts to insert or update `recovery_keys.id = 1` on every configuration run that reaches this stage.
- `CheckForDefaultAccounts` looks for role IDs 1 and 2 and creates missing local-admin and admin starter accounts.

Important limitations:

- `SetRecoveryKey` prints and swallows SQL failures, so its method cannot make configuration fail explicitly.
- Database configuration and the current session are global static state.
- There is no connection pool, dependency-injection container, migration tool, or multi-repository transaction boundary.
- Starter-account fallback checks still use fixed account IDs 1 and 2.
- `BOOTSTRAP_KEY` is stored directly in starter-account rows rather than hashed by the current code.

## Registration

Implemented behavior:

- Username, email address, international phone number, and password collection
- Username length validation from 6 through 19 characters
- Structural email validation from 6 through 253 characters
- International phone input beginning with `+`, digit-only syntax after the plus sign, and libphonenumber validation
- Full-data confirmation and correction of username, email, or phone number
- BCrypt password creation through `PasswordService`
- A guard in `RegistrationService` that rejects a null or blank collected password hash
- Account insertion with pending status, intern role, unassigned department, no menu access, and `is_system_account = false`

The previously documented correction-path hash loss has been fixed: corrected profile data returns to the confirmation prompt, and the confirmed path assigns the value returned by `PasswordFlow.policy` to `RegistrationService.hashedPWSD`.

Remaining limitations:

- Username and email uniqueness rely on database constraints.
- `CreateAccount` has no independent null or blank password-hash guard.
- `CreateAccount` returns no result and swallows SQL exceptions, so `RegistrationFlow` cannot report reliable persistence success.
- The validator accepts email addresses longer than the documented `accounts.email VARCHAR(100)` column.
- Complete registration, correction, duplicate, and repository behavior is not covered by automated tests.

## Password Handling

`PasswordService` requires at least 10 characters with an uppercase letter, lowercase letter, digit, and special character. User-created and recovery passwords use BCrypt cost 15. Starter-account passwords and the startup recovery-key hash use cost 12.

The previous array-clearing order defect has been fixed. The current sequence converts the validated original character array to a string, hashes it, then clears both character arrays in a `finally` block and releases the temporary string reference.

Current gaps:

- The 15 tests cover policy methods, retype comparison, terminal fallback, and array clearing, but not a complete console-input-to-BCrypt-verification path.
- An invalid retyped password aborts the current password flow instead of offering a dedicated retype loop.
- Three invalid policy attempts throw an exception, although the message claims that an account will be disabled; no account update occurs in `PasswordService`.
- Hidden login, new-password, retype, and recovery-key input requires `System.console()`. Run interactive flows from a real terminal.

## Login and Session Refactor

The current login path is split into focused types:

- `CollectLoginValues` collects the username and hidden password.
- `SetupCurrentSession` verifies account existence, password, and status.
- `CallPasswordPolicyRules` handles invalid-password thresholds.
- `HandleAccountStatus` handles account-status behavior.
- `StoreLogs` carries `accountName`, `canUseSystem`, and `reason` to the login-attempt repository.
- `SessionAccount` stores account ID, name, status ID, menu access, system-account flag, and role ID.
- `CurrentSession` stores the current record and provides `getCurrentAccount`, `setCurrentAccount`, `isLoggedIn`, and `clear`.

`LoginFlow` processes one credential submission and persists one login-attempt row. If no active menu-ready session was created, `AuthController` displays the authentication menu again.

| Account status | `LogsForDB.canUseSystem` | Current effect |
| --- | --- | --- |
| `active` | `true` | Loads account values and stores `SessionAccount` in `CurrentSession`. |
| `disabled` | `false` | Rejects the login. |
| `pending` | `true` | Runs department request setup but creates no session; authentication continues. |
| `locked` | `false` | Rejects the login. |
| `on_quarantine` | `false` | Rejects the login. |
| `waiting_for_password_change` | `true` | Attempts a password and activation update, creates no session, and requires another login. |
| `suspicious` | `false` | Warns and rejects the login without a session. |

The `canUseSystem` name does not match all outcomes. Pending and password-change paths are stored as successful login attempts and cause `LoginFlow` to log success even though they do not create an authenticated session. The password-change branch also returns success even if `UpdateUserPassword` reports failure.

`CurrentSession.clear()` now exists, but no implemented logout action calls it.

## Failed-Login Policy

This path currently has a blocking contract defect:

1. `CountFailedLoginAttempts` counts rows whose `failure_reason` is exactly `INVALID_PASSWORD` during the preceding 24 hours.
2. `CallPasswordPolicyRules` returns the text `to many false attempts`.
3. `LoginFlow` persists that returned text as the new failure reason.

Consequently, new invalid-password rows do not increase the count used by the policy. Historical rows with `INVALID_PASSWORD` can still trigger the checks, but the current attempt is evaluated before it is stored.

When a matching historical count exists, the ordered policy is:

| Previously stored matching count | Status update |
| --- | --- |
| 25 or more | `on_quarantine`, ID 5 |
| 6 through 24 | `suspicious`, ID 7 |
| 5 | `locked`, ID 4 |

There is no reset or archival policy after successful login, recovery, or administrator action. The in-memory retry counter in `CallPasswordPolicyRules` is recreated for each failed login and does not track retries across authentication-menu iterations.

All login outcomes are inserted into `login_attempts`. Unknown usernames use a null `account_id`. Successful active rows currently also receive the non-null reason `account status is active`.

## Starter Accounts

Missing starter accounts are detected by role:

- Role 1: local admin
- Role 2: admin

Both are created as system accounts with status ID 6 (`waiting_for_password_change`), no menu access, `requires_password_change = true`, recovery key ID 1, and BCrypt cost-12 password hashes.

On first login, `UpdateUserPassword` changes the hash and then runs a second statement that sets status ID 1, clears `requires_password_change`, and enables menu access. These statements are not one transaction. The user must authenticate again before a session is created.

## Recovery

- `RECOVERY_KEY` is rehashed with a new salt on every configuration run that reaches this stage, and its row is then upserted when persistence succeeds.
- Recovery input is hidden through `System.console()`.
- BCrypt verifies the entered key against `recovery_keys.id = 1`.
- Four invalid key checks end the recovery flow.
- `FindRecoverableUser` displays only accounts where `is_system_account = true`.
- `SelectUserForRecover` then accepts any existing account name, so the final target is not limited to that displayed system-account list.
- `UpdateSystemAccountPassword` changes only `password_hash`; it does not change status, password-change flags, menu access, or session state.

Missing or blank recovery hashes are not validated before BCrypt verification, and recovery repository methods do not return structured success results.

## Pending Access Requests

Pending users can select department ID 1 through 11. A department-specific job menu is displayed, but no job choice is collected. The System job menu is shown only when the account is already assigned to department 11 or 5; newly registered pending users start in department 12.

`CreateAccessRequest` stores:

- The requesting account ID
- The selected department ID
- Job `unassigned`
- Role ID 9 (`intern`)
- The database default request status, ID 3

Job and role selection, duplicate prevention, approval, rejection, account activation, and decision auditing are not implemented.

## Menu and Service Routing

The July 23 refactor introduced typed routing:

- `AdminMenu` defines five immutable `MenuOption` values.
- Each option maps to a `ServiceAction`.
- `MenuControllerParent` returns `MenuContextStructure(userRole, action)`.
- `FrontController` forwards that context to `ServiceController`.
- `ADMIN_USER_REQUESTS` invokes `ShowCurrentRequests`.

Current action behavior:

| Displayed route | State |
| --- | --- |
| Admin: Requests | Connected; prints every joined request row. |
| Admin: User | Declared action; unsupported by `ServiceController`. |
| Admin: Security | Declared action; unsupported by `ServiceController`. |
| Admin: Logs | Declared action; unsupported by `ServiceController`. |
| Admin: Logout | Declared action; unsupported; does not clear the session. |
| Local admin dashboard | Returned by `MenuControllerParent`; unsupported by `ServiceController`. |

Unsupported actions throw `IllegalStateException`, which terminates the application through the generic bootstrap error handler. Roles other than 1 and 2 are rejected by `MenuControllerParent`.

`ShowCurrentRequests` prints directly from the repository and does not filter by request status. `ServiceController` does not independently authorize the selected action against the current session. `RequestMenu`, `RouteService`, and `UIController` remain empty placeholders. The obsolete `MenuController`, `SubMenuController`, and `MenuValues` types have been removed.

## Logging

`LogManager` exposes typed methods backed by ten program-state enums. Its active SLF4J logger names are `AUTH`, `CONFIG`, `SECURITY`, `SYSTEM`, `SQL`, `CREDENTIALS`, `BOOT`, and `MENU`.

`logback.xml` routes those categories to the console and separate files under `logs/`. It also configures `ACCESS` and `DATABASE`, but `LogManager` has no matching logger fields. `LogManager.recovery` currently writes through the `SYSTEM` logger.

Remaining limitations:

- File appenders have no rotation, size limit, or retention policy.
- Diagnostic and user-facing console output remain mixed.
- Many classes still print diagnostic messages directly.
- Several log messages contain usernames or detailed account state.

## Automated Test Coverage

Covered:

- Password length and character-class rules
- Password retype equality and mismatch
- Missing-console behavior for direct password collection
- Clearing both, either, or null password arrays
- Username, email, and phone validators
- Registration confirmation and correction-input validators
- Registration-service null and blank password-hash guard

Not covered:

- Complete password creation and BCrypt verification
- Complete registration and correction flows
- Login verification, status outcomes, and failed-attempt thresholds
- Session creation, clearing, and logout
- Recovery retries and target restrictions
- Menu option mapping and service dispatch
- JDBC repositories and schema integration
- Logging configuration behavior

## Not Implemented

- Complete role/action authorization
- Access-request approval, rejection, and account activation
- Working admin actions beyond request listing
- Working local-admin services and logout
- Patient records, appointments, treatment, billing, and reporting
- JavaFX or REST interfaces
- CI, formatting, linting, static analysis, coverage reporting, Docker, and deployment automation

## Commands

Run from a terminal after completing the environment and database setup:

```powershell
.\mvnw.cmd test
.\mvnw.cmd exec:java
```

Global Maven equivalents are `mvn test` and `mvn exec:java`. Follow `../setup/ENV_SETUP.md` and `../setup/DB_SETUP.md` before starting the interactive application.
