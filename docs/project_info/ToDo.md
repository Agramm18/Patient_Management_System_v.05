# ToDo

Status date: 2026-06-03

This file is the central GitHub-facing task overview for the current implementation stage. It focuses on implementation dependencies: what is required, what it enables, and which concrete tasks should be handled next.

`mvn -DskipTests compile` currently succeeds. There is no `src/test` directory yet, so automated tests are not available.

## Current Implementation Chain

The current authentication foundation is now in place for startup, configuration, registration, login, starter account password change, recovery-key handling, and pending-user access request groundwork.

The next development milestone is a complete pending-user access request and approval flow:

```text
valid pending account
-> department selection
-> job selection
-> role selection
-> stored access request
-> admin approval or rejection
-> account activation
-> routed main menu
```

Patient records, appointments, billing, JavaFX, REST APIs, and deployment work should remain out of scope until this chain is stable.

## Development Roadmap

Recommended implementation order for the current stage:

```text
1. Registration data integrity
2. Job selection
3. Role selection
4. Complete access request persistence
5. Admin approval and rejection
6. Account activation
7. Main menu routing
8. Account status policy persistence
9. Tests
10. Naming and documentation cleanup
```

### Roadmap Notes

| Step | Required outcome |
| --- | --- |
| Registration data integrity | New accounts are always valid pending accounts with complete required data. |
| Job selection | Pending users can select a real job and the selected job is stored in the access request. |
| Role selection | Pending users can request a role and the selected role is stored in the access request. |
| Access request persistence | Department, job, role, and optional request context are stored consistently. |
| Admin approval and rejection | Admins can review pending requests and approve or reject them. |
| Account activation | Approved accounts are updated from pending to active with department, job, role, permission, and menu access. |
| Main menu routing | Active users are routed into the correct menu based on approved access data. |
| Account status policy persistence | Failed-login thresholds update real account state instead of only printing policy messages. |
| Tests | Core auth, registration, recovery, and access flows can be changed without silent regressions. |
| Naming and documentation cleanup | The codebase and GitHub documentation remain understandable as the project grows. |

## Priority Overview

| Priority | Area | Required so that |
| --- | --- | --- |
| high | Registration data integrity | valid pending accounts exist before access requests are created |
| high | Job selection | access requests contain the actual requested job instead of `unassigned` |
| high | Role selection | access requests contain the intended role instead of always using the intern role |
| high | Access approval | pending users can become active users through a controlled admin workflow |
| high | Account status policy | failed-login thresholds affect real account status |
| high | Main menu routing | active users can continue into the application after login |
| mid | Tests | the core flows can be changed without breaking existing behavior unnoticed |
| mid | Naming and cleanup | the codebase stays maintainable while the feature set grows |
| low | Future product features | patient management and UI work can start after the access foundation is stable |

## Done Baseline

The following foundation is considered complete enough for the current stage:

| Area | Status | Notes |
| --- | --- | --- |
| Runtime password input | done | Login, password creation, and recovery require terminal-backed `System.console()` input. |
| Startup fail-fast behavior | done | Invalid `.env`, invalid database connection, or invalid DB runtime config stop boot before authentication. |
| Recovery baseline | done | `RECOVERY_KEY` is required, hashed into `recovery_keys`, checked with BCrypt, and used to reset a selected account password hash. |
| Starter account password change | done | Starter accounts in `waiting_for_password_change` can set a new password and become active. |
| Pending access request groundwork | partial | Pending users can select a department and create an access request with default job and role values. |

## Registration Data Integrity

Registration data integrity is required so that every newly registered account is a valid pending account before it enters the access-request workflow.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Fix the registration correction path. If entered data is changed, the data should be shown again, reconfirmed, and only then should password creation run. |  | `RegistrationService.java`, `RegistrationFlow.java` |
| high | Prevent account creation when the password hash is null or blank. `CreateAccount` should reject invalid required values before the DB insert. |  | `CreateAccount.java` |
| high | Add username and email uniqueness checks before account creation, with clear user-facing messages. |  | `CreateAccount.java`, `CheckUserInDB.java`, optional validation helper |
| mid | Improve email and phone validation beyond basic `@`, `+`, and length checks. |  | `RegistrationService.java` |

### Enables

- Pending users can be created consistently.
- Access requests are tied to real accounts with complete account data.
- Later admin approval does not need to handle broken registration records.

## Job Selection

Job selection is required so that an access request records the actual job a pending user requested.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Implement `SelectJob` so it can validate a selected job and return a stored job value. |  | `SelectJob.java` |
| high | Connect `SelectJob` in `FirstLogin` after department selection and department job-menu display. |  | `FirstLogin.java` |
| high | Pass the selected job into `HandleAccessManagement` and store it in `access_management.requested_job`. |  | `FirstLogin.java`, `HandleAccessManagement.java` |
| mid | Complete placeholder department job menus. Several menus still print only labels instead of selectable jobs. |  | `MedicalJobsMenu.java`, `EmergencyJobsMenu.java`, `LaboratoryJobsMenu.java`, `itJobsMenu.java`, `FinanceJobsMenu.java`, `OfficeJobsMenu.java`, `AdministrationJobsMenu.java` |
| mid | Decide whether jobs stay as strings for now or move into a dedicated database table later. |  | `DB_SETUP.md`, `HandleAccessManagement.java`, job menu classes |

### Enables

- Pending-user access requests become meaningful.
- Admin approval can assign the requested job to the account.
- Main-menu routing can later use department and job together.

## Role Selection

Role selection is required so that access requests contain the intended role instead of always defaulting to intern.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Change `RoleValidation` so it returns the selected role ID. |  | `RoleValidation.java` |
| high | Connect role selection to the first-login access request flow. |  | `FirstLogin.java`, `RoleValidation.java` |
| high | Store the selected role in `access_management.requested_role` instead of using the default intern role. |  | `HandleAccessManagement.java` |
| mid | Replace role magic numbers with named constants or an enum-like helper. |  | `CreateAccount.java`, `CreateDefaultAccounts.java`, `RoleValidation.java`, `DB_SETUP.md` |
| mid | Fix the role check logic that treats `user_role` like a string even though the schema stores it as an integer foreign key. |  | `CheckRoles.java` |

### Enables

- Access approval can update the user account with the requested role.
- Permission and menu access can later be derived from role and department.
- The RBAC foundation becomes usable for real application routing.

## Access Request Persistence

Complete access request persistence is required so that admin approval has all required information available in the database.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Store `requested_department`, `requested_job`, and `requested_role` from user input instead of defaults. |  | `FirstLogin.java`, `HandleAccessManagement.java` |
| mid | Decide how duplicate pending requests from the same account should be handled: block, update latest pending request, or keep request history. |  | `HandleAccessManagement.java`, database constraints or query logic |
| mid | Add request reason support if access approval needs context beyond department, job, and role. |  | `FirstLogin.java`, `HandleAccessManagement.java`, `DB_SETUP.md` |

### Enables

- Admins can review complete requests.
- Pending accounts can be activated based on explicit requested access.
- Repeated or conflicting requests can be handled predictably.

## Admin Approval Workflow

Admin approval is required so that pending users can become active through a controlled access-management process.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Add an admin workflow to list pending access requests with username, department, job, role, and status. |  | `MenuController.java`, new service/repository classes |
| high | Add approve behavior that updates account department, job, role, permission/menu access, and account status. |  | new service/repository classes, `HandleAccessManagement.java` |
| high | Add reject behavior that stores a reject reason and leaves the account in the chosen blocked state. |  | new service/repository classes, `HandleAccessManagement.java` |
| high | Activate pending users only after approval. Creating an access request should not make the account fully usable. |  | `LoginVerification.java`, admin approval flow |

### Enables

- Pending accounts can move into active accounts.
- Admin work becomes part of the runtime instead of only database maintenance.
- Main-menu access can be granted based on approved account data.

## Account Status And Security Policy

Account status persistence is required so that security decisions made during login actually affect later runtime behavior.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Implement `ExecutePWSDPolicy.locked`, `suspicious`, and `quarantine` or replace them with a shared status-update repository. |  | `ExecutePWSDPolicy.java`, `SetNewStatus.java` |
| high | Persist failed-login threshold status changes to `accounts.account_status`. |  | `LoginVerification.java`, `CountFailedLoginAttempts.java`, `ExecutePWSDPolicy.java` |
| high | Define reset behavior after successful login or admin action. Decide whether `login_attempts`, `accounts.failed_password_attempts`, or both are the source of truth. |  | `LoginVerification.java`, `CountFailedLoginAttempts.java`, `DB_SETUP.md` |
| mid | Review whether pending access-request creation should be logged as success, partial success, or blocked login. |  | `LoginVerification.java`, `CollectLogs.java` |
| mid | Review recovery control flow so failed key validation cannot continue into password reset. |  | `RecoveryFlow.java`, `CheckKeyStatus.java` |
| mid | Make account status messages consistent for `disabled`, `locked`, `on_quarantine`, and `pending`. |  | `LoginVerification.java`, CLI text |

### Enables

- Brute-force protection becomes functional instead of only informational.
- Admin approval and account management can handle locked or quarantined accounts.
- Login and recovery behavior remains consistent after repeated failures or invalid recovery attempts.

## Main Menu And Runtime Routing

Main-menu routing is required so that active users can continue into the application after authentication.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Return authenticated user context from login instead of only returning success/failure. |  | `LoginFlow.java`, `LoginVerification.java` |
| high | Implement `FrontController.RequestType.MENU` routing. |  | `FrontController.java`, `MenuController.java` |
| high | Route active users into a menu based on account role, department, job, permission, or `has_access_to_menu`. |  | `AuthController.java`, `MenuController.java`, repository helpers |
| mid | Decide whether `ServiceController` and `uiController` are needed in the console version or should remain reserved for later layers. |  | `ServiceController.java`, `uiController.java`, `BootConfigService.java` |
| mid | Add clean return paths for registration, failed login, successful access-request creation, recovery, and normal exits. |  | `AuthController.java`, `RegistrationFlow.java`, `LoginFlow.java`, `RecoveryFlow.java`, `BootConfigService.java` |

### Enables

- Active users can reach application features after login.
- Department/job/role approval data can control runtime access.
- Future patient-management features have a real entry point.

## Tests And Verification

Tests are required so that the access-request foundation can evolve without silent regressions.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| mid | Add JUnit dependencies and create `src/test/java`. |  | `pom.xml`, `src/test/java` |
| mid | Test password validation and terminal-only console behavior where possible. |  | `PasswordService.java`, test files |
| mid | Test registration validation and correction flow. |  | `RegistrationService.java`, test files |
| mid | Test recovery-key validation and password-reset behavior. |  | `RecoveryFlow.java`, `RecoveryCheck.java`, repository test files |
| mid | Test job and role selection after these services return values. |  | `SelectJob.java`, `RoleValidation.java`, test files |
| mid | Add database-backed integration tests later for repositories. |  | `Repository/**/*`, test files |
| low | Add GitHub Actions after basic tests exist. |  | `.github/workflows/*` |

### Enables

- Safer refactoring of auth, job, role, recovery, and access-request code.
- Faster verification before pushing to GitHub.
- Clear proof that core flows work.

## Naming And Cleanup

Naming and cleanup are required so that the codebase remains maintainable, but most of this should not block the current job/access implementation unless the same files are already being changed.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| mid | Clean up inconsistent class, method, package, and folder names such as `uiController`, `itJobsMenu`, `userAccunt`, `logsRepository`, `TECHNICHAL.md`, and `diagramms`. |  | multiple Java and docs files |
| mid | Replace magic numbers for role, department, and status IDs with named constants or enum-like helpers. |  | `CreateAccount.java`, `CreateDefaultAccounts.java`, `LoginVerification.java`, `DB_SETUP.md` |
| low | Fix spelling in console messages such as `sucsessfully`, `WARING`, `Adress`, `emtpy`, `exsit`, `querry`, and `where saved`. |  | multiple CLI/service/repository classes |
| low | Remove unused imports and placeholder variables when touching files for functional changes. |  | multiple Java files |

### Enables

- Easier navigation through the codebase.
- Lower risk when adding jobs, roles, and admin workflows.
- Cleaner GitHub presentation.

## Documentation And Project Structure

Documentation updates are required so that GitHub reflects the actual runtime behavior and current implementation priorities.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| mid | Keep `CURRENT_STATUS.md`, `ToDo.md`, setup docs, project structure docs, and UML aligned after auth/access changes. | [x] | `docs/project_info/*`, `docs/setup/*`, `docs/architecture/*` |
| mid | Decide whether SQL setup belongs only in Markdown docs or should also be versioned as SQL files. `PROJECT_STRUCTURE.md` and `DB_SETUP.md` still reference `Query.sql` and `Query_1.sql`, while `.gitignore` ignores `*.sql`. |  | `PROJECT_STRUCTURE.md`, `DB_SETUP.md`, `.gitignore` |
| low | Add Maven Wrapper, formatter/linter, and logging framework later. |  | `pom.xml`, new config files |

### Enables

- GitHub readers can understand the current state without reading the whole codebase.
- Setup documentation stays aligned with actual runtime requirements.
- Architecture diagrams stay useful as the controller flow grows.

## Future Product Features

These features require the authentication and access-management foundation to be stable first.

| Priority | Status | Task |
| --- | --- | --- |
| low | missing | Patient records and patient search |
| low | missing | Appointment scheduling |
| low | missing | Treatment and visit workflows |
| low | missing | Billing and finance workflows |
| low | missing | Reporting and analytics |
| low | missing | JavaFX UI |
| low | missing | REST API support |
| low | missing | Docker, Kubernetes, and deployment tooling |

## Recommended Next Work

1. Fix the registration correction path and block account creation with missing password hashes.
2. Implement `SelectJob` and connect selected jobs to `HandleAccessManagement`.
3. Change `RoleValidation` to return a role ID and store it in access requests.
4. Complete access request persistence with department, job, and role from user input.
5. Add admin approval and rejection behavior.
6. Persist failed-login account status changes.
7. Connect active users to a real main menu.
8. Add tests once registration, recovery, job, and role behavior have stable return values.

## Open Decisions

- Should creating an access request count as successful login, partial login, or blocked login?
- Which roles are allowed to approve access requests?
- Should jobs stay as strings or move into a dedicated database table?
- Should recovery remain limited to system accounts or be generalized to normal user accounts?
- Should recovery reset only the password hash or also reset account status and menu access flags?
- Should `DBManager` stay as global runtime configuration or later be replaced with dependency injection?
