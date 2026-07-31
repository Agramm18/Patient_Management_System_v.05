# Project Archive

Last synchronized: 2026-07-31.

This document records completed milestones, later refactors, and superseded statements. It is historical context, not the active task list.

For current information, use:

- [Current project status](../project_info/CURRENT_STATUS.md)
- [Project backlog](../project_info/ToDo.md)
- [Project structure](../architecture/PROJECT_STRUCTURE.md)

## Project Evolution

### Original Python Version

The first Patient Management System version was created in Python as a programming and Object-Oriented Programming learning project. It focused on command-line input and application logic without the current Java architecture, MySQL repositories, authentication model, or automated tests.

### Java Rebuild

The project was rebuilt in Java to practice larger application structure, Maven, MySQL, JDBC, authentication, security, controller and repository boundaries, testing, logging, access management, and technical documentation.

## Milestone History

### Application Foundation

- Java 21 Maven project created
- `app.Main` entry point created
- Bootstrap behavior moved into `BootConfigService`
- `FrontController` and request types introduced
- Configuration, authentication, menu, and service routes connected
- `UI` and `EXIT` request values reserved

### Configuration and Database

- `.env` existence and value validation implemented
- MySQL JDBC URL creation and connection validation implemented
- Static runtime database settings stored in `DBManager`
- Configuration failure made fatal before authentication
- Recovery-key startup persistence added
- Environment and database setup documentation separated
- `EnvSetup` added on 2026-06-25 to validate and store all 13 environment values before database settings are exposed

### Starter Accounts

- Missing local-admin and admin accounts detected by database role ID
- Missing starter accounts created automatically
- Starter passwords hashed with BCrypt
- Starter accounts marked as system accounts
- Starter accounts initialized with `waiting_for_password_change`
- First password update activates the account and enables menu access
- Admin starter creation aligned with `ADMIN_PASSWORD_DEFAULT`

### Registration

- Username, email, phone, and password collection implemented
- Registration validators separated from input loops for unit testing
- Email structure validation expanded
- International phone validation added through libphonenumber
- Pending-account insertion implemented
- Confirmation and field-correction flow introduced
- On 2026-07-20, corrected values were returned to one confirmation loop and the confirmed `PasswordFlow` result was stored in `RegistrationService.hashedPWSD`
- Helper-level null and blank password-hash checks added

The repository-level account insert still has no independent null or blank hash guard and does not return an explicit result.

### Password Handling

- Password policy and retype validation implemented
- User-created password hashes configured with BCrypt cost 15
- Starter-account and recovery-key hashes configured with BCrypt cost 12
- Password policy, retype, terminal-input, and array-clearing tests added
- On 2026-07-19, password conversion and cleanup order was corrected so hashing uses the original password before both character arrays are cleared

An end-to-end interactive hash-verification test and safe full-flow behavior without `System.console()` remain open.

### Login, Session, and Security Policy

- Username lookup and BCrypt password verification implemented
- Account-status routing implemented
- Login attempts persisted to MySQL
- Invalid-password counting over a 24-hour window introduced
- Locked, suspicious, and quarantine status-update repositories implemented
- `CurrentUser` introduced as the first active runtime account object
- Static `CurrentSession` introduced for the active account
- Login values loaded through the login repository

On 2026-07-23, the login and session structure changed:

- `CurrentUser` was replaced by the immutable `SessionAccount` record.
- `CurrentSession` was updated with `setCurrentAccount`, `getCurrentAccount`, `isLoggedIn`, and `clear`.
- The input service was named `CollectLoginValues`.
- Credential and account-status verification moved to `SetupCurrentSession`.
- Status-specific work moved to `HandleAccountStatus`.
- Login-attempt output moved into the `StoreLogs` record.
- Failed-password policy calls moved to `PasswordPolicies`.

At that point, the refactor left a failure-reason contract mismatch: the policy returned `to many false attempts`, while the counting query accepted only `INVALID_PASSWORD`. It also evaluated only the previously persisted count.

### Typed Login Outcomes

On 2026-07-24:

- `StoreLogs` replaced its ambiguous success boolean with `LoginOutcome`.
- `LoginFlow` began persisting success only for `PERMITTED`.
- Pending requests and password changes began returning to authentication as unsuccessful login attempts.
- Rejected and invalid outcomes began repeating inside the credential loop.
- A failed first password update began returning `WAITING_FOR_PASSWORD_CHANGE` instead of unconditional success.

### Multi-Period Failed-Login Policy

On 2026-07-24 and 2026-07-25:

- `PolicyThreshold` introduced named base thresholds for locked, suspicious, and quarantine transitions.
- `TimePeriod` introduced day, week, month, year, five-year, and ten-year windows with scaling factors.
- `PolicieThresholdStructure` added a typed record for the six counts.
- Wrong-password persistence and counting were aligned on `INVALID_PASSWORD`.
- `includingAttempt()` added the current attempt in memory before threshold evaluation.
- `CountFailedLoginAttempts` expanded from one count to six SQL queries.

The policy still performs counting, status updates, and attempt persistence as separate operations and has no automated boundary or integration coverage.

### Recovery

- Recovery key added to required environment configuration
- Recovery key hashed and stored at `recovery_keys.id = 1`
- Hidden recovery-key input implemented
- BCrypt key verification implemented
- Four-attempt key limit implemented
- System-account list display implemented
- Selected account password update implemented

On 2026-07-24, recovery password updates also began setting account status ID 1. They still do not reconcile `requires_password_change`, menu access, or session state, and the final target lookup still accepts any existing account.

The final account lookup still accepts any existing account rather than only the system accounts displayed to the user.

### Pending Access Requests

- Department menu and range validation implemented
- Department-specific job menu classes added
- System-department display guard added
- Access-request insertion implemented with the selected department and default job and role values
- Access-request listing query added under `Repository.ServiceRepository.AdminServices`

Job and role selection, duplicate handling, review decisions, and account activation remain incomplete.

### Initial Menu and Service Baseline

- Role-aware local-admin and admin menu routing introduced
- `MenuFlow` added for validated numeric choices
- A menu context value introduced to transfer role and selected option
- `ServiceController` introduced as a service dispatcher
- On 2026-06-18, the first admin option was connected directly to `ShowCurrentRequests`

### Intermediate Menu Refactor

On 2026-06-24 and 2026-06-25:

- Admin and local-admin menus moved under `ServiceMenus/ParrentMenus`
- Empty `RequestMenu` and `SubMenuController` placeholders were added
- The menu context expanded to parent, role, and child values
- The admin parent menu was reduced to five options
- Logging calls migrated to the typed logging facade

That intermediate state disconnected `ShowCurrentRequests` and left role handlers that only logged startup.

### Typed Menu and Service-Action Refactor

On 2026-07-22 and 2026-07-23:

- Numeric parent and child contexts were replaced by `MenuContextStructure(userRole, action)`.
- `MenuOption` and `ServiceAction` were introduced.
- All five admin labels were mapped to stable typed actions.
- `MenuController` was replaced by `MenuControllerParent`.
- `SubMenuController` was removed from the controller graph and source tree.
- The selected action was forwarded through `FrontController` to `ServiceController`.
- `ADMIN_USER_REQUESTS` was connected to `ShowCurrentRequests`.
- Unknown role IDs were rejected explicitly.

The remaining four admin actions and `LOCAL_ADMIN_DASHBOARD` currently reach the unsupported-action exception. There is no repeated menu loop or connected logout action.

### Logging

- Logback dependency added
- `src/main/resources/logback.xml` added with console and per-category file appenders
- Generated `logs/` output excluded from Git
- Original single-switch `LogManager` replaced on 2026-06-25
- Dedicated state enums added for boot, authentication, configuration, security, SQL, system, recovery, menu, account, and other input events
- Logging migration started across bootstrap, configuration, authentication, recovery, menu, and repositories

### Automated Tests

- JUnit Jupiter and Surefire added
- `PasswordServiceTest` added
- `RegistrationServiceTest` added
- libphonenumber-based validation covered by phone tests
- On 2026-07-18, 53 tests passed: 11 password-service tests and 42 registration-service tests
- On 2026-07-23, the reorganized suite passed 55 tests: 15 password-service tests and 40 registration-service tests
- On 2026-07-31, the same 55-test suite passed after the typed outcome and multi-period policy changes

The current suite has 0 failures, 0 errors, and 0 skipped tests, but it remains limited to service-level validation helpers.

### Build Tooling

- Maven Wrapper 3.3.4 scripts added on 2026-07-17
- Wrapper configured for Maven 3.9.16
- An earlier Windows PowerShell run exposed a generated-wrapper null-target issue
- On 2026-07-23, `.\mvnw.cmd test` ran successfully in Windows PowerShell
- On 2026-07-31, `.\mvnw.cmd test` again ran successfully in Windows PowerShell

### Documentation

- Documentation organized under `docs/project_info`, `docs/setup`, `docs/architecture`, and `docs/archive`
- Current status, roadmap, setup, architecture, and Mermaid flow documents created
- Documentation synchronized with the service and request-listing baseline on 2026-06-18
- All files under `docs` synchronized with the 89-source and 53-test snapshot on 2026-07-18
- `ToDo.md` updated for the typed menu baseline and 55-test suite on 2026-07-23
- All 11 Markdown documents under `docs` and the supporting Mermaid source synchronized with the 93-source/55-test snapshot on 2026-07-23
- All 11 Markdown documents under `docs` and the supporting Mermaid source synchronized with the 98-source/55-test snapshot on 2026-07-31

## Superseded Statements

The following older claims are no longer current:

- There are no automated tests. There are now 55 passing unit tests.
- The suite contains 11 password tests and 42 registration tests. It now contains 15 and 40 respectively.
- The project contains 76, 89, 91, or 93 production Java files. It now contains 98.
- The Windows Maven Wrapper cannot run the test suite in PowerShell. The wrapper completed the verified 2026-07-31 test run.
- Active session data is stored in `Unknown` or `CurrentUser`. It is stored in `SessionAccount`.
- `CurrentSession` has no clear method. `clear()` now exists, although logout does not call it.
- Menu routing uses `MenuValues` or numeric parent and child contexts. It now uses a typed `ServiceAction`.
- `SubMenuController` is injected but unused. The class and dependency were removed.
- `ShowCurrentRequests` is disconnected. It is invoked by `ADMIN_USER_REQUESTS`.
- Login persistence uses a `canUseSystem` boolean. It now uses `LoginOutcome`, and only `PERMITTED` is stored as successful.
- Invalid-password persistence uses `to many false attempts` while counting uses `INVALID_PASSWORD`. Both now use `INVALID_PASSWORD`.
- Policy evaluation excludes the current attempt. `includingAttempt()` now adds it in memory before evaluation.
- Failed-password policy uses only a 24-hour count. It now evaluates six scaled time windows.
- Recovery changes only `password_hash`. It now changes the hash and account status ID 1.
- Both role-specific service handlers only log startup. The service now switches on actions and implements request listing.
- Password creation hashes an already-cleared array. The conversion and cleanup order was fixed.
- Registration correction loses the returned hash. Password collection now follows the final confirmation loop.
- There is no `src/main/resources` directory. `logback.xml` exists there.
- Logback uses only default console configuration. Explicit console and file appenders are configured.
- Environment values are stored only in `EnvValidationService`. `EnvSetup` validates the complete value set first.
- The admin menu contains eight options. It currently contains five parent options.
- `uiController` is the current class name. It was renamed to `UIController`.
- The old `LogType` switch is current. It was replaced by typed methods and state enums.
- A root `Query.sql` file is the database source. SQL files are ignored; `DB_SETUP.md` is the documented schema source.

## Historical Limitations Still Present

- Failed-login counts, status updates, and attempt persistence are not transactional.
- Multi-window SQL semantics and scaled thresholds lack automated coverage.
- The in-memory retry count is recreated for each invalid password.
- Active accounts can receive `PERMITTED` and a session without menu access.
- Complete password and registration flows lack end-to-end tests.
- Repository-level password-hash validation and structured outcomes are incomplete.
- Access requests still use default job and role values.
- Approval, rejection, and activation workflows do not exist.
- Recovery's final account lookup is not limited to system accounts, and recovery leaves menu and password-change fields inconsistent.
- Four admin actions, the local-admin dashboard, logout, and a repeated menu loop are not implemented.
- Service-layer role/action authorization is incomplete.
- Logging migration remains partial.
- Naming and repository result handling remain inconsistent.
- Automated tests do not cover repositories or complete runtime flows.
- Patient-management product features are not implemented.
