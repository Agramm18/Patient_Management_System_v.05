# Current Project Status

Last synchronized: 2026-07-18.

Patient Management System V5.01 is a Java 21 console application focused on configuration, authentication, account security, recovery, session state, pending access requests, logging, and early menu routing.

## Verified Build State

| Item | Current state |
| --- | --- |
| Production sources | 89 Java files under `src/main/java` |
| Test sources | 2 Java files under `src/test/java` |
| Automated tests | 53 passing, 0 failed, 0 errored, 0 skipped |
| Test classes | `PasswordServiceTest` and `RegistrationServiceTest` |
| Application resources | `src/main/resources/logback.xml` |
| Java target | Java 21 through Maven `source` and `target` properties |
| Maven Wrapper | Wrapper 3.3.4 configured for Maven 3.9.16 |

`mvn test` was verified successfully on 2026-07-18. The Windows wrapper script did not start in the current PowerShell environment because its generated script attempted to index a null filesystem-link target; the test was therefore run with the Maven 3.9.16 distribution already installed by the wrapper.

## Active Runtime

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> ConfigController
-> FrontController(AUTH)
-> AuthController authentication loop
-> CurrentSession after an active login
-> FrontController(MENU)
-> MenuController
-> FrontController(SERVICE)
-> ServiceController
```

`FrontController` handles `CONFIG`, `AUTH`, `MENU`, and `SERVICE`. `UI` and `EXIT` are declared request types without active switch cases. A `SubMenuController` instance is injected into `FrontController`, but there is no `SUB_MENU` request type or dispatch path yet.

## Configuration and Bootstrap

- `EnvValidationService` checks that `.env` exists in the project root.
- It constructs an `EnvSetup` record from all 13 required environment values.
- `EnvSetup` rejects missing or blank values and requires `DB_PORT` to be between 1 and 65535.
- `SQLValidationService` builds and tests `jdbc:mysql://<host>:<port>/<database>`.
- `DBManager.initialize` stores the JDBC URL and credentials in static runtime fields.
- `HandleRecoveryKey` hashes `RECOVERY_KEY` with BCrypt cost 12.
- `SetRecoveryKey` upserts the hash into `recovery_keys.id = 1` on every successful startup.
- `CheckForDefaultAccounts` looks for role IDs 1 and 2 and creates missing starter accounts.
- Failed configuration or an invalid runtime session ends the process through `System.exit(1)`.

## Authentication Menu

`AuthController` repeatedly offers registration, login, system-account recovery, and exit. It returns to `BootConfigService` only when an active user exists in `CurrentSession`, has menu access, and has account status ID 1. Registration, recovery, pending-login setup, suspicious-login handling, and first password changes return to the authentication menu.

## Registration

Implemented behavior:

- Username, email address, phone number, and password collection
- Username length validation for 6 to 19 characters
- Structural email checks with a maximum accepted length of 253 characters
- International phone input beginning with `+`, digit-only validation, and libphonenumber validation
- Confirmation and single-field correction prompts
- BCrypt password generation through `PasswordService`
- Account insertion with pending status, intern role, unassigned department, no menu access, and `is_system_account = false`

Known registration defects:

- The correction branch calls `PasswordFlow.policy` but does not assign the returned hash to `RegistrationService.hashedPWSD`.
- `CreateAccount` does not reject a null or blank password hash before attempting the insert.
- Username and email uniqueness rely only on database constraints.
- The correction flow does not return to one consistent full-data confirmation step.
- Repository failures are printed and swallowed instead of being returned to the flow.

## Password Handling

`PasswordService` requires at least 10 characters with uppercase, lowercase, numeric, and special characters. Password entry and re-entry use `System.console()`, and BCrypt generation uses cost 15 for user-created passwords. Starter-account passwords and the startup recovery-key hash use cost 12.

The current end-to-end password creation path has a critical defect: `validateRetypedPassword` clears the original password array before `convertCHARtoString` creates the value that is hashed. The unit tests validate password rules and array comparison independently, so the 53-test suite does not detect this full-flow problem.

A terminal-backed console is required for login passwords, new passwords, and recovery-key input. IDE runs without `System.console()` cannot complete these flows reliably.

## Login, Status, and Session Behavior

| Account status | Current behavior |
| --- | --- |
| `active` | Loads account values, creates `CurrentUser`, stores it in `CurrentSession`, and reports success. |
| `disabled` | Rejects login and continues the login loop. |
| `pending` | Runs department selection and inserts an access request, then returns to the authentication menu without creating a session. |
| `locked` | Rejects login and continues the login loop. |
| `on_quarantine` | Rejects login and continues the login loop. |
| `waiting_for_password_change` | Runs password creation, updates the password and activation fields, then requires a new login. |
| `suspicious` | Reports a successful login result with a warning reason but creates no session, so the authentication menu continues. |

`CurrentUser` stores the username, account ID, account-status ID, menu-access flag, system-account flag, and role ID. `CurrentSession` stores one static current user. There is no logout or explicit session reset.

Every completed login result is inserted into `login_attempts`, including unknown usernames with a null account ID.

## Failed-Login Policy

`CountFailedLoginAttempts` counts stored `INVALID_PASSWORD` rows from the previous 24 hours. `LoginVerification` then applies these status changes:

| Previously stored count | Status update |
| --- | --- |
| 5 or more | `locked`, ID 4 |
| 6 or more | `suspicious`, ID 7 |
| 25 or more | `on_quarantine`, ID 5 |

The current failed attempt is persisted after policy evaluation, so each threshold is reached one attempt later than the displayed count implies. There is no reset policy after a successful login or administrator action.

## Starter Accounts

Missing starter accounts are detected by role ID:

- Role 1: local admin
- Role 2: admin

Created starter accounts are system accounts, use BCrypt hashes, reference recovery key ID 1, require a password change, start in status 6, and have no menu access. A successful first password update sets status 1, clears `requires_password_change`, and enables menu access.

The fallback password-hash checks use fixed account IDs 1 and 2 rather than the created account or role, so they do not reliably validate databases with different ID histories.

## Recovery

- `RECOVERY_KEY` is rehashed and upserted on every successful startup.
- Recovery input is hidden through `System.console()`.
- BCrypt verifies the entered key against `recovery_keys.id = 1`.
- Four invalid key attempts end the recovery flow.
- The displayed account list is filtered to `is_system_account = true`.
- The final lookup accepts any existing account name and is therefore not restricted to the displayed system accounts.
- Recovery updates only `password_hash`; it does not change status, password-change flags, menu access, or session state.

Missing recovery rows and null hashes are not handled before BCrypt verification.

## Pending Access Requests

Pending users choose a department ID from 1 through 11. A department-specific job menu is displayed, but no job choice is collected. System department jobs are displayed only when the account is currently assigned to department 11 or 5.

`CreateAccessRequest` stores:

- The requesting account ID
- The selected department ID
- Job `unassigned`
- Role ID 9 (`intern`)
- The database default request status, ID 3

Job selection, role selection, duplicate-request handling, approval, rejection, account activation, and authorization checks are not connected.

## Menu and Service Routing

- Role 1 displays `LocalAdminMenu`, which currently has no selectable options.
- Role 2 displays an admin menu with five options: Requests, User, Security, Logs, and Logout.
- `MenuFlow` validates the selected admin parent option.
- `MenuValues` carries `parentKonext`, `userRole`, and `childKontext`; the child context is currently always 0.
- `SubMenuController` and `RequestMenu` are empty placeholders.
- `ServiceController` dispatches by role, but both private handlers only write a service-start log entry.
- `ShowCurrentRequests` contains a JDBC query for access requests, but no current controller or service invokes it.
- Roles other than 1 and 2 fail in `MenuController` with an unknown-role exception.

## Logging

`src/main/resources/logback.xml` defines a console appender and individual file appenders under `logs/` for `AUTH`, `CONFIG`, `SECURITY`, `ACCESS`, `DATABASE`, `SYSTEM`, `SQL`, `CREDENTIALS`, `BOOT`, and `MENU`.

`LogManager` now exposes typed methods using ten program-state enums. Its active logger fields are `AUTH`, `CONFIG`, `SECURITY`, `SYSTEM`, `SQL`, `CREDENTIALS`, `BOOT`, and `MENU`.

Current limitations:

- `ACCESS` and `DATABASE` appenders exist but have no corresponding active `LogManager` logger fields.
- Recovery events are written to the `SYSTEM` logger.
- Many diagnostic messages still use `System.out.println`.
- User-facing output and diagnostic output are not consistently separated.
- File appenders do not rotate or retain logs by policy.
- Several logs include usernames and detailed account state.

## Test Coverage

`PasswordServiceTest` covers password policy validation, retype validation, and the no-console fallback. `RegistrationServiceTest` covers username, email, phone-number, registration-state, correction-choice, and confirmation-choice validation.

Not covered:

- Complete password creation and hash verification
- Complete registration and correction flows
- Database repositories
- Login status routing and threshold behavior
- Recovery limits and target restrictions
- Session creation and reset
- Menu, submenu, and service routing
- Logging configuration behavior

## Not Implemented

- Complete role-based authorization
- Access-request review, approval, rejection, and account activation
- Working admin and local-admin service actions
- Logout and session clearing
- Patient records, appointments, treatment, billing, and reporting
- JavaFX or REST interfaces
- CI, formatter, linter, static analysis, Docker, and deployment automation

## Commands

```powershell
.\mvnw.cmd test
.\mvnw.cmd exec:java
```

If Maven is installed globally, the equivalent commands are `mvn test` and `mvn exec:java`. Complete `../setup/ENV_SETUP.md` and `../setup/DB_SETUP.md` before starting the application.
