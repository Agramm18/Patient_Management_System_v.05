# Current Project Status

Last synchronized: 2026-07-31.

Patient Management System V5.01 is a Java 21 Maven console application. Its current scope is application bootstrap, configuration, authentication, account recovery, session state, failed-login policies, pending access requests, logging, and the first menu-to-service route. It is a learning and portfolio project, not a production-ready hospital system.

## Verified Snapshot

| Item | Verified state |
| --- | --- |
| Production sources | 98 Java files under `src/main/java` |
| Test sources | 2 Java files under `src/test/java` |
| Automated tests | 55 passed, 0 failed, 0 errored, 0 skipped |
| Test split | 15 `PasswordServiceTest` and 40 `RegistrationServiceTest` tests |
| Resource | `src/main/resources/logback.xml` |
| Java target | Java 21 through Maven `source` and `target` properties |
| Maven Wrapper | Wrapper 3.3.4 configured for Maven 3.9.16 |

`.\mvnw.cmd test` completed successfully in Windows PowerShell on 2026-07-31. The suite does not connect to MySQL and does not exercise complete console workflows.

## Connected Runtime

```text
Main
-> BootConfigService
-> FrontController(CONFIG)
-> ConfigController
-> FrontController(AUTH)
-> AuthController
-> LoginFlow when login is selected
-> SetupCurrentSession
-> SessionAccount stored in CurrentSession for an active account
-> FrontController(MENU)
-> MenuControllerParent
-> MenuContextStructure(userRole, ServiceAction)
-> FrontController(SERVICE)
-> ServiceController
```

`FrontController` handles `CONFIG`, `AUTH`, `MENU`, and `SERVICE`. `UI` and `EXIT` are declared but not dispatched, and `UIController` is empty. After authentication, the application performs one menu dispatch and one service dispatch. A successful request-listing action then reaches the end of `main`.

`BootConfigService` catches every runtime exception from configuration, authentication, menu, and service work, prints `System Config Failed`, and exits with status 1. The message is therefore not limited to configuration failures.

## Configuration and Bootstrap

- `EnvValidationService` requires a project-root `.env` file.
- `EnvSetup` validates all 13 required values and a database port from 1 through 65535.
- `SQLValidationService` tests `jdbc:mysql://<host>:<port>/<database>`.
- `DBManager.initialize` stores the JDBC URL and credentials in static fields.
- Each repository operation opens its own JDBC connection.
- `HandleRecoveryKey` hashes `RECOVERY_KEY` with BCrypt cost 12.
- `SetRecoveryKey` upserts `recovery_keys.id = 1` on each successful startup path.
- Missing role-ID 1 and role-ID 2 starter accounts are created automatically.

Important limitations:

- Recovery-key persistence failures are swallowed and cannot fail configuration explicitly.
- Starter-account fallback checks use fixed account IDs 1 and 2.
- `BOOTSTRAP_KEY` is stored unchanged in starter-account rows.
- Database configuration and session state are global static state.
- There is no connection pool, dependency injection, migration tool, or transaction spanning repositories.

## Registration and Password Creation

Registration collects and validates a username, email address, international phone number, and password. Usernames must contain 6 through 19 characters. Emails may contain 6 through 253 characters and pass custom structural checks. Phone numbers must use an international `+` prefix and pass libphonenumber validation.

The confirmation flow can correct username, email, or phone before asking for full confirmation again. The confirmed password hash is stored by `RegistrationService`, which rejects a null or blank collected hash. `CreateAccount` inserts a pending, non-system account with role ID 9, department ID 12, and no menu access.

Password creation requires at least 10 characters with uppercase, lowercase, digit, and special-character content. User-created and recovery passwords use BCrypt cost 15; starter passwords and the startup recovery key use cost 12. The validated password is converted and hashed before both character arrays are cleared.

Remaining gaps:

- Username and email uniqueness rely on database constraints.
- Java accepts longer email values than the documented `VARCHAR(100)` column.
- `CreateAccount` has no independent blank-hash guard and returns no result.
- A password retype mismatch aborts the current password service call.
- A null retyped array can be dereferenced.
- Three invalid password-creation attempts throw an account-disabled message without changing an account.
- Complete password and registration flows are not tested end to end.

## Login Outcomes and Session State

`StoreLogs` carries `accountName`, `LoginOutcome`, and a free-form `reason`. `LoginFlow` converts only `LoginOutcome.PERMITTED` to `login_attempts.is_success = true`.

| Outcome or status | Current effect |
| --- | --- |
| Unknown username | Persists `USERNAME_NOT_FOUND` as a failed attempt and asks for credentials again. |
| Invalid password | Runs `PasswordPolicies`, persists `INVALID_PASSWORD`, and asks again. |
| `active` | Loads a `SessionAccount`, stores it in `CurrentSession`, and returns `PERMITTED`. |
| `disabled`, `locked`, `on_quarantine`, `suspicious` | Returns `REJECTED` and continues the login loop. |
| `pending` | Runs `FirstLoginFlow`, returns `PENDING_REQUEST`, and returns to the authentication menu without a session. |
| `waiting_for_password_change` | A successful update returns `PASSWORD_CHANGED` to authentication without a session; a failed update returns `WAITING_FOR_PASSWORD_CHANGE` and repeats the credential loop. |
| SQL or input error inside setup | Returns a typed failure and continues the login loop. |

Current session limitations:

- The active branch does not require `hasAccessToMenu()` before setting the session or returning `PERMITTED`.
- `AuthController` requires a non-null session with menu access and status ID 1 before leaving authentication, so an active account without menu access leaves a stale session while authentication continues.
- `CurrentSession.clear()` exists but has no production caller.
- Login has no explicit cancel option after entering the login flow.
- A missing terminal console during credential collection occurs before `SetupCurrentSession` and reaches the generic fatal handler.

## Failed-Login Policy

The former reason mismatch is fixed. Both persistence and counting use `INVALID_PASSWORD`. `PolicieThresholdStructure.includingAttempt()` adds the current failure to every window before thresholds are checked; the attempt is persisted afterward by `LoginFlow`.

`CountFailedLoginAttempts` performs six separate queries:

| Window | Current SQL semantics | Factor |
| --- | --- | --- |
| Day | Current calendar day from `CURDATE()` | 1 |
| Week | From `CURDATE() - INTERVAL 7 DAY` | 2 |
| Month | Current calendar month | 4 |
| Year | Rolling 365 days from `NOW()` | 8 |
| Five years | From `CURDATE() - INTERVAL 5 YEAR` | 25 |
| Ten years | From `CURDATE() - INTERVAL 10 YEAR` | 50 |

Thresholds are the base value multiplied by the window factor:

| Transition | Day | Week | Month | Year | Five years | Ten years |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Locked, status 4 | 5 | 10 | 20 | 40 | 125 | 250 |
| Suspicious, status 7 | 6 | 12 | 24 | 48 | 150 | 300 |
| Quarantine, status 5 | 25 | 50 | 100 | 200 | 625 | 1250 |

`PasswordPolicies` checks quarantine, then suspicious, then locked. Any one window reaching its scaled threshold triggers the transition.

Remaining policy risks:

- Counting, status update, and attempt insertion are not one transaction.
- A status can be changed for an attempt that later fails to persist.
- Query failures return zero instead of an explicit error.
- Calendar-day and rolling-window semantics are inconsistent.
- The instance retry counter is recreated for every failed password and therefore displays 1 each time.
- No tests cover the queries, factors, boundaries, or transitions.
- No reset or archival policy exists after success, recovery, or administrator action.

## Starter Accounts and Recovery

Missing local-admin and admin accounts are detected by role IDs 1 and 2. They are created as system accounts with status ID 6, no menu access, `requires_password_change = true`, recovery key ID 1, and BCrypt cost-12 hashes.

On the first valid login, `UpdateUserPassword` updates the hash and then performs a second update that sets status ID 1, clears `requires_password_change`, and enables menu access. These operations use separate connections. The method can report success even if the second update fails. The account must log in again afterward.

Recovery uses four key attempts and displays only system accounts. The final lookup still accepts any existing account name. `UpdateSystemAccountPassword` now updates both `password_hash` and account status ID 1, but returns no result and does not clear `requires_password_change`, enable menu access, or clear/update the session. Missing or malformed recovery hashes are not checked before BCrypt verification.

## Pending Access Requests

Pending users can request department IDs 1 through 11. A department-specific job menu is displayed, but no job is collected. `CreateAccessRequest` stores the account ID, selected department, job `unassigned`, role ID 9, and the schema's default request status.

Job and role selection, duplicate prevention, structured repository results, approval, rejection, atomic activation, and decision auditing are not implemented.

## Menu and Service Routing

`AdminMenu` exposes five immutable options mapped to `ServiceAction`. `MenuControllerParent` creates `MenuContextStructure(userRole, action)`. Only `ADMIN_USER_REQUESTS` is implemented in `ServiceController`; it calls `ShowCurrentRequests` and prints all joined request rows without status filtering.

| Route | State |
| --- | --- |
| Admin: Requests | Connected to `ShowCurrentRequests`. |
| Admin: User | Declared, unsupported. |
| Admin: Security | Declared, unsupported. |
| Admin: Logs | Declared, unsupported. |
| Admin: Logout | Declared, unsupported; session remains set. |
| Local admin dashboard | Returned by menu controller, unsupported. |

Unsupported actions throw `IllegalStateException` and terminate through the generic bootstrap handler. Roles other than 1 and 2 are rejected. `ServiceController` trusts the supplied context and does not independently verify its role/action combination against the current session.

## Logging and Tests

`LogManager` has eight active logger fields: `AUTH`, `CONFIG`, `SECURITY`, `SYSTEM`, `SQL`, `CREDENTIALS`, `BOOT`, and `MENU`. Logback also configures unused `ACCESS` and `DATABASE` categories. Ten non-rolling files are written under `logs/`; recovery events use `SYSTEM`.

Logging remains mixed with direct console diagnostics. There is no rotation or retention policy, and several messages contain account names or detailed state.

Automated tests cover password rules, retype comparison, array clearing, missing-console behavior at the direct input-method level, registration validators, confirmation/correction helpers, and the registration hash guard. They do not cover complete interactive flows, JDBC repositories, login outcomes, policy windows, session behavior, recovery, menu routing, or Logback configuration.

## Not Implemented

- Complete role/action authorization and access-request decisions
- Working admin actions beyond request listing
- Working local-admin services, logout, and a repeated menu loop
- Patient records, appointments, treatment, billing, and reporting
- JavaFX or REST interfaces
- CI, formatting, linting, static analysis, coverage reporting, Docker, or deployment automation

## Commands

Run from a terminal after completing environment and database setup:

```powershell
.\mvnw.cmd test
.\mvnw.cmd exec:java
```

Global Maven equivalents are `mvn test` and `mvn exec:java`.
