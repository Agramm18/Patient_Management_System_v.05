# ToDo

Status date: 2026-06-01

This file is the central GitHub-facing task overview for the current implementation stage. It focuses on implementation dependencies: what is required, what it enables, and which concrete tasks should be handled next.

`mvn -DskipTests compile` currently succeeds. There is no `src/test` directory yet, so automated tests are not available.

## Current Implementation Chain

The next development milestone is a complete pending-user access request flow:

```text
stable input/runtime
-> valid pending account
-> department selection
-> job selection
-> role selection
-> stored access request
-> admin approval
-> active account
-> routed main menu
```

Patient records, appointments, billing, JavaFX, REST APIs, and deployment work should remain out of scope until this chain is stable.

## Development Roadmap

Recommended implementation order for the current stage:

```text
1. Runtime/input stability
2. Registration data integrity
3. Jobs
4. Roles
5. Permissions
6. Access request persistence
7. Admin approval/rejection
8. Account activation
9. Menu logic
10. Tests and documentation updates
```

### Roadmap Notes

| Step | Required outcome |
| --- | --- |
| Runtime/input stability | Login, registration, password creation, and recovery input work reliably in supported terminal runs. |
| Registration data integrity | New accounts are always valid pending accounts with complete required data. |
| Jobs | Pending users can select a real job and the selected job is stored in the access request. |
| Roles | Pending users can request a role and the selected role is stored in the access request. |
| Permissions | Approved role/job/department combinations can be mapped to permissions and menu access. |
| Access request persistence | Department, job, role, and optional request context are stored consistently. |
| Admin approval/rejection | Admins can review pending requests and approve or reject them. |
| Account activation | Approved accounts are updated from pending to active with department, job, role, permission, and menu access. |
| Menu logic | Active users are routed into the correct menu based on approved access data. |
| Tests and documentation updates | The finished access flow is covered by tests and reflected in project documentation. |

## Priority Overview

| Priority | Area | Required so that |
| --- | --- | --- |
| high | Runtime and input stability | authentication, registration, password creation, and first-login flows can be tested reliably |
| high | Registration data integrity | valid pending accounts exist before access requests are created |
| high | Job selection | access requests contain the actual requested job instead of `unassigned` |
| high | Role selection | access requests contain the intended role instead of always using the intern role |
| high | Access approval | pending users can become active users through a controlled admin workflow |
| high | Account status policy | failed-login thresholds affect real account status |
| high | Main menu routing | active users can continue into the application after login |
| mid | Tests | the core access flow can be changed without breaking existing behavior unnoticed |
| mid | Naming and cleanup | the codebase stays maintainable while the feature set grows |
| low | Future product features | patient management and UI work can start after the access foundation is stable |

## Runtime And Input Stability

Runtime and input stability is required so that authentication, registration, recovery, and first-login flows can run reliably in supported terminal contexts. IDE runs are intentionally not supported for password entry because `System.console()` can be unavailable or inconsistent there.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Enforce terminal-only password input when `System.console()` is null. `LoginInputCollector`, `PasswordService`, and `RecoveryCheck` should fail fast instead of using an IDE/Scanner fallback. | [x] | `LoginInputCollector.java`, `PasswordService.java`, `RecoveryCheck.java` |
| high | Make startup fail fast. Invalid `.env` values or failed DB connection checks should stop boot instead of allowing later startup steps to continue with invalid runtime state. | [x] | `ConfigController.java`, `EnvValidationService.java`, `SQLValidationService.java`, `DBManager.java` |
| mid | Replace print-only error handling with clear success/failure results or exceptions where controller decisions depend on repository success. |  | `Repository/**/*`, `ConfigController.java`, `LoginVerification.java` |

### Enables

- Reliable manual testing of login, registration, recovery, and pending-user setup.
- Safer continuation into job and role selection.
- Cleaner failure behavior before database-backed flows run.

## Registration Data Integrity

Registration data integrity is required so that every newly registered account is a valid pending account before it enters the access-request workflow.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Fix the registration correction path. If entered data is changed, the data should be shown again, reconfirmed, and only then should password creation run. |  | `RegistrationService.java`, `RegistrationFlow.java` |
| high | Prevent account creation when the password hash is null or blank. `CreateAccount` should reject invalid required values before the DB insert. |  | `CreateAccount.java` |
| high | Add username and email uniqueness checks before account creation, with clear user-facing messages. |  | `CreateAccount.java`, `CheckUserInDB.java`, optional new validation helper |
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
| mid | Make account status messages consistent for `disabled`, `locked`, `on_quarantine`, and `pending`. |  | `LoginVerification.java`, CLI text |

### Enables

- Brute-force protection becomes functional instead of only informational.
- Admin approval and account management can handle locked or quarantined accounts.
- Login behavior remains consistent after repeated failures.

## Main Menu And Runtime Routing

Main-menu routing is required so that active users can continue into the application after authentication.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Return authenticated user context from login instead of only returning success/failure. |  | `LoginFlow.java`, `LoginVerification.java` |
| high | Implement `FrontController.RequestType.MENU` routing. |  | `FrontController.java`, `MenuController.java` |
| high | Route active users into a menu based on account role, department, job, permission, or `has_access_to_menu`. |  | `AuthController.java`, `MenuController.java`, repository helpers |
| mid | Decide whether `ServiceController` and `uiController` are needed in the console version or should remain reserved for later layers. |  | `ServiceController.java`, `uiController.java`, `BootConfigService.java` |
| mid | Add clean return paths for registration, failed login, successful access-request creation, and normal exits. |  | `AuthController.java`, `RegistrationFlow.java`, `LoginFlow.java`, `BootConfigService.java` |

### Enables

- Active users can reach application features after login.
- Department/job/role approval data can control runtime access.
- Future patient-management features have a real entry point.

## Recovery Flow

Recovery completion is required so that recovery-key validation results in an actual password reset instead of only creating an unused password hash.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| high | Select or identify the target account for recovery. |  | `RecoveryFlow.java` |
| high | Save the new password hash to the selected account after recovery-key validation succeeds. |  | `RecoveryFlow.java`, `UpdateUserPWSD.java`, optional new repository method |
| mid | Decide whether recovery applies only to system accounts or also to normal users. |  | `RecoveryFlow.java`, docs |
| mid | Add `RECOVERY_KEY` to the `.env` template because the code already requires it. |  | `ENV_SETUP.md`, `EnvValidationService.java` |

### Enables

- Recovery becomes a functional account-management flow.
- System account recovery can be tested without direct database edits.
- Environment setup documentation matches runtime validation.

## Tests And Verification

Tests are required so that the access-request foundation can evolve without silent regressions.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| mid | Add JUnit dependencies and create `src/test/java`. |  | `pom.xml`, `src/test/java` |
| mid | Test password validation and terminal-only console behavior where possible. |  | `PasswordService.java`, test files |
| mid | Test registration validation and correction flow. |  | `RegistrationService.java`, test files |
| mid | Test job and role selection after these services return values. |  | `SelectJob.java`, `RoleValidation.java`, test files |
| mid | Add database-backed integration tests later for repositories. |  | `Repository/**/*`, test files |
| low | Add GitHub Actions after basic tests exist. |  | `.github/workflows/*` |

### Enables

- Safer refactoring of auth, job, role, and access-request code.
- Faster verification before pushing to GitHub.
- Clear proof that core flows work.

## Naming And Cleanup

Naming and cleanup are required so that the codebase remains maintainable, but most of this should not block the current job/access implementation unless the same files are already being changed.

### Work To Do Now

| Priority | Task | Done | Files |
| --- | --- | --- | --- |
| mid | Clean up inconsistent class, method, package, and folder names such as `uiController`, `itJobsMenu`, `userAccunt`, `logsRepository`, `TECHNICHAL.md`, and `diagramms`. |  | multiple Java and docs files |
| mid | Replace magic numbers for role, department, and status IDs with named constants or enum-like helpers. |  | `CreateAccount.java`, `CreateDefaultAccounts.java`, `LoginVerification.java`, `DB_SETUP.md` |
| low | Fix spelling in console messages such as `sucsessfully`, `WARING`, `Adress`, `emtpy`, `wsa`, and `where saved`. |  | multiple CLI/service/repository classes |
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
| mid | Keep `CURRENT_STATUS.md`, `ToDo.md`, and UML aligned with implemented behavior after job/access changes. |  | `docs/project_info/*`, `docs/architecture/diagramms/patient-management-uml.mmd` |
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

1. Make startup fail fast for invalid runtime state.
2. Fix the registration correction path and block account creation with missing password hashes.
3. Implement `SelectJob` and connect selected jobs to `HandleAccessManagement`.
4. Change `RoleValidation` to return a role ID and store it in access requests.
5. Complete access request persistence with department, job, and role from user input.
6. Add admin approval and rejection behavior.
7. Persist failed-login account status changes.
8. Connect active users to a real main menu.
9. Add tests once job and role selection have stable return values.

## Open Decisions

- Should creating an access request count as successful login, partial login, or blocked login?
- Which roles are allowed to approve access requests?
- Should jobs stay as strings or move into a dedicated database table?
- Should recovery reset only system accounts or also normal user accounts?
- Should `DBManager` stay as global runtime configuration or later be replaced with dependency injection?
