# Future Plans

Last synchronized: 2026-07-31.

The project is still completing authentication, authorization, access management, menu routing, recovery, logging, and test foundations. Patient-management features should follow only after those paths are reliable.

The active, itemized backlog is maintained in [ToDo.md](ToDo.md). This document describes direction and sequencing rather than completed work.

## Immediate Stabilization

1. Test and harden the multi-window failed-login policy, including SQL window semantics and all scaled thresholds.
2. Make attempt persistence and account-status transitions report failures consistently and use transactions where required.
3. Require active status and menu access before a session is considered permitted.
4. Make starter-account password and activation changes atomic.
5. Handle missing or cancelled terminal input as controlled authentication outcomes.
6. Add repository-level password-hash validation and explicit persistence results.
7. Restrict recovery targets and reconcile all account fields changed by recovery.
8. Add complete login, session, password, registration, recovery, and menu-routing tests.

## Menu and Authorization Direction

The current connected route is:

```text
CurrentSession
-> MenuControllerParent
-> MenuOption and ServiceAction
-> MenuContextStructure
-> ServiceController
-> ADMIN_USER_REQUESTS
-> ShowCurrentRequests
```

Near-term work will implement the remaining admin actions, local-admin services, logout, and a persistent menu loop. The service layer must validate every role/action combination against the active session, even when a context is constructed directly. Repository results should be returned as structured data and rendered by the CLI layer.

## Access-Request Direction

The intended workflow is:

```text
validated registration
-> pending account
-> department, job, and role request
-> authorized administrator review
-> approval or rejection transaction
-> atomic account activation and permission update
-> active session
-> authorized service actions
-> logout and session cleanup
```

The workflow needs duplicate handling, request reasons where required, pending-only queries, approver and decision metadata, and rollback-safe updates.

## Authentication and Security

- Keep login outcomes and persisted reasons typed and stable.
- Define remediation for suspicious, locked, and quarantined accounts.
- Define reset and retention rules for failed-login history.
- Replace numeric status, role, and department values with named types.
- Prevent stale static session state between authentication attempts.
- Reduce personal and account-state details in logs.
- Separate security audit events from operational diagnostics.
- Replace or formally define the purpose of the stored bootstrap key.

## Testing and Build Quality

- Add complete-flow tests for password creation and registration correction.
- Add login tests for unknown users, wrong passwords, every account status, and session creation.
- Add policy tests for all time windows, factors, boundaries, persistence failures, and status transitions.
- Add recovery tests for retry limits, missing keys, target restrictions, and account-field updates.
- Add menu, authorization, service-dispatch, and logout tests.
- Add MySQL integration tests against an isolated database.
- Separate unit and integration-test execution.
- Use Maven `release` 21.
- Add formatting, static analysis, coverage, and continuous integration.

## Logging Improvements

- Complete the migration from direct diagnostic printing to structured logging.
- Keep intentional CLI output separate from diagnostics.
- Align active loggers and configured appenders.
- Add rolling files, retention limits, and environment-specific levels.
- Avoid credentials, recovery values, and unnecessary personal data.

## First Functional Release Goals

- Stable registration, login, recovery, account-status, and session behavior
- Complete access-request and approval workflow
- Enforced role, department, permission, and action authorization
- Working admin and local-admin console services
- Patient record creation, lookup, and updates
- Appointment scheduling
- Staff and department management
- Audit and security-event review
- Consistent error handling and automated unit and integration tests

## Long-Term Product Direction

### Interfaces and APIs

- Complete console workflows first.
- Add a JavaFX desktop interface after service boundaries stabilize.
- Add a REST API after authorization is enforced independently of the UI.
- Add external integrations only for defined product requirements.

### Infrastructure

- Add Docker after local setup and integration tests are stable.
- Add deployment automation after a usable release exists.
- Evaluate cloud hosting, Kubernetes, or Redis only for concrete operational requirements.

### Monitoring and Data

- Structured audit logging and application metrics
- Prometheus and Grafana where operationally justified
- Documented retention and access rules
- Reporting, analytics, and audited data export
- Machine-learning experiments only for a concrete, ethically reviewed use case
