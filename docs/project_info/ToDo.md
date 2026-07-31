# Project Backlog

Status date: 2026-07-31.

The current tree contains 98 production Java files, 2 test files, and 55 JUnit test methods. `.\mvnw.cmd test` passed on 2026-07-31 with 55 tests, 0 failures, 0 errors, and 0 skipped tests.

This file contains unfinished work only. Completed milestones are recorded in [CURRENT_STATUS.md](CURRENT_STATUS.md) and [ARCHIVE.md](../archive/ARCHIVE.md) so the active backlog remains concise.

## Current Priorities

### Login and Session Correctness

- [ ] Replace free-form login persistence reasons with a typed value or derive them from `LoginOutcome`.
- [ ] Create a `SessionAccount` and return `PERMITTED` only when the account is active and has menu access.
- [ ] Clear stale `CurrentSession` state before a new authentication attempt.
- [ ] Make the password-hash update and the activation/menu-access update one transaction and return failure when either step fails.
- [ ] Make login-attempt persistence and account-status transitions consistent when either database write fails.
- [ ] Align SQL windows with the declared `TimePeriod` model, especially calendar-day versus rolling 24-hour behavior.
- [ ] Remove or implement the per-session retry counter, which is recreated for each invalid password.
- [ ] Define reset or archival behavior for failed-login history after successful login, recovery, or administrator action.
- [ ] Select one source of truth for failed attempts: `login_attempts` or `accounts.failed_password_attempts`.
- [ ] Replace numeric account-status IDs with named constants or typed values.
- [ ] Correct misspelled and mismatched outcome values such as `USERNAME_NOT_FOUNT`, `UNKOWN_ACCOUNT_STATUS`, and `UNKNOWN_ACCOUNT-STATUS`.
- [ ] Add tests for unknown users, invalid passwords, every account status, all policy windows, 5/6/25 boundaries, persistence counts, stale sessions, and database failures.
- [ ] Handle missing `System.console()` and cancelled hidden input without terminating bootstrap or dereferencing null arrays.

### Menu and Admin Routing

- [ ] Implement `ADMIN_DISPLAY_ACCOUNTS`.
- [ ] Implement `ADMIN_SECURITY_OPTIONS`.
- [ ] Implement `ADMIN_VIEW_LOGS`.
- [ ] Implement `ADMIN_LOGOUT`, clear `CurrentSession`, and return to authentication.
- [ ] Implement `LOCAL_ADMIN_DASHBOARD` with selectable local-admin actions.
- [ ] Add a controlled menu loop so the application does not end after one service call.
- [ ] Enforce role/action authorization inside the service layer instead of trusting `MenuContextStructure`.
- [ ] Handle null, unsupported, and unauthorized actions without the generic fatal bootstrap path.
- [ ] Define menu behavior for roles other than local admin and admin.
- [ ] Map database role IDs explicitly to typed roles or remove the unused `AccountRoles` enum.
- [ ] Implement `RequestMenu` as a real submenu or remove it.
- [ ] Remove remaining routing leftovers such as `AdminMenu.menuSize`, the empty `ServiceController` constructor, the unused `scanner` parameter, and unused local variables.
- [ ] Return service data separately from console rendering.
- [ ] Add tests for option mapping, request delegation, authorization, unsupported actions, unknown roles, local-admin routing, menu repetition, and logout.

### Registration and Password Flow

- [ ] Add a repository-level null and blank password-hash guard to `CreateAccount`.
- [ ] Return an explicit account-creation result to `RegistrationFlow`.
- [ ] Add explicit username and email uniqueness checks before insert.
- [ ] Align accepted email length with the database column or expand the schema.
- [ ] Review email cases such as multiple `@` characters and invalid domain labels.
- [ ] Review phone-number boundary behavior around libphonenumber validation.
- [ ] Add an end-to-end password test that verifies the generated BCrypt hash against the entered password.
- [ ] Add tests for the three-attempt password-policy limit and cancelled password re-entry.
- [ ] Add complete registration and correction-flow tests.
- [ ] Add correction-choice tests for values outside 1 through 3 and nonnumeric input.
- [ ] Return a controlled outcome when a password re-entry does not match instead of aborting the flow.

### Access Requests

- [ ] Implement `CollectUserJob` and collect a job from every department menu.
- [ ] Make `CollectUserRole` return the selected role ID.
- [ ] Pass selected department, job, and role values through `FirstLoginFlow`.
- [ ] Replace default job `unassigned` and role ID 9 with the selected values.
- [ ] Prevent or deliberately merge duplicate pending requests.
- [ ] Add a request reason if it is part of the workflow.
- [ ] Return explicit success or failure from `CreateAccessRequest`.
- [ ] Return structured request data instead of printing rows in `ShowCurrentRequests`.
- [ ] Filter the admin request list by pending status.

## Critical Before Real Use

### Recovery Safety

- [ ] Restrict `SelectUserForRecover` to `is_system_account = true` if recovery remains system-account-only.
- [ ] Validate a missing, blank, or malformed `recovery_keys.id = 1` hash before BCrypt verification.
- [ ] Return an explicit result from `UpdateSystemAccountPassword`.
- [ ] Define and update recovery-related status, `requires_password_change`, menu-access, and session fields consistently.
- [ ] Test the four-attempt limit, missing-key behavior, cancelled console input, and target restriction.

### Authorization and Account Activation

- [ ] Authorize access-request viewing and decisions.
- [ ] Implement approval and rejection transactions.
- [ ] Persist the approver, decision timestamp, request status, and rejection reason.
- [ ] Apply department, job, role, permission, account status, and menu access atomically.
- [ ] Prevent direct repository use from bypassing service authorization.
- [ ] Test approval, rejection, duplicate handling, unauthorized access, and transaction rollback.

### Configuration and Database Integrity

- [ ] Propagate `SetRecoveryKey` failures so configuration cannot report false success.
- [ ] Replace fixed account IDs 1 and 2 in starter-account fallback checks.
- [ ] Distinguish duplicates, missing hashes, missing rows, and connection failures.
- [ ] Return explicit results from password, account, access-request, and status repositories.
- [ ] Close every `ResultSet` with try-with-resources.
- [ ] Add versioned schema migrations.
- [ ] Add repository integration tests against an isolated database.

### Security and Audit

- [ ] Prevent credentials, recovery values, and unnecessary personal data from being logged.
- [ ] Avoid logging usernames and detailed account state unless an authorized audit requirement needs them.
- [ ] Separate security audit events from operational diagnostics.
- [ ] Define retention and access rules for logs and login-attempt data.
- [ ] Replace the plain-text stored bootstrap key or document and enforce its security purpose.

## Product Backlog

### Patient Records

- [ ] Design the patient data model and reference tables.
- [ ] Create patient records with validated personal and contact data.
- [ ] View and update records through authorized services.
- [ ] Implement patient search and filtering.
- [ ] Audit record creation and changes.
- [ ] Define archival and deletion rules.

### Appointments and Clinical Workflows

- [ ] Design appointment, staff, department, room, and status relationships.
- [ ] Create, reschedule, and cancel appointments without resource conflicts.
- [ ] Add daily and department appointment views.
- [ ] Add visits, treatment records, diagnoses, and clinical notes with strict authorization.
- [ ] Add laboratory orders and result tracking.
- [ ] Add medication and prescription workflows.
- [ ] Define emergency and inpatient scope.

### Staff, Billing, and Reporting

- [ ] Implement staff profiles linked to accounts.
- [ ] Implement department, job, role, and permission administration.
- [ ] Add staff assignment, availability, and scheduling.
- [ ] Add billing and payment records with separated finance permissions.
- [ ] Add operational reports, analytics, and audited data export.

### Product Interfaces

- [ ] Complete and stabilize the console workflows.
- [ ] Design and implement a JavaFX interface.
- [ ] Add a REST API after service boundaries and authorization are stable.
- [ ] Add end-to-end tests for primary patient workflows.

## Engineering Backlog

### Build and Test Tooling

- [ ] Configure Maven with `release` 21 instead of separate `source` and `target` values.
- [ ] Separate unit and integration-test execution.
- [ ] Add database test configuration.
- [ ] Add formatting, linting, static analysis, and coverage reporting.
- [ ] Add continuous integration after tests are independent of local terminal and database state.

### Logging

- [ ] Align Logback categories with active `LogManager` fields or remove unused `ACCESS` and `DATABASE` appenders.
- [ ] Route recovery events consistently.
- [ ] Migrate diagnostic `System.out.println` calls while retaining intentional CLI output.
- [ ] Add rolling files, retention limits, and environment-specific log levels.
- [ ] Add focused logging-configuration tests where filesystem behavior is contractual.

### Cleanup and Naming

- [ ] Rename inconsistent identifiers such as `userAccunt`, `sqlQuerry`, `ParrentMenus`, `PolicieBehaviour`, `itJobsMenu`, and `logsRepository`.
- [ ] Normalize public method names such as `SystemAccounts`, `Value`, `Logs`, and `CurrentRequests`.
- [ ] Rename `TECHNICHAL.md` and `diagramms` with all references updated together.
- [ ] Replace broad `SELECT *` queries with required columns.
- [ ] Remove unused fields, imports, methods, and placeholder classes without a planned responsibility.
- [ ] Implement or remove `RouteService` and `UIController`.
- [ ] Replace direct `System.exit` calls with controlled shutdown where practical.
- [ ] Clean duplicate `.gitignore` patterns.
- [ ] Add diagnostics for legacy `.env` keys such as `DB_PWSD`, `LOCAL_ADMIN_PWSD`, and `ADMIN_PWSD_DEFAULT`.
- [ ] Update the root `README.md` to the 98-source, 2-test-file, 55-test baseline and the current typed login policy.

### Optional Infrastructure

- [ ] Add Docker after local setup and integration tests are stable.
- [ ] Add deployment automation after a usable application release exists.
- [ ] Evaluate cloud hosting when deployment requirements are known.
- [ ] Evaluate Kubernetes or Redis only for a concrete operational requirement.

## Open Decisions

- [ ] Define how blocked suspicious accounts are remediated or reset.
- [ ] Confirm whether recovery remains permanently restricted to system accounts.
- [ ] Define which roles may view, approve, or reject access requests.
- [ ] Decide whether jobs remain strings or move to a reference table.
- [ ] Confirm the exact failed-login window and escalation policy.
- [ ] Decide whether logs remain split by category or move to combined rolling files and database audit events.
