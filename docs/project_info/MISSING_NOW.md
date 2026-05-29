# Missing Now

Last synchronized: 2026-05-29.

This file lists what is currently missing from the implemented Java console application. Later roadmap ideas belong in `FUTURE_PLANS.md`; environment and database setup details belong only in the setup documentation.

## Short-Term Missing Now

These items are directly connected to the current boot, authentication, login, registration, and access request flows.

### Authentication And Access Requests

- Complete job selection during the first-login access request.
- Connect `SelectJob` to the pending-user setup flow.
- Complete requested role selection during first login or replace the current role-selection draft with a cleaner flow.
- Connect `RoleValidation`, `roleMenu`, and `CheckRoles` to real access request behavior.
- Store the requested job and requested role from user input instead of defaulting to `unassigned` and `intern`.
- Add an admin workflow to approve or reject access requests.
- Activate pending users only after an access request has been approved.
- Add admin actions to unlock, disable, quarantine, reactivate, or otherwise manage accounts.
- Decide how duplicate or repeated access requests from the same account should be handled.

### Login Security And Account Status

- Implement the empty `ExecutePWSDPolicy.locked`, `ExecutePWSDPolicy.suspicious`, and `ExecutePWSDPolicy.quarantine` methods.
- Decide whether `SetNewStatus` should become the shared status-update repository or be removed.
- Persist account status changes when failed-password thresholds are reached.
- Decide whether to use only `login_attempts` for failed password counting or also update `accounts.failed_password_attempts`.
- Add a clear reset strategy after successful login or administrator action.
- Review status messages so `disabled`, `locked`, and `on_quarantine` are described consistently.
- Keep `CheckSystemAccounts` only if it is needed by the active password-change flow; otherwise remove or replace it.

### Runtime And Controller Flow

- Add a connected main menu after successful active login.
- Implement department, job, role, and permission based routing after authentication.
- Implement real behavior behind `MenuController`.
- Implement real behavior behind `ServiceController`.
- Decide whether `uiController` is needed for the console version or should be reserved for a later UI layer.
- Decide how the application should return from registration or login back to the auth menu.
- Add clean shutdown behavior for expected user exits and fatal startup failures.

### Registration And Input Handling

- Review the registration correction path so users can change entered data, reconfirm it, and still create a password hash.
- Add uniqueness checks before creating accounts, especially for username and email.
- Improve email and phone validation beyond the current basic checks.
- Improve password input fallback behavior when `System.console()` is unavailable.
- Add fallback handling for password confirmation in `PasswordService.retypePWSD`.
- Add consistent validation and retry behavior across all console flows.
- Avoid leaking unnecessary debug or sensitive runtime information through console output.

### Configuration Stability

- Make startup fail fast when required configuration validation fails.
- Make database connection validation return a clear success or failure instead of only printing errors.
- Prevent later startup steps from running with missing or invalid runtime database values.
- Make starter account detection deterministic by checking the intended starter account identities, not only role IDs.

### Naming And Cleanup

- Clean up inconsistent class, method, package, and folder names such as `uiController`, `itJobsMenu`, `userAccunt`, `logsRepository`, `TECHNICHAL.md`, and `diagramms`.
- Remove unused imports and placeholder variables.
- Review duplicate or unused helper classes.
- Keep console text, flow logic, and repository logic separated as the application grows.

### Tests And Documentation

- Add unit tests for password validation, registration validation, login checks, and access request logic.
- Add integration tests for database-backed repositories.
- Add tests for failed login thresholds and starter account password changes.
- Update diagrams whenever new controller, repository, or access workflow behavior is connected.
- Keep environment setup details in `docs/setup/ENV_SETUP.md`.
- Keep database setup details in `docs/setup/DB_SETUP.md`.

## Core Product Features Still Missing

These items are still missing from the actual patient management product scope:

- Patient records.
- Patient search and lookup.
- Appointment scheduling.
- Treatment or visit workflows.
- Billing and finance workflows.
- Department administration.
- Staff administration.
- Role and permission administration.
- Audit logging for security and account events.
- Reporting and analytics.
- A graphical user interface.
- External API access.
- Deployment tooling.

## Current Priority

The next practical milestone is to finish the authentication and access-management foundation before adding patient workflows. That means completing job and role requests, implementing approval or rejection, applying account status changes, and adding tests around these flows.
