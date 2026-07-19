# Project Backlog

Status date: 2026-07-19.

The last verified `mvn test` run passed 53 tests. The test sources now contain 57 tests: 15 `PasswordServiceTest` tests and 42 `RegistrationServiceTest` tests. A new complete run is still pending because of the known Maven Wrapper failure in Windows PowerShell.

## Category Guide

| Category | Meaning |
| --- | --- |
| `DONE` | Verified project foundations that already exist |
| `Current` | The small set of tasks being worked on now |
| `Urgent` | Work that should follow the current focus |
| `Critical` | Security, correctness, and data-integrity blockers before real use |
| `Core Production` | Features that form the actual patient-management system |
| `Side Tasks` | Tooling, cleanup, documentation, and optional infrastructure |

## DONE

### Project and Configuration

- [x] Create the Java 21 Maven project.
- [x] Add Maven Wrapper scripts configured for Maven 3.9.16.
- [x] Implement controller-based bootstrap and front-controller dispatch.
- [x] Validate `.env` existence and required values.
- [x] Add the `EnvSetup` record for validated environment values.
- [x] Validate the MySQL connection and initialize static `DBManager` settings.
- [x] Hash and upsert the recovery key during startup.
- [x] Detect and create missing local-admin and admin starter accounts.

### Authentication Foundation

- [x] Implement registration input and validation.
- [x] Implement the BCrypt password-policy and password-retype baseline.
- [x] Implement login and BCrypt verification.
- [x] Persist login attempts.
- [x] Persist locked, suspicious, and quarantine status updates.
- [x] Add the starter-account first-password-change route.
- [x] Add the four-attempt recovery-key limit.
- [x] Add the pending-user department-request baseline.
- [x] Add `CurrentUser` and the static `CurrentSession` baseline.

### Menu and Access Foundation

- [x] Add local-admin and admin parent-menu routing.
- [x] Add the five-option admin parent menu.
- [x] Add `MenuValues` with parent, role, and child context.
- [x] Add role-based `ServiceController` dispatch.
- [x] Implement the first access-request listing query.

### Logging and Tests

- [x] Replace the former single `LogType` switch with typed logging methods and state enums.
- [x] Configure console and category file appenders in `logback.xml`.
- [x] Exclude generated logs through `.gitignore`.
- [x] Configure JUnit 5 and Surefire.
- [x] Add password validation and retype unit tests.
- [x] Add registration username, email, phone, and choice validation tests.
- [x] Verify all 53 tests on 2026-07-18.

## Current

Current focus: make password creation and registration produce a valid, persistable account every time.

### Password Creation

- [x] Convert and hash the original password before clearing its character array.
- [x] Clear sensitive character arrays exactly once after the hash input is no longer needed.
- [x] Handle the retyped password deliberately instead of discarding the reassigned method parameter.
- [x] Handle a missing terminal console without entering an endless retry loop.
- [x] Add focused tests for clearing both password arrays, null arrays, and either array independently.
- [ ] Add an end-to-end test that verifies the generated BCrypt hash against the entered password.
- [ ] Add tests for the three-attempt password-policy limit.

### Registration Integrity

- [ ] Store the hash returned by `PasswordFlow.policy` in `RegistrationService.hashedPWSD` after every correction path.
- [ ] Return every changed field to one complete registration confirmation step.
- [ ] Reject a null or blank password hash before `CreateAccount` executes SQL.
- [ ] Return account-creation success or failure to `RegistrationFlow`.
- [ ] Add complete registration and correction-flow tests.

### Current Completion Check

- [ ] Verify that a normally entered password matches the stored BCrypt hash.
- [ ] Verify that every registration correction path produces a nonblank hash.
- [ ] Run the complete unit-test suite with no failures.

## Urgent

### Registration Validation

- [ ] Add explicit username uniqueness checks before insert.
- [ ] Add explicit email uniqueness checks before insert.
- [ ] Review email edge cases such as multiple `@` characters and invalid domain labels.
- [ ] Review phone-number boundary behavior around the libphonenumber checks.

### Authentication and Session

- [ ] Define one result type for authenticated, rejected, password-changed, pending-requested, and suspicious outcomes.
- [ ] Decide whether suspicious accounts are blocked, restricted, or forced to change passwords.
- [ ] Decide whether starter accounts are logged in automatically after their first password change.
- [ ] Add logout and clear `CurrentSession`.
- [ ] Prevent stale static session state across repeated authentication attempts.
- [ ] Test active-session creation and every account-status branch.

### Failed-Login Policy

- [ ] Include the current failed attempt before threshold evaluation.
- [ ] Define non-overlapping semantics for locked, suspicious, and quarantine thresholds.
- [ ] Define reset behavior after login success, recovery, or administrator action.
- [ ] Replace numeric account-status IDs with named constants or typed values.
- [ ] Decide whether `accounts.failed_password_attempts` should be removed or used.
- [ ] Add boundary tests for attempts 5, 6, and 25.

### Menu and Service Routing

- [ ] Add a real submenu dispatch path or remove the unused `SubMenuController` dependency.
- [ ] Implement `RequestMenu`.
- [ ] Populate and validate `MenuValues.childKontext`.
- [ ] Add selectable local-admin parent options.
- [ ] Connect the five admin parent options to submenus or commands.
- [ ] Define behavior for role IDs other than 1 and 2.
- [ ] Add routing tests for parent context, child context, role, and invalid values.

### Access Requests

- [ ] Implement `CollectUserJob`.
- [ ] Collect an actual job choice from every department job menu.
- [ ] Make `CollectUserRole` return the selected role ID.
- [ ] Connect job and role selection to `FirstLogin`.
- [ ] Replace default job `unassigned` and role ID 9 with selected values.
- [ ] Decide how duplicate pending requests are handled.
- [ ] Add a request reason if required.
- [ ] Return success or failure from `CreateAccessRequest`.
- [ ] Reconnect the request-listing query to the active menu and service path.
- [ ] Return structured request data instead of printing from `ShowCurrentRequests`.
- [ ] Filter request listings by pending status.

## Critical

These items must be resolved before the application handles real users or patient data.

### Recovery Safety

- [ ] Restrict `SelectUserForRecover` to accounts with `is_system_account = true`.
- [ ] Handle a missing or blank `recovery_keys.id = 1` value before BCrypt verification.
- [ ] Return an explicit result from `UpdateSystemAccountPassword`.
- [ ] Define which status, password-change, and menu-access fields recovery updates.
- [ ] Test the four-attempt limit and system-account target restriction.

### Authorization and Account Activation

- [ ] Authorize access-request viewing and decisions.
- [ ] Implement approve and reject transactions.
- [ ] Persist the approver, approval timestamp, and rejection reason.
- [ ] Apply department, job, role, permission, status, and menu access atomically.
- [ ] Test approval, rejection, duplicate handling, and transaction rollback.
- [ ] Prevent direct repository access from bypassing service authorization.

### Configuration and Database Integrity

- [ ] Propagate `SetRecoveryKey` failures so configuration cannot report false success.
- [ ] Replace fixed account IDs 1 and 2 in starter-password fallback checks.
- [ ] Distinguish duplicate accounts, missing hashes, and database connection failures.
- [ ] Return explicit results from password-update and account repositories.
- [ ] Close result sets consistently with try-with-resources.
- [ ] Add versioned schema migrations instead of relying only on setup SQL.
- [ ] Add repository integration tests using an isolated database.

### Security and Audit

- [ ] Prevent credentials, recovery values, and unnecessary personal data from being logged.
- [ ] Avoid logging usernames and detailed account state unless required for an authorized audit event.
- [ ] Separate security audit events from operational diagnostics.
- [ ] Define retention and access rules for security and login-attempt data.

## Core Production

This section contains the actual patient-management product. Start it after the authentication, authorization, and data-integrity foundation is stable.

### Patient Records

- [ ] Design the patient data model and required reference tables.
- [ ] Create patient records with validated personal and contact data.
- [ ] View and update patient records through authorized services.
- [ ] Implement patient search and filtering.
- [ ] Track record creation, changes, and responsible users.
- [ ] Define archival and deletion rules for patient records.

### Appointments

- [ ] Design appointment, staff, department, room, and status relationships.
- [ ] Create, reschedule, and cancel appointments.
- [ ] Prevent conflicting appointments for staff, patients, and rooms.
- [ ] Add daily and department appointment views.
- [ ] Record appointment status and attendance.

### Clinical Workflows

- [ ] Add visits and treatment records.
- [ ] Add diagnoses and clinical notes with strict authorization.
- [ ] Add laboratory orders and result tracking.
- [ ] Add medication and prescription workflows.
- [ ] Add emergency and inpatient workflow foundations if they remain in scope.

### Staff and Departments

- [ ] Implement staff profiles linked to user accounts.
- [ ] Implement department, job, role, and permission administration.
- [ ] Assign staff to departments and operational responsibilities.
- [ ] Add staff availability and scheduling foundations.

### Billing and Reporting

- [ ] Add billing and payment records.
- [ ] Add finance workflows with separated permissions.
- [ ] Add operational reports and analytics.
- [ ] Add secure data export with audit logging.

### Production Interface

- [ ] Complete the console workflows before replacing them.
- [ ] Design and implement the JavaFX user interface.
- [ ] Add a REST API only after service boundaries and authorization are stable.
- [ ] Add end-to-end tests for the primary patient workflows.

## Side Tasks

### Build and Quality Tooling

- [ ] Resolve the Maven Wrapper null-target failure observed in Windows PowerShell.
- [ ] Configure Maven with `release` 21 instead of separate `source` and `target` values.
- [ ] Separate unit and integration-test execution.
- [ ] Add database test configuration.
- [ ] Add a formatter and static analysis.
- [ ] Add continuous integration.
- [ ] Add test coverage reporting.

### Logging Improvements

- [ ] Align Logback categories with active `LogManager` fields.
- [ ] Decide whether the `ACCESS` and `DATABASE` appenders should be used or removed.
- [ ] Route recovery events to a dedicated logger if required.
- [ ] Migrate diagnostic `System.out.println` calls while retaining intentional CLI output.
- [ ] Add rolling files and retention limits.
- [ ] Define development and production log levels.
- [ ] Test logging configuration only where filesystem behavior is part of the contract.

### Cleanup and Naming

- [ ] Rename inconsistent identifiers such as `userAccunt`, `sqlQuerry`, `parentKonext`, `childKontext`, `itJobsMenu`, and `logsRepository`.
- [ ] Normalize public method names such as `SystemAccounts`, `Value`, `Logs`, and `CurrentRequests`.
- [ ] Rename `TECHNICHAL.md` and `diagramms` with all references updated together.
- [ ] Replace broad `SELECT *` queries with required columns.
- [ ] Remove unused fields, imports, and placeholder classes without a planned responsibility.
- [ ] Decide whether `RouteService` should be implemented or removed.
- [ ] Separate CLI rendering from repository and security-policy code.
- [ ] Replace direct `System.exit` calls with controlled shutdown where practical.
- [ ] Clean duplicate `.gitignore` patterns.
- [ ] Add diagnostics for legacy `.env` keys such as `DB_PWSD`, `LOCAL_ADMIN_PWSD`, and `ADMIN_PWSD_DEFAULT`.

### Optional Infrastructure

- [ ] Add Docker after local setup and integration tests are stable.
- [ ] Add deployment automation after a usable application release exists.
- [ ] Evaluate cloud hosting only when deployment requirements are known.
- [ ] Evaluate Kubernetes or Redis only for a concrete operational requirement.

### Open Decisions

- [ ] Define the exact policy for suspicious accounts.
- [ ] Confirm whether recovery remains permanently restricted to system accounts.
- [ ] Define which roles may view, approve, or reject access requests.
- [ ] Decide whether jobs remain strings or move to a reference table.
- [ ] Decide whether `login_attempts` is the only source of truth for failed-password policy.
- [ ] Decide whether logs remain split by category or move to combined rolling files and database audit events.
- [ ] Confirm that the console workflow will be completed before JavaFX or REST development begins.
