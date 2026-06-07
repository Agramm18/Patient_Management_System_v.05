# Current Project Status

Last synchronized: 2026-06-07.

Patient Management System V5.01 is currently a Java 21 console application focused on configuration, authentication, account security, recovery, and access-request foundations.

The project contains 67 Java source files. The active runtime is:

```text
Main
-> BootConfigService
-> FrontController
-> ConfigController
-> AuthController
```

Only `CONFIG` and `AUTH` are routed by `FrontController`. `MENU`, `SERVICE`, `UI`, and `EXIT` are reserved request types.

## Build State

- `mvn -DskipTests compile` succeeds as of 2026-06-07.
- No `src/test` directory or automated test suite exists.
- No `src/main/resources` directory or explicit Logback configuration exists.
- The application must be run from a terminal for hidden password and recovery-key input.

## Implemented Runtime

### Bootstrap and Configuration

- `Main` creates the shared `Scanner` and starts `BootConfigService`.
- `BootConfigService` creates all controllers and routes through configuration before authentication.
- Invalid environment values, failed database validation, or invalid `DBManager` initialization stop startup.
- `EnvValidationService` validates the `.env` file and required values.
- `SQLValidationService` builds and tests the MySQL JDBC connection.
- `DBManager` stores the runtime connection values and opens repository connections.
- `HandleRecoveryKey` hashes `RECOVERY_KEY`.
- `SetRecoveryKey` inserts or updates `recovery_keys.id = 1`.
- `CheckForDefaultAccounts` creates missing role-1 and role-2 starter accounts.

### Authentication Menu

`AuthController` continuously offers:

1. Registration
2. Login
3. Recover System Accounts
4. Exit

After registration, login, or recovery returns, the authentication menu is shown again.

### Registration

- Collects username, email address, and phone number.
- Validates username length, basic email format, and basic phone format.
- Shows the collected data and allows one field to be changed.
- Uses `PasswordService` to validate and hash the password with BCrypt.
- Creates an account with pending status, intern role, unassigned department, no menu access, and `is_system_account = false`.
- Registration-related output is being migrated from direct console messages to `LogManager`.

### Login

- Collects the username through `Scanner`.
- Collects the password through terminal-backed `System.console()`.
- Verifies usernames and BCrypt password hashes through `CheckUserInDB`.
- Loads the account status from `account_status`.
- Writes every completed login result to `login_attempts`.
- Repeats login attempts until `LoginVerification` returns success.

Current account-status behavior:

| Status | Current behavior |
| --- | --- |
| `active` | Login returns success. No main menu is routed yet. |
| `disabled` | Login fails and repeats. |
| `pending` | Starts the first-login access-request flow and records the result as successful with a reason. |
| `locked` | Login fails and repeats. |
| `on_quarantine` | Login fails and repeats. |
| `waiting_for_password_change` | Requires a new password, then activates the account and enables menu access. |
| `suspicious` | Login returns success with a warning reason. |

### Failed-Login Policy

`CountFailedLoginAttempts` counts `INVALID_PASSWORD` records for the account from the previous 24 hours. `ExecutePWSDPolicy` now persists status changes:

| Stored failed-password count | Status update |
| --- | --- |
| `>= 5` | `locked`, status ID `4` |
| `>= 6` | `suspicious`, status ID `7` |
| `>= 25` | `on_quarantine`, status ID `5` |

The current login attempt is written after policy evaluation, so the database count does not include the attempt currently being processed. This makes threshold changes occur one attempt later than the displayed threshold suggests.

### Starter Accounts

Missing starter accounts are detected by role:

- Role `1`: local admin
- Role `2`: admin

Created starter accounts:

- Are marked as system accounts
- Use BCrypt password hashes
- Reference recovery key ID `1`
- Start with `waiting_for_password_change`
- Require a password change
- Do not initially have menu access

After a successful first password change, `UpdateUserPassword` sets the account to active, clears `requires_password_change`, and enables menu access.

### Recovery

- Requires the recovery key through `System.console()`.
- Loads the stored hash from `recovery_keys.id = 1`.
- Verifies the entered key with BCrypt.
- Allows up to four invalid recovery-key attempts.
- Lists accounts where `is_system_account = true`.
- Accepts an account name and updates its password hash.

The displayed list is limited to system accounts, but the final account lookup currently accepts any existing account name. Recovery updates only `password_hash`.

### Pending-User Access Request

- Pending users select a department from IDs `1` through `11`.
- A matching department job menu is displayed.
- Access to the System department menu is guarded by the account's currently assigned department.
- `CreateAccessRequest` stores the account ID and selected department.
- Requested job remains `unassigned`.
- Requested role remains role `9` (`intern`).

Job selection, role selection, approval, rejection, and activation are not connected.

### Logging

The project now includes Logback and a custom `LogManager` facade with category loggers such as `BOOT`, `CONFIG`, `AUTH`, `SECURITY`, `SQL`, `DATABASE`, and `CREDENTIALS`.

Logging migration is partial:

- Boot, configuration, recovery, and parts of registration use `LogManager`.
- Many login, repository, menu, and password messages still use `System.out.println`.
- No `logback.xml` exists, so Logback uses its default configuration.
- Some declared `LogType` values are not handled by the current `LogManager.log` switch and therefore produce no output.

## Current Limitations

- No connected main menu exists after active login.
- Patient-management product features are not implemented.
- Registration correction can continue without reconfirmation or password creation.
- Registration does not perform explicit username or email uniqueness checks before insert.
- Admin starter-account creation reads a different password key than environment validation.
- Failed-login threshold counting excludes the current attempt.
- Recovery selection is not strictly limited to system accounts.
- Access requests store default job and role values.
- No admin approval or rejection workflow exists.
- Logging migration is incomplete.
- Repository error handling and return values are inconsistent.
- No automated tests, CI pipeline, Maven Wrapper, formatter, Docker setup, or JavaFX UI exists.

## Run Commands

```bash
mvn -DskipTests compile
mvn exec:java
```

See `../setup/ENV_SETUP.md` and `../setup/DB_SETUP.md` before running the application.
