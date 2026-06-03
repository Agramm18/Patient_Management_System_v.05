# Current Project Status

Last synchronized: 2026-06-03.

Patient Management System V5.01 is currently a Java 21 console application. The active implementation focuses on application bootstrapping, local configuration validation, MySQL connectivity, account registration, login, starter account creation, password handling, login attempt logging, recovery-key based system-account password reset, and the first groundwork for pending-user access requests.

The project is not a complete patient management system yet. Patient records, appointments, billing, treatment workflows, reporting, full administration workflows, a graphical UI, REST APIs, and deployment tooling are still outside the implemented runtime.

## Active Scope

The currently active runtime is:

```text
Main -> BootConfigService -> FrontController -> ConfigController -> AuthController
```

Only the `CONFIG` and `AUTH` controller routes are active. `MENU`, `SERVICE`, `UI`, and `EXIT` exist in the controller enum but are not implemented as routed application phases yet.

## Implemented

The current application includes:

- Maven project setup with Java 21.
- Console startup through `app.Main`.
- Boot orchestration through `BootConfigService`.
- Controller dispatch through `FrontController`.
- Configuration startup through `ConfigController`.
- Local environment validation through `EnvValidationService`, including `RECOVERY_KEY` validation.
- MySQL JDBC URL creation and connection checks through `SQLValidationService`.
- Runtime database connection access through `DBManager`.
- Startup fail-fast behavior when environment validation, database connection validation, or DB runtime initialization fails.
- Recovery-key hashing through `HandleRecoveryKey` and startup persistence through `SetRecoveryKey`.
- Starter account checks through `CheckForDefaultAccounts`.
- Automatic creation of missing `local_admin` and `admin` starter accounts through `CreateDefaultAccounts`.
- BCrypt password hashing for starter accounts, registered users, login verification, and recovery-key verification.
- Registration through `RegistrationFlow`, `RegistrationService`, `PasswordFlow`, and `CreateAccount`.
- Login input collection through `LoginInputCollector`.
- Username, password, and account status checks through `LoginVerification` and `CheckUserInDB`.
- Account status handling for `active`, `disabled`, `pending`, `locked`, `on_quarantine`, and `waiting_for_password_change`.
- First password change flow for starter accounts in `waiting_for_password_change`.
- Password update and activation for starter accounts through `UpdateUserPWSD`.
- System-account recovery menu route through `AuthController` and `RecoveryFlow`.
- Recovery-key input through `RecoveryCheck`, stored-key loading through `CollectRecoveryKey`, key validation through `CheckKeyStatus`, target account lookup through `SelectUserForRecovery` and `SelectUserForRecover`, and password update through `UpdateSystemAccount`.
- Login attempt persistence through `app.Repository.logsRepository.CollectLogs`.
- Failed password counting over recent login attempts through `CountFailedLoginAttempts`.
- Placeholder password policy status routing through `ExecutePWSDPolicy`.
- First-login access request groundwork for pending users.
- Department selection through `DepartmentMenu` and `SelectDepartment`.
- Department-specific job menu placeholders.
- Access request storage through `HandleAccessManagement`.

## Current Runtime Flow

1. `app.Main` creates a shared `Scanner`, prints the startup message, displays the loader, and starts `BootConfigService`.
2. `BootConfigService` creates `AuthController`, `ConfigController`, `MenuController`, `ServiceController`, and `uiController`.
3. `BootConfigService` creates a `FrontController` and routes first to `CONFIG`.
4. `ConfigController` validates local configuration, builds the database connection values, initializes `DBManager`, hashes `RECOVERY_KEY`, stores it in `recovery_keys`, and checks starter accounts.
5. Missing starter accounts are created automatically and receive `waiting_for_password_change`.
6. If configuration returns success, the application routes to `AUTH`.
7. `AuthController` shows the authentication menu with registration, login, recovery, and exit.
8. Registration collects username, email, phone number, and password, then creates a pending account.
9. Login checks whether the username exists, verifies the password with BCrypt, loads the account status, and writes a login attempt log.
10. Pending users enter the first-login access request flow and select a department.
11. Starter accounts with `waiting_for_password_change` must create a new password before their status is changed to `active`.
12. Recovery validates the recovery key, asks for the account to recover, and updates the selected account password hash.

## Current Recovery State

The recovery baseline is considered done for the current stage.

Current behavior:

- `.env` must provide `RECOVERY_KEY`.
- During startup, `HandleRecoveryKey` hashes the plain recovery key with BCrypt.
- `SetRecoveryKey` stores the hash in `recovery_keys` with `id = 1`, updating the same row on later startup runs.
- The auth menu exposes `Recover System Accounts`.
- `RecoveryCheck` reads the recovery key through terminal-only password input.
- `CollectRecoveryKey` loads the stored hash from the database.
- `CheckKeyStatus` verifies the entered key with BCrypt.
- `SelectUserForRecovery` asks which account should be recovered.
- `SelectUserForRecover` checks whether the account exists.
- `PasswordService` creates a new password hash.
- `UpdateSystemAccount` writes the new password hash to `accounts.password_hash`.

Remaining hardening for recovery belongs to security and cleanup work, not to the baseline completion item. Examples: enforcing that a failed recovery-key check stops the flow immediately, deciding whether recovery should be limited to system accounts, and deciding whether recovery should also reset account status fields.

## Current Access Request State

Pending users can create an access request during first login. The current flow stores the requesting account and selected department. Requested job and requested role still use default values:

- `requested_job`: `unassigned`
- `requested_role`: `9` (`intern`)

Approval, rejection, activation after approval, and admin-side request management are not implemented yet.

## Current Security State

The system uses BCrypt for password hashing and password verification. Password entry for login, password creation, and recovery requires a terminal-backed `System.console()` and fails fast when that console is unavailable.

Login attempts are written to the database. Failed password attempts can be counted over a 24-hour window, but the status transition methods for locking, quarantine, and suspicious activity are currently placeholders and do not update account state yet.

## Documentation Boundaries

Environment configuration details are maintained only in `docs/setup/ENV_SETUP.md`.

Database setup, schema details, seed data, required IDs, and verification queries are maintained only in `docs/setup/DB_SETUP.md`.

This status file intentionally describes runtime behavior without duplicating setup instructions.

## Current Limitations

- There is no connected main menu after a successful active login.
- Patient management is not implemented.
- Appointment, billing, treatment, reporting, and hospital administration workflows are not implemented.
- Job selection is not implemented beyond displaying placeholder department job menus.
- Role selection exists as a menu and validation class, but it is not connected to the active access request flow.
- Access requests are stored, but there is no approval, rejection, or activation workflow.
- `ExecutePWSDPolicy` contains empty status transition methods, so failed password policy thresholds do not yet change account status.
- `SetNewStatus`, `MenuController`, `ServiceController`, `uiController`, `AccountPolicy`, and `SelectJob` are placeholders.
- The registration correction path needs review to ensure changed data is reconfirmed and password creation still runs.
- Repository operations still mostly print errors instead of returning consistent success/failure results.
- There are no automated tests yet.
- The application is still console-based; JavaFX, REST API support, Docker, CI, and production logging are planned for later stages.

## Run Commands

Build the project:

```bash
mvn clean package
```

Compile without packaging:

```bash
mvn -DskipTests compile
```

Start the console application:

```bash
mvn exec:java
```
