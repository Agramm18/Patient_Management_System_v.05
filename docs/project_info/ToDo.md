# Project Backlog

Status date: 2026-07-23.

The last verified `.\mvnw.cmd test` run passed 55 tests on 2026-07-23: 15 `PasswordServiceTest` tests and 40 `RegistrationServiceTest` tests. The Maven Wrapper is currently verified in Windows PowerShell. The verified source snapshot contains 93 production Java files and 2 test source files.

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
- [x] Add repository methods that persist locked, suspicious, and quarantine status updates.
- [x] Add the starter-account first-password-change route.
- [x] Add the four-attempt recovery-key limit.
- [x] Add the pending-user department-request baseline.
- [x] Store active-account state in the `CurrentAccountInSessionValues` record and static `CurrentSession`.
- [x] Add `CurrentSession.isLoggedIn()` and `CurrentSession.clear()`.
- [x] Split login orchestration into `SetupCurrentSession`, `HandleAccountStatusTasks`, and `CallPasswordPolicyRules`.
- [x] Add the `LogsForDB` result record used by login-attempt persistence.

### Menu and Access Foundation

- [x] Add local-admin and admin parent-menu routing through `MenuControllerParent`.
- [x] Model the five-option admin parent menu with immutable `MenuOption` entries and typed `ServiceAction` values.
- [x] Replace numeric parent and child contexts with `MenuContextStructure(userRole, action)`.
- [x] Remove the obsolete `MenuController` and `SubMenuController` split from the active controller flow.
- [x] Forward the selected `ServiceAction` through `FrontController` to `ServiceController`.
- [x] Connect `ADMIN_USER_REQUESTS` to `ShowCurrentRequests`.
- [x] Reject unknown role IDs explicitly in `MenuControllerParent`.
- [x] Implement the first access-request listing query.

### Logging and Tests

- [x] Replace the former single `LogType` switch with typed logging methods and state enums.
- [x] Configure console and category file appenders in `logback.xml`.
- [x] Exclude generated logs through `.gitignore`.
- [x] Configure JUnit 5 and Surefire.
- [x] Add password validation and retype unit tests.
- [x] Add registration username, email, phone, confirmation-state, correction-choice, and password-hash guard tests.
- [x] Re-check Maven Wrapper test execution in Windows PowerShell.
- [x] Verify all 55 tests on 2026-07-23 after the typed menu-routing refactor.

## Current

Current focus: correct the refactored login outcome and failed-attempt contracts, complete the typed menu actions, and close the remaining end-to-end test gaps.

### Login Outcome and Failed-Attempt Correctness

- [x] Replace the former `LoginVerification` class with smaller credential, status, and password-policy components.
- [x] Replace the mutable current-user object with `CurrentAccountInSessionValues`.
- [ ] Use one canonical invalid-password reason in both `CallPasswordPolicyRules` and `CountFailedLoginAttempts`; the writer currently stores `to many false attempts` while the query counts only `INVALID_PASSWORD`.
- [ ] Include the current invalid-password attempt before applying the 5, 6, and 25 thresholds.
- [ ] Replace the ambiguous `canUseSystem` boolean with explicit outcomes for authenticated, pending-requested, password-changed, and rejected flows.
- [ ] Do not persist pending or failed password-update flows as successful authenticated logins.
- [ ] Propagate `UpdateUserPassword` failure instead of returning success unconditionally from the password-change status branch.
- [ ] Add tests for unknown users, invalid passwords, every account status, login-attempt reasons, and threshold boundaries.

### Menu Action Routing

- [x] Map every displayed admin option to a stable `ServiceAction`.
- [x] Carry the selected action through `MenuContextStructure`.
- [x] Route `ADMIN_USER_REQUESTS` to the current request-listing query.
- [ ] Implement `ADMIN_DISPLAY_ACCOUNTS`.
- [ ] Implement `ADMIN_SECURITY_OPTIONS`.
- [ ] Implement `ADMIN_VIEW_LOGS`.
- [ ] Implement `ADMIN_LOGOUT`, clear `CurrentSession`, and return to the authentication flow.
- [ ] Implement `LOCAL_ADMIN_DASHBOARD`; it currently reaches the unsupported-action exception.
- [ ] Add a controlled menu loop so the application does not end after one menu/service pass.
- [ ] Authorize each role/action combination inside the service layer before invoking a command.
- [ ] Add tests for option-to-action mapping, request delegation, unsupported actions, unknown roles, local-admin routing, and logout.
- [ ] Remove current routing cleanup leftovers: `AdminMenu.menuSize`, unused `Map` and `Consumer` imports, the empty `ServiceController` constructor, and unused parameters.

### Password Creation

- [x] Convert and hash the original password before clearing its character array.
- [x] Clear sensitive character arrays exactly once after the hash input is no longer needed.
- [x] Handle the retyped password deliberately instead of discarding the reassigned method parameter.
- [x] Handle a missing terminal console without entering an endless retry loop.
- [x] Add focused tests for clearing both password arrays, null arrays, and either array independently.
- [ ] Add an end-to-end test that verifies the generated BCrypt hash against the entered password.
- [ ] Add tests for the three-attempt password-policy limit.

### Registration Integrity

- [x] Store the hash returned by `PasswordFlow.policy` in `RegistrationService.hashedPWSD` on the confirmed registration path.
- [x] Return every changed field to one complete registration confirmation step.
- [x] Reject a null or blank collected password hash before `CreateAccount` is reached.
- [ ] Add a repository-level guard so direct `CreateAccount.newAccount` calls cannot insert a null or blank hash.
- [ ] Return account-creation success or failure to `RegistrationFlow`.
- [ ] Add complete registration and correction-flow tests.
- [x] Restore focused tests for registration confirmation, correction-choice parsing, invalid yes/no values, and password-hash guard behavior.
- [ ] Add correction-choice boundary tests for values outside 1-3 and user-facing nonnumeric input handling.

### Current Completion Check

- [ ] Verify that a normally entered password matches the stored BCrypt hash.
- [ ] Verify that every registration correction path produces a nonblank hash.
- [x] Verify helper-level null and blank password-hash rejection.
- [x] Run the complete unit-test suite with no failures.
- [ ] Verify that each invalid password advances the persisted 24-hour count exactly once.
- [ ] Verify that only an active, menu-enabled account produces an authenticated session.
- [ ] Verify that every displayed menu option has defined behavior and no valid action reaches the default exception.
- [ ] Verify that logout clears the session and returns to authentication without terminating the process.

## Urgent

### Registration Validation

- [ ] Add explicit username uniqueness checks before insert.
- [ ] Add explicit email uniqueness checks before insert.
- [ ] Review email edge cases such as multiple `@` characters and invalid domain labels.
- [ ] Review phone-number boundary behavior around the libphonenumber checks.

### Authentication and Session

- [x] Introduce the `LogsForDB` result record as a common transport type.
- [ ] Replace its boolean and free-form reason with explicit authenticated, rejected, password-changed, pending-requested, and suspicious outcomes.
- [ ] Decide whether suspicious accounts are blocked, restricted, or forced to change passwords.
- [ ] Decide whether starter accounts are logged in automatically after their first password change.
- [ ] Prevent stale static session state across repeated authentication attempts.
- [ ] Test active-session creation and every account-status branch.

### Failed-Login Policy

- [ ] Fix the failure-reason mismatch so newly stored invalid attempts are counted.
- [ ] Include the current failed attempt before threshold evaluation.
- [ ] Define non-overlapping semantics for locked, suspicious, and quarantine thresholds.
- [ ] Define reset behavior after login success, recovery, or administrator action.
- [ ] Replace numeric account-status IDs with named constants or typed values.
- [ ] Decide whether `accounts.failed_password_attempts` should be removed or used.
- [ ] Add boundary tests for attempts 5, 6, and 25.

### Menu and Service Routing

- [ ] Decide whether `RequestMenu` is still required; implement it as a submenu or remove the empty placeholder.
- [ ] Add selectable local-admin parent options.
- [ ] Map database role IDs explicitly to `AccountRoles` and never rely on enum ordinals, or remove the unused enum.
- [ ] Define supported menu behavior for application roles other than local admin and admin.
- [ ] Validate null, unsupported, and unauthorized role/action combinations.
- [ ] Replace fatal handling of unfinished menu actions with a controlled user-facing flow.

### Access Requests

- [ ] Implement `CollectUserJob`.
- [ ] Collect an actual job choice from every department job menu.
- [ ] Make `CollectUserRole` return the selected role ID.
- [ ] Connect job and role selection to `FirstLoginFlow`.
- [ ] Replace default job `unassigned` and role ID 9 with selected values.
- [ ] Decide how duplicate pending requests are handled.
- [ ] Add a request reason if required.
- [ ] Return success or failure from `CreateAccessRequest`.
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
- [ ] Enforce role/action authorization in `ServiceController` so a constructed context cannot invoke an admin command directly.
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

- [ ] Rename inconsistent identifiers such as `userAccunt`, `sqlQuerry`, `ParrentMenus`, `itJobsMenu`, and `logsRepository`.
- [ ] Normalize public method names such as `SystemAccounts`, `Value`, `Logs`, and `CurrentRequests`.
- [ ] Rename `TECHNICHAL.md` and `diagramms` with all references updated together.
- [ ] Replace broad `SELECT *` queries with required columns.
- [ ] Remove unused fields, imports, and placeholder classes without a planned responsibility.
- [ ] Decide whether `RouteService` should be implemented or removed.
- [ ] Separate CLI rendering from repository and security-policy code.
- [ ] Replace direct `System.exit` calls with controlled shutdown where practical.
- [ ] Clean duplicate `.gitignore` patterns.
- [ ] Add diagnostics for legacy `.env` keys such as `DB_PWSD`, `LOCAL_ADMIN_PWSD`, and `ADMIN_PWSD_DEFAULT`.
- [ ] Sync the root `README.md` with the 93-source/55-test result, the typed menu flow, and the refactored login classes.

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
