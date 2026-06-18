# ToDo

Status date: 2026-06-18.

This is the current implementation backlog. It is ordered by dependency and risk rather than by product feature.

`mvn -DskipTests compile` currently succeeds. No automated tests exist.

## Current Milestone

Complete and stabilize the authentication, session, menu-routing, and access-management foundation:

```text
configuration
-> registration
-> login and security policy
-> recovery
-> current-user session
-> role-aware menu routing
-> service routing
-> complete access request
-> approval
-> activation
-> usable role menus
```

## Completed Baseline

- [x] Java 21 Maven project
- [x] Controller-based bootstrap
- [x] `.env` and MySQL connection validation
- [x] Runtime database connection manager
- [x] Missing starter-account creation
- [x] Registration and BCrypt password creation baseline
- [x] Login and BCrypt verification baseline
- [x] Login attempt persistence
- [x] Starter-account first password change
- [x] Recovery-key hashing, persistence, and verification
- [x] Four-attempt recovery-key limit
- [x] Pending-user department request baseline
- [x] Persisted failed-login status updates
- [x] Initial SLF4J and Logback integration
- [x] `CurrentUser` and `CurrentSession` runtime session baseline
- [x] `FrontController.RequestType.MENU` routing baseline
- [x] `MenuController` role routing baseline for local admin and admin
- [x] Admin and local-admin menu display classes
- [x] `MenuFlow.chooseOption` returns a validated selected option
- [x] `MenuValues` passes menu choice and role into the service layer
- [x] `FrontController.RequestType.SERVICE` routing baseline
- [x] `ServiceController` role and choice dispatch baseline
- [x] Admin option `1` lists current access-management requests
- [x] Default admin account creation now uses `ADMIN_PASSWORD_DEFAULT`
- [x] Local setup no longer requires duplicate admin-password environment variables
- [x] Current documentation synchronized

## Highest Priority Defects

### Environment and Starter Accounts

- [ ] Add validation or explicit failure when a starter-account password cannot be loaded.

### Registration Integrity

- [ ] Re-show and reconfirm registration data after a field is changed.
- [ ] Prevent `CreateAccount` from running with a null or blank password hash.
- [ ] Add explicit username and email uniqueness checks.
- [ ] Improve email and phone validation.
- [ ] Return repository success or failure instead of only printing messages.

### Login Session and Status Handling

- [ ] Create a consistent session result for every login status that should be treated as successful.
- [ ] Decide whether `suspicious` login should create menu access, require password change, or remain blocked.
- [ ] After a starter-account password change, decide whether the user should be logged in automatically or asked to log in again.
- [ ] Guard `BootConfigService` against a null `CurrentSession` before calling user session methods.
- [ ] Prevent success logs from exposing user identity and security state more broadly than needed.

### Failed-Login Policy

- [ ] Include the current failed attempt when evaluating thresholds.
- [ ] Confirm and document the intended thresholds for locked, suspicious, and quarantine states.
- [ ] Define how failed-attempt history is reset after success or admin action.
- [ ] Replace status-ID magic numbers with named values.
- [ ] Decide whether `accounts.failed_password_attempts` is still required.

### Recovery Safety

- [ ] Restrict the final recovery target lookup to `is_system_account = true`, or rename the feature.
- [ ] Handle missing `recovery_keys.id = 1` without passing null to BCrypt.
- [ ] Return success or failure from password update repositories.
- [ ] Decide whether recovery should also change status, password-change, or menu-access fields.

## Menu and Runtime Routing

- [x] Return the selected admin menu option from `MenuFlow.chooseOption`.
- [x] Store the selected menu option in `MenuValues`.
- [ ] Implement local-admin menu options.
- [ ] Implement admin menu options `2` through `8`.
- [ ] Expand admin option `1` from raw request listing into a complete review workflow.
- [ ] Route authorized admin options to dedicated service classes.
- [ ] Decide the long-term boundary between `ServiceController`, `RouteService`, and `app.Services`.
- [ ] Define behavior for role IDs other than `1` and `2`.
- [ ] Decide whether `uiController` is needed in the console runtime.
- [ ] Replace direct `System.exit` calls with controlled application shutdown where practical.

## Logging Migration

- [ ] Add `src/main/resources/logback.xml`.
- [ ] Define console and file output policy.
- [ ] Handle every declared `LogType` in `LogManager.log`.
- [ ] Add the missing `break` after `AUTH_DEBUG`.
- [ ] Remove unused loggers and imports from `LogManager`.
- [ ] Decide whether success events should use `INFO` or a structured event field.
- [ ] Migrate login, password, repository, and menu diagnostics from `System.out.println`.
- [ ] Keep user-facing CLI messages separate from diagnostic logs.
- [ ] Prevent credentials and secrets from being logged.

## Access Request Workflow

### Job and Role Selection

- [ ] Implement `CollectUserJob`.
- [ ] Complete every department job menu.
- [ ] Make `CollectUserRole` return the selected role ID.
- [ ] Connect job and role selection to `FirstLogin`.
- [ ] Replace default access-request job and role values with selected values.

### Request Persistence

- [ ] Return success or failure from `CreateAccessRequest`.
- [ ] Decide how duplicate pending requests are handled.
- [ ] Add request reason input if required.
- [ ] Decide whether pending-user request creation is a successful login event.

### Approval and Activation

- [x] Add a first admin-side access-request listing query.
- [ ] Filter admin request listing by pending request status when required.
- [ ] Return structured results from the request listing instead of printing directly in the repository.
- [ ] List pending requests only for authorized administrators.
- [ ] Add approve behavior.
- [ ] Add reject behavior and reject reasons.
- [ ] Apply approved department, job, role, permission, and menu access to the account.
- [ ] Activate accounts only after approval.

## Testing and Quality

- [ ] Add JUnit dependencies and `src/test/java`.
- [ ] Unit-test password validation.
- [ ] Unit-test registration validation and reconfirmation.
- [ ] Test account-status routing.
- [ ] Test failed-login thresholds.
- [ ] Test recovery retry and target restrictions.
- [ ] Test current-user session creation.
- [ ] Test menu routing for local admin and admin users.
- [ ] Add repository integration tests.
- [ ] Add Maven Wrapper.
- [ ] Add formatter and static analysis.
- [ ] Add CI after the initial test suite exists.

## Naming and Cleanup

- [ ] Rename inconsistent identifiers such as `uiController`, `itJobsMenu`, `userAccunt`, `sqlQuerry`, and `logsRepository`.
- [ ] Correct the `TECHNICHAL.md` and `diagramms` names when links can be updated safely.
- [ ] Remove unused classes or connect them to the runtime.
- [ ] Remove unused imports and fields.
- [ ] Replace inconsistent direct console error handling with shared patterns.
- [ ] Decide whether empty directories under `src/main/java/app/Services` should be kept.
- [ ] Decide whether `RouteService` should be implemented or removed.

## Product Features After Foundation Work

- [ ] Patient records and search
- [ ] Appointment scheduling
- [ ] Treatment and visit workflows
- [ ] Billing and finance workflows
- [ ] Reporting and analytics
- [ ] JavaFX UI
- [ ] REST API
- [ ] Docker and deployment tooling

## Open Decisions

- Should `suspicious` login be treated as success, require password change, or stay blocked?
- Should recovery remain limited to system accounts?
- Which roles may approve access requests?
- Should jobs remain strings or move to a dedicated table?
- Which login-attempt data is the source of truth for security policy?
- Should application logs be stored only in files, only in the database, or in both?
- Should the console menu remain the main runtime target before JavaFX or REST work begins?
