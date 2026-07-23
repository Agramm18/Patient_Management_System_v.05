# Future Plans

Last synchronized: 2026-07-23.

The project is still completing its authentication, security, access-management, menu, logging, and test foundations. Patient-management features should follow only after those paths are reliable.

## Recently Completed Baseline

The following work is now part of the current implementation rather than future work:

- Password input is converted and hashed before sensitive character arrays are cleared.
- Both password arrays are cleared after the hash input is no longer needed.
- Registration returns corrected profile values to one confirmation loop and stores the resulting password hash.
- Helper-level registration checks reject null and blank collected hashes.
- The menu layer uses `MenuOption`, `ServiceAction`, and `MenuContextStructure` instead of numeric parent and child contexts.
- `MenuControllerParent` forwards the selected typed action to `ServiceController`.
- `ADMIN_USER_REQUESTS` invokes `ShowCurrentRequests`.
- The active session value is the immutable `CurrentAccountInSessionValues` record.
- Credential verification, account-status behavior, login-log values, and failed-password policy calls have been separated into dedicated login services.
- The Windows Maven Wrapper runs the complete 55-test suite successfully.

## Immediate Stabilization

1. Use one stable failure-reason value for invalid passwords in both login-attempt persistence and `CountFailedLoginAttempts`.
2. Include the current failed attempt in policy evaluation and define non-overlapping locked, suspicious, and quarantine transitions.
3. Add boundary tests for the failed-attempt thresholds at 5, 6, and 25 attempts.
4. Add an end-to-end password test that verifies the generated BCrypt hash against the original password.
5. Make the full password flow terminate cleanly when `System.console()` is unavailable and handle a missing retyped password safely.
6. Add a repository-level null and blank hash guard to `CreateAccount`.
7. Return explicit success or failure from account creation, password updates, access-request inserts, and recovery-key persistence.
8. Restrict recovery updates to system accounts and handle a missing or blank recovery-key hash before BCrypt verification.

## Menu and Service-Action Direction

The current connected route is:

```text
active CurrentSession
-> MenuControllerParent
-> AdminMenu MenuOption
-> MenuContextStructure(userRole, ServiceAction)
-> ServiceController
-> ADMIN_USER_REQUESTS
-> ShowCurrentRequests
```

Near-term routing work:

- Implement `ADMIN_DISPLAY_ACCOUNTS`.
- Implement `ADMIN_SECURITY_OPTIONS`.
- Implement `ADMIN_VIEW_LOGS`.
- Connect `ADMIN_LOGOUT` to `CurrentSession.clear()` and return to authentication.
- Implement `LOCAL_ADMIN_DASHBOARD` instead of sending it to the unsupported-action exception.
- Add a controlled menu loop so a session does not end after one menu and service pass.
- Enforce role/action authorization in the service layer even when a context is constructed directly.
- Validate null and unsupported actions with controlled user-facing results.
- Map database role IDs explicitly or remove the currently unused `AccountRoles` enum.
- Define menu behavior for roles other than local admin and admin.
- Decide whether the empty `RequestMenu` is still required as a submenu or should be removed.
- Separate command execution and returned data from console rendering.

## Access Requests

- Collect and store a real requested job.
- Return and store the selected requested role.
- Connect job and role selection to `FirstLoginFlow`.
- Prevent unintended duplicate pending requests.
- Add an optional request reason if the workflow requires it.
- Return structured request data instead of printing rows from `ShowCurrentRequests`.
- Filter request listings by status.
- Implement authorized approval and rejection transactions.
- Apply approved department, job, role, permission, account status, and menu access atomically.
- Record the approving administrator, decision timestamp, and rejection reason.

The intended workflow is:

```text
valid registration
-> pending account
-> complete department, job, and role request
-> authorized administrator review
-> approval or rejection
-> account activation and permissions
-> active current-account session
-> parent menu
-> authorized service action
-> logout and session cleanup
```

## Authentication and Security

- Define one explicit outcome model for authenticated, rejected, password-changed, pending-requested, and suspicious results.
- Decide whether suspicious accounts are blocked, restricted, or forced to change passwords.
- Decide whether starter accounts are logged in automatically after their first password change.
- Prevent stale static session state across repeated authentication attempts.
- Reset or archive failed-login history after successful recovery or an administrator action.
- Replace status, role, and department magic numbers with named constants or typed values.
- Define which account fields recovery changes in addition to `password_hash`.
- Reduce identity and account-state details in logs.
- Separate security audit events from operational diagnostics.

## Testing and Build Quality

- Add complete-flow tests for password creation and registration correction.
- Add login tests for unknown users, wrong passwords, every account status, and active-session creation.
- Add failed-attempt persistence and policy-boundary tests.
- Add recovery retry, missing-key, and system-account-scope tests.
- Add tests for option-to-action mapping, request delegation, unsupported actions, unknown roles, local-admin routing, and logout.
- Add repository integration tests against an isolated MySQL database.
- Separate unit and integration-test execution.
- Use Maven `release` 21 instead of separate `source` and `target` values.
- Add formatting, linting, static analysis, and coverage reporting.
- Add continuous integration after tests are independent of local terminal and database state.

## Logging Improvements

- Complete the migration from direct diagnostic printing to `LogManager`.
- Keep intentional CLI messages as console output.
- Align `LogManager` categories with all Logback appenders or remove unused appenders.
- Route recovery events consistently.
- Add rolling files, retention limits, and a production-safe log-level policy.
- Avoid logging credentials, recovery values, and unnecessary personal data.
- Add configuration tests only where logger routing and filesystem behavior are part of the contract.

## First Functional Release Goals

- Stable registration, login, recovery, account-status, and session behavior
- Complete access-request and approval workflow
- Enforced role, department, permission, and action authorization
- Working admin and local-admin console services
- Patient record creation and search
- Appointment scheduling
- Staff and department management
- Audit and security-event review
- Consistent error handling and logging
- Automated unit and repository integration tests

## Long-Term Product Plans

### User Interfaces and APIs

- Complete and stabilize the console workflows first.
- Add a JavaFX desktop interface.
- Add a REST API only after service boundaries and authorization are stable.
- Add external service integrations only for defined product requirements.

### Infrastructure

- Add Docker after local setup and integration tests are stable.
- Add cloud deployment after a usable application release exists.
- Evaluate Kubernetes only if operational complexity justifies it.
- Evaluate Redis only for a defined caching or session use case.

### Monitoring and Security

- Structured audit logging
- Application metrics
- Prometheus and Grafana
- Security monitoring and alerting
- Documented retention and access rules

### Data and Reporting

- Reporting and analytics
- Secure data export and import
- Python-based data processing
- Machine-learning experiments only for a concrete, ethically reviewed use case
