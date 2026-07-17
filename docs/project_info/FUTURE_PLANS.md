# Future Plans

Last synchronized: 2026-07-18.

The project is still completing its account, security, access-management, menu, logging, and test foundations. Patient-management features should follow only after those paths are reliable.

## Immediate Stabilization

1. Fix end-to-end password creation so the entered password is converted and hashed before sensitive arrays are cleared.
2. Fix registration correction so the generated password hash is stored and all corrected values return to one confirmation step.
3. Reject null or blank password hashes before repository inserts or updates.
4. Add complete-flow tests that verify a generated BCrypt hash against the original password.
5. Make repository operations return explicit results instead of swallowing SQL failures.
6. Correct failed-login threshold ordering and include the current attempt.
7. Restrict recovery updates to system accounts and handle a missing recovery-key row safely.

## Menu and Access-Management Direction

The next stable application chain should be:

```text
valid registration
-> pending account
-> complete department, job, and role request
-> authorized administrator review
-> approval or rejection
-> account activation and permissions
-> active current-user session
-> parent menu
-> submenu
-> working service action
-> logout and session cleanup
```

Near-term routing work:

- Define a `SUB_MENU` request or remove the unused `SubMenuController` dependency.
- Implement `RequestMenu` and populate `MenuValues.childKontext`.
- Connect the Requests parent option to an authorized service.
- Reconnect `ShowCurrentRequests` through a service that returns structured data.
- Filter request listings by status and authorization.
- Implement approval and rejection transactions.
- Add usable local-admin options.
- Define behavior for non-admin roles.

## Access Requests

- Collect and store a real requested job.
- Return and store a selected requested role.
- Prevent unintended duplicate pending requests.
- Add an optional request reason if the workflow requires it.
- Apply approved department, job, role, permission, account status, and menu access consistently.
- Record the approving administrator and decision timestamp.
- Keep repository queries separate from console rendering.

## Authentication and Security

- Define one explicit outcome model for all account statuses.
- Decide whether suspicious accounts are blocked, forced to change passwords, or granted restricted access.
- Reset or archive failed-login history after successful recovery or an administrator action.
- Replace status, role, and department magic numbers with named constants or typed values.
- Add logout and clear `CurrentSession` reliably.
- Reduce identity and account-state details in logs.
- Separate audit events from operational diagnostics.

## Testing and Build Quality

- Add end-to-end service tests for password creation and registration correction.
- Add tests for login status routing, current-session creation, and failed-attempt thresholds.
- Add recovery retry and system-account-scope tests.
- Add menu, submenu, and service-routing tests.
- Add repository integration tests against an isolated MySQL database.
- Resolve the Windows PowerShell Maven Wrapper startup issue.
- Use Maven `release` 21 instead of separate `source` and `target` values.
- Add formatting, linting, and static analysis.
- Add continuous integration after the test suite is independent of local terminal and database state.

## Logging Improvements

- Complete the migration from direct diagnostic printing to `LogManager`.
- Keep intentional CLI messages as console output.
- Align `LogManager` categories with all Logback appenders or remove unused appenders.
- Route recovery events to a dedicated logger if separate recovery logs are required.
- Add rolling files, retention limits, and a production-safe log level policy.
- Add tests or configuration validation for logger routing.

## First Functional Release Goals

- Stable registration, login, recovery, account-status, and session behavior
- Complete access-request and approval workflow
- Enforced role, department, permission, and menu access
- Working admin and local-admin console services
- Patient record creation and search
- Appointment scheduling
- Staff and department management
- Audit and security event review
- Consistent error handling and logging
- Automated unit and repository integration tests

## Long-Term Product Plans

### User Interfaces and APIs

- JavaFX desktop interface
- REST API
- External service integrations where required

### Infrastructure

- Docker
- Cloud deployment
- Kubernetes only if operational complexity justifies it
- Redis only for a defined caching or session use case

### Monitoring and Security

- Structured audit logging
- Application metrics
- Prometheus and Grafana
- Security monitoring and alerting

### Data and Reporting

- Reporting and analytics
- Data export and import
- Python-based data processing
- Machine-learning experiments only for a concrete, ethically reviewed use case
